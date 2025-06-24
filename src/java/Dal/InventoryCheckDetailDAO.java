/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Context.DBContext;
import Models.InventoryCheckDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class InventoryCheckDetailDAO {
    private final Connection connection;

    public InventoryCheckDetailDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy tất cả chi tiết kiểm kê theo ID phiếu kiểm kê
    public List<InventoryCheckDetail> getDetailsByInventoryCheckID(int inventoryCheckID) {
        List<InventoryCheckDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM InventoryCheckDetail WHERE InventoryCheckID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventoryCheckID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDetail(rs));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDetailDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Thêm chi tiết kiểm kê
    public boolean insertDetail(InventoryCheckDetail detail) {
        String sql = "INSERT INTO InventoryCheckDetail (InventoryCheckID, ProductID, QuantitySystem, QuantityActual, Note) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detail.getInventoryCheckID());
            ps.setInt(2, detail.getProductID());
            ps.setInt(3, detail.getQuantitySystem());
            ps.setInt(4, detail.getQuantityActual());
            ps.setString(5, detail.getNote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDetailDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Xóa chi tiết kiểm kê theo ID phiếu kiểm kê
    public boolean deleteDetailsByInventoryCheckID(int inventoryCheckID) {
        String sql = "DELETE FROM InventoryCheckDetail WHERE InventoryCheckID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventoryCheckID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDetailDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    private InventoryCheckDetail mapResultSetToDetail(ResultSet rs) throws SQLException {
        InventoryCheckDetail detail = new InventoryCheckDetail(
            rs.getInt("InventoryCheckID"),
            rs.getInt("ProductID"),
            rs.getInt("QuantitySystem"),
            rs.getInt("QuantityActual"),
            rs.getString("Note")
        );
        detail.setInventoryCheckDetailID(rs.getInt("InventoryCheckDetailID"));
        return detail;
    }
    public static void main(String[] args) {
    try (Connection conn = new DBContext("SWP7").getConnection()) {
        InventoryCheckDetailDAO dao = new InventoryCheckDetailDAO(conn);

        // ID của phiếu kiểm kê cần test (chắc chắn tồn tại)
        int testInventoryCheckID = 1;

        // Thêm chi tiết mới
        InventoryCheckDetail d1 = new InventoryCheckDetail(testInventoryCheckID, 1, 10, 9, "Lệch -1");
        InventoryCheckDetail d2 = new InventoryCheckDetail(testInventoryCheckID, 2, 5, 6, "Dư +1");
        dao.insertDetail(d1);
        dao.insertDetail(d2);
        System.out.println("✅ Đã thêm chi tiết phiếu kiểm kê");

        // Lấy danh sách chi tiết theo InventoryCheckID
        List<InventoryCheckDetail> details = dao.getDetailsByInventoryCheckID(testInventoryCheckID);
        for (InventoryCheckDetail d : details) {
            System.out.println("📋 Chi tiết: " + d);
        }

        // Xoá tất cả chi tiết nếu muốn
        // dao.deleteDetailsByInventoryCheckID(testInventoryCheckID);
        // System.out.println("🗑️ Đã xóa chi tiết của phiếu kiểm kê " + testInventoryCheckID);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
