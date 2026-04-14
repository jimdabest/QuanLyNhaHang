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

            return cs.executeUpdate() > 0;
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
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHoaDon);
            return cs.executeUpdate() > 0;
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