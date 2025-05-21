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
import Models.TransferReceipt;

/**
 *
 * @author ADMIN
 */
public class TransferReceiptDAO extends DBContext {

    public Vector<TransferReceipt> getAllProduct(String sql) {
        Vector<TransferReceipt> listTransferReceipt = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                TransferReceipt p = new TransferReceipt(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getDate(6),
                        rs.getString(7));

                listTransferReceipt.add(p);
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return listTransferReceipt;
    }

    public int insertTransferReceipt(TransferReceipt p) {
        String sql = "INTO [dbo].[TransferReceipt]\n"
                + "           ([ProductID]\n"
                + "           ,[FromInventoryID]\n"
                + "           ,[ToInventoryID]\n"
                + "           ,[Quantity]\n"
                + "           ,[TransferDate]\n"
                + "           ,[Note]\n"
                + "     VALUES(?,?,?,?,?,?,?)";

        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getProductID());
            ptm.setString(2, p.getFromInventoryID());
            ptm.setString(3, p.getToInventoryID());
            ptm.setInt(4, p.getQuantity());
            ptm.setDate(5, (Date) p.getTransferDate());
            ptm.setString(6, p.getNote());
            

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public TransferReceipt searchTransferReceipt(String TransferReceiptID) {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[TransferReceipt]\n"
                + "  WHERE TransferReceiptID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, TransferReceiptID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                TransferReceipt p = new TransferReceipt(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getDate(6),
                        rs.getString(7));
                
                return p;
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return null;
    }

    

    

    public void updateTransferReceipt(TransferReceipt p) {
        String sql = "UPDATE [dbo].[TransferReceipt]\n"
                + "   SET ([ProductID]\n"
                + "           ,[FromInventoryID]\n"
                + "           ,[ToInventoryID]\n"
                + "           ,[Quantity]\n"
                + "           ,[TransferDate]\n"
                + "           ,[Note]\n"
                + " WHERE TransferReceiptID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getProductID());
            ptm.setString(2, p.getFromInventoryID());
            ptm.setString(3, p.getToInventoryID());
            ptm.setInt(4, p.getQuantity());
            ptm.setDate(5, (Date) p.getTransferDate());
            ptm.setString(6, p.getNote());

            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    

    
}
