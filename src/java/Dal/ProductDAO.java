/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import Models.Product;

/**
 *
 * @author ADMIN
 */
public class ProductDAO extends DBContext {

    public Vector<Product> getAllProduct(String sql) {
        Vector<Product> listProduct = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Product p = new Product(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getDate(8),
                        rs.getString(9));

                listProduct.add(p);
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return listProduct;
    }

    public int insertProduct(Product p) {
        String sql = "INSERT INTO [dbo].[tblProduct]\n"
                + "           ([ProductName]\n"
                + "           ,[CategoryID]\n"
                + "           ,[UnitID]\n"
                + "           ,[Price]\n"
                + "           ,[Description]\n"
                + "           ,[Status]\n"
                + "           ,[CreatedDate]\n"
                + "           ,[CreatedBy]\n"
                + "     VALUES(?,?,?,?,?,?,?,?)";

        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getProductName());
            ptm.setString(2, p.getCategoryID());
            ptm.setString(3, p.getUnitID());
            ptm.setDouble(4, p.getPrice());
            ptm.setString(5, p.getDescription());
            ptm.setInt(6, p.getStatus());
            ptm.setDate(7, (Date) p.getCreatedDate());
            ptm.setString(8, p.getCreatedBy());
            

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public Product searchProduct(int productID) {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[tblProduct]\n"
                + "  WHERE productID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, productID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                Product p = new Product(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getDate(8),
                        rs.getString(9));
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return null;
    }

    

    

    public void updateProduct(Product p) {
        String sql = "UPDATE [dbo].[tblProduct]\n"
                + "   SET ([ProductName]\n"
                + "           ,[CategoryID]\n"
                + "           ,[UnitID]\n"
                + "           ,[Price]\n"
                + "           ,[Description]\n"
                + "           ,[Status]\n"
                + "           ,[CreatedDate]\n"
                + "           ,[CreatedBy]\n"
                + " WHERE productID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getProductName());
            ptm.setString(2, p.getCategoryID());
            ptm.setString(3, p.getUnitID());
            ptm.setDouble(4, p.getPrice());
            ptm.setString(5, p.getDescription());
            ptm.setInt(6, p.getStatus());
            ptm.setDate(7, (Date) p.getCreatedDate());
            ptm.setString(8, p.getCreatedBy());
            ptm.setString(10, p.getProductID());

            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public void changeStatus(int productID, int newStatus) {
        //code here
        String sql = "update tblProduct\n"
                + "set status=?\n"
                + "where productID=?";
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, newStatus);
            ptm.setInt(2, productID);
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }

    }
    public void delete(int productID){}

    
}
