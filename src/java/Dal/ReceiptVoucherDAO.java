package Dal;

import Context.DBContext;
import Models.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiptVoucherDAO {
    private Connection connection;

    public ReceiptVoucherDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean insertReceiptVoucher(ReceiptVoucher rv) {
        String sql = "INSERT INTO ReceiptVoucher (ShopID, EmployeeID, CustomerID, ReceiptDate, Amount, Note, Status, CreatedDate, TypeID, PaymentMethodID) "
           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rv.getShopID());
            ps.setInt(2, rv.getEmployeeID());
            if (rv.getCustomerID() != null) {
                ps.setInt(3, rv.getCustomerID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setTimestamp(4, new Timestamp(rv.getReceiptDate().getTime()));
            ps.setBigDecimal(5, rv.getAmount());
            ps.setString(6, rv.getNote());
            ps.setBoolean(7, rv.isStatus());
            ps.setTimestamp(8, new Timestamp(rv.getCreatedDate().getTime()));
            ps.setInt(9, rv.getTypeID());
            ps.setInt(10, rv.getPaymentMethodID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public List<ReceiptVoucher> getAllReceiptVouchers() {
        List<ReceiptVoucher> list = new ArrayList<>();
        String sql = "SELECT * FROM ReceiptVoucher";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToReceiptVoucher(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public ReceiptVoucher getReceiptVoucherByID(int id) {
        String sql = "SELECT * FROM ReceiptVoucher WHERE ReceiptVoucherID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReceiptVoucher(rs);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public boolean deleteReceiptVoucher(int id) {
        String sql = "DELETE FROM ReceiptVoucher WHERE ReceiptVoucherID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean updateReceiptVoucher(ReceiptVoucher rv) {
        String sql = "UPDATE ReceiptVoucher SET ShopID = ?, EmployeeID = ?, CustomerID = ?, ReceiptDate = ?, "
           + "Amount = ?, Note = ?, Status = ?, CreatedDate = ?, TypeID = ?, PaymentMethodID = ? "
           + "WHERE ReceiptVoucherID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rv.getShopID());
            ps.setInt(2, rv.getEmployeeID());
            if (rv.getCustomerID() != null) {
                ps.setInt(3, rv.getCustomerID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setTimestamp(4, new Timestamp(rv.getReceiptDate().getTime()));
            ps.setBigDecimal(5, rv.getAmount());
            ps.setString(6, rv.getNote());
            ps.setBoolean(7, rv.isStatus());
            ps.setTimestamp(8, new Timestamp(rv.getCreatedDate().getTime()));
            ps.setInt(9, rv.getTypeID());
            ps.setInt(10, rv.getPaymentMethodID());
ps.setInt(11, rv.getReceiptVoucherID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    private ReceiptVoucher mapResultSetToReceiptVoucher(ResultSet rs) throws SQLException {
        ReceiptVoucher rv = new ReceiptVoucher();
        rv.setReceiptVoucherID(rs.getInt("ReceiptVoucherID"));
        rv.setShopID(rs.getInt("ShopID"));
        rv.setEmployeeID(rs.getInt("EmployeeID"));

        int custID = rs.getInt("CustomerID");
        rv.setCustomerID(rs.wasNull() ? null : custID);

        rv.setReceiptDate(rs.getTimestamp("ReceiptDate"));
        rv.setAmount(rs.getBigDecimal("Amount"));
        rv.setNote(rs.getString("Note"));
        rv.setStatus(rs.getBoolean("Status"));
        rv.setCreatedDate(rs.getTimestamp("CreatedDate"));
        rv.setTypeID(rs.getInt("TypeID"));
        rv.setPaymentMethodID(rs.getInt("PaymentMethodID")); 
        return rv;
    }
public List<ReceiptVoucher> filterReceiptVouchers(
    Integer shopID, Integer employeeID, Integer typeID, 
    BigDecimal minAmount, BigDecimal maxAmount,
    Date fromDate, Date toDate, Integer paymentMethodID) {

    List<ReceiptVoucher> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM ReceiptVoucher WHERE 1=1");

    if (shopID != null) sql.append(" AND ShopID = ?");
    if (employeeID != null) sql.append(" AND EmployeeID = ?");
    if (typeID != null) sql.append(" AND TypeID = ?");
    if (minAmount != null) sql.append(" AND Amount >= ?");
    if (maxAmount != null) sql.append(" AND Amount <= ?");
    if (fromDate != null) sql.append(" AND ReceiptDate >= ?");
    if (toDate != null) sql.append(" AND ReceiptDate <= ?");
    if (paymentMethodID != null) sql.append(" AND PaymentMethodID = ?");

    sql.append(" ORDER BY ReceiptDate DESC");

    try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
        int index = 1;
        if (shopID != null) ps.setInt(index++, shopID);
        if (employeeID != null) ps.setInt(index++, employeeID);
        if (typeID != null) ps.setInt(index++, typeID);
        if (minAmount != null) ps.setBigDecimal(index++, minAmount);
        if (maxAmount != null) ps.setBigDecimal(index++, maxAmount);
        if (fromDate != null) ps.setTimestamp(index++, new Timestamp(fromDate.getTime()));
        if (toDate != null) ps.setTimestamp(index++, new Timestamp(toDate.getTime()));
        if (paymentMethodID != null) ps.setInt(index++, paymentMethodID);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToReceiptVoucher(rs));
            }
        }
    } catch (SQLException e) {
        Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
    }

    return list;
}

    // Test nhanh
    public static void main(String[] args) {
        try (Connection conn = new DBContext("ShopDB_Go1").getConnection()) {
            ReceiptVoucherDAO dao = new ReceiptVoucherDAO(conn);
            List<ReceiptVoucher> list = dao.getAllReceiptVouchers();
            for (ReceiptVoucher rv : list) {
                System.out.println(rv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
