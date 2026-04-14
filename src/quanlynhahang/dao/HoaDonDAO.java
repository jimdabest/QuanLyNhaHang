package quanlynhahang.dao;

import quanlynhahang.dto.HoaDonDTO;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO quản lý hóa đơn và các thao tác liên quan đến bảng HoaDon.
 * Hỗ trợ thao tác CRUD và gọi stored procedure liên quan đến mở bàn, thanh toán.
 */
public class HoaDonDAO implements IDAO<HoaDonDTO, String> {

    /**
     * Thêm hóa đơn mới vào hệ thống.
     * Hàm này giao tiếp với stored procedure để mở bàn và khởi tạo hóa đơn.
     * @param obj đối tượng HoaDonDTO chứa thông tin hóa đơn cần tạo
     * @return true nếu thêm thành công, false nếu thất bại
     */
    @Override
    public boolean insert(HoaDonDTO obj) {
        return moBanMoi(obj.getMaBan(), obj.getMaKhachHang());
    }

    /**
     * Nghiệp vụ mở bàn bằng stored procedure sp_MoBan.
     * @param maBan mã bàn cần mở
     * @param maKhachHang mã khách hàng nếu có, có thể null cho khách vãng lai
     * @return true nếu mở bàn thành công, false nếu thất bại
     */
    public boolean moBanMoi(String maBan, String maKhachHang) {
        String sql = "{CALL sp_MoBan(?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maBan);
            cs.setString(2, maKhachHang); // Có thể để null nếu khách vãng lai

            cs.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Nghiệp vụ thanh toán hóa đơn bằng stored procedure sp_ThanhToanVaTichDiem.
     * Stored procedure này chịu trách nhiệm chốt time-out, tính điểm tích lũy và giải phóng bàn.
     * @param maHoaDon mã hóa đơn cần thanh toán
     * @return true nếu thanh toán thành công, false nếu có lỗi
     */
    public boolean thanhToanHoaDon(String maHoaDon) {
        String sql = "{CALL sp_ThanhToanVaTichDiem(?)}";
        String thongTinTruocSql = "SELECT h.maKhachHang, h.maBan, h.thanhTien, kh.tongChiTieu FROM HoaDon h LEFT JOIN KhachHang kh ON h.maKhachHang = kh.maKhachHang WHERE h.maHoaDon = ?";
        String tongChiTieuSauSql = "SELECT tongChiTieu FROM KhachHang WHERE maKhachHang = ?";
        String capNhatTongChiTieuSql = "UPDATE KhachHang SET tongChiTieu = tongChiTieu + ? WHERE maKhachHang = ? AND trangThai = 1";
        String capNhatTrangThaiBanSauThanhToanSql = "UPDATE BanAn SET trangThai = CASE WHEN EXISTS (SELECT 1 FROM PhieuDatBan WHERE maBan = ? AND trangThai = N'Chờ nhận') THEN N'Đã đặt' ELSE N'Trống' END WHERE maBan = ?";

        String maKhachHang = null;
        String maBan = null;
        double thanhTienHoaDon = 0.0;
        Double tongChiTieuTruoc = null;

           try (Connection conn = DBConnection.getConnection();
               CallableStatement cs = conn.prepareCall(sql)) {

            // Đọc trạng thái trước khi thanh toán để đối chiếu với dữ liệu sau SP.
            try (PreparedStatement ps = conn.prepareStatement(thongTinTruocSql)) {
                ps.setString(1, maHoaDon);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        maKhachHang = rs.getString("maKhachHang");
                        maBan = rs.getString("maBan");
                        thanhTienHoaDon = rs.getDouble("thanhTien");
                        double tongTemp = rs.getDouble("tongChiTieu");
                        if (!rs.wasNull()) {
                            tongChiTieuTruoc = tongTemp;
                        }
                    }
                }
            }

            cs.setString(1, maHoaDon);
            cs.execute();

            // Nếu bàn còn phiếu đặt chờ nhận thì giữ trạng thái "Đã đặt" thay vì "Trống".
            if (maBan != null && !maBan.trim().isEmpty()) {
                try {
                    try (PreparedStatement ps = conn.prepareStatement(capNhatTrangThaiBanSauThanhToanSql)) {
                        ps.setString(1, maBan);
                        ps.setString(2, maBan);
                        ps.executeUpdate();
                    }
                } catch (SQLException capNhatBanEx) {
                    capNhatBanEx.printStackTrace();
                }
            }

            // Fallback cộng bù chỉ chạy khi cần và không được làm ảnh hưởng kết quả thanh toán.
            if (maKhachHang != null && tongChiTieuTruoc != null && thanhTienHoaDon > 0) {
                try {
                    Double tongChiTieuSau = null;
                    try (PreparedStatement ps = conn.prepareStatement(tongChiTieuSauSql)) {
                        ps.setString(1, maKhachHang);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                double tongTemp = rs.getDouble("tongChiTieu");
                                if (!rs.wasNull()) {
                                    tongChiTieuSau = tongTemp;
                                }
                            }
                        }
                    }

                    if (tongChiTieuSau != null) {
                        double daTang = tongChiTieuSau - tongChiTieuTruoc;
                        double canTangThem = thanhTienHoaDon - daTang;

                        if (canTangThem > 0.5) {
                            try (PreparedStatement ps = conn.prepareStatement(capNhatTongChiTieuSql)) {
                                ps.setDouble(1, canTangThem);
                                ps.setString(2, maKhachHang);
                                ps.executeUpdate();
                            }
                        }
                    }
                } catch (SQLException fallbackEx) {
                    fallbackEx.printStackTrace();
                }
            }

            // Nếu stored procedure đã chạy xong, xem như thanh toán thành công.
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật khách hàng theo số điện thoại và tự động tính lại tiền giảm giá cho hóa đơn.
     * Nếu số điện thoại rỗng thì xem như khách vãng lai (không giảm giá).
     *
     * @param maHoaDon mã hóa đơn cần áp dụng ưu đãi
     * @param soDienThoai số điện thoại khách hàng
     * @return true nếu cập nhật thành công, false nếu lỗi hoặc không tìm thấy khách theo SĐT
     */
    public boolean capNhatGiamGiaTheoSoDienThoai(String maHoaDon, String soDienThoai) {
        // Để trống SĐT sẽ giữ nguyên khách hàng hiện có trên hóa đơn (nếu có),
        // từ đó trigger/thủ tục thanh toán vẫn cộng TổngChiTiêu đúng khách.
        String updateKhachHienTaiSql = "UPDATE HoaDon SET tienGiamGia = CASE WHEN maKhachHang IS NULL THEN 0 ELSE tongTien * dbo.fn_LayGiamGiaKhachHang(maKhachHang) END, thanhTien = CASE WHEN tongTien - (CASE WHEN maKhachHang IS NULL THEN 0 ELSE tongTien * dbo.fn_LayGiamGiaKhachHang(maKhachHang) END) < 0 THEN 0 ELSE tongTien - (CASE WHEN maKhachHang IS NULL THEN 0 ELSE tongTien * dbo.fn_LayGiamGiaKhachHang(maKhachHang) END) END WHERE maHoaDon = ? AND trangThai = N'Chưa TT'";
        String getMaKhachSql = "SELECT maKhachHang FROM KhachHang WHERE soDienThoai = ? AND trangThai = 1";
        String updateMemberSql = "UPDATE HoaDon SET maKhachHang = ?, tienGiamGia = tongTien * dbo.fn_LayGiamGiaKhachHang(?), thanhTien = CASE WHEN tongTien - (tongTien * dbo.fn_LayGiamGiaKhachHang(?)) < 0 THEN 0 ELSE tongTien - (tongTien * dbo.fn_LayGiamGiaKhachHang(?)) END WHERE maHoaDon = ? AND trangThai = N'Chưa TT'";

        try (Connection conn = DBConnection.getConnection()) {
            if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(updateKhachHienTaiSql)) {
                    ps.setString(1, maHoaDon);
                    return ps.executeUpdate() > 0;
                }
            }

            String maKhachHang = null;
            try (PreparedStatement ps = conn.prepareStatement(getMaKhachSql)) {
                ps.setString(1, soDienThoai.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        maKhachHang = rs.getString("maKhachHang");
                    }
                }
            }

            if (maKhachHang == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(updateMemberSql)) {
                ps.setString(1, maKhachHang);
                ps.setString(2, maKhachHang);
                ps.setString(3, maKhachHang);
                ps.setString(4, maKhachHang);
                ps.setString(5, maHoaDon);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin cơ bản của hóa đơn.
     * @param obj đối tượng HoaDonDTO chứa dữ liệu cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @Override
    public boolean update(HoaDonDTO obj) {
        String sql = "UPDATE HoaDon SET maBan = ?, maKhachHang = ?, trangThai = ? WHERE maHoaDon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaBan());
            ps.setString(2, obj.getMaKhachHang());
            ps.setString(3, obj.getTrangThai());
            ps.setString(4, obj.getMaHoaDon());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Hủy hóa đơn bằng cách cập nhật trạng thái thành "Đã hủy".
     * Không xóa vật lý để giữ lịch sử giao dịch và báo cáo.
     * @param key mã hóa đơn cần hủy
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @Override
    public boolean delete(String key) {
        // Hóa đơn không xóa vật lý để giữ lịch sử báo cáo
        String sql = "UPDATE HoaDon SET trangThai = N'Đã hủy' WHERE maHoaDon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy toàn bộ hóa đơn, sắp xếp theo thời gian vào giảm dần.
     * @return danh sách HoaDonDTO
     */
    @Override
    public ArrayList<HoaDonDTO> getAll() {
        ArrayList<HoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY thoiGianVao DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new HoaDonDTO(
                        rs.getString("maHoaDon"),
                        rs.getString("maBan"),
                        rs.getString("maKhachHang"),
                        rs.getTimestamp("thoiGianVao"),
                        rs.getTimestamp("thoiGianRa"),
                        rs.getDouble("tongTien"),
                        rs.getDouble("tienGiamGia"),
                        rs.getDouble("thanhTien"),
                        rs.getString("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy hóa đơn theo mã hóa đơn.
     * @param key mã hóa đơn cần lấy
     * @return HoaDonDTO nếu tồn tại, null nếu không
     */
    @Override
    public HoaDonDTO getById(String key) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HoaDonDTO(
                        rs.getString("maHoaDon"),
                        rs.getString("maBan"),
                        rs.getString("maKhachHang"),
                        rs.getTimestamp("thoiGianVao"),
                        rs.getTimestamp("thoiGianRa"),
                        rs.getDouble("tongTien"),
                        rs.getDouble("tienGiamGia"),
                        rs.getDouble("thanhTien"),
                        rs.getString("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}