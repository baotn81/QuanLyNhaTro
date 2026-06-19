import java.sql.*;

public class QueryRows {
    public static void main(String[] args) {
        String server = "localhost";
        String port = "1433";
        String database = "QuanLyNhaTro";
        String username = "sa";
        String password = "12345";
        String url = "jdbc:sqlserver://" + server + ":" + port +
                ";databaseName=" + database +
                ";encrypt=true;" +
                "trustServerCertificate=true;" +
                "loginTimeout=30;";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                System.out.println("Connection successful!");
                String sql = "SELECT * FROM HoaDon";
                try (PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("MaHD: %s | MaKhach: %s | MaPhong: %s | TrangThai (Raw): %s\n",
                                rs.getString("MaHD"),
                                rs.getString("MaKhach"),
                                rs.getString("MaPhong"),
                                rs.getObject("TrangThai")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
