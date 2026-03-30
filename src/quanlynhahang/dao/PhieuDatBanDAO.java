package quanlynhahang.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// Import DTO của bạn (nếu có)
// import quanlynhahang.dto.PhieuDatBanDTO;

public class PhieuDatBanDAO {

    // 1. GỌI THỦ TỤC HỦY ĐẶT BÀN (sp_HuyPhieuDat)
    public boolean huyDatBan(String maDatBan) {
        String sql = "{call sp_HuyPhieuDat(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maDatBan);
            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Lỗi hủy bàn: " + e.getMessage());
            return false;
        }
    }

    // 2. LẤY DANH SÁCH ĐẶT BÀN HÔM NAY TỪ VIEW (v_PhieuDatBanHomNay)
    // Bạn có thể đổi kiểu trả về thành ArrayList<PhieuDatBanDTO>
    public void inDanhSachKhachHenHomNay() {
        // Gọi View y hệt như gọi một Table bình thường
        String sql = "SELECT * FROM v_PhieuDatBanHomNay";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("--- DANH SÁCH KHÁCH ĐẶT BÀN HÔM NAY ---");
            while (rs.next()) {
                System.out.println("Bàn: " + rs.getString("MaBan")
                        + " | Khách: " + rs.getString("TenKhachHang")
                        + " | Giờ hẹn: " + rs.getString("GioKhachHen"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}