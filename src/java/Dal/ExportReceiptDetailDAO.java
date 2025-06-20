/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.ExportReceiptDetail;
import Context.DBContext;
import Models.ExportReceipt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class ExportReceiptDetailDAO {
    private static final Logger LOGGER = Logger.getLogger(ExportReceiptDetailDAO.class.getName());
    private Connection connection;

    public ExportReceiptDetailDAO(Connection connection) {
        this.connection = connection;
    }

    // Get all export receipt details
    public List<ExportReceiptDetail> getAllDetails() {
        List<ExportReceiptDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM ExportReceiptDetail";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(buildExportReceiptDetail(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all export receipt details", e);
        }
        return list;
    }

    // Get by ExportReceiptID
    public List<ExportReceiptDetail> getDetailsByReceiptID(int receiptID) {
        List<ExportReceiptDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM ExportReceiptDetail WHERE ExportReceiptID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, receiptID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(buildExportReceiptDetail(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching export receipt details by receipt ID", e);
        }
        return list;
    }

    // Insert
    public void insertDetail(ExportReceiptDetail d) {
        String sql = "INSERT INTO ExportReceiptDetail (ExportReceiptID, ProductID, Quantity, Price, Note) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, d.getImportReceiptID()); // Dù tên là "importReceiptID" trong model, thực ra là export
            ps.setString(2, d.getProductID());
            ps.setInt(3, d.getQuantity());
            ps.setDouble(4, d.getPrice());
            ps.setString(5, d.getNote());
            ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting export receipt detail", e);
        }
    }

    // Delete
    public void deleteDetail(int detailID) {
        String sql = "DELETE FROM ExportReceiptDetail WHERE ExportReceiptDetailID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detailID);
            ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting export receipt detail", e);
        }
    }

    // Helper method
    private ExportReceiptDetail buildExportReceiptDetail(ResultSet rs) throws SQLException {
        ExportReceiptDetail detail = new ExportReceiptDetail(
                rs.getInt("ExportReceiptID"),
                rs.getString("ProductID"),
                rs.getInt("Quantity"),
                rs.getDouble("Price"),
                rs.getString("Note")
        );
        detail.setEportReceiptDetailID(rs.getInt("ExportReceiptDetailID"));
        return detail;
    }

    // Test main
    public static void main(String[] args) {
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            ExportReceiptDetailDAO dao = new ExportReceiptDetailDAO(conn);

            // Insert test
            ExportReceiptDetail newDetail = new ExportReceiptDetail(
                    2,
                    "P001",
                    5,
                    200.0,
                    "Hàng xuất kho"
            );
           // dao.insertDetail(newDetail);
            System.out.println("Insert thành công");

            // Get all
            List<ExportReceiptDetail> all = dao.getAllDetails();
            for (ExportReceiptDetail d : all) {
                System.out.println(d);
            }

            // Get by receipt ID
            List<ExportReceiptDetail> listByID = dao.getDetailsByReceiptID(2);
            System.out.println("Danh sách theo ExportReceiptID = 2:");
            for (ExportReceiptDetail d : listByID) {
                System.out.println(d);
            }

            // Delete (chỉ thử nếu có ID cụ thể)
          //  dao.deleteDetail(123); // thay 123 bằng ID hợp lệ nếu cần
            System.out.println("Xóa thành công");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
