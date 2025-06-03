/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.Inventory;
import Models.InvoiceDetail;
import Models.Shop;
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

    public List<InvoiceDetail> getAllInvoiceDetail(String sql) {
        List<InvoiceDetail> list = new ArrayList<>();
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(new InvoiceDetail(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getInt(5),
                        rs.getDouble(6)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean addInvoiceDetailAndUpdateInventory(InvoiceDetail detail, String shopID) {
        InventoryDAO iDAO = new InventoryDAO(connection);
        Inventory inventory = iDAO.getInventoryByShopAndProduct(detail.getProductID(), shopID);

        int currentQuantity = inventory.getQuantity();
        if (currentQuantity < detail.getQuantity()) {
            System.out.println("Tồn kho không đủ! Hiện tại có " + currentQuantity);
            return false;
        }
        String sql = "INSERT INTO InvoiceDetail (InvoiceID, ProductID, UnitPrice, Quantity, Discount, TotalPrice)"
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, detail.getInvoiceID());
            ptm.setString(2, detail.getProductID());
            ptm.setDouble(3, detail.getUnitPrice());
            ptm.setInt(4, detail.getQuantity());
            ptm.setDouble(5, detail.getDiscount());
            ptm.setDouble(6, detail.getTotalPrice());
            ptm.executeUpdate();
            
            int newQuantity = currentQuantity - detail.getQuantity();
            boolean updateInven = iDAO.updateInventoryQuantity(inventory.getInventoryID(), newQuantity);
            return updateInven;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
