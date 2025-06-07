/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public Employee findEmployeeByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE Username = ?";
        Employee emp = new Employee();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage() + ex.getStackTrace());
                return emp;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage() + ex.getStackTrace());
            return emp;
        }
        return emp;
    }
public List<Employee> getAllEmployeesByShopID(int shopId) throws SQLException {
    List<Employee> employees = new ArrayList<>();
    String sql = "SELECT * FROM Employees WHERE ShopID = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, shopId);
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
    } catch (SQLException ex) {
        System.out.println("Error retrieving employees by ShopID: " + ex.getMessage());
        ex.printStackTrace();
    }

    return employees;
}
}
