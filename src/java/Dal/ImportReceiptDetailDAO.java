/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Models.ImportReceiptDetail;
import Context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.time.Instant;
/**
 *
 * @author Thai Anh
 */
public class ImportReceiptDetailDAO {
     private static final Logger LOGGER = Logger.getLogger(ImportReceiptDetailDAO.class.getName());
    private Connection connection;

    public ImportReceiptDetailDAO(Connection connection) {
        this.connection = connection;
    }

    // Get all import receipt details
    public List<ImportReceiptDetail> getAllDetails() {
        List<ImportReceiptDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM ImportReceiptDetail";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(buildImportReceiptDetail(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all import receipt details", e);
        }
        return list;
    }

    // Get by ImportReceiptID
    public List<ImportReceiptDetail> getDetailsByReceiptID(int receiptID) {
        List<ImportReceiptDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM ImportReceiptDetail WHERE ImportReceiptID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, receiptID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(buildImportReceiptDetail(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching import receipt details by receipt ID", e);
        }
        return list;
    }

    // Insert
    public void insertDetail(ImportReceiptDetail d) {
        String sql = "INSERT INTO ImportReceiptDetail (ImportReceiptDetailID, ImportReceiptID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, d.getImportReceiptDetailID());
            ps.setInt(2, d.getImportReceiptID());
            ps.setString(3, d.getProductID());
            ps.setInt(4, d.getQuantity());
            ps.setFloat(5, (float) d.getPrice());
            ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting import receipt detail", e);
        }
    }

    // Delete
    public void deleteDetail(int detailID) {
        String sql = "DELETE FROM ImportReceiptDetail WHERE ImportReceiptDetailID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detailID);
            ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting import receipt detail", e);
        }
    }

    // Helper method
    private ImportReceiptDetail buildImportReceiptDetail(ResultSet rs) throws SQLException {
        return new ImportReceiptDetail(
                rs.getInt("ImportReceiptDetailID"),
                rs.getInt("ImportReceiptID"),
                rs.getString("ProductID"),
                rs.getInt("Quantity"),
                rs.getFloat("Price")
        );
    }
    public static void main(String[] args) {
        // Kết nối database (thay đổi URL, user, pass tùy cấu hình của bạn)
        

        try(Connection conn = new DBContext("SWP2").getConnection())  {
            ImportReceiptDetailDAO dao = new ImportReceiptDetailDAO(conn);

            // Test insert
            ImportReceiptDetail newDetail = new ImportReceiptDetail(
                    106,  // ImportReceiptDetailID (giả định)
                    2,     // ImportReceiptID
                    "P001",// ProductID
                    10,    // Quantity
                    150.0f // Price
                    //(1, 1, 'P01', 100, 10000),
            );
            dao.insertDetail(newDetail);
            System.out.println("Inserted successfully.");

            // Test getAllDetails
            List<ImportReceiptDetail> allDetails = dao.getAllDetails();
            System.out.println("All Details:");
            for (ImportReceiptDetail detail : allDetails) {
                System.out.println(detail);
            }

            // Test getDetailsByReceiptID
            List<ImportReceiptDetail> receiptDetails = dao.getDetailsByReceiptID(1);
            System.out.println("Details for ImportReceiptID = 1:");
            for (ImportReceiptDetail detail : receiptDetails) {
                System.out.println(detail);
            }

            // Test delete
            dao.deleteDetail(1001);
            System.out.println("Deleted detail with ID = 1001");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
