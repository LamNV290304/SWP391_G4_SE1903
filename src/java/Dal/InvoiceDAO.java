/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import DTO.ShopOwnerRevenuaDto;
import Models.Invoice;
import Models.InvoiceDetail;
import java.math.BigDecimal;
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

        String sql = "SELECT InvoiceID, CustomerID, EmployeeID, ShopID, InvoiceDate, "
                + "TotalAmount, VatAmount, VATRateID, Note, Status "
                + "FROM Invoice";
        List<Invoice> list = new ArrayList<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(new Invoice(
                        rs.getInt("InvoiceID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getTimestamp("InvoiceDate"),
                        rs.getBigDecimal("TotalAmount"),
                        rs.getBigDecimal("VatAmount"),
                        rs.getInt("VATRateID"),
                        rs.getString("Note"),
                        rs.getBoolean("Status")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public int addInvoice(Invoice i) throws SQLException {
        String sqlInsert = "INSERT INTO [dbo].[Invoice]\n"
                + "           ([CustomerID],[EmployeeID],[ShopID],[InvoiceDate],[TotalAmount],VatAmount, VATRateID,[Note],[Status])\n"
                + "VALUES (?,?,?,?,?,?,?,?,?)";

        int generatedId = -1;
        long startTime = System.currentTimeMillis();

        try (PreparedStatement ptmInsert = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ptmInsert.setInt(1, i.getCustomerID());
            ptmInsert.setInt(2, i.getEmployeeID());
            ptmInsert.setInt(3, i.getShopID());
            ptmInsert.setTimestamp(4, Timestamp.from(Instant.now()));
            ptmInsert.setBigDecimal(5, i.getTotalAmount());
            ptmInsert.setBigDecimal(6, i.getVatAmount());
            ptmInsert.setInt(7, i.getVatRateID());
            ptmInsert.setString(8, i.getNote());
            ptmInsert.setBoolean(9, i.isStatus());

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
        String sql = "SELECT InvoiceID, CustomerID, EmployeeID, ShopID, InvoiceDate, "
                + "TotalAmount, VatAmount, VATRateID, Note, Status "
                + "FROM Invoice WHERE InvoiceID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return new Invoice(
                        rs.getInt("InvoiceID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getTimestamp("InvoiceDate"),
                        rs.getBigDecimal("TotalAmount"),
                        rs.getBigDecimal("VatAmount"),
                        rs.getInt("VATRateID"),
                        rs.getString("Note"),
                        rs.getBoolean("Status")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Invoice> getInvoicesByCustomerID(int customerID) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT i.InvoiceID, i.CustomerID, c.CustomerName, i.EmployeeID, e.FullName AS EmployeeName, "
                + "i.ShopID, s.ShopName, i.InvoiceDate, i.TotalAmount, i.VatAmount, i.VATRateID, i.Note, i.Status \n"
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
                            rs.getString("EmployeeName"),
                            rs.getInt("ShopID"),
                            rs.getTimestamp("InvoiceDate"),
                            rs.getBigDecimal("TotalAmount"),
                            rs.getBigDecimal("VatAmount"),
                            rs.getInt("VATRateID"),
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
        StringBuilder sqlBuilder = new StringBuilder("SELECT i.InvoiceID, i.CustomerID, c.CustomerName, i.EmployeeID, e.FullName AS EmployeeName, "
                + "i.ShopID, s.ShopName, i.InvoiceDate, i.TotalAmount, i.VatAmount, i.VATRateID, i.Note, i.Status \n" // THÊM VatAmount và VATRateID
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
                            rs.getBigDecimal("TotalAmount"),
                            rs.getBigDecimal("VatAmount"),
                            rs.getInt("VATRateID"),
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
        String sql = "SELECT i.InvoiceID, i.CustomerID, c.CustomerName, i.EmployeeID, "
                + "e.FullName AS EmployeeName, "
                + "i.ShopID, s.ShopName, "
                + "i.InvoiceDate, i.TotalAmount, i.VatAmount, i.VATRateID, i.Note, i.Status \n"
                + "FROM Invoice i\n"
                + "JOIN Customer c ON i.CustomerID = c.CustomerID\n"
                + "JOIN [dbo].[Employee] e ON i.EmployeeID = e.EmployeeID\n"
                + "JOIN Shop s ON i.ShopID = s.ShopID \n"
                + "WHERE CAST(i.InvoiceID AS VARCHAR) LIKE ? OR c.CustomerName COLLATE Latin1_General_CI_AI LIKE ?";
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
                        rs.getString("CustomerName"),
                        rs.getInt("EmployeeID"),
                        rs.getString("EmployeeName"),
                        rs.getInt("ShopID"),
                        rs.getTimestamp("InvoiceDate"),
                        rs.getBigDecimal("TotalAmount"),
                        rs.getBigDecimal("VatAmount"),
                        rs.getInt("VATRateID"),
                        rs.getString("Note"),
                        rs.getBoolean("Status"),
                        rs.getString("ShopName")
                );

                l.add(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return l;
    }

    public boolean updateInvoice(Invoice i) {
        String sql = "UPDATE [dbo].[Invoice]\n"
                + "    SET [CustomerID] = ?\n"
                + "      ,[EmployeeID] = ?\n"
                + "      ,[ShopID] = ?\n"
                + "      ,[InvoiceDate] = ?\n"
                + "      ,[TotalAmount] = ?\n"
                + "      ,[Note] = ?\n"
                + "      ,[Status] = ?\n"
                + "      ,[VATRateID] = ?\n"
                + "      ,[VatAmount] = ?\n"
                + " WHERE InvoiceID = ? ";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, i.getCustomerID());
            ptm.setInt(2, i.getEmployeeID());
            ptm.setInt(3, i.getShopID());
            ptm.setTimestamp(4, i.getInvoiceDate());
            ptm.setBigDecimal(5, i.getTotalAmount());
            ptm.setString(6, i.getNote());
            ptm.setBoolean(7, i.isStatus());
            ptm.setInt(8, i.getVatRateID());
            ptm.setBigDecimal(9, i.getVatAmount());

            ptm.setInt(10, i.getInvoiceID());

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
        String sql = "SELECT i.*, c.CustomerName, s.ShopName, e.FullName AS EmployeeName \n"
                + "FROM [dbo].[Invoice] i \n"
                + "JOIN [dbo].[Customer] c ON i.CustomerID = c.CustomerID\n"
                + "JOIN [dbo].[Shop] s ON i.ShopID = s.ShopID\n"
                + "JOIN [dbo].[Employee] e ON i.EmployeeID = e.EmployeeID\n"
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
                        rs.getString("EmployeeName"),
                        rs.getInt("ShopID"),
                        rs.getTimestamp("InvoiceDate"),
                        rs.getBigDecimal("TotalAmount"),
                        rs.getBigDecimal("VatAmount"),
                        rs.getInt("VATRateID"),
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

    public List<ShopOwnerRevenuaDto> getShopRevenue(Date fromDate, Date toDate, int page, int pageSize, String searchName) throws SQLException {
        List<ShopOwnerRevenuaDto> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT s.ShopID, s.ShopName,
               SUM(i.TotalAmount) AS TotalRevenue
        FROM Shop s
        LEFT JOIN Invoice i ON s.ShopID = i.ShopID AND i.Status = 1
        WHERE 1=1
    """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND s.ShopName LIKE ? ");
        }
        if (fromDate != null) {
            sql.append(" AND i.InvoiceDate >= ? ");
        }
        if (toDate != null) {
            sql.append(" AND i.InvoiceDate <= ? ");
        }

        sql.append(" GROUP BY s.ShopID, s.ShopName ")
                .append(" ORDER BY s.ShopID ")
                .append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName.trim() + "%");
            }
            if (fromDate != null) {
                ps.setTimestamp(index++, new java.sql.Timestamp(fromDate.getTime()));
            }
            if (toDate != null) {
                ps.setTimestamp(index++, new java.sql.Timestamp(toDate.getTime()));
            }

            ps.setInt(index++, (page - 1) * pageSize);
            ps.setInt(index, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int shopId = rs.getInt("ShopID");
                    String shopName = rs.getString("ShopName");
                    BigDecimal total = rs.getBigDecimal("TotalRevenue");
                    if (total == null) {
                        total = BigDecimal.ZERO;
                    }
                    list.add(new ShopOwnerRevenuaDto(shopId, shopName, total));
                }
            }
        }

        return list;
    }

    public int countShopsWithRevenue(Date fromDate, Date toDate, String searchName) throws SQLException {
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) FROM (
            SELECT s.ShopID
            FROM Shop s
            LEFT JOIN Invoice i ON s.ShopID = i.ShopID AND i.Status = 1
            WHERE 1=1
    """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND s.ShopName LIKE ? ");
        }
        if (fromDate != null) {
            sql.append(" AND i.InvoiceDate >= ? ");
        }
        if (toDate != null) {
            sql.append(" AND i.InvoiceDate <= ? ");
        }

        sql.append(" GROUP BY s.ShopID ) AS temp");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName.trim() + "%");
            }
            if (fromDate != null) {
                ps.setTimestamp(index++, new java.sql.Timestamp(fromDate.getTime()));
            }
            if (toDate != null) {
                ps.setTimestamp(index++, new java.sql.Timestamp(toDate.getTime()));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public static void main(String[] args) {

        Connection dbConnection = null;
        try {

            DBContext dbContext = new DBContext("SWP1");
            dbConnection = dbContext.getConnection();

            if (dbConnection != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");

                InvoiceDAO invoiceDAO = new InvoiceDAO(dbConnection);

                System.out.println("\n--- Test searchInvoiceByKey ---");
                String searchKey = "c";
                List<Invoice> searchResults = invoiceDAO.searchInvoiceByKey(searchKey);

                if (searchResults.isEmpty()) {
                    System.out.println("Không tìm thấy hóa đơn nào với từ khóa: '" + searchKey + "'");
                } else {
                    System.out.println("Tìm thấy " + searchResults.size() + " hóa đơn với từ khóa: '" + searchKey + "'");
                    for (Invoice inv : searchResults) {
                        System.out.println("InvoiceID: " + inv.getInvoiceID()
                                + ", Customer: " + inv.getCustomerName()
                                + ", Date: " + inv.getInvoiceDate()
                                + ", Total: " + inv.getTotalAmount());
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Lỗi khi đóng kết nối: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
