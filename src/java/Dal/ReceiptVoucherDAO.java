/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Context.DBContext;
import Models.ReceiptVoucher;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class ReceiptVoucherDAO {
      private final Connection connection;

    public ReceiptVoucherDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy tất cả phiếu thu
    public List<ReceiptVoucher> getAll() {
        List<ReceiptVoucher> list = new ArrayList<>();
        String sql = "SELECT * FROM ReceiptVoucher";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToReceiptVoucher(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Thêm phiếu thu
    public boolean insert(ReceiptVoucher rv) {
        String sql = "INSERT INTO ReceiptVoucher (ShopID, EmployeeID, CustomerID, ReceiptDate, Amount, Note, Status, CreatedDate) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rv.getShopID());
            ps.setInt(2, rv.getEmployeeID());

            if (rv.getCustomerID() != null) {
                ps.setInt(3, rv.getCustomerID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setTimestamp(4, new Timestamp(rv.getReceiptDate().getTime()));
            ps.setDouble(5, rv.getAmount());
            ps.setString(6, rv.getNote());
            ps.setBoolean(7, rv.isStatus());
            ps.setTimestamp(8, new Timestamp(rv.getCreatedDate().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Lấy phiếu thu theo ID
    public ReceiptVoucher getByID(int id) {
        String sql = "SELECT * FROM ReceiptVoucher WHERE ReceiptVoucherID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReceiptVoucher(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Cập nhật phiếu thu
    public boolean update(ReceiptVoucher rv) {
        String sql = "UPDATE ReceiptVoucher SET ShopID=?, EmployeeID=?, CustomerID=?, ReceiptDate=?, Amount=?, Note=?, Status=?, CreatedDate=? "
                   + "WHERE ReceiptVoucherID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rv.getShopID());
            ps.setInt(2, rv.getEmployeeID());
            if (rv.getCustomerID() != null) {
                ps.setInt(3, rv.getCustomerID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setTimestamp(4, new Timestamp(rv.getReceiptDate().getTime()));
            ps.setDouble(5, rv.getAmount());
            ps.setString(6, rv.getNote());
            ps.setBoolean(7, rv.isStatus());
            ps.setTimestamp(8, new Timestamp(rv.getCreatedDate().getTime()));
            ps.setInt(9, rv.getReceiptVoucherID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Xoá phiếu thu
    public boolean delete(int id) {
        String sql = "DELETE FROM ReceiptVoucher WHERE ReceiptVoucherID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Ánh xạ ResultSet -> ReceiptVoucher
    private ReceiptVoucher mapResultSetToReceiptVoucher(ResultSet rs) throws SQLException {
        ReceiptVoucher rv = new ReceiptVoucher(
                rs.getInt("ShopID"),
                rs.getInt("EmployeeID"),
                rs.getObject("CustomerID") != null ? rs.getInt("CustomerID") : null,
                rs.getTimestamp("ReceiptDate"),
                rs.getDouble("Amount"),
                rs.getString("Note"),
                rs.getBoolean("Status"),
                rs.getTimestamp("CreatedDate")
        );
        rv.setReceiptVoucherID(rs.getInt("ReceiptVoucherID"));
        return rv;
    }
    public static void main(String[] args) {
    try (Connection conn = new DBContext("Test").getConnection()) {
        ReceiptVoucherDAO dao = new ReceiptVoucherDAO(conn);
        ReceiptVoucher rv = new ReceiptVoucher(1,2,1,
                new Date(),
                150000,
                "Thu tiền hàng",
                true, new Date());

        dao.insert(rv);
        dao.getAll().forEach(System.out::println);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
