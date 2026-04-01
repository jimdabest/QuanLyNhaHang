package quanlynhahang.dao;

import quanlynhahang.dto.ChiTietHoaDonDTO;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietHoaDonDAO implements IDAO<ChiTietHoaDonDTO, String> {

    @Override
    public boolean insert(ChiTietHoaDonDTO obj) {
        // Khi thêm món, Trigger SQL của bạn sẽ tự động gọi hàm fn_TongTienHoaDon để cập nhật bảng HoaDon
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maMon, soLuong, giaBan, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaHoaDon());
            ps.setString(2, obj.getMaMon());
            ps.setInt(3, obj.getSoLuong());
            ps.setDouble(4, obj.getGiaBan());
            ps.setString(5, obj.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ChiTietHoaDonDTO obj) {
        // Cập nhật số lượng hoặc ghi chú của một món cụ thể trong hóa đơn
        String sql = "UPDATE ChiTietHoaDon SET soLuong = ?, ghiChu = ? WHERE maHoaDon = ? AND maMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, obj.getSoLuong());
            ps.setString(2, obj.getGhiChu());
            ps.setString(3, obj.getMaHoaDon());
            ps.setString(4, obj.getMaMon());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String key) {
        // Interface nhận 1 tham số String, nên hàm này hiểu là xóa TẤT CẢ các món của 1 mã hóa đơn
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
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
     * Hàm bổ sung: Xóa đích danh 1 món khỏi hóa đơn
     * Rất quan trọng khi nhân viên bấm nút "Xóa món" trên JTable
     */
    public boolean deleteSpecificMon(String maHoaDon, String maMon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ps.setString(2, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<ChiTietHoaDonDTO> getAll() {
        ArrayList<ChiTietHoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ChiTietHoaDonDTO(
                        rs.getString("maHoaDon"),
                        rs.getString("maMon"),
                        rs.getInt("soLuong"),
                        rs.getDouble("giaBan"),
                        rs.getString("ghiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Hàm bổ sung: Lấy danh sách món ăn theo mã hóa đơn
     * Dùng để hiển thị lên JTable bên màn hình gọi món
     */
    public ArrayList<ChiTietHoaDonDTO> getByHoaDonId(String maHoaDon) {
        ArrayList<ChiTietHoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietHoaDonDTO(
                        rs.getString("maHoaDon"),
                        rs.getString("maMon"),
                        rs.getInt("soLuong"),
                        rs.getDouble("giaBan"),
                        rs.getString("ghiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ChiTietHoaDonDTO getById(String key) {
        // Do dùng khóa chính tổng hợp nên hàm 1 tham số này ít được dùng trực tiếp
        return null;
    }
}