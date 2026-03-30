package quanlynhahang.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class HoaDonDAO {

    // 1. GỌI THỦ TỤC MỞ BÀN (sp_MoBan)
    public boolean moBanMoi(String maBan, String maKhachHang) {
        // Cú pháp gọi Stored Procedure trong Java: {call ten_sp(?, ?)}
        String sql = "{call sp_MoBan(?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maBan);

            // Nếu khách vãng lai (không có mã KH), truyền NULL vào SQL
            if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
                cs.setNull(2, java.sql.Types.VARCHAR);
            } else {
                cs.setString(2, maKhachHang);
            }

            cs.execute(); // Dùng execute() thay vì executeUpdate() cho SP
            return true;

        } catch (SQLException e) {
            System.err.println("Lỗi khi mở bàn: " + e.getMessage());
            return false;
        }
    }

    // 2. GỌI THỦ TỤC THANH TOÁN (sp_ThanhToanVaTichDiem)
    public boolean thanhToanHoaDon(String maHoaDon) {
        String sql = "{call sp_ThanhToanVaTichDiem(?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, maHoaDon);
            cs.execute();
            return true;

        } catch (SQLException e) {
            // Nếu SQL Server quăng RAISERROR, nó sẽ chạy vào đây
            System.err.println("Giao dịch thất bại: " + e.getMessage());
            return false;
        }
    }
}