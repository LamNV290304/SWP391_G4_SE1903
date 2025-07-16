/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Context.DatabaseHelper;
import DTO.ShopSubscriptionDTO;
import Models.ServicePackage;
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

    public ShopSubscriptionDTO getActiveSubscriptionByShopId(int shopOwnerId) throws SQLException {
        String sql = "SELECT ss.*, so.ShopName, sp.Name AS PackageName, sp.Price, sp.Description, sp.DurationInDays "
                + "FROM ShopSubscriptions ss "
                + "JOIN ShopOwners so ON ss.ShopOwnerId = so.Id "
                + "JOIN ServicePackages sp ON ss.PackageId = sp.Id "
                + "WHERE ss.ShopOwnerId = ? and ss.IsActive = 1 "
                + "ORDER BY ss.Id DESC ";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopOwnerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ShopSubscriptionDTO sub = new ShopSubscriptionDTO(
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
            // Bước 1: Kết nối tới database
            Connection connection = DBContext.getCentralConnection();

            // Bước 2: Khởi tạo DAO
            ShopSubscriptionDAO dao = new ShopSubscriptionDAO(connection);
            ServicePackageDAO packageDAO = new ServicePackageDAO(connection);

            // Bước 3: Gán ID bạn muốn test
            int subscriptionId = 1004; // <- sửa ID này cho phù hợp với dữ liệu thực tế trong DB

            // Bước 4: Gọi hàm getById()
            ShopSubscription sub = dao.getById(subscriptionId);

            // Bước 5: In ra kết quả kiểm tra
            if (sub != null) {
                System.out.println("Subscription ID: " + sub.getId());
                System.out.println("Shop Owner ID: " + sub.getShopOwnerId());
                System.out.println("Package ID: " + sub.getPackageId());
                System.out.println("Start Date: " + sub.getStartDate());
                System.out.println("End Date: " + sub.getEndDate());
                System.out.println("Duration In Days: " + sub.getPackageDurationInDays());
            } else {
                System.out.println("Không tìm thấy subscription với ID = " + subscriptionId);
            }
            ServicePackage servicePackage = packageDAO.getById(sub.getPackageId());

            Date now = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(sub.getEndDate().after(now) ? sub.getEndDate() : now);
            cal.add(Calendar.DATE, servicePackage.getDurationInDays());
            Date newExpireDate = new Date(cal.getTimeInMillis());
            
            System.out.println(newExpireDate);

            // Đóng kết nối
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
