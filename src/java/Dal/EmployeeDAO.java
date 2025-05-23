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

/**
 *
 * @author Admin
 */
public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Username, Password, Fullname, Phone, Status, CreateDate, RoleId, ShopId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getUsername());
            stmt.setString(2, employee.getPassword());
            stmt.setString(3, employee.getFullname());
            stmt.setString(4, employee.getPhone());
            stmt.setBoolean(5, employee.isStatus());
            stmt.setDate(6, new java.sql.Date(employee.getCreateDate().getTime()));
            stmt.setInt(7, employee.getRole().getId());
            stmt.setInt(8, employee.getShop().getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public Employee findEmployeeByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE Username = ?";
        Employee emp = new Employee();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    emp.setId(rs.getInt("Id"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("Fullname"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreateDate"));

                    Role role = new Role();
                    role.setId(rs.getInt("RoleId"));
                    emp.setRole(role);

                    Shop shop = new Shop();
                    shop.setId(rs.getInt("ShopId"));
                    emp.setShop(shop);

                    return emp;
                }
            }
        }
        return emp;
    }

}
