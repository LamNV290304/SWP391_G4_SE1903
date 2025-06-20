/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
            // Gán ID cũ để update đúng dòng
            detail.setInvoiceDetailID(existingDetail.getInvoiceDetailID());
            detail.setShopID(shopID); // để update tồn kho

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
        return null; // không tìm thấy
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
                // Khôi phục tồn kho cũ
                Inventory oldInventory = inventoryDAO.getInventoryByShopAndProduct(oldDetail.getProductID(), oldDetail.getShopID());

                if (oldInventory == null) {
                    throw new Exception("Không tìm thấy tồn kho cũ");
                }
                int restoredQty = oldInventory.getQuantity() + oldDetail.getQuantity();
                if (!inventoryDAO.updateInventoryQuantity(oldInventory.getInventoryID(), restoredQty)) {
                    throw new Exception("Cập nhật tồn kho cũ thất bại");
                }

                // Trừ tồn kho mới
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
                // Không đổi product, chỉ đổi số lượng
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
            e.printStackTrace(); // In lỗi cụ thể ra console
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
                    // Tạo đối tượng Product
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
                            rs.getDouble("TotalPrice"), // Lấy TotalPrice từ DB
                            product // Gán đối tượng Product đã tạo
                    );
                    list.add(detail);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy chi tiết hóa đơn kèm sản phẩm", ex);
        }
        return list;
    }

    public static void main(String[] args) {
        DBContext connection = new DBContext("SWP1");
        InvoiceDetailDAO iDetailDAO = new InvoiceDetailDAO(connection.getConnection());
        int testInvoiceID = 56; // Ví dụ: ID của một hóa đơn đã có
        int testProductID = 1; // Ví dụ: ID của một sản phẩm
//        BigDecimal testUnitPrice = 50000.0;
        int testQuantity = 1; // Ví dụ: Số lượng muốn thêm
        double testDiscount = 0.0; // Ví dụ: Giảm giá
        int testShopID = 1; // Ví dụ: ID của cửa hàng

//        InvoiceDetail newDetail = new InvoiceDetail(testInvoiceID, testProductID, testUnitPrice, testQuantity, testDiscount);
//          boolean success = iDetailDAO.addInvoiceDetailAndUpdateInventory(newDetail, testShopID);
//
//                if (success) {
//                    
//                    System.out.println("--> Kết quả: THÀNH CÔNG! (Chi tiết hóa đơn đã được thêm/cập nhật và tồn kho đã điều chỉnh)");
//                } else {
//                    
//                    System.out.println("--> Kết quả: THẤT BẠI! (Kiểm tra console để xem lỗi chi tiết)");
//                }
////        
//            for (InvoiceDetail detail : details) {
//                System.out.println("InvoiceDetailID: " + detail.getInvoiceDetailID());
//                System.out.println("  Product Name: " + (detail.getProduct() != null ? detail.getProduct().getProductName() : "N/A"));
//                System.out.println("  Quantity: " + detail.getQuantity());
//                System.out.println("  TotalPrice: " + detail.getTotalPrice());
//                System.out.println("--------------------");
//            }
//        }

//        if (detail != null) {
//            System.out.println("Trước khi update:");
//            System.out.println("ProductID: " + detail.getProductID());
//            System.out.println("Quantity: " + detail.getQuantity());
//            System.out.println("Discount: " + detail.getDiscount());
//            System.out.println("Tìm tồn kho với shopID = " + detail.getShopID() + ", productID = " + detail.getProductID());
//
//
//            // Cập nhật thông tin
//            detail.setProductID(1); // ví dụ sửa productID
//            detail.setQuantity(1);       // ví dụ sửa số lượng
//            detail.setDiscount(0.15);    // ví dụ sửa discount
//
//            boolean success = i.updateInvoiceDetail(detail, inventoryDAO);
//
//            if (success) {
//                System.out.println("Cập nhật thành công.");
//            } else {
//                System.out.println("Cập nhật thất bại.");
//            }
//        } else {
//            System.out.println("Không tìm thấy InvoiceDetail với ID = 20");
//        }
//
//    }
//        InvoiceDetail existingDetail = i.getInvoiceDetailByInvoiceDetailID(4);
//        if (existingDetail == null) {
//            System.out.println("Không tìm thấy InvoiceDetail với ID = " + 4);
//            return;
//        }
//        InvoiceDetail updatedDetail = new InvoiceDetail();
//        updatedDetail.setInvoiceDetailID(4); // rất quan trọng!
//        updatedDetail.setInvoiceID(existingDetail.getInvoiceID()); // giữ nguyên
//        updatedDetail.setProductID(existingDetail.getProductID()); // giữ nguyên
//        updatedDetail.setUnitPrice(450000.0);   // Giá mới
//
//        updatedDetail.setQuantity(existingDetail.getQuantity() + 2); // tăng số lượng
//        updatedDetail.setDiscount(0.2);      // giảm giá 20%
//        updatedDetail.setShopID(existingDetail.getShopID());
//        String shopID = "S001";
//        updatedDetail.setShopID(existingDetail.getShopID());
//        updatedDetail.calculateTotalPrice();
//        boolean result = i.updateInvoiceDetail(updatedDetail, inventoryDAO);
//        if (result) {
//            System.out.println("Cập nhật thành công!");
//            InvoiceDetail afterUpdate = i.getInvoiceDetailByInvoiceDetailID(4);
//            System.out.println("Sau khi update:");
//            System.out.println(afterUpdate);
//        } else {
//            System.out.println("Cập nhật thất bại!");
//        }// rất quan trọng
        //add
//        String invoiceID = "INV001";
//        String shopID = "S001";
//        String productID = "P001";
//
//        // Thêm lần 1
//        InvoiceDetail detail1 = new InvoiceDetail();
//        detail1.setInvoiceID(invoiceID);
//        detail1.setProductID(productID);
//        detail1.setUnitPrice(100000.0);
//        detail1.setQuantity(-1);
//        detail1.setDiscount(0.1);
//        detail1.calculateTotalPrice();
//
//        boolean result1 = dao.addInvoiceDetailAndUpdateInventory(detail1, shopID);
//        System.out.println("Thêm lần 1: " + (result1 ? "Thành công" : "Thất bại"));
//
//        // Thêm lần 2, cùng sản phẩm, cùng hóa đơn, cùng cửa hàng, quantity khác
//        InvoiceDetail detail2 = new InvoiceDetail();
//        detail2.setInvoiceID(invoiceID);
//        detail2.setProductID(productID);
//        detail2.setUnitPrice(100000.0);
//        detail2.setQuantity(-1);  // muốn cộng thêm 3
//        detail2.setDiscount(0.1);
//        detail2.calculateTotalPrice();
//
//        boolean result2 = dao.addInvoiceDetailAndUpdateInventory(detail2, shopID);
//        System.out.println("Thêm lần 2: " + (result2 ? "Thành công" : "Thất bại"));
//delete
//        int invoiceDetailIDToDelete = 9; // thay bằng InvoiceDetailID bạn muốn xóa
//        boolean deletedByDetailID = i.deleteByDetailID(invoiceDetailIDToDelete);
//        if (deletedByDetailID) {
//            System.out.println("Xóa InvoiceDetail có InvoiceDetailID = " + invoiceDetailIDToDelete + " thành công.");
//        } else {
//            System.out.println("Xóa InvoiceDetail theo InvoiceDetailID thất bại hoặc không có dữ liệu.");
//        }
//        InvoiceDetail newDetail = new InvoiceDetail();
//        newDetail.setInvoiceID("INV1005"); // phải tồn tại trong bảng Invoice
//        newDetail.setProductID("P001");   // phải tồn tại trong bảng Product và Inventory
//        newDetail.setUnitPrice(100.0);
//        newDetail.setQuantity(2);         // <= số lượng tồn kho hiện tại
//        newDetail.setDiscount(0.1);       // 10% discount
//        newDetail.calculateTotalPrice();  // tính giá sau giảm
//
//        // shopID cần truyền đúng để kiểm tra tồn kho
//        String shopID = "S001"; // phải tồn tại trong bảng Shop & Inventory
//
//        boolean result = i.addInvoiceDetailAndUpdateInventory(newDetail, shopID);
//        if (result) {
//            System.out.println("Thêm invoice detail và cập nhật tồn kho thành công.");
//        } else {
//            System.out.println("Thêm thất bại: Không đủ tồn kho hoặc lỗi SQL.");
//        }
    }
}
