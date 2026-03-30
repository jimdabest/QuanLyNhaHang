package quanlynhahang.dao;

import quanlynhahang.dto.ChiTietHoaDonDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChiTietHoaDonDAO {

    // 1. ORDER: THÊM MÓN HOẶC CỘNG DỒN VÀO HÓA ĐƠN
    public boolean themMonVaoHoaDon(String maHoaDon, String maMon, int soLuongThem, double giaBan, String ghiChu) {
        // Kiểm tra xem món này đã có trong bill chưa
        String checkSql = "SELECT SoLuong FROM ChiTietHoaDon WHERE MaHoaDon = ? AND MaMon = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            checkPs.setString(1, maHoaDon);
            checkPs.setString(2, maMon);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // ĐÃ TỒN TẠI -> Cập nhật cộng dồn số lượng
                int slHienTai = rs.getInt("SoLuong");
                String updateSql = "UPDATE ChiTietHoaDon SET SoLuong = ? WHERE MaHoaDon = ? AND MaMon = ?";
                try(PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setInt(1, slHienTai + soLuongThem);
                    updatePs.setString(2, maHoaDon);
                    updatePs.setString(3, maMon);
                    return updatePs.executeUpdate() > 0;
                }
            } else {
                // CHƯA TỒN TẠI -> Thêm dòng mới
                String insertSql = "INSERT INTO ChiTietHoaDon (MaHoaDon, MaMon, SoLuong, GiaBan, GhiChu) VALUES (?, ?, ?, ?, ?)";
                try(PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setString(1, maHoaDon);
                    insertPs.setString(2, maMon);
                    insertPs.setInt(3, soLuongThem);
                    insertPs.setDouble(4, giaBan);
                    insertPs.setString(5, ghiChu);
                    return insertPs.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi gọi món: " + e.getMessage());
            return false;
        }
    }

    // 2. LẤY DANH SÁCH CÁC MÓN KHÁCH ĐANG ĂN ĐỂ IN RA BILL
    public ArrayList<ChiTietHoaDonDTO> getChiTietCuaHoaDon(String maHoaDon) {
        ArrayList<ChiTietHoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHoaDon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietHoaDonDTO(
                        rs.getString("MaHoaDon"),
                        rs.getString("MaMon"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("GiaBan"),
                        rs.getString("GhiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. XÓA MÓN KHỎI BILL (Khi khách trả lại món)
    public boolean xoaMonKhoiHoaDon(String maHoaDon, String maMon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHoaDon = ? AND MaMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            ps.setString(2, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}