/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.SalarySetting;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author ADMIN
 */
public class SalarySettingDAO {

    private Connection connection;

    public SalarySettingDAO(Connection connection) {
        this.connection = connection;
    }

    public Vector<SalarySetting> getAllSalarySetting(String sql) {
        Vector<SalarySetting> listSalarySetting = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                SalarySetting p = new SalarySetting(
                        rs.getInt("SalarySettingID"),
                        rs.getInt("EmployeeID"),
                        rs.getString("SalaryType"),
                        rs.getBigDecimal("Amount")
                );

                // Check if employee information is available (from JOIN query)
                try {
                    p.setFullName(rs.getString("FullName"));
                    p.setUsername(rs.getString("Username"));
                } catch (SQLException e) {
                    // Columns don't exist, ignore
                }

                listSalarySetting.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listSalarySetting;
    }

    /**
     * Get all salary settings with employee information
     *
     * @return Vector of SalarySetting with employee details
     */
    public Vector<SalarySetting> getAllSalarySettingWithEmployeeInfo() {
        String sql = "SELECT ss.*, e.FullName, e.Username FROM SalarySetting ss "
                + "LEFT JOIN Employee e ON ss.EmployeeID = e.EmployeeID "
                + "ORDER BY e.FullName";
        return getAllSalarySetting(sql);
    }

    /**
     * Search salary settings by employee name
     *
     * @param employeeName Employee name to search
     * @return Vector of matching SalarySetting records
     */
    public Vector<SalarySetting> searchByEmployeeName(String employeeName) {
        String sql = "SELECT ss.*, e.FullName, e.Username FROM SalarySetting ss "
                + "LEFT JOIN Employee e ON ss.EmployeeID = e.EmployeeID "
                + "WHERE e.FullName LIKE ? "
                
                + "ORDER BY e.FullName";
        Vector<SalarySetting> listSalarySetting = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + employeeName + "%");
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                SalarySetting p = new SalarySetting(
                        rs.getInt("SalarySettingID"),
                        rs.getInt("EmployeeID"),
                        rs.getString("SalaryType"),
                        rs.getBigDecimal("Amount"),
                        rs.getString("FullName"),
                        rs.getString("Username")
                );
                listSalarySetting.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listSalarySetting;
    }

    /**
     * Search salary settings by salary type
     *
     * @param salaryType Salary type to search
     * @return Vector of matching SalarySetting records
     */
    public Vector<SalarySetting> searchBySalaryType(String salaryType) {
        String sql = "SELECT ss.*, e.FullName, e.Username FROM SalarySetting ss "
                + "LEFT JOIN Employee e ON ss.EmployeeID = e.EmployeeID "
                + "WHERE ss.SalaryType = ? "
                + "ORDER BY e.FullName";
        Vector<SalarySetting> listSalarySetting = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, salaryType);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                SalarySetting p = new SalarySetting(
                        rs.getInt("SalarySettingID"),
                        rs.getInt("EmployeeID"),
                        rs.getString("SalaryType"),
                        rs.getBigDecimal("Amount"),
                        rs.getString("FullName"),
                        rs.getString("Username")
                );
                listSalarySetting.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listSalarySetting;
    }

    public int insertSalarySetting(SalarySetting p) {
        String sql = "INSERT INTO SalarySetting (EmployeeID, SalaryType, Amount) VALUES (?, ?, ?)";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getEmployeeID());
            ptm.setString(2, p.getSalaryType());
            ptm.setBigDecimal(3, p.getAmount());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int updateSalarySetting(SalarySetting p) {
        String sql = "UPDATE SalarySetting SET EmployeeID = ?, SalaryType = ?, Amount = ? WHERE SalarySettingID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getEmployeeID());
            ptm.setString(2, p.getSalaryType());
            ptm.setBigDecimal(3, p.getAmount());
            ptm.setInt(4, p.getSalarySettingID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteSalarySetting(int salarySettingID) {
        String sql = "DELETE FROM SalarySetting WHERE SalarySettingID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salarySettingID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public SalarySetting searchSalarySetting(int salarySettingID) {
        String sql = "SELECT ss.*, e.FullName, e.Username FROM SalarySetting ss "
                + "LEFT JOIN Employee e ON ss.EmployeeID = e.EmployeeID "
                + "WHERE ss.SalarySettingID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salarySettingID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                SalarySetting p = new SalarySetting(
                        rs.getInt("SalarySettingID"),
                        rs.getInt("EmployeeID"),
                        rs.getString("SalaryType"),
                        rs.getBigDecimal("Amount"),
                        rs.getString("FullName"),
                        rs.getString("Username")
                );
                return p;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public SalarySetting getSalarySettingByEmployee(int employeeID) {
        String sql = "SELECT ss.*, e.FullName, e.Username FROM SalarySetting ss "
                + "LEFT JOIN Employee e ON ss.EmployeeID = e.EmployeeID "
                + "WHERE ss.EmployeeID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, employeeID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                SalarySetting p = new SalarySetting(
                        rs.getInt("SalarySettingID"),
                        rs.getInt("EmployeeID"),
                        rs.getString("SalaryType"),
                        rs.getBigDecimal("Amount"),
                        rs.getString("FullName"),
                        rs.getString("Username")
                );
                return p;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Check if employee already has salary setting
     *
     * @param employeeID Employee ID to check
     * @return true if employee has salary setting, false otherwise
     */
    public boolean hasSalarySetting(int employeeID) {
        String sql = "SELECT COUNT(*) FROM SalarySetting WHERE EmployeeID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, employeeID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Get all employees without salary setting
     *
     * @return Vector of employee IDs without salary setting
     */
    public Vector<Integer> getEmployeesWithoutSalarySetting() {
        String sql = "SELECT e.EmployeeID FROM Employee e "
                + "LEFT JOIN SalarySetting ss ON e.EmployeeID = ss.EmployeeID "
                + "WHERE ss.EmployeeID IS NULL AND e.EmployeeID != 1"; // Exclude admin
        Vector<Integer> employeeIds = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                employeeIds.add(rs.getInt("EmployeeID"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employeeIds;
    }

    /**
     * Get statistics about salary settings by type
     *
     * @return String with statistics information
     */
    public String getSalaryStatistics() {
        String sql = "SELECT SalaryType, COUNT(*) as Count, AVG(Amount) as AvgAmount, "
                + "MIN(Amount) as MinAmount, MAX(Amount) as MaxAmount "
                + "FROM SalarySetting GROUP BY SalaryType";
        StringBuilder stats = new StringBuilder();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                stats.append(String.format("Type: %s, Count: %d, Avg: %.2f, Min: %.2f, Max: %.2f\n",
                        rs.getString("SalaryType"),
                        rs.getInt("Count"),
                        rs.getDouble("AvgAmount"),
                        rs.getDouble("MinAmount"),
                        rs.getDouble("MaxAmount")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stats.toString();
    }

    public static void main(String[] args) {
        try {
            DBContext connection = new DBContext("Test");
            SalarySettingDAO dao = new SalarySettingDAO(connection.getConnection());

            // Test getAllSalarySettingWithEmployeeInfo
            Vector<SalarySetting> list = dao.searchByEmployeeName("linh");
            System.out.println("=== All Salary Settings with Employee Info ===");
            for (SalarySetting s : list) {
                System.out.println("ID: " + s.getSalarySettingID()
                        + " | Employee: " + s.getFullName()
                        + " | Username: " + s.getUsername()
                        + " | Type: " + s.getSalaryTypeDisplayName()
                        + " | Amount: " + s.getAmountDisplayText());
            }

            // Test statistics
            System.out.println("\n=== Salary Statistics ===");
            System.out.println(dao.getSalaryStatistics());

            // Test employees without salary setting
            System.out.println("\n=== Employees without Salary Setting ===");
            Vector<Integer> employeesWithoutSetting = dao.getEmployeesWithoutSalarySetting();
            System.out.println("Employee IDs: " + employeesWithoutSetting);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
