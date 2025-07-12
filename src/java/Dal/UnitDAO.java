/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Unit;
import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author duckh
 */
public class UnitDAO {

    private Connection connection;

    public UnitDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();

        String sql = "SELECT UnitID, Description FROM Unit";

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnitID(rs.getInt("UnitID"));
                unit.setDescription(rs.getString("Description"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            throw new RuntimeException("Error fetching all units: " + e.getMessage(), e);
        }
        return units;
    }

    public Unit getUnitByID(int unitID) {
        Unit unit = null;
        String sql = "SELECT UnitID, Description FROM Unit WHERE UnitID = ?";

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, unitID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    unit = new Unit();
                    unit.setUnitID(rs.getInt("UnitID"));
                    unit.setDescription(rs.getString("Description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching unit by ID: " + e.getMessage(), e);
        }
        return unit;
    }

    public boolean addUnit(Unit unit) {
        String sql = "INSERT INTO Unit (Description) VALUES (?)";
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, unit.getDescription());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding unit: " + e.getMessage(), e);
        }
    }

    public boolean updateUnit(Unit unit) {
        String sql = "UPDATE Unit SET Description = ? WHERE UnitID = ?";
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, unit.getDescription());
            pstmt.setInt(2, unit.getUnitID());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating unit: " + e.getMessage(), e);
        }
    }

    public boolean deleteUnit(int unitID) {
        String sql = "DELETE FROM Unit WHERE UnitID = ?";
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, unitID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting unit: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        DBContext dbContext = new DBContext("SWP1");
        Connection connection = dbContext.getConnection();
        UnitDAO u = new UnitDAO(connection);
        List<Unit> l = u.getAllUnits();
        for (Unit unit : l) {
            System.out.println(unit.getDescription());
        }
    }
}
