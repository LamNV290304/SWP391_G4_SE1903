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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShopDAO {

    private Connection connection;

    public ShopDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Shop> getAllShops() {
        List<Shop> shops = new ArrayList<>();
        String sql = "SELECT * FROM Shop";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                shops.add(extractShop(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return shops;
    }

    public Shop getShopById(int shopId) {
        String sql = "SELECT * FROM Shop WHERE ShopID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractShop(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public boolean createShop(Shop shop) {
        String sql = "INSERT INTO Shop (ShopName, Address, Phone, Email, Status, CreatedDate, CreatedBy) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, shop.getShopName());
            ps.setString(2, shop.getAddress());
            ps.setString(3, shop.getPhone());
            ps.setString(4, shop.getEmail());
            ps.setBoolean(5, shop.isStatus());
            ps.setTimestamp(6, new Timestamp(shop.getCreatedDate().getTime()));
            ps.setString(7, shop.getCreatedBy());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean updateShop(Shop shop) {
        String sql = "UPDATE Shop SET ShopName = ?, Address = ?, Phone = ?, Email = ?, Status = ?, CreatedDate = ?, CreatedBy = ? " +
                     "WHERE ShopID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, shop.getShopName());
            ps.setString(2, shop.getAddress());
            ps.setString(3, shop.getPhone());
            ps.setString(4, shop.getEmail());
            ps.setBoolean(5, shop.isStatus());
            ps.setTimestamp(6, new Timestamp(shop.getCreatedDate().getTime()));
            ps.setString(7, shop.getCreatedBy());
            ps.setInt(8, shop.getShopID());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean deleteShop(int shopId) {
        String sql = "UPDATE Shop SET Status = 0 WHERE ShopID = ?"; // soft delete

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    private Shop extractShop(ResultSet rs) throws SQLException {
        Shop shop = new Shop();
        shop.setShopID(rs.getInt("ShopID"));
        shop.setShopName(rs.getString("ShopName"));
        shop.setAddress(rs.getString("Address"));
        shop.setPhone(rs.getString("Phone"));
        shop.setEmail(rs.getString("Email"));
        shop.setStatus(rs.getBoolean("Status"));
        shop.setCreatedDate(rs.getTimestamp("CreatedDate"));
        shop.setCreatedBy(rs.getString("CreatedBy"));
        return shop;
    }
    public Shop getShopByName(String shopName, String databaseName) {
    String sql = "SELECT * FROM " + databaseName + ".dbo.Shop WHERE ShopName = ?";
    
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, shopName);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return extractShop(rs);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
}

}

