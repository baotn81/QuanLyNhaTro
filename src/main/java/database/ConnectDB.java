package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "QuanLyNhaTro";

    private static final String USERNAME = "sa";
    private static final String PASSWORD = "12345";

    private static final String URL =
            "jdbc:sqlserver://" + SERVER + ":" + PORT +
                    ";databaseName=" + DATABASE +
                    ";encrypt=true;" +
                    "trustServerCertificate=true;" +
                    "loginTimeout=30;";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✅ Đã nạp Driver SQL Server thành công.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy Driver SQL Server JDBC!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (conn != null) {
                System.out.println("🔗 Kết nối Database thành công!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối Database!");
            System.err.println("Chi tiết: " + e.getMessage());
            System.err.println("Kiểm tra: Server, Port, Database Name, Username/Password");
        }
        return conn;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("🔌 Đã đóng kết nối Database.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Kiểm tra kết nối Database: THÀNH CÔNG");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Kiểm tra kết nối thất bại: " + e.getMessage());
        }
        return false;
    }

    public static String getConnectionURL() {
        return URL;
    }
}