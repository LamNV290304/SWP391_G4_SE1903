/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

/**
 *
 * @author Thai Anh
 */

import Models.Shop;
import Context.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShopDAO {

    public List<Shop> getAllShops(String databaseName) {
        List<Shop> shops = new ArrayList<>();
        String sql = "SELECT * FROM Shop";
        try (Connection conn = DBContext.getConnection(databaseName);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Shop shop = new Shop(
                        rs.getString("ShopID"),
                        rs.getString("ShopName"),
                        rs.getString("Address"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getBoolean("Status"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getString("CreatedBy")
                );
                shops.add(shop);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return shops;
    }
    
    public Shop getShopByID(String shopID, String databaseName) {
        String sql = "SELECT * FROM Shop WHERE ShopID = ?";
        try (Connection conn = DBContext.getConnection(databaseName);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shopID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Shop(
                            rs.getString("ShopID"),
                            rs.getString("ShopName"),
                            rs.getString("Address"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getBoolean("Status"),
                            rs.getTimestamp("CreatedDate"),
                            rs.getString("CreatedBy")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertShop(Shop shop, String databaseName) {
        String sql = "INSERT INTO Shop (ShopID, ShopName, Address, Phone, Email, Status, CreatedDate, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection(databaseName);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shop.getShopID());
            ps.setString(2, shop.getShopName());
            ps.setString(3, shop.getAddress());
            ps.setString(4, shop.getPhone());
            ps.setString(5, shop.getEmail());
            ps.setBoolean(6, shop.isStatus());
            ps.setTimestamp(7, new Timestamp(shop.getCreatedDate().getTime()));
            ps.setString(8, shop.getCreatedBy());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateShop(Shop shop, String databaseName) {
        String sql = "UPDATE Shop SET ShopName=?, Address=?, Phone=?, Email=?, Status=?, CreatedDate=?, CreatedBy=? WHERE ShopID=?";
        try (Connection conn = DBContext.getConnection(databaseName);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shop.getShopName());
            ps.setString(2, shop.getAddress());
            ps.setString(3, shop.getPhone());
            ps.setString(4, shop.getEmail());
            ps.setBoolean(5, shop.isStatus());
            ps.setTimestamp(6, new Timestamp(shop.getCreatedDate().getTime()));
            ps.setString(7, shop.getCreatedBy());
            ps.setString(8, shop.getShopID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteShop(String shopID, String databaseName) {
        String sql = "DELETE FROM Shop WHERE ShopID = ?";
        try (Connection conn = DBContext.getConnection(databaseName);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shopID);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void main(String[] args) {
         String dbName = "SWP4"; // Thay đổi tùy theo CSDL của bạn
        ShopDAO dao = new ShopDAO();

        // Insert test
       // Shop shop = new Shop("S009", "HAHA", "Hanoi", "0262995295", "Haha@gmail.com", "Nô", new Date(), "haha");
   //     boolean inserted = dao.insertShop(shop, dbName);
   //     System.out.println("Insert: " + inserted);

        // Get all test
        List<Shop> shops = dao.getAllShops(dbName);
        for (Shop s : shops) {
            System.out.println(s.getShopName() + " - " + s.getAddress());
        }

        // Get by ID test
        Shop s = dao.getShopByID("S001", dbName);
        if (s != null) {
            System.out.println("Found: " + s.getShopName());
        }

        // Update test
        s.setPhone("0987654321");
        boolean updated = dao.updateShop(s, dbName);
        System.out.println("Update: " + updated);

        // Delete test
      //  boolean deleted = dao.deleteShop("S001", dbName);
       // System.out.println("Delete: " + deleted);
    }
}

