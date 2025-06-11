/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import Models.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Admin
 */
public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Username, Password, Fullname, Phone, Email, Status, CreateDate, RoleId, ShopId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getUsername());
            stmt.setString(2, employee.getPassword());
            stmt.setString(3, employee.getFullname());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getEmail());  // thêm email ở vị trí thứ 5
            stmt.setBoolean(6, employee.isStatus());
            stmt.setDate(7, new java.sql.Date(employee.getCreateDate().getTime()));
            stmt.setInt(8, employee.getRole().getId());
            stmt.setString(9, employee.getShop().getShopID());
            stmt.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + ex.getStackTrace());
            return false;
        }
    }

    public Employee findEmployeeByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE Username = ? AND Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("Fullname"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreateDate"));
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    return emp;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy hoặc lỗi
    }
public List<Employee> getAllEmployeesByShopID(int shopId) throws SQLException {
    List<Employee> employees = new ArrayList<>();
    String sql = "SELECT * FROM Employees WHERE ShopID = ?";

    public List<Employee> getEmployeesByPage(int page, int pageSize, String sortBy, String sortDirection, String searchKeyword) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        // Validate cột sắp xếp để tránh SQL Injection
        List<String> validSortColumns = Arrays.asList("EmployeeID", "Username", "Fullname", "CreateDate");
        if (!validSortColumns.contains(sortBy)) {
            sortBy = "EmployeeID";
        }

        // Validate thứ tự sắp xếp
        if (!"ASC".equalsIgnoreCase(sortDirection) && !"DESC".equalsIgnoreCase(sortDirection)) {
            sortDirection = "ASC";
        }

        String sql = "SELECT * FROM Employee WHERE Fullname LIKE ? OR Username LIKE ? "
                + "ORDER BY " + sortBy + " " + sortDirection + " "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String keyword = "%" + searchKeyword + "%";
            stmt.setString(1, keyword);
            stmt.setString(2, keyword);
            stmt.setInt(3, (page - 1) * pageSize);
            stmt.setInt(4, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("Fullname"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setEmail(rs.getString("Email"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreateDate"));
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    employees.add(emp);
                }
            }
        }

        return employees;
    }

    public int getTotalEmployeeCount(String searchKeyword) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Employee WHERE Fullname LIKE ? OR Username LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String keyword = "%" + searchKeyword + "%";
            stmt.setString(1, keyword);
            stmt.setString(2, keyword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET Password = ?, Fullname = ?, Phone = ?, Email = ?, Status = ?, RoleId = ?, ShopId = ? WHERE EmployeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getPassword());
            stmt.setString(2, employee.getFullname());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getEmail());
            stmt.setBoolean(5, employee.isStatus());
            stmt.setInt(6, employee.getRole().getId());
            stmt.setString(7, employee.getShop().getShopID());
            stmt.setInt(8, employee.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println("Update failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}
}
