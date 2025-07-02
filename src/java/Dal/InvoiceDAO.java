/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Invoice;
import Models.InvoiceDetail;

import java.util.ArrayList;

import java.sql.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duckh
 */
public class InvoiceDAO {

    private Connection connection;

    public InvoiceDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Invoice> getAllInvoices() {
        String sql = "Select * From Invoice";
        List<Invoice> list = new ArrayList<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(new Invoice(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getTimestamp(5),
                        rs.getDouble(6),
                        rs.getString(7),
                        rs.getBoolean(8)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public int addInvoice(Invoice i) throws SQLException {
        String sqlInsert = "INSERT INTO [dbo].[Invoice]\n"
                + "           ([CustomerID],[EmployeeID],[ShopID],[InvoiceDate],[TotalAmount],[Note],[Status])\n"
                + "VALUES (?,?,?,?,?,?,?)";

        int generatedId = -1;
        long startTime = System.currentTimeMillis();

        try (PreparedStatement ptmInsert = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ptmInsert.setInt(1, i.getCustomerID());
            ptmInsert.setInt(2, i.getEmployeeID());
            ptmInsert.setInt(3, i.getShopID());
            ptmInsert.setTimestamp(4, Timestamp.from(Instant.now()));
            ptmInsert.setDouble(5, i.getTotalAmount());
            ptmInsert.setString(6, i.getNote());
            ptmInsert.setBoolean(7, i.isStatus());

            long preUpdate = System.currentTimeMillis();
            int affectedRows = ptmInsert.executeUpdate();
            long postUpdate = System.currentTimeMillis();
            System.out.println("Time for executeUpdate: " + (postUpdate - preUpdate) + "ms");

            if (affectedRows > 0) {
                try (java.sql.ResultSet rs = ptmInsert.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }

                } catch (SQLException ex) {

                    ex.printStackTrace();
                    generatedId = -1;
                }

            }
        }
        return generatedId;
    }

    public boolean deleteInvoice(int invoiceID) {
        Invoice invoice = searchInvoice(invoiceID);
        if (invoice == null) {
            System.out.println("Không tìm thấy hóa đơn: " + invoiceID);
            return false;
        }

        if (invoice.isStatus()) {
            System.out.println("Không thể xóa hóa đơn đã thanh toán");
            return false;
        }
        InvoiceDetailDAO detailDAO = new InvoiceDetailDAO(connection);

        List<InvoiceDetail> detailsDelete = detailDAO.getDetailByInvoiceID(invoiceID);
        for (InvoiceDetail invoiceDetail : detailsDelete) {
            if (!detailDAO.deleteByDetailID(invoiceDetail.getInvoiceDetailID())) {
                System.out.println("Lỗi: Không thể hoàn trả tồn kho và xóa chi tiết hóa đơn ID: " + invoiceDetail.getInvoiceDetailID());
                return false;
            }
        }

        String sql = "DELETE FROM Invoice WHERE InvoiceID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, invoiceID);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;

    }

    public Invoice searchInvoice(int invoiceID) {
        String sql = "SELECT * FROM Invoice WHERE InvoiceID = ?";
        List<Invoice> l = new ArrayList<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return new Invoice(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getInt(4), rs.getTimestamp(5),
                        rs.getDouble(6), rs.getString(7), rs.getBoolean(8));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Invoice> getInvoicesByCustomerID(int customerID) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT i.*, c.CustomerName, s.ShopName, e.FullName AS EmployeeName \n"
                + "FROM [dbo].[Invoice] i \n"
                + "JOIN [dbo].[Customer] c ON i.CustomerID = c.CustomerID\n"
                + "JOIN [dbo].[Shop] s ON i.ShopID = s.ShopID\n"
                + "JOIN [dbo].[Employee] e ON i.EmployeeID = e.EmployeeID\n"
                + "WHERE i.CustomerID = ? ORDER BY i.InvoiceID DESC";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, customerID);
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    list.add(new Invoice(
                            rs.getInt("InvoiceID"),
                            rs.getInt("CustomerID"),
                            rs.getString("CustomerName"),
                            rs.getInt("EmployeeID"),
                            rs.getString("EmployeeName"), // Giả sử Invoice model có trường này
                            rs.getInt("ShopID"),
                            rs.getTimestamp("InvoiceDate"),
                            rs.getDouble("TotalAmount"),
                            rs.getString("Note"),
                            rs.getBoolean("Status"),
                            rs.getString("ShopName")
                    ));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // =============================================================
    public List<Invoice> getInvoicesByDateRange_UsingCastInSQL(Date startDate, Date endDate, int pageIndex, int pageSize) {
        List<Invoice> invoices = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT i.*, c.CustomerName, s.ShopName, e.FullName AS EmployeeName \n"
                + "FROM [dbo].[Invoice] i \n"
                + "JOIN [dbo].[Customer] c ON i.CustomerID = c.CustomerID\n"
                + "JOIN [dbo].[Shop] s ON i.ShopID = s.ShopID\n"
                + "JOIN [dbo].[Employee] e ON i.EmployeeID = e.EmployeeID\n"
                + "WHERE 1=1 ");

        if (startDate != null) {
            sqlBuilder.append(" AND CAST(i.InvoiceDate AS DATE) >= ?");
        }
        if (endDate != null) {
            sqlBuilder.append(" AND CAST(i.InvoiceDate AS DATE) <= ?");
        }

        sqlBuilder.append(" ORDER BY i.InvoiceID DESC ");
        sqlBuilder.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement pstmt = connection.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            if (startDate != null) {
                pstmt.setDate(paramIndex++, startDate);
            }
            if (endDate != null) {
                pstmt.setDate(paramIndex++, endDate);
            }

            int offset = (pageIndex - 1) * pageSize;
            pstmt.setInt(paramIndex++, offset);
            pstmt.setInt(paramIndex++, pageSize);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice(
                            rs.getInt("InvoiceID"),
                            rs.getInt("CustomerID"),
                            rs.getString("CustomerName"),
                            rs.getInt("EmployeeID"),
                            rs.getString("EmployeeName"),
                            rs.getInt("ShopID"),
                            rs.getTimestamp("InvoiceDate"),
                            rs.getDouble("TotalAmount"),
                            rs.getString("Note"),
                            rs.getBoolean("Status"),
                            rs.getString("ShopName")
                    );
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public int getTotalInvoiceCount_UsingCastInSQL(Date startDate, Date endDate) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM [dbo].[Invoice] i WHERE 1=1 ");

        if (startDate != null) {
            sqlBuilder.append(" AND CAST(i.InvoiceDate AS DATE) >= ?");
        }
        if (endDate != null) {
            sqlBuilder.append(" AND CAST(i.InvoiceDate AS DATE) <= ?");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            if (startDate != null) {
                pstmt.setDate(paramIndex++, startDate);
            }
            if (endDate != null) {
                pstmt.setDate(paramIndex++, endDate);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Invoice> searchInvoiceByKey(String key) {
        String sql = "SELECT i.*, c.CustomerName, i.EmployeeID, i.ShopID, s.ShopName\n"
                + "                FROM Invoice i\n"
                + "                JOIN Customer c ON i.CustomerID = c.CustomerID\n"
                + "                JOIN Shop s ON i.ShopID = s.ShopID \n"
                + "               WHERE i.InvoiceID  LIKE ? OR c.CustomerName COLLATE Latin1_General_CI_AI LIKE ?";
        List<Invoice> l = new ArrayList<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + key + "%");
            ptm.setString(2, "%" + key + "%");
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {

                Invoice i = new Invoice(
                        rs.getInt("InvoiceID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getTimestamp("InvoiceDate"),
                        rs.getDouble("TotalAmount"),
                        rs.getString("Note"),
                        rs.getBoolean("Status")
                );
                i.setCustomerName(rs.getString("CustomerName"));
                i.setShopName(rs.getString("shopName"));

                l.add(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return l;
    }

    public boolean updateInvoice(Invoice i) {
        String sql = "UPDATE [dbo].[Invoice]\n"
                + "   SET \n"
                + "      [CustomerID] = ?\n"
                + "      ,[EmployeeID] = ?\n"
                + "      ,[ShopID] = ?\n"
                + "      ,[InvoiceDate] = ?\n"
                + "      ,[TotalAmount] = ?\n"
                + "      ,[Note] = ?\n"
                + "      ,[Status] = ?\n"
                + " WHERE InvoiceID =?";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, i.getCustomerID());
            ptm.setInt(2, i.getEmployeeID());
            ptm.setInt(3, i.getShopID());
            ptm.setTimestamp(4, i.getInvoiceDate());
            ptm.setDouble(5, i.getTotalAmount());
            ptm.setString(6, i.getNote());
            ptm.setBoolean(7, i.isStatus());
            ptm.setInt(8, i.getInvoiceID());

            int n = ptm.executeUpdate();
            return n > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;

        }
    }

// phan trang
    public List<Invoice> getInvoicesByPage(int pageIndex, int pageSize) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT i.*, c.CustomerName, s.ShopName \n"
                + "FROM [dbo].[Invoice] i \n"
                + "JOIN [dbo].[Customer] c ON i.CustomerID = c.CustomerID\n"
                + "JOIN [dbo].[Shop] s ON i.ShopID = s.ShopID\n"
                + "ORDER BY i.InvoiceID DESC \n"
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            int offset = (pageIndex - 1) * pageSize;
            ptm.setInt(1, offset);
            ptm.setInt(2, pageSize);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(new Invoice(
                        rs.getInt("InvoiceID"),
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getTimestamp("InvoiceDate"),
                        rs.getDouble("TotalAmount"),
                        rs.getString("Note"),
                        rs.getBoolean("Status"),
                        rs.getString("ShopName")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public int getTotalInvoiceCount() {
        String sql = "SELECT COUNT(*) FROM Invoice";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean updateInvoiceCustomer(Invoice invoice) {
        String sql = "UPDATE [dbo].[Invoice] SET [CustomerID] = ? WHERE InvoiceID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, invoice.getCustomerID());
            ptm.setInt(2, invoice.getInvoiceID());
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateInvoiceStatus(int invoiceID, boolean newStatus) {
        String sql = "UPDATE [dbo].[Invoice] SET [Status] = ? WHERE InvoiceID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setBoolean(1, newStatus);
            ptm.setInt(2, invoiceID);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean finalizeInvoice(int invoiceID, int customerID) {
        String sql = "UPDATE Invoices SET Status = ?, CustomerID = ? WHERE InvoiceID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, true); // Set status to true (đã thanh toán)
            ps.setInt(2, customerID); // Gán customerID vào hóa đơn
            ps.setInt(3, invoiceID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi finalizeInvoice: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {

        DBContext connection = new DBContext("SWP1");
        InvoiceDAO dao = new InvoiceDAO(connection.getConnection());
        int pageIndex = 2;
        int pageSize = 2;

        List<Invoice> invoices1 = dao.getInvoicesByPage(pageIndex, pageSize);
        for (Invoice inv : invoices1) {
            System.out.printf("%s | %s | %s | %s | %s | %s | %.2f | %s | %b%n",
                    inv.getInvoiceID(),
                    inv.getCustomerID(),
                    inv.getCustomerName(),
                    inv.getEmployeeID(),
                    inv.getShopID(),
                    inv.getInvoiceDate(),
                    inv.getTotalAmount(),
                    inv.getNote(),
                    inv.isStatus());
        }

//        Invoice newInvoice = new Invoice(
//                "INV10020",
//                "CUST004",
//                "EMP002",
//                "SHOP001",
//                Timestamp.from(Instant.now()),
//                2000000.0,
//                "Mua quần áo mùa đông",
//                false);
//        dao.addInvoice(newInvoice);
//        String sql = "SELECT *\n"
//                + "  FROM [dbo].[Invoice]";
//        for (Invoice inv : dao.getAllInvoices(sql)) {
//            System.out.printf("%s  %s | %s | %s | %s | %.2f | %s | %b%n",
//                    inv.getInvoiceID(),
//                    inv.getCustomerID(),
//                    inv.getEmployeeID(),
//                    inv.getShopID(),
//                    inv.getInvoiceDate(),
//                    inv.getTotalAmount(),
//                    inv.getNote(),
//                    inv.isStatus());
//        }
        //update hoa don
//        Invoice invoiceToUpdate = dao.searchInvoice("INV1005");
//        if (invoiceToUpdate != null) {
//            System.out.println("Tìm thấy hóa đơn: " + invoiceToUpdate.getInvoiceID());
//            dao.updateInvoice(new Invoice(invoiceToUpdate.getInvoiceID(),
//                    "C001", "E001", "S002", Timestamp.from(Instant.now()), 0.0, "Mua áo khoác", false));
//            System.out.println("Đã update hóa đơn.");
//            Invoice updatedInvoice = dao.searchInvoice("INV1005");
//            System.out.printf("%s  %s | %s | %s | %s | %.2f | %s | %b%n",
//                    updatedInvoice.getInvoiceID(),
//                    updatedInvoice.getCustomerID(),
//                    updatedInvoice.getEmployeeID(),
//                    updatedInvoice.getShopID(),
//                    updatedInvoice.getInvoiceDate(),
//                    updatedInvoice.getTotalAmount(),
//                    updatedInvoice.getNote(),
//                    updatedInvoice.isStatus());
//        } else {
//            System.out.println("Cập nhật hóa đơn thất bại.");
//        }
//            //delete hoa don
//        Invoice i = dao.searchInvoice("INV10020");
//
//        if (i != null) {
//            boolean success = dao.deleteInvoice(i.getInvoiceID());
//            if (success) {
//                System.out.println("Xóa đơn thành công.");
//            } else {
//                System.out.println("Không xóa được hóa đơn.");
//            }
//
//            List<Invoice> invoices = dao.getAllInvoices("SELECT * FROM Invoice");
//            for (Invoice inv : invoices) {
//                System.out.printf("%s | %s | %s | %s | %s | %.2f | %s | %b%n",
//                        inv.getInvoiceID(),
//                        inv.getCustomerID(),
//                        inv.getEmployeeID(),
//                        inv.getShopID(),
//                        inv.getInvoiceDate(),
//                        inv.getTotalAmount(),
//                        inv.getNote(),
//                        inv.isStatus());
//            }
//
//        } else {
//            System.out.println("Không tìm thấy hóa đơn.");
//        }
    }
}
