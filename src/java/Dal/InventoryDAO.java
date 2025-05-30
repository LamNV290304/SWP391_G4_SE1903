/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.Inventory;
import Models.Product;
import Models.Shop;
import Context.DBContext;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.time.Instant;
/**
 *
 * @author Thai Anh
 */
public class InventoryDAO {
  private Connection connection;

    public InventoryDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy toàn bộ hàng tồn kho
    public List<Inventory> getAllInventories() {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated " +
                     "FROM Inventory i " +
                     "JOIN Product p ON i.ProductID = p.ProductID " +
                     "LEFT JOIN Shop s ON i.ShopID = s.ShopID";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setInventoryID(rs.getString("InventoryID"));

                Product p = new Product();
                p.setProductID(rs.getString("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                inv.setProduct(p);

                Shop s = new Shop();
                s.setShopID(rs.getString("ShopID"));
                s.setShopName(rs.getString("ShopName"));
                inv.setShop(s);

                inv.setQuantity(rs.getInt("Quantity"));
                inv.setLastUpdated(rs.getTimestamp("LastUpdated"));

                list.add(inv);
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Cập nhật số lượng hàng tồn kho
    public boolean updateInventoryQuantity(String inventoryID, int newQuantity) {
        String sql = "UPDATE Inventory SET Quantity = ?, LastUpdated = GETDATE() WHERE InventoryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setString(2, inventoryID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Lấy hàng tồn kho theo ID
    public Inventory getInventoryByID(String inventoryID) {
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated " +
                     "FROM Inventory i " +
                     "JOIN Product p ON i.ProductID = p.ProductID " +
                     "LEFT JOIN Shop s ON i.ShopID = s.ShopID " +
                     "WHERE i.InventoryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, inventoryID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setInventoryID(rs.getString("InventoryID"));

                    Product p = new Product();
                    p.setProductID(rs.getString("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    inv.setProduct(p);

                    Shop s = new Shop();
                    s.setShopID(rs.getString("ShopID"));
                    s.setShopName(rs.getString("ShopName"));
                    inv.setShop(s);

                    inv.setQuantity(rs.getInt("Quantity"));
                    inv.setLastUpdated(rs.getTimestamp("LastUpdated"));

                    return inv;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Lấy danh sách hàng tồn kho trong một cửa hàng cụ thể
    public List<Inventory> getAllInventoriesInStore(String storeId) {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated " +
                     "FROM Inventory i " +
                     "JOIN Product p ON i.ProductID = p.ProductID " +
                     "LEFT JOIN Shop s ON i.ShopID = s.ShopID " +
                     "WHERE i.ShopID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setInventoryID(rs.getString("InventoryID"));

                    Product p = new Product();
                    p.setProductID(rs.getString("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    inv.setProduct(p);

                    Shop s = new Shop();
                    s.setShopID(rs.getString("ShopID"));
                    s.setShopName(rs.getString("ShopName"));
                    inv.setShop(s);

                    inv.setQuantity(rs.getInt("Quantity"));
                    inv.setLastUpdated(rs.getTimestamp("LastUpdated"));

                    list.add(inv);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }
    public static void main(String[] args) {
       try (Connection conn = new DBContext("SWP2").getConnection()) {
        InventoryDAO dao = new InventoryDAO(conn);
        List<Inventory> inventories = dao.getAllInventoriesInStore("S01");
        if (inventories.isEmpty()) {
            System.out.println("❌ Không có hàng tồn kho nào.");
        } else {
            for (Inventory inv : inventories) {
                System.out.println("🆔 Mã kho: " + inv.getInventoryID());
                System.out.println("📦 Sản phẩm: " + inv.getProduct().getProductName());
                System.out.println("🏬 Cửa hàng: " + inv.getShop().getShopName());
                System.out.println("📊 Số lượng: " + inv.getQuantity());
                System.out.println("🕒 Cập nhật: " + inv.getLastUpdated());
                System.out.println("----------------------------------");
            }
        }
    } catch (SQLException e) {
        Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
    }
    }
}
