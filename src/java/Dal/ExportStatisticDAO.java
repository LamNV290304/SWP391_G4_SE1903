/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import DTO.SalesEmployeeStatisticDto;
import DTO.SoldProductDetailDto;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duckh
 */
public class ExportStatisticDAO {

    private Connection connection;

    public ExportStatisticDAO(Connection connection) {
        this.connection = connection;
    }

    public List<SalesEmployeeStatisticDto> getCashierSummaryStatistics1(List<Integer> employeeIds, Date startDate, Date endDate, int shopId) throws SQLException {
        List<SalesEmployeeStatisticDto> list = new ArrayList<>();

        if (employeeIds == null || employeeIds.isEmpty()) {
            return list; 
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < employeeIds.size(); i++) {
            placeholders.append("?");
            if (i < employeeIds.size() - 1) {
                placeholders.append(",");
            }
        }

        String sql = "SELECT E.EmployeeID, E.FullName, "
                + "ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) AS TotalRevenue, "
                + "ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, "
                + "ISNULL(SUM(ID.Quantity), 0) AS TotalProductsSold, "
                + "CASE WHEN COUNT(DISTINCT I.InvoiceID) > 0 THEN "
                + "ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) / "
                + "CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ELSE 0 END AS AverageRevenuePerOrder "
                + "FROM Employee E "
                + "JOIN Invoice I ON E.EmployeeID = I.EmployeeID "
                + "JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID "
                + "WHERE I.EmployeeID IN (" + placeholders + ") "
                + "AND E.ShopID = ? AND I.Status = 1 "
                + "AND I.InvoiceDate BETWEEN ? AND ? "
                + "GROUP BY E.EmployeeID, E.FullName "
                + "ORDER BY E.EmployeeID";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int paramIndex = 1;

            // Set employee IDs
            for (Integer empId : employeeIds) {
                ps.setInt(paramIndex++, empId);
            }

            // Set shopId
            ps.setInt(paramIndex++, shopId);

            // Set date range
            ps.setTimestamp(paramIndex++, new java.sql.Timestamp(startDate.getTime()));
            ps.setTimestamp(paramIndex, new java.sql.Timestamp(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SalesEmployeeStatisticDto dto = new SalesEmployeeStatisticDto();
                    dto.setEmployeeID(rs.getInt("EmployeeID"));
                    dto.setFullName(rs.getString("FullName"));
                    dto.setTotalRevenue(rs.getBigDecimal("TotalRevenue"));
                    dto.setTotalOrders(rs.getInt("TotalOrders"));
                    dto.setTotalProductsSold(rs.getInt("TotalProductsSold"));
                    dto.setAverageRevenuePerOrder(rs.getBigDecimal("AverageRevenuePerOrder"));
                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<SalesEmployeeStatisticDto> getCashierSummaryStatistics(Integer employeeId, Date startDate, Date endDate, int shopId) throws SQLException {
        List<SalesEmployeeStatisticDto> list = new ArrayList<>();

        String sql = "SELECT E.EmployeeID, E.FullName, "
                + "ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) AS TotalRevenue, "
                + "ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, "
                + "ISNULL(SUM(ID.Quantity), 0) AS TotalProductsSold, "
                + "CASE WHEN COUNT(DISTINCT I.InvoiceID) > 0 THEN "
                + "ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) / "
                + "CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ELSE 0 END AS AverageRevenuePerOrder "
                + "FROM Employee E "
                + "JOIN Invoice I ON E.EmployeeID = I.EmployeeID "
                +
                "JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID "
                + "WHERE I.EmployeeID = ? AND E.ShopID = ? AND I.Status = 1 "
                + "AND I.InvoiceDate BETWEEN ? AND ? "
                + "GROUP BY E.EmployeeID, E.FullName "
                + "ORDER BY E.EmployeeID";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {;
            ps.setInt(1, employeeId);
            ps.setInt(2, shopId);
            ps.setTimestamp(3, new java.sql.Timestamp(startDate.getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SalesEmployeeStatisticDto dto = new SalesEmployeeStatisticDto();
                    dto.setEmployeeID(rs.getInt("EmployeeID"));
                    dto.setFullName(rs.getString("FullName"));
                    dto.setTotalRevenue(rs.getBigDecimal("TotalRevenue"));
                    dto.setTotalOrders(rs.getInt("TotalOrders"));
                    dto.setTotalProductsSold(rs.getInt("TotalProductsSold"));
                    dto.setAverageRevenuePerOrder(rs.getBigDecimal("AverageRevenuePerOrder"));
                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<SoldProductDetailDto> getSoldProductDetails(int employeeId, Date startDate, Date endDate) throws SQLException {
        List<SoldProductDetailDto> list = new ArrayList<>();

        String sql = "SELECT P.ProductID, P.ProductName, "
                + "SUM(ID.Quantity) AS QuantitySold, "
                + "ID.UnitPrice, "
                + "SUM(ID.Quantity * ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS Amount "
                + "FROM Invoice I "
                + "JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID "
                + "JOIN Product P ON ID.ProductID = P.ProductID "
                + "WHERE I.EmployeeID = ? AND I.Status = 1 AND I.InvoiceDate BETWEEN ? AND ? "
                + "GROUP BY P.ProductID, P.ProductName, ID.UnitPrice "
                + "ORDER BY P.ProductID";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setTimestamp(2, new java.sql.Timestamp(startDate.getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SoldProductDetailDto dto = new SoldProductDetailDto();
                    dto.setProductID(rs.getInt("ProductID"));
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setQuantitySold(rs.getInt("QuantitySold"));
                    dto.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                    dto.setAmount(rs.getBigDecimal("Amount"));
                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<SoldProductDetailDto> getSoldProductDetailsForExport(
            Integer employeeId, Integer shopId, Date startDate, Date endDate) throws SQLException {

        List<SoldProductDetailDto> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT P.ProductID, P.ProductName, SUM(ID.Quantity) AS QuantitySold, ");
        sql.append("AVG(ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS UnitPrice, ");
        sql.append("SUM(ID.Quantity * ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS Amount ");
        sql.append("FROM Product P ");
        sql.append("JOIN InvoiceDetail ID ON P.ProductID = ID.ProductID ");
        sql.append("JOIN Invoice I ON ID.InvoiceID = I.InvoiceID ");
        sql.append("WHERE I.SaleEmployeeID = ? ");
        params.add(employeeId);

        sql.append(" AND I.Status = 1 ");

        if (shopId != null) {
            sql.append(" AND I.ShopID = ? ");
            params.add(shopId);
        }

        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(new java.sql.Date(startDate.getTime()));
            params.add(new java.sql.Date(endDate.getTime()));
        }

        sql.append(" GROUP BY P.ProductID, P.ProductName ");
        sql.append(" ORDER BY P.ProductName ");

        try (PreparedStatement ps = this.connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new SoldProductDetailDto(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getInt("QuantitySold"),
                            rs.getBigDecimal("UnitPrice"),
                            rs.getBigDecimal("Amount")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return list;
    }

    public static void main(String[] args) {
        Connection dbConnection = null;
        try {
            // Kết nối đến database
            DBContext dbContext = new DBContext("ShopDB_SWPP");
            dbConnection = dbContext.getConnection();

            System.out.println("✅ Kết nối cơ sở dữ liệu thành công!");

            ExportStatisticDAO dao = new ExportStatisticDAO(dbConnection);

            // Thiết lập tham số
            int employeeId = 2;
            Timestamp start = Timestamp.valueOf("2025-06-22 00:00:00");
            Timestamp end = Timestamp.valueOf("2025-07-22 23:59:59");

            // Gọi hàm cần test
            List<SoldProductDetailDto> list = dao.getSoldProductDetails(employeeId, start, end);

            // In kết quả ra console
            if (list.isEmpty()) {
                System.out.println("❌ Không có dữ liệu sản phẩm đã bán.");
            } else {
                System.out.println("📦 Danh sách sản phẩm đã bán bởi nhân viên ID " + employeeId + ": Cashier");
                for (SoldProductDetailDto dto : list) {
                    System.out.println("🔹 ProductID: " + dto.getProductID());
                    System.out.println("   ProductName: " + dto.getProductName());
                    System.out.println("   QuantitySold: " + dto.getQuantitySold());
                    System.out.println("   UnitPrice: " + dto.getUnitPrice());
                    System.out.println("   Amount: " + dto.getAmount());
                    System.out.println("-------------------------");
                }
            }

        } catch (Exception e) {
            System.err.println("❗ Lỗi khi chạy hàm getSoldProductDetails:");
            e.printStackTrace();
        }
    }
}
