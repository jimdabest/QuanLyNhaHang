package quanlynhahang.dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp tiện ích quản lý kết nối đến cơ sở dữ liệu SQL Server.
 * Đọc cấu hình kết nối từ file .env thay vì hardcode trực tiếp trong mã nguồn.
 */
public class DBConnection {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory(System.getProperty("user.dir"))
            .filename(".env")
            .ignoreIfMissing()
            .load();

    private static final String SERVER = getEnv("DB_SERVER", "localhost");
    private static final String PORT = getEnv("DB_PORT", "1444");
    private static final String DATABASE = getEnv("DB_DATABASE", "QuanLyNhaHang");
    private static final String USER = getEnv("DB_USER", "sa");
    private static final String PASS = getEnv("DB_PASS", "");
    private static final String ENCRYPT = getEnv("DB_ENCRYPT", "true");
    private static final String TRUST_SERVER_CERTIFICATE = getEnv("DB_TRUST_SERVER_CERTIFICATE", "true");

    private static final String URL = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=%s;trustServerCertificate=%s;",
            SERVER, PORT, DATABASE, ENCRYPT, TRUST_SERVER_CERTIFICATE);

    /**
     * Mở kết nối đến SQL Server.
     * @return đối tượng Connection nếu kết nối thành công, null nếu thất bại
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Thiếu thư viện JDBC Driver trong thư mục lib!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể kết nối SQL Server. Kiểm tra file .env và thông số DB.");
            e.printStackTrace();
        }
        return null;
    }

    private static String getEnv(String key, String defaultValue) {
        String value = dotenv.get(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    /**
     * Đóng kết nối cơ sở dữ liệu một cách an toàn.
     * @param conn đối tượng Connection cần đóng
     */
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