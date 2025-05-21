/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import Models.Product;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class DAOProduct extends DBContext {
    
    public int updateProduct(Product pro) {
    int n = 0;
    String sql = """
                 UPDATE [Product]
                 SET 
                    [ProductName] = ?, [CategoryID] = ?, [UnitID] = ?, 
                    [Price] = ?, [Description] = ?, [Status] = ?, 
                    [CreatedDate] = ?, [CreatedBy] = ?
                 WHERE [ProductID] = ?
                 """;
    try {
        PreparedStatement pre = connection.prepareStatement(sql);
        pre.setString(1, pro.getProductName());
        pre.setString(2, pro.getCategoryID());
        pre.setString(3, pro.getUnitID());
        pre.setDouble(4, pro.getPrice());
        pre.setString(5, pro.getDescription());
        pre.setBoolean(6, pro.isStatus());
        pre.setString(7, pro.getCreatedDate());
        pre.setString(8, pro.getCreatedBy());
        pre.setString(9, pro.getProductID());

        n = pre.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(DAOProduct.class.getName()).log(Level.SEVERE, null, ex);
    }
    return n;
}

    
    public int addProduct(Product pro) {
        int n = 0;
        String sql = "INSERT INTO [Product]([ProductID],[ProductName],[CategoryID],[UnitID]\n"
                + "           ,[Price],[Description],[Status],[CreatedDate],[CreatedBy])\n"
                + "     VALUES\n"
                + "           (?,?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement pre = connection.prepareStatement(sql);
            pre.setString(1, pro.getProductID());
            pre.setString(2, pro.getProductName());
            pre.setString(3, pro.getCategoryID());
            pre.setString(4, pro.getUnitID());
            pre.setDouble(5, pro.getPrice());
            pre.setString(6, pro.getDescription());
            pre.setBoolean(7, pro.isStatus());
            pre.setString(8, pro.getCreatedDate());
            pre.setString(9, pro.getCreatedBy());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public Vector<Product> getProduct(String sql) {
        Vector<Product> vector = new Vector<Product>();

        PreparedStatement pre;
        try {
            pre = connection.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                String ProductID = rs.getString(1);
                String ProductName = rs.getString(2);
                String CategoryID = rs.getString(3);
                String UnitID = rs.getString(4);
                double Price = rs.getDouble(5);
                String Description = rs.getString(6);
                boolean Status = rs.getBoolean(7);
                String CreatedDate = rs.getString(8);
                String CreatedBy = rs.getString(9);
                Product pro = new Product(ProductID, ProductName, CategoryID, UnitID, Price, Description, Status, CreatedDate, CreatedBy);
                vector.add(pro);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
    public static void main(String[] args) {
        DAOProduct dao = new DAOProduct();
        //int n = dao.addProduct(new Product("P002", "Ao", "C001", "U001", 100, "none", true, "10-10-2000", "10-10-2000"));
        int n = dao.updateProduct(new Product("P002", "Quan", "C001", "U001", 100, "none", true, "10-10-2000", "10-10-2000"));
        Vector<Product> vector = dao.getProduct("select * from Product");
        for (Product product : vector) {
            System.out.println(product);
        }
    }

}
