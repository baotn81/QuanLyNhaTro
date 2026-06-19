import java.sql.*;

public class QueryConstraints {
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
                DatabaseMetaData metaData = conn.getMetaData();
                
                System.out.println("\n--- Foreign Keys in table 'HoaDon' ---");
                try (ResultSet rs = metaData.getImportedKeys(null, null, "HoaDon")) {
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        String pkTableName = rs.getString("PKTABLE_NAME");
                        String pkColumnName = rs.getString("PKCOLUMN_NAME");
                        String fkColumnName = rs.getString("FKCOLUMN_NAME");
                        System.out.printf("FK Column: %-15s References: %s(%s)\n", 
                                          fkColumnName, pkTableName, pkColumnName);
                    }
                    if (!found) {
                        System.out.println("No foreign keys found.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
