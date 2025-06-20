/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.TransferReceiptDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author ADMIN
 */
public class TransferReceiptDetailDAO {

    private Connection connection;

    public TransferReceiptDetailDAO(Connection connection) {
        this.connection = connection;
    }

    public Vector<TransferReceiptDetail> getAllTransferReceiptDetail(String sql){
        Vector<TransferReceiptDetail> listTransferReceiptDetail = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                TransferReceiptDetail p = new TransferReceiptDetail(rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4));

                listTransferReceiptDetail.add(p);
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return listTransferReceiptDetail;
    }

    public int insertTransferReceiptDetail(TransferReceiptDetail p) {
        String sql = "INSERT INTO [dbo].[TransferReceiptDetail]\n"
                + "           ([TransferReceiptID]\n"
                + "           ,[ProductID]\n"
                + "           ,[Quantity])\n"
                + "     VALUES (?,?,?)";

        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getTransferReceiptID());
            ptm.setString(2, p.getProductID());
            ptm.setInt(3, p.getQuantity());

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteTransferReceiptDetail(int TransferReceiptDetailID) {
        String sql = "DELETE FROM [dbo].[TransferReceiptDetail]\n"
                + "      WHERE TransferReceiptDetailID=?";
        int n = 0;

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, TransferReceiptDetailID);

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return n;
    }

    public TransferReceiptDetail searchTransferReceiptDetail(int TransferReceiptDetailID) {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[TransferReceiptDetail]\n"
                + "  WHERE TransferReceiptDetailID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, TransferReceiptDetailID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                TransferReceiptDetail p = new TransferReceiptDetail(rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4)
                );

                return p;
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return null;
    }

    public void updateTransferReceiptDetail(TransferReceiptDetail p) {
        String sql = "UPDATE [dbo].[TransferReceipt]\n"
                + "   SET (\n"
                + "           ,[FromInventoryID]\n"
                + "           ,[ToInventoryID]\n"
                + "           ,[TransferDate]\n"
                + "           ,[Note]\n"
                + "           ,[Status])\n"
                + " WHERE TransferReceiptID=?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getTransferReceiptDetailID());
            ptm.setInt(2, p.getTransferReceiptID());
            ptm.setString(3, p.getProductID());
            ptm.setInt(4, p.getQuantity());
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public static void main(String[] args) {
    try {
        // Bước 1: Kết nối tới database
        DBContext connection = new DBContext("SWP6");

        // Bước 2: Khởi tạo DAO
        TransferReceiptDetailDAO dao = new TransferReceiptDetailDAO(connection.getConnection());

        // Bước 3: Tạo đối tượng cần insert
        // Giả sử TransferReceiptID = "TR001", ProductID = "P002", Quantity = 10
        TransferReceiptDetail newDetail = new TransferReceiptDetail(1, "P002", 10);

        // Bước 4: Gọi hàm insert
        int result = dao.insertTransferReceiptDetail(newDetail);

        // Bước 5: Kiểm tra kết quả
        if (result > 0) {
            System.out.println("Insert thành công!");
        } else {
            System.out.println("Insert thất bại!");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


}
