
package lab5.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public
class DbSqlServer {

    // Sửa đúng thông tin của bạn
    // Nếu dùng SQL Auth:
    // jdbc:sqlserver://localhost:1433;databaseName=Lab5_KiemThu;encrypt=true;trustServerCertificate=true;user=sa;password=123;
    //
    // Nếu dùng Windows Auth (trên Windows):
    // jdbc:sqlserver://localhost:1433;databaseName=Lab5_KiemThu;encrypt=true;trustServerCertificate=true;integratedSecurity=true;
  private
    static final String URL =
        "jdbc:sqlserver://localhost:1433;" +
        "databaseName=Lab5_KiemThu;" +
        "encrypt=true;trustServerCertificate=true;" +
        "user=sa;password=123;";

  public
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
