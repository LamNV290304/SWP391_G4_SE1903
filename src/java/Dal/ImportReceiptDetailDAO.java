package Dal;

import Models.ImportReceiptDetail;
import Context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String sql = "INSERT INTO ImportReceiptDetail (ImportReceiptDetailID, ImportReceiptID, ProductID, Quantity, Price, Note) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, d.getImportReceiptDetailID());
            ps.setInt(2, d.getImportReceiptID());
            ps.setString(3, d.getProductID());
            ps.setInt(4, d.getQuantity());
            ps.setDouble(5, d.getPrice());
            ps.setString(6, d.getNote());
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
                rs.getDouble("Price"),
                rs.getString("Note")
        );
    }

    // Test main method
    public static void main(String[] args) {
        try (Connection conn = new DBContext("SWP6").getConnection()) {
            ImportReceiptDetailDAO dao = new ImportReceiptDetailDAO(conn);

            // Test insert
            ImportReceiptDetail newDetail = new ImportReceiptDetail(
                    106,
                    4,
                    "P001",
                    10,
                    150.0,
                    "Hàng nhập đợt 1"
            );
          //  dao.insertDetail(newDetail);
            System.out.println("Inserted successfully.");

            // Test getAllDetails
            List<ImportReceiptDetail> allDetails = dao.getAllDetails();
            System.out.println("All Details:");
            for (ImportReceiptDetail detail : allDetails) {
                System.out.println("ID: " + detail.getImportReceiptDetailID()
                        + ", Product: " + detail.getProductID()
                        + ", Qty: " + detail.getQuantity()
                        + ", Price: " + detail.getPrice()
                        + ", Note: " + detail.getNote());
            }

            // Test getDetailsByReceiptID
            List<ImportReceiptDetail> receiptDetails = dao.getDetailsByReceiptID(2);
            System.out.println("Details for ImportReceiptID = 2:");
            for (ImportReceiptDetail detail : receiptDetails) {
                System.out.println("ID: " + detail.getImportReceiptDetailID()
                        + ", Product: " + detail.getProductID()
                        + ", Qty: " + detail.getQuantity()
                        + ", Price: " + detail.getPrice()
                        + ", Note: " + detail.getNote());
            }

            // Test delete
            dao.deleteDetail(1231);
            System.out.println("Deleted detail with ID = 106");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
