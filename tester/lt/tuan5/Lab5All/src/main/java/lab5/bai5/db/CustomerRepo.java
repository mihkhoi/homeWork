
package lab5.bai5.db;

import lab5.common.db.DbSqlServer;

import java.sql.*;

public
class CustomerRepo {

  public
    boolean existsCustomerId(String customerId) throws Exception {
        String sql = "SELECT 1 FROM Customers WHERE CustomerId = ?;";
        try(Connection c = DbSqlServer.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

  public
    boolean existsEmail(String email) throws Exception {
        String sql = "SELECT 1 FROM Customers WHERE Email = ?;";
        try(Connection c = DbSqlServer.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

  public
    void insert(Customer cst) throws Exception {
        String sql = "INSERT INTO Customers(CustomerId, FullName, Email, Phone, Address, PasswordHash, Dob, Gender) " +
                     "VALUES(?,?,?,?,?,?,?,?);";
        try(Connection c = DbSqlServer.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cst.customerId);
            ps.setString(2, cst.fullName);
            ps.setString(3, cst.email);
            ps.setString(4, cst.phone);
            ps.setString(5, cst.address);
            ps.setString(6, cst.passwordHash);
            if (cst.dob == null)
                ps.setNull(7, Types.DATE);
            else
                ps.setDate(7, Date.valueOf(cst.dob));
            ps.setString(8, cst.gender);
            ps.executeUpdate();
        }
    }

  public
    static class Customer {
      public
        String customerId, fullName, email, phone, address, passwordHash;
      public
        String dob; // yyyy-mm-dd hoặc null
      public
        String gender; // Nam/Nữ/Khác hoặc null
    }
}
