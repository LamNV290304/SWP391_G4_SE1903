package Dal;

import Models.ImportReceiptDetail;
import Context.DBContext;
import Models.ImportReceipt;
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
                rs.getInt("ImportReceiptID"),
                rs.getString("ProductID"),
                rs.getInt("Quantity"),
                rs.getDouble("Price"),
                rs.getString("Note")
        );
    }
// Lấy ImportReceiptID dựa trên các thông tin còn lại
public Integer getImportReceiptIDByInfo(ImportReceipt ir) {
    String sql = "SELECT ImportReceiptID FROM ImportReceipt " +
                 "WHERE Code = ? AND SupplierID = ? AND EmployeeID = ? AND ShopID = ? " +
                 "AND ReceiptDate = ? AND TotalAmount = ? AND Note = ? AND Status = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, ir.getCode());
        ps.setString(2, ir.getSupplierID());
        ps.setString(3, ir.getEmployeeID());
        ps.setString(4, ir.getShopID());

        // Dùng Timestamp để đảm bảo đúng kiểu dữ liệu DATETIME
        ps.setTimestamp(5, new Timestamp(ir.getReceiptDate().getTime()));
        ps.setFloat(6, (float) ir.getTotalAmount());
        ps.setString(7, ir.getNote());
        ps.setBoolean(8, ir.isStatus());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ImportReceiptID");
            }
        }
    } catch (SQLException e) {
        Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
    }
    return null; // Nếu không tìm thấy
}

    // Test main method
    public static void main(String[] args) {
        try (Connection conn = new DBContext("SWP4").getConnection()) {
            ImportReceiptDetailDAO dao = new ImportReceiptDetailDAO(conn);

            // Test insert
            ImportReceiptDetail newDetail = new ImportReceiptDetail(
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
