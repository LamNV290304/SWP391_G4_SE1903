/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Context.DBContext;
import Models.Unit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class UnitDAO {
       private Connection connection;

    public UnitDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy danh sách tất cả đơn vị (Status = 1)
    public List<Unit> getAllActiveUnits() {
    List<Unit> units = new ArrayList<>();
    String sql = "SELECT UnitID, Description, Status FROM Unit WHERE Status = 1 ORDER BY Description";

    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Unit unit = new Unit();
            unit.setUnitID(rs.getInt("UnitID"));
            unit.setDescription(rs.getString("Description"));
            unit.setStatus(rs.getInt("Status")); // ❗ Bổ sung dòng này
            units.add(unit);
        }
    } catch (SQLException e) {
        Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, e);
    }

    return units;
}

    // Lấy đơn vị theo ID
    public Unit getUnitById(int unitID) {
        String sql = "SELECT UnitID, Description FROM Unit WHERE UnitID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Unit(rs.getInt("UnitID"), rs.getString("Description"));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Thêm đơn vị
    public boolean addUnit(Unit unit) {
        String sql = "INSERT INTO Unit (Description, Status) VALUES (?, 1)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unit.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Cập nhật đơn vị
    public boolean updateUnit(Unit unit) {
        String sql = "UPDATE Unit SET Description = ? WHERE UnitID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unit.getDescription());
            stmt.setInt(2, unit.getUnitID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Vô hiệu hóa đơn vị
    public boolean deactivateUnit(int unitID) {
        String sql = "UPDATE Unit SET Status = 0 WHERE UnitID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(UnitDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
    public static void main(String[] args) {
        // Kết nối DB
        DBContext db = new DBContext("Test"); // hoặc tên DB thực tế của bạn
        UnitDAO unitDAO = new UnitDAO(db.getConnection());

        // 1. Test thêm đơn vị
        Unit newUnit = new Unit("Thùng");
        boolean added = unitDAO.addUnit(newUnit);
        System.out.println("Thêm đơn vị mới: " + (added ? "Thành công" : "Thất bại"));

        // 2. Test lấy tất cả đơn vị còn hoạt động
        List<Unit> units = unitDAO.getAllActiveUnits();
        System.out.println("Danh sách đơn vị:");
        for (Unit u : units) {
            System.out.println("ID: " + u.getUnitID() + ", Mô tả: " + u.getDescription());
        }

        // 3. Test lấy đơn vị theo ID
        int testID = 1; // Thay bằng ID thực tế có trong DB
        Unit unit = unitDAO.getUnitById(testID);
        if (unit != null) {
            System.out.println("Tìm thấy đơn vị: " + unit.getDescription());
        } else {
            System.out.println("Không tìm thấy đơn vị với ID = " + testID);
        }

        // 4. Test cập nhật đơn vị
        if (unit != null) {
            unit.setDescription("Chai (sửa)");
            boolean updated = unitDAO.updateUnit(unit);
            System.out.println("Cập nhật đơn vị: " + (updated ? "Thành công" : "Thất bại"));
        }

        // 5. Test vô hiệu hóa
        int deactiveID = 2; // ID tồn tại trong DB
        boolean deactivated = unitDAO.deactivateUnit(deactiveID);
        System.out.println("Vô hiệu hóa đơn vị có ID " + deactiveID + ": " + (deactivated ? "OK" : "Fail"));
    }
}
