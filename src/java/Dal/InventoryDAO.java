/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.Inventory;
import Models.Product;
import Models.Shop;
import Context.DBContext;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;
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
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated "
                + "FROM Inventory i "
                + "JOIN Product p ON i.ProductID = p.ProductID "
                + "LEFT JOIN Shop s ON i.ShopID = s.ShopID";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setInventoryID(rs.getInt("InventoryID"));

                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                inv.setProduct(p);

                Shop s = new Shop();
                s.setShopID(rs.getInt("ShopID"));
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
    public boolean updateInventoryQuantity(int inventoryID, int newQuantity) {
        String sql = "UPDATE Inventory SET Quantity = ?, LastUpdated = GETDATE() WHERE InventoryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, inventoryID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // lay haang ton kho theo shop id va product id
    public Inventory getInventoryByShopAndProduct(int productID, int shopID) {
        String sql = "SELECT i.InventoryID, i.ProductID,p.ProductName, i.ShopID,s.ShopName, i.Quantity, i.LastUpdated\n"
                + "  FROM Inventory i join Product p on i.ProductID = p.ProductID LEFT join Shop s on i.ShopID = s.ShopID\n"
                + "WHERE i.ProductID= ? and i.ShopID =?";
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, productID);
            ptm.setInt(2, shopID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                Inventory i = new Inventory();
                i.setInventoryID(rs.getInt("inventoryID"));

                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                i.setProduct(p);

                Shop s = new Shop();
                s.setShopID(rs.getInt("ShopID"));
                s.setShopName(rs.getString("ShopName"));
                i.setShop(s);

                i.setQuantity(rs.getInt("Quantity"));
                i.setLastUpdated(rs.getTimestamp("LastUpdated"));
                return i;

            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Lấy hàng tồn kho theo ID
    public Inventory getInventoryByID(int inventoryID) {
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated"
                + "FROM Inventory i "
                + "JOIN Product p ON i.ProductID = p.ProductID "
                + "LEFT JOIN Shop s ON i.ShopID = s.ShopID "
                + "WHERE i.InventoryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventoryID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setInventoryID(rs.getInt("InventoryID"));

                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    inv.setProduct(p);

                    Shop s = new Shop();
                    s.setShopID(rs.getInt("ShopID"));
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
    public List<Inventory> getAllInventoriesInStore(int storeId) {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated "
                + "FROM Inventory i "
                + "JOIN Product p ON i.ProductID = p.ProductID "
                + "LEFT JOIN Shop s ON i.ShopID = s.ShopID "
                + "WHERE i.ShopID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setInventoryID(rs.getInt("InventoryID"));

                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    inv.setProduct(p);

                    Shop s = new Shop();
                    s.setShopID(rs.getInt("ShopID"));
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

    public List<Inventory> getAllInventoriesInProductNameAndStoreID(String search, String storeId) {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated "
                + "FROM Inventory i "
                + "JOIN Product p ON i.ProductID = p.ProductID "
                + "LEFT JOIN Shop s ON i.ShopID = s.ShopID "
                + "WHERE p.ProductName COLLATE Latin1_General_CI_AI LIKE ? AND i.ShopID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + search + "%");     // tìm gần đúng theo ProductName
            ps.setString(2, storeId);                // lọc theo ShopID

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setInventoryID(rs.getInt("InventoryID"));

                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    inv.setProduct(p);

                    Shop s = new Shop();
                    s.setShopID(rs.getInt("ShopID"));
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
public int countProductsInStockByShop(int shopId, int minQuantity, int maxQuantity) {
    String sql = "SELECT COUNT(DISTINCT ProductID) FROM Inventory WHERE ShopID = ? AND Quantity BETWEEN ? AND ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, shopId);
        ps.setInt(2, minQuantity);
        ps.setInt(3, maxQuantity);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

    public List<Inventory> getAllInventoriesInProductIDAndStoreID(int search, int storeId) {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated "
                + "FROM Inventory i "
                + "JOIN Product p ON i.ProductID = p.ProductID "
                + "LEFT JOIN Shop s ON i.ShopID = s.ShopID "
                + "WHERE p.ProductID = ? AND i.ShopID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, search);
            ps.setInt(2, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setInventoryID(rs.getInt("InventoryID"));

                    Product p = new Product();
                    p.setProductID(rs.getInt("ProductID"));
                    p.setProductName(rs.getString("ProductName"));
                    inv.setProduct(p);

                    Shop s = new Shop();
                    s.setShopID(rs.getInt("ShopID"));
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

    public Inventory getInventoryByShopAndProductAndLastUpdated(int productID, int shopID, Date lastUpdated) {
    String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated " +
                 "FROM Inventory i " +
                 "JOIN Product p ON i.ProductID = p.ProductID " +
                 "LEFT JOIN Shop s ON i.ShopID = s.ShopID " +
                 "WHERE i.ProductID = ? AND i.ShopID = ? AND i.LastUpdated = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, productID);
        ps.setInt(2, shopID);
        ps.setTimestamp(3, new java.sql.Timestamp(lastUpdated.getTime()));

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Inventory inv = new Inventory();
                inv.setInventoryID(rs.getInt("InventoryID"));

                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                inv.setProduct(p);

                Shop s = new Shop();
                s.setShopID(rs.getInt("ShopID"));
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

    public boolean insertInventory(Inventory inventory) {
        String sql = "INSERT INTO Inventory ( ProductID, ShopID, Quantity, LastUpdated) "
                + "VALUES ( ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventory.getProduct().getProductID());
            ps.setInt(2, inventory.getShop().getShopID());
            ps.setInt(3, inventory.getQuantity());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // hoặc inventory.getLastUpdated()
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
public List<Inventory> getListInventoryBy(int productID, int shopID) {
    List<Inventory> list = new ArrayList<>();
    String sql = "SELECT i.InventoryID, i.ProductID, p.ProductName, i.ShopID, s.ShopName, i.Quantity, i.LastUpdated "
               + "FROM Inventory i "
               + "JOIN Product p ON i.ProductID = p.ProductID "
               + "LEFT JOIN Shop s ON i.ShopID = s.ShopID "
               + "WHERE i.ProductID = ? AND i.ShopID = ? "
               + "ORDER BY i.LastUpdated ASC"; // <-- Sắp xếp từ cũ đến mới

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, productID);
        ps.setInt(2, shopID);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setInventoryID(rs.getInt("InventoryID"));

                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                inv.setProduct(p);

                Shop s = new Shop();
                s.setShopID(rs.getInt("ShopID"));
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

public int getQuantityBy(int productID, int shopID) {
    String sql = "SELECT SUM(Quantity) AS TotalQuantity FROM Inventory WHERE ProductID = ? AND ShopID = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, productID);
        ps.setInt(2, shopID);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("TotalQuantity");
            }
        }
    } catch (SQLException e) {
        Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
    }
    return 0;
}
public BigDecimal getTotalQuantityByShop(int shopId) {
   String sql = "SELECT SUM(i.Quantity * p.ImportPrice) AS TotalValue "
               + "FROM Inventory i "
               + "JOIN Product p ON i.ProductID = p.ProductID "
               + "WHERE i.ShopID = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, shopId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("TotalValue");
                return total != null ? total : BigDecimal.ZERO;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return BigDecimal.ZERO;
}
public int getInventoryQuantityByProductAndShop(int productId, int shopId) {
    String sql = "SELECT SUM(Quantity) FROM Inventory WHERE ProductID = ? AND ShopID = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, productId);
        ps.setInt(2, shopId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1); // Nếu không có bản ghi, sẽ trả về 0
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

    public static void main(String[] args) {
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            InventoryDAO dao = new InventoryDAO(conn);
            List<Inventory> inventories = dao.getAllInventories();
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
            // Inventory inv = dao.getInventoryByShopAndProduct("1", "2");
            //dao.updateInventoryQuantity(inv.getInventoryID(), inv.getQuantity()+10);
            // System.out.println(inv.toString());
        } catch (SQLException e) {
            Logger.getLogger(InventoryDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
