/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import DTO.ShopSubscriptionDTO;
import java.sql.*;

/**
 *
 * @author Admin
 */
public class ShopSubscriptionDAO {
    private Connection connection;
    
    public ShopSubscriptionDAO (Connection connection){
        this.connection = connection;
    }
    
    public ShopSubscriptionDTO getCurrentSubscriptionByShopOwnerId(int shopOwnerId) {
        String sql = """
            SELECT ss.*, sp.Name AS PackageName
            FROM ShopSubscriptions ss
            JOIN ServicePackages sp ON ss.PackageId = sp.Id
            WHERE ss.ShopOwnerId = ? AND ss.IsActive = 1
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopOwnerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ShopSubscriptionDTO s = new ShopSubscriptionDTO();
                s.setId(rs.getInt("Id"));
                s.setShopOwnerId(rs.getInt("ShopOwnerId"));
                s.setPackageId(rs.getInt("PackageId"));
                s.setPaymentId(rs.getInt("PaymentId"));
                s.setStartDate(rs.getDate("StartDate"));
                s.setEndDate(rs.getDate("EndDate"));
                s.setIsActive(rs.getBoolean("IsActive"));
                s.setNote(rs.getString("Note"));
                s.setPackageName(rs.getString("PackageName")); // nếu DTO có
                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
