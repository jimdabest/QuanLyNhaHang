package quanlynhahang.dao;

import quanlynhahang.dto.KhachHangDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KhachHangDAO {

    // 1. LẤY DANH SÁCH KHÁCH HÀNG
    public ArrayList<KhachHangDTO> getAll() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new KhachHangDTO(
                        rs.getString("MaKhachHang"),
                        rs.getString("MaHang"),
                        rs.getString("TenKH"),
                        rs.getString("SoDienThoai"),
                        rs.getDouble("TongChiTieu"),
                        rs.getDate("NgayDangKy"), // Dùng java.sql.Date
                        rs.getInt("TrangThai") == 1
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. THÊM KHÁCH HÀNG MỚI (Dành cho chức năng Đăng ký thẻ thành viên)
    public boolean insert(KhachHangDTO kh) {
        String sql = "INSERT INTO KhachHang (MaKhachHang, MaHang, TenKH, SoDienThoai, TongChiTieu, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getMaHang()); // Mặc định thường là H01 (Thành viên Đồng)
            ps.setString(3, kh.getTenKH());
            ps.setString(4, kh.getSoDienThoai());
            ps.setDouble(5, kh.getTongChiTieu());
            ps.setInt(6, kh.isTrangThai() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm khách: Trùng số điện thoại hoặc mã KH!");
            return false;
        }
    }

    // 3. TÌM KHÁCH BẰNG SỐ ĐIỆN THOẠI (Lúc thanh toán hỏi số điện thoại để tích điểm)
    public KhachHangDTO timKhachHangTheoSDT(String sdt) {
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhachHangDTO(
                        rs.getString("MaKhachHang"),
                        rs.getString("MaHang"),
                        rs.getString("TenKH"),
                        rs.getString("SoDienThoai"),
                        rs.getDouble("TongChiTieu"),
                        rs.getDate("NgayDangKy"),
                        rs.getInt("TrangThai") == 1
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}