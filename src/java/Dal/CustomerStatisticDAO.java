/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.LoyalCustomerDto;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author duckh
 */
public class CustomerStatisticDAO {

    private Connection connection;

    public CustomerStatisticDAO(Connection connection) {
        this.connection = connection;
    }

    public List<LoyalCustomerDto> getTopCustomers(Date startDate, Date endDate, int shopId, int limit) throws SQLException {
        List<LoyalCustomerDto> result = new ArrayList<>();

        String sql = "SELECT TOP (?) c.CustomerID, c.CustomerName, c.Phone, c.Email, COUNT(i.InvoiceID) AS PurchaseCount "
                + "FROM Invoice i JOIN Customer c ON i.CustomerID = c.CustomerID "
                + "WHERE i.InvoiceDate BETWEEN ? AND ? "
                + "AND c.CustomerName != N'Khách vãng lai'"
                + "AND i.ShopID = ? "
                + "GROUP BY c.CustomerID, c.CustomerName, c.Phone, c.Email "
                + "ORDER BY PurchaseCount DESC";

        PreparedStatement ps = connection.prepareStatement(sql);
        int index = 1;
        ps.setInt(index++, limit);
        ps.setDate(index++, startDate);
        ps.setDate(index++, endDate);
        if (shopId > 0) {
            ps.setInt(index++, shopId);
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            LoyalCustomerDto dto = new LoyalCustomerDto();
            dto.setCustomerId(rs.getInt("CustomerID"));
            dto.setCustomerName(rs.getString("CustomerName"));
            dto.setPhone(rs.getString("Phone"));
            dto.setEmail(rs.getString("Email"));
            dto.setPurchaseCount(rs.getInt("PurchaseCount"));
            result.add(dto);
        }
        return result;
    }

    public List<LoyalCustomerDto> searchLoyalCustomers(String key, Date startDate, Date endDate, int shopId, int offset, int limit) throws SQLException {
        List<LoyalCustomerDto> result = new ArrayList<>();
        String sql = "SELECT c.CustomerID, c.CustomerName, c.Phone, c.Email, COUNT(i.InvoiceID) AS PurchaseCount "
                + "FROM Customer c "
                + "JOIN Invoice i ON c.CustomerID = i.CustomerID "
                + "WHERE i.ShopID = ? AND i.InvoiceDate BETWEEN ? AND ? "
                + "AND (c.CustomerName COLLATE Latin1_General_CI_AI LIKE ? OR c.Phone COLLATE Latin1_General_CI_AI LIKE ?) "
                + "AND c.CustomerName != N'Khách vãng lai' "
                + "GROUP BY c.CustomerID, c.CustomerName, c.Phone, c.Email "
                + "ORDER BY PurchaseCount DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);
            ps.setString(4, "%" + key + "%");
            ps.setString(5, "%" + key + "%");
            ps.setInt(6, offset);
            ps.setInt(7, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LoyalCustomerDto dto = new LoyalCustomerDto(
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("PurchaseCount") 
                );
                result.add(dto);
            }
        }
        return result;
    }

    public int countLoyalCustomers(String key, Date startDate, Date endDate, int shopId) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT c.CustomerID) "
                + "FROM Customer c JOIN Invoice i ON c.CustomerID = i.CustomerID "
                + "WHERE i.ShopID = ? AND i.InvoiceDate BETWEEN ? AND ? "
                + "AND (c.CustomerName COLLATE Latin1_General_CI_AI LIKE ? OR c.Phone COLLATE Latin1_General_CI_AI LIKE ?) AND c.CustomerName != N'Khách vãng lai'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);
            ps.setString(4, "%" + key + "%");
            ps.setString(5, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static void main(String[] args) {

        Connection dbConnection = null;
        try {
            DBContext dbContext = new DBContext("ShopDB_SWPP");
            dbConnection = dbContext.getConnection();

            if (dbConnection != null) {
                System.out.println("✅ Kết nối cơ sở dữ liệu thành công!");

                CustomerStatisticDAO customerDAO = new CustomerStatisticDAO(dbConnection);

                Date startDate = Date.valueOf("2024-01-01");
                Date endDate = Date.valueOf("2025-12-31");
                int shopId = 1;
                int limit = 5;

                List<LoyalCustomerDto> topCustomers = customerDAO.getTopCustomers(startDate, endDate, shopId, limit);

                if (topCustomers.isEmpty()) {
                    System.out.println("❌ Không có khách hàng thân thiết nào trong khoảng thời gian này.");
                } else {
                    System.out.println("✅ Danh sách top " + limit + " khách hàng thân thiết:");
                    for (LoyalCustomerDto dto : topCustomers) {
                        System.out.println("ID: " + dto.getCustomerId()
                                + ", Name: " + dto.getCustomerName()
                                + ", Phone: " + dto.getPhone()
                                + ", Email: " + dto.getEmail()
                                + ", Total Spent: " + dto.getPurchaseCount());
                    }
                }
            } else {
                System.out.println("❌ Kết nối cơ sở dữ liệu thất bại!");
            }

        } catch (Exception ex) {
            System.err.println("Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
