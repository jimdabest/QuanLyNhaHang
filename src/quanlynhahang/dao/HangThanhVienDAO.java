package quanlynhahang.dao;

import quanlynhahang.dto.HangThanhVienDTO;
import java.sql.*;
import java.util.ArrayList;

public class HangThanhVienDAO implements IDAO<HangThanhVienDTO, String> {

    @Override
    public boolean insert(HangThanhVienDTO obj) {
        String sql = "INSERT INTO HangThanhVien (maHang, tenHang, mucGiamGia, dieuKienTongChiTieu, quyenLoiKhac) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaHang());
            ps.setString(2, obj.getTenHang());
            ps.setDouble(3, obj.getMucGiamGia());
            ps.setDouble(4, obj.getDieuKienTongChiTieu());
            ps.setString(5, obj.getQuyenLoiKhac());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(HangThanhVienDTO obj) {
        String sql = "UPDATE HangThanhVien SET tenHang = ?, mucGiamGia = ?, dieuKienTongChiTieu = ?, quyenLoiKhac = ? WHERE maHang = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getTenHang());
            ps.setDouble(2, obj.getMucGiamGia());
            ps.setDouble(3, obj.getDieuKienTongChiTieu());
            ps.setString(4, obj.getQuyenLoiKhac());
            ps.setString(5, obj.getMaHang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String key) {
        String sql = "DELETE FROM HangThanhVien WHERE maHang = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể xóa hạng thành viên do có liên kết khóa ngoại với Khách Hàng.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<HangThanhVienDTO> getAll() {
        ArrayList<HangThanhVienDTO> list = new ArrayList<>();
        // Sắp xếp tăng dần theo điều kiện chi tiêu để dễ hiển thị trên GUI
        String sql = "SELECT * FROM HangThanhVien ORDER BY dieuKienTongChiTieu ASC";
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new HangThanhVienDTO(
                        rs.getString("maHang"),
                        rs.getString("tenHang"),
                        rs.getDouble("mucGiamGia"),
                        rs.getDouble("dieuKienTongChiTieu"),
                        rs.getString("quyenLoiKhac")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public HangThanhVienDTO getById(String key) {
        String sql = "SELECT * FROM HangThanhVien WHERE maHang = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new HangThanhVienDTO(
                        rs.getString("maHang"),
                        rs.getString("tenHang"),
                        rs.getDouble("mucGiamGia"),
                        rs.getDouble("dieuKienTongChiTieu"),
                        rs.getString("quyenLoiKhac"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}