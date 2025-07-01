/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Context.DatabaseHelper;
import DTO.ShopSubscriptionDTO;
import java.sql.*;

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
        String sql = "SELECT ss.*, so.ShopName, sp.Name AS PackageName, sp.Price, sp.Description "
                + "FROM ShopSubscriptions ss "
                + "JOIN ShopOwners so ON ss.ShopOwnerId = so.Id "
                + "JOIN ServicePackages sp ON ss.PackageId = sp.Id "
                + "WHERE ss.ShopOwnerId = ?";

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
                        rs.getString("Description")
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

            ShopSubscriptionDTO subscription = dao.getActiveSubscriptionByShopId(shopOwnerId);

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
}
