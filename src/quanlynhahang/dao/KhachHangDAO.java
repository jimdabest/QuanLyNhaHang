package quanlynhahang.dao;

import quanlynhahang.dto.KhachHangDTO;
import java.sql.*;
import java.util.ArrayList;

public class KhachHangDAO implements IDAO<KhachHangDTO, String> {

    @Override
    public boolean insert(KhachHangDTO obj) {
        String sql = "INSERT INTO KhachHang (maKhachHang, maHang, tenKH, soDienThoai, tongChiTieu, ngayDangKy, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaKhachHang());
            ps.setString(2, obj.getMaHang());
            ps.setString(3, obj.getTenKH());
            ps.setString(4, obj.getSoDienThoai());
            ps.setDouble(5, obj.getTongChiTieu());
            // Chuyển từ java.util.Date sang java.sql.Date để lưu vào SQL
            ps.setDate(6, new java.sql.Date(obj.getNgayDangKy().getTime()));
            ps.setBoolean(7, obj.isTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(KhachHangDTO obj) {
        String sql = "UPDATE KhachHang SET maHang = ?, tenKH = ?, soDienThoai = ?, tongChiTieu = ?, ngayDangKy = ?, trangThai = ? WHERE maKhachHang = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaHang());
            ps.setString(2, obj.getTenKH());
            ps.setString(3, obj.getSoDienThoai());
            ps.setDouble(4, obj.getTongChiTieu());
            ps.setDate(5, new java.sql.Date(obj.getNgayDangKy().getTime()));
            ps.setBoolean(6, obj.isTrangThai());
            ps.setString(7, obj.getMaKhachHang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String key) {
        // Thực hiện xóa mềm bằng cách cập nhật trangThai = false để giữ lịch sử hóa đơn
        String sql = "UPDATE KhachHang SET trangThai = 0 WHERE maKhachHang = ?";
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
    public ArrayList<KhachHangDTO> getAll() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE trangThai = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new KhachHangDTO(
                        rs.getString("maKhachHang"),
                        rs.getString("maHang"),
                        rs.getString("tenKH"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("tongChiTieu"),
                        rs.getDate("ngayDangKy"),
                        rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public KhachHangDTO getById(String key) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ? OR soDienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhachHangDTO(
                        rs.getString("maKhachHang"),
                        rs.getString("maHang"),
                        rs.getString("tenKH"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("tongChiTieu"),
                        rs.getDate("ngayDangKy"),
                        rs.getBoolean("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}