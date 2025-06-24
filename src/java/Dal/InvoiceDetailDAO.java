package Dal;

import Context.DBContext;
import Models.Inventory;
import Models.InvoiceDetail;
import Models.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duckh
 */
public class InvoiceDetailDAO {

    private Connection connection;

    public InvoiceDetailDAO(Connection connection) {
        this.connection = connection;
    }

    public List<InvoiceDetail> getAllInvoiceDetail() {
        String sql = "SELECT [InvoiceDetailID]\n"
                + "      ,[InvoiceID]\n"
                + "      ,[ProductID]\n"
                + "      ,[UnitPrice]\n"
                + "      ,[Quantity]\n"
                + "      ,[Discount]\n"
                + "      ,[TotalPrice]\n"
                + "  FROM [dbo].[InvoiceDetail]";
        List<InvoiceDetail> list = new ArrayList<>();
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(new InvoiceDetail(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getDouble(6)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean addInvoiceDetailAndUpdateInventory(InvoiceDetail detail, int shopID) {
        InventoryDAO iDAO = new InventoryDAO(connection);

        Inventory inventory = iDAO.getInventoryByShopAndProduct(detail.getProductID(), shopID);
        if (inventory == null) {
            System.out.println("Không tìm thấy tồn kho với ProductID = " + detail.getProductID() + " và ShopID = " + shopID);
            return false;
        }
        int currentQuantity = inventory.getQuantity();
        if (currentQuantity < detail.getQuantity()) {
            System.out.println("Tồn kho không đủ! Hiện tại có " + currentQuantity);
            return false;
        }
        InvoiceDetail existingDetail = getInvoiceDetailByInvoiceIDAndProductID(detail.getInvoiceID(), detail.getProductID());
        if (existingDetail != null) {

            detail.setInvoiceDetailID(existingDetail.getInvoiceDetailID());
            detail.setShopID(shopID); 

            detail.setQuantity(existingDetail.getQuantity() + detail.getQuantity());
            detail.calculateTotalPrice();

            return updateInvoiceDetail(detail, iDAO);
        }
        String sql = "INSERT INTO InvoiceDetail (InvoiceID, ProductID, UnitPrice, Quantity, Discount, TotalPrice)"
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, detail.getInvoiceID());
            ptm.setInt(2, detail.getProductID());
            ptm.setBigDecimal(3, detail.getUnitPrice());
            ptm.setInt(4, detail.getQuantity());
            ptm.setDouble(5, detail.getDiscount());
            ptm.setDouble(6, detail.getTotalPrice());
            ptm.executeUpdate();

            int updatedInventory = currentQuantity - detail.getQuantity();
            boolean updateInven = iDAO.updateInventoryQuantity(inventory.getInventoryID(), updatedInventory);
            return updateInven;

        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public InvoiceDetail getInvoiceDetailByInvoiceIDAndProductID(int invoiceID, int productID) {
        String sql = "SELECT [InvoiceDetailID]\n"
                + "      ,[InvoiceID]\n"
                + "      ,[ProductID]\n"
                + "      ,[UnitPrice]\n"
                + "      ,[Quantity]\n"
                + "      ,[Discount]\n"
                + "      ,[TotalPrice]\n"
                + "  FROM [dbo].[InvoiceDetail]\n"
                + "	WHERE InvoiceID = ? AND ProductID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setInvoiceDetailID(rs.getInt("InvoiceDetailID"));
                detail.setInvoiceID(rs.getInt("InvoiceID"));
                detail.setProductID(rs.getInt("ProductID"));
                detail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setDiscount(rs.getDouble("Discount"));

                return detail;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public InvoiceDetail getInvoiceDetailByInvoiceDetailID(int invoiceDetailID) {
        String sql = "SELECT iv.InvoiceDetailID\n"
                + "      ,iv.InvoiceID\n"
                + "      ,iv.ProductID\n"
                + "      ,iv.UnitPrice\n"
                + "	  ,i.ShopID\n"
                + "      ,iv.Quantity\n"
                + "      ,iv.Discount\n"
                + "      ,TotalPrice\n"
                + "  FROM [dbo].[InvoiceDetail] iv  join Invoice i on iv.InvoiceID =  i.InvoiceID\n"
                + "  Where [InvoiceDetailID] = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceDetailID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                InvoiceDetail i = new InvoiceDetail(
                        rs.getInt("InvoiceDetailID"),
                        rs.getInt("InvoiceID"),
                        rs.getInt("ProductID"),
                       rs.getBigDecimal("UnitPrice"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Discount")
                );
                i.setShopID(rs.getInt("ShopID"));
                return i;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<InvoiceDetail> getDetailByInvoiceID(int invoiceID) {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT [InvoiceDetailID]\n"
                + "      ,[InvoiceID]\n"
                + "      ,[ProductID]\n"
                + "      ,[UnitPrice]\n"
                + "      ,[Quantity]\n"
                + "      ,[Discount]\n"
                + "      ,[TotalPrice]\n"
                + "  FROM [dbo].[InvoiceDetail]\n"
                + "  WHERE InvoiceID = ?";

        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                InvoiceDetail i = new InvoiceDetail(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getInt(5), rs.getDouble(6));
                list.add(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean updateInvoiceDetail(InvoiceDetail newDetail, InventoryDAO inventoryDAO) {
        try {
            InvoiceDetail oldDetail = getInvoiceDetailByInvoiceDetailID(newDetail.getInvoiceDetailID());
            if (oldDetail == null) {
                throw new Exception("Không tìm thấy InvoiceDetail để sửa");
            }
            if (newDetail.getQuantity() == 0) {
                return deleteByDetailID(newDetail.getInvoiceDetailID());
            }
            boolean productChanged = oldDetail.getProductID() != (newDetail.getProductID());
            if (productChanged) {
           
                Inventory oldInventory = inventoryDAO.getInventoryByShopAndProduct(oldDetail.getProductID(), oldDetail.getShopID());

                if (oldInventory == null) {
                    throw new Exception("Không tìm thấy tồn kho cũ");
                }
                int restoredQty = oldInventory.getQuantity() + oldDetail.getQuantity();
                if (!inventoryDAO.updateInventoryQuantity(oldInventory.getInventoryID(), restoredQty)) {
                    throw new Exception("Cập nhật tồn kho cũ thất bại");
                }

            
                Inventory newInventory = inventoryDAO.getInventoryByShopAndProduct(newDetail.getProductID(), newDetail.getShopID());
                if (newInventory == null) {
                    throw new Exception("Không tìm thấy tồn kho mới");
                }
                if (newInventory.getQuantity() < newDetail.getQuantity()) {
                    throw new Exception("Không đủ tồn kho cho sản phẩm mới");
                }

                int newQty = newInventory.getQuantity() - newDetail.getQuantity();
                if (!inventoryDAO.updateInventoryQuantity(newInventory.getInventoryID(), newQty)) {
                    throw new Exception("Cập nhật tồn kho mới thất bại");
                }

            } else {
       
                Inventory inventory = inventoryDAO.getInventoryByShopAndProduct(newDetail.getProductID(), newDetail.getShopID());
                if (inventory == null) {
                    throw new Exception("Không tìm thấy tồn kho");
                }
                int diff = newDetail.getQuantity() - oldDetail.getQuantity();
                if (diff > 0 && inventory.getQuantity() < diff) {
                    throw new Exception("Không đủ tồn kho để tăng số lượng");
                }
                int updatedQty = inventory.getQuantity() - diff;
                if (!inventoryDAO.updateInventoryQuantity(inventory.getInventoryID(), updatedQty)) {
                    throw new Exception("Cập nhật tồn kho thất bại");
                }
            }

            newDetail.calculateTotalPrice();
            String sql = "UPDATE InvoiceDetail SET InvoiceID=?, ProductID=?, UnitPrice=?, Quantity=?, Discount=?, TotalPrice=? WHERE InvoiceDetailID=?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, newDetail.getInvoiceID());
                ps.setInt(2, newDetail.getProductID());
                ps.setBigDecimal(3, newDetail.getUnitPrice());
                ps.setInt(4, newDetail.getQuantity());
                ps.setDouble(5, newDetail.getDiscount());
                ps.setDouble(6, newDetail.getTotalPrice());
                ps.setInt(7, newDetail.getInvoiceDetailID());

                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật chi tiết hóa đơn:");
            e.printStackTrace(); 
            return false;
        }
    }

    public boolean deleteByInvoiceID(int invoiceID) {
        String sql = "DELETE FROM [dbo].[InvoiceDetail] WHERE InvoiceID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, invoiceID);
            return ptm.executeUpdate() >= 0;
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteByDetailID(int invoiceDetailID) {
        String sql = "DELETE FROM [dbo].[InvoiceDetail]\n"
                + "      WHERE InvoiceDetailID=?";
        InvoiceDetail detail = getInvoiceDetailByInvoiceDetailID(invoiceDetailID);
        InventoryDAO iDAO = new InventoryDAO(connection);
        if (detail != null) {
            Inventory inventory = iDAO.getInventoryByShopAndProduct(detail.getProductID(), detail.getShopID());
            if (inventory == null) {
                return false;
            }
            int restoredQuantity = inventory.getQuantity() + detail.getQuantity();
            boolean updateInventory = iDAO.updateInventoryQuantity(inventory.getInventoryID(), restoredQuantity);
            if (!updateInventory) {
                return false;
            }
        }
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceDetailID);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<InvoiceDetail> getDetailsWithProductInfoByInvoiceID(int invoiceID) {
        List<InvoiceDetail> list = new ArrayList<>();
        
        String sql = "SELECT id.[InvoiceDetailID], id.[InvoiceID], id.[ProductID], id.[UnitPrice], "
                + "id.[Quantity], id.[Discount], id.[TotalPrice], "
                + "p.[ProductName]" 
                + "FROM [dbo].[InvoiceDetail] id "
                + "JOIN [dbo].[Product] p ON id.ProductID = p.ProductID "
                + "WHERE id.InvoiceID = ?";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, invoiceID);
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
    
                    Product product = new Product(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getString("CategoryID"),
                            rs.getString("UnitID"),
                            rs.getBigDecimal("SellingPrice"),
                            rs.getString("Description"),
                            rs.getBoolean("Status"),
                            rs.getTimestamp("CreatedDate").toLocalDateTime(),
                            rs.getString("CreatedBy")
                    );

                    InvoiceDetail detail = new InvoiceDetail(
                            rs.getInt("InvoiceDetailID"),
                            rs.getInt("InvoiceID"),
                            rs.getInt("ProductID"),
                            rs.getBigDecimal("UnitPrice"),
                            rs.getInt("Quantity"),
                            rs.getDouble("Discount"),
                            rs.getDouble("TotalPrice"), 
                            product 
                    );
                    list.add(detail);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy chi tiết hóa đơn kèm sản phẩm", ex);
        }
        return list;
    }
}
    