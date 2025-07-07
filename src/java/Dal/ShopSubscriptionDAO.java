/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Context.DatabaseHelper;
import DTO.ShopSubscriptionDto;
import Models.ShopSubscription;
import java.sql.*;
import java.util.Calendar;

/**
 *
 * @author Admin
 */
public class ShopSubscriptionDAO {

    private Connection connection;

    public ShopSubscriptionDAO(Connection connection) {
        this.connection = connection;
    }

    public ShopSubscriptionDto getActiveSubscriptionByShopId(int shopOwnerId) throws SQLException {
        String sql = "SELECT ss.*, so.ShopName, sp.Name AS PackageName, sp.Price, sp.Description, sp.DurationInDays "
                + "FROM ShopSubscriptions ss "
                + "JOIN ShopOwners so ON ss.ShopOwnerId = so.Id "
                + "JOIN ServicePackages sp ON ss.PackageId = sp.Id "
                + "WHERE ss.ShopOwnerId = ? and ss.IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopOwnerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ShopSubscriptionDto sub = new ShopSubscriptionDto(
                        rs.getInt("Id"),
                        rs.getInt("ShopOwnerId"),
                        rs.getInt("PackageId"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getBoolean("IsActive"),
                        rs.getString("Note"),
                        rs.getString("ShopName"),
                        rs.getString("PackageName"),
                        rs.getBigDecimal("Price"),
                        rs.getString("Description"),
                        rs.getInt("DurationInDays")
                );
                return sub;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            // Kết nối tới DB
            Connection connection = DBContext.getCentralConnection(); // đảm bảo method này tồn tại

            // Khởi tạo DAO
            ShopSubscriptionDAO dao = new ShopSubscriptionDAO(connection);

            // Test với một shopOwnerId cụ thể
            int shopOwnerId = 8003; // sửa ID này theo dữ liệu có sẵn trong DB

            ShopSubscriptionDto subscription = dao.getActiveSubscriptionByShopId(shopOwnerId);

            // In kết quả
            if (subscription != null) {
                System.out.println("Subscription ID: " + subscription.getId());
                System.out.println("Shop Name: " + subscription.getShopName());
                System.out.println("Package Name: " + subscription.getPackageName());
                System.out.println("Start Date: " + subscription.getStartDate());
                System.out.println("End Date: " + subscription.getEndDate());
                System.out.println("Price: " + subscription.getPackagePrice());
                System.out.println("Description: " + subscription.getPackageDescription());
            } else {
                System.out.println("Không tìm thấy đăng ký hoạt động nào cho shopOwnerId = " + shopOwnerId);
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertNewSubscription(int shopOwnerId, int packageId, int paymentId) throws SQLException {
        String sql = "INSERT INTO ShopSubscriptions (ShopOwnerId, PackageId, StartDate, EndDate, IsActive, Note) "
                + "VALUES (?, ?, ?, ?, 1, ?)";

        Date startDate = new Date(System.currentTimeMillis());
        int durationDays = getPackageDuration(packageId); // Implement this method to query the package duration
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, durationDays);
        Date endDate = new Date(cal.getTimeInMillis());

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopOwnerId);
            ps.setInt(2, packageId);
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            ps.setString(5, "Generated from Payment ID: " + paymentId);
            ps.executeUpdate();
        }
    }

    private int getPackageDuration(int packageId) throws SQLException {
        String sql = "SELECT DurationInDays FROM ServicePackages WHERE Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("DurationInDays");
            }
        }
        throw new SQLException("Không tìm thấy gói dịch vụ với ID: " + packageId);
    }

    public void cancelPreviousSubscriptions(int shopOwnerId) throws SQLException {
        String sql = "UPDATE ShopSubscriptions SET IsActive = 0 WHERE ShopOwnerId = ? AND IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopOwnerId);
            ps.executeUpdate();
        }
    }

    public ShopSubscription getById(int subscriptionId) throws SQLException {
        String sql = "SELECT s.*, p.DurationInDays FROM ShopSubscriptions s JOIN ServicePackages p ON s.PackageId = p.Id WHERE s.Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subscriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ShopSubscription sub = new ShopSubscription();
                    sub.setId(rs.getInt("Id"));
                    sub.setPackageId(rs.getInt("PackageId"));
                    sub.setShopOwnerId(rs.getInt("ShopOwnerId"));
                    sub.setStartDate(rs.getDate("StartDate"));
                    sub.setEndDate(rs.getDate("EndDate"));
                    sub.setPackageDurationInDays(rs.getInt("DurationInDays"));
                    return sub;
                }
            }
        }
        return null;
    }

    public void extendSubscription(int subscriptionId, java.sql.Date newEndDate) throws SQLException {
        String sql = "UPDATE ShopSubscriptions SET EndDate = ? WHERE Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, newEndDate);
            ps.setInt(2, subscriptionId);
            ps.executeUpdate();
        }
    }
}
