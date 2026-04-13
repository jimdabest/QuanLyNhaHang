package quanlynhahang.test;

import
        quanlynhahang.dao.DBConnection;
import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("--- Đang thử kết nối đến DB: QuanLyNhaHang ---");

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("========================================");
            System.out.println("KẾT NỐI THÀNH CÔNG!");
            System.out.println("Database 'QuanLyNhaHang' đã sẵn sàng.");
            System.out.println("========================================");

            DBConnection.closeConnection(conn);
        } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("THẤT BẠI: Vui lòng kiểm tra:");
            System.out.println("- Tên Database có đúng là 'QuanLyNhaHang' chưa?");
            System.out.println("- SQL Server Service đã chạy chưa?");
            System.out.println("- Mật khẩu tài khoản 'sa' đúng chưa?");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
}