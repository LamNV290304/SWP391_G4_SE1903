/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.InventoryCheck;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class InventoryCheckDAO {

    private final Connection connection;

    public InventoryCheckDAO(Connection connection) {
        this.connection = connection;
    }

    public List<InventoryCheck> getAllInventoryChecks() {
        List<InventoryCheck> list = new ArrayList<>();
        String sql = "SELECT * FROM InventoryCheck";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToInventoryCheck(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public InventoryCheck getInventoryCheckByID(int id) {
        String sql = "SELECT * FROM InventoryCheck WHERE InventoryCheckID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventoryCheck(rs);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public boolean insertInventoryCheck(InventoryCheck ic) {
        String sql = "INSERT INTO InventoryCheck (EmployeeID, ShopID, CheckDate, Note) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ic.getEmployeeID());
            ps.setInt(2, ic.getShopID());
            ps.setTimestamp(3, new Timestamp(ic.getCheckDate().getTime()));
            ps.setString(4, ic.getNote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean deleteInventoryCheck(int id) {
        String sql = "DELETE FROM InventoryCheckDetail WHERE InventoryCheckID = ?;"
                + "DELETE FROM InventoryCheck WHERE InventoryCheckID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public Integer getInventoryCheckIDByInfo(InventoryCheck ic) {
        String sql = "SELECT InventoryCheckID FROM InventoryCheck "
                + "WHERE EmployeeID = ? AND ShopID = ? AND CheckDate = ? AND Note = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ic.getEmployeeID());
            ps.setInt(2, ic.getShopID());
            ps.setTimestamp(3, new Timestamp(ic.getCheckDate().getTime()));
            ps.setString(4, ic.getNote());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("InventoryCheckID");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public int getLatestInventoryCheckID() {
        String sql = "SELECT TOP 1 InventoryCheckID FROM InventoryCheck ORDER BY InventoryCheckID DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("InventoryCheckID");
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }

    private InventoryCheck mapResultSetToInventoryCheck(ResultSet rs) throws SQLException {
        InventoryCheck ic = new InventoryCheck(
                rs.getInt("EmployeeID"),
                rs.getInt("ShopID"),
                rs.getTimestamp("CheckDate"),
                rs.getString("Note")
        );
        ic.setInventoryCheckID(rs.getInt("InventoryCheckID"));
        return ic;
    }

    public List<InventoryCheck> filterInventoryChecks(String shopID, String employeeID, String fromDate, String toDate) {
        List<InventoryCheck> list = new ArrayList<>();

        // Xây dựng câu lệnh SQL động
        StringBuilder sql = new StringBuilder("SELECT * FROM InventoryCheck WHERE 1=1");

        if (shopID != null && !shopID.isEmpty()) {
            sql.append(" AND ShopID = ").append(shopID);
        }

        if (employeeID != null && !employeeID.isEmpty()) {
            sql.append(" AND EmployeeID = ").append(employeeID);
        }

        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND CheckDate >= '").append(fromDate).append("'");
        }

        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND CheckDate <= '").append(toDate).append("'");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString()); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToInventoryCheck(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(InventoryCheckDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return list;
    }

    public static void main(String[] args) {
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            InventoryCheckDAO dao = new InventoryCheckDAO(conn);

            // Thêm kiểm kê mới
            InventoryCheck check = new InventoryCheck(2, 1, new java.util.Date(), "Kiểm kê test tháng 6");
            //dao.insertInventoryCheck(check);
            System.out.println("✅ Đã thêm phiếu kiểm kê");

            // Lấy tất cả phiếu kiểm kê
            List<InventoryCheck> list = dao.getAllInventoryChecks();
            for (InventoryCheck i : list) {
                System.out.println("🧾 " + i);
            }

            // Lấy 1 phiếu kiểm kê theo ID
            InventoryCheck found = dao.getInventoryCheckByID(list.get(list.size() - 1).getInventoryCheckID());
            System.out.println("🔍 Tìm thấy phiếu: " + found);

            // Xoá phiếu kiểm kê (nếu cần)
            dao.deleteInventoryCheck(17);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
