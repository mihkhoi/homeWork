
package lab5.bai4;

import lab5.common.db.DbSqlServer;
import java.sql.Connection;
import java.sql.PreparedStatement;

public
class PaymentHistoryRepo {
  public
    void insert(String type, int age, int payment) throws Exception {
        String sql = "INSERT INTO PaymentHistory(PatientType, Age, Payment) VALUES (?,?,?)";
        try(Connection c = DbSqlServer.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setInt(2, age);
            ps.setInt(3, payment);
            ps.executeUpdate();
        }
    }
}
