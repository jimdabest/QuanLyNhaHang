package quanlynhahang.dao;

import quanlynhahang.dto.MonAnDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MonAnDAO {

    // 1. LẤY DANH SÁCH TẤT CẢ MÓN ĂN
    public ArrayList<MonAnDTO> getAll() {
        ArrayList<MonAnDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM MonAn";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MonAnDTO mon = new MonAnDTO(
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getString("PhanLoai"),
                        rs.getDouble("GiaHienTai"),
                        rs.getInt("TrangThaiPhucVu") == 1 // Convert BIT trong SQL ra boolean
                );
                list.add(mon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. THÊM MÓN MỚI
    public boolean insert(MonAnDTO mon) {
        String sql = "INSERT INTO MonAn (MaMon, TenMon, PhanLoai, GiaHienTai, TrangThaiPhucVu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getMaMon());
            ps.setString(2, mon.getTenMon());
            ps.setString(3, mon.getPhanLoai());
            ps.setDouble(4, mon.getGiaHienTai());
            ps.setInt(5, mon.isTrangThaiPhucVu() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // 3. CẬP NHẬT MÓN ĂN
    public boolean update(MonAnDTO mon) {
        String sql = "UPDATE MonAn SET TenMon=?, PhanLoai=?, GiaHienTai=?, TrangThaiPhucVu=? WHERE MaMon=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getTenMon());
            ps.setString(2, mon.getPhanLoai());
            ps.setDouble(3, mon.getGiaHienTai());
            ps.setInt(4, mon.isTrangThaiPhucVu() ? 1 : 0);
            ps.setString(5, mon.getMaMon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // 4. "XÓA MỀM" (Ngừng phục vụ món này để không ảnh hưởng Hóa đơn cũ)
    public boolean xoaMem(String maMon) {
        String sql = "UPDATE MonAn SET TrangThaiPhucVu = 0 WHERE MaMon=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}