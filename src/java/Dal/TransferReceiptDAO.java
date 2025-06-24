/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.TransferReceipt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class TransferReceiptDAO {
    private Connection connection;

    public TransferReceiptDAO(Connection connection) {
        this.connection = connection;
    }

    public Vector<TransferReceipt> getAllTransferReceipt(String sql)  {

        Vector<TransferReceipt> listTransferReceipt = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                TransferReceipt p = new TransferReceipt(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getInt(6));

                listTransferReceipt.add(p);
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return listTransferReceipt;
    }

    public int insertTransferReceipt(TransferReceipt p) {
        String sql = "INSERT INTO [dbo].[TransferReceipt]\n"

                + "( [FromShopID], [ToShopID], [TransferDate], [Note], [Status])\n"

                + "VALUES (?, ?, ?, ?, ?)";

        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);

            ptm.setInt(1, p.getFromShopID());
            ptm.setInt(2, p.getToShopID());
            ptm.setDate(3, new java.sql.Date(p.getTransferDate().getTime()));
            ptm.setString(4, p.getNote());
            ptm.setInt(5, p.getStatus());


            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }


    public int deleteTransferReceipt(int TransferReceiptID) {
        String sql = "DELETE FROM [dbo].[TransferReceipt]\n"
                + "      WHERE transferReceiptID=?";
        int n = 0;

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);

            ptm.setInt(1, TransferReceiptID);

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return n;
    }


    public TransferReceipt searchTransferReceipt(int TransferReceiptID) {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[TransferReceipt]\n"
                + "  WHERE TransferReceiptID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, TransferReceiptID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                TransferReceipt p = new TransferReceipt(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getInt(6)
                );

                return p;
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return null;
    }

    

    public void updateTransferReceipt(TransferReceipt p) {
        String sql = "UPDATE [dbo].[TransferReceipt]\n"
                + "   SET (\n"
                + "           ,[FromShopID]\n"
                + "           ,[ToShopID]\n"
                + "           ,[TransferDate]\n"
                + "           ,[Note]\n"
                + "           ,[Status])\n"
                + " WHERE TransferReceiptID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getFromShopID());
            ptm.setInt(2, p.getToShopID());
            ptm.setDate(3, (java.sql.Date) (Date) p.getTransferDate());
            ptm.setString(4, p.getNote());
            ptm.setInt(5, p.getStatus());
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public int updateTransferReceiptStatus(int transferReceiptID, int status) {
        int result = 0;
        try {
            String sql = "UPDATE TransferReceipt SET Status = ? WHERE TransferReceiptID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, transferReceiptID);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    
     
}
