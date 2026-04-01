package quanlynhahang.dao;

import quanlynhahang.dto.HoaDonDTO;
import java.sql.*;
import java.util.ArrayList;

public class HoaDonDAO implements IDAO<HoaDonDTO, String> {

    @Override
    public boolean insert(HoaDonDTO obj) {
        // Sử dụng hàm moBan để thực hiện chèn hóa đơn mới thông qua Proc
        return moBan(obj.getMaBan(), obj.getMaKhachHang());
    }

    /**
     * Nghiệp vụ Mở bàn: Gọi Stored Procedure sp_MoBan
     * SQL tự động: Tạo HD mới + Đổi trạng thái bàn sang 'Đang ăn'
     */
    public boolean moBan(String maBan, String maKhachHang) {
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
     * Nghiệp vụ Thanh toán: Gọi Stored Procedure sp_ThanhToanVaTichDiem
     * SQL tự động: Chốt giờ ra, tính điểm, nâng hạng khách, giải phóng bàn
     */
    public boolean thanhToan(String maHoaDon) {
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