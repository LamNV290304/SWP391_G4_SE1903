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

    public Vector<TransferReceipt> getAllTransferReceipt(String sql) throws SQLException {
        Vector<TransferReceipt> listTransferReceipt = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                TransferReceipt p = new TransferReceipt(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
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
                + "([TransferReceiptID], [FromShopID], [ToShopID], [TransferDate], [Note], [Status])\n"
                + "VALUES (?, ?, ?, ?, ?, ?)";

        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getTransferReceiptID());
            ptm.setString(2, p.getFromShopID());
            ptm.setString(3, p.getToShopID());
            ptm.setDate(4, new java.sql.Date(p.getTransferDate().getTime()));
            ptm.setString(5, p.getNote());
            ptm.setInt(6, p.getStatus());

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteTransferReceipt(String TransferReceiptID) {
        String sql = "DELETE FROM [dbo].[TransferReceipt]\n"
                + "      WHERE transferReceiptID=?";
        int n = 0;

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, TransferReceiptID);

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
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
            ptm.setString(1, p.getFromShopID());
            ptm.setString(2, p.getToShopID());
            ptm.setDate(3, (java.sql.Date) (Date) p.getTransferDate());
            ptm.setString(4, p.getNote());
            ptm.setInt(5, p.getStatus());
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public int updateTransferReceiptStatus(String transferReceiptID, int status) {
        int result = 0;
        try {
            String sql = "UPDATE TransferReceipt SET Status = ? WHERE TransferReceiptID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setString(2, transferReceiptID);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            DBContext connection = new DBContext("SWP6");
            TransferReceiptDAO dao = new TransferReceiptDAO(connection.getConnection());

            // Tạo đối tượng TransferReceipt
            TransferReceipt newReceipt = new TransferReceipt(
                    "T003", // TransferReceiptID
                    "S001", // FromInventoryID
                    "S002", // ToInventoryID
                    new java.util.Date(), // TransferDate (ngày hiện tại)
                    "Test insert transfer", // Note
                    0 // Status
            );

            // Gọi hàm insert
            int result = dao.insertTransferReceipt(newReceipt);

            // Kiểm tra kết quả
            if (result > 0) {
                System.out.println("Insert thành công phiếu chuyển kho: " + newReceipt.getTransferReceiptID());
            } else {
                System.out.println("Insert thất bại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
}
