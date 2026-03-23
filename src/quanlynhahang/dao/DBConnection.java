package quanlynhahang.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Thông số kết nối - Bạn hãy kiểm tra lại User và Pass của SQL Server máy mình
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhaHang;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "phamNguyen@"; // Thay bằng mật khẩu SQL của bạn

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Đăng ký Driver với hệ thống
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Mở kết nối
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Thiếu thư viện JDBC Driver trong thư mục lib!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể kết nối SQL Server. Kiểm tra Port 1433 hoặc User/Pass.");
            e.printStackTrace();
        }
        return conn;
    }

    // Hàm đóng kết nối an toàn
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}