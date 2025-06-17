/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class RoleDAO {

    private Connection connection;

    public RoleDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Role> getAllRoles() throws SQLException {
        List<Role> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT RoleID, RoleName FROM Role WHERE RoleName <> 'Admin'");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("RoleID"));
                role.setName(rs.getString("RoleName"));
                list.add(role);
            }
        }
        return list;
    }
    
    public static void main(String[] args) {
    try (Connection conn = DBContext.getConnection("ShopDB_TTest")) {
        RoleDAO roleDAO = new RoleDAO(conn);
        List<Role> roles = roleDAO.getAllRoles();

        for (Role role : roles) {
            System.out.println("RoleID: " + role.getId() + ", RoleName: " + role.getName());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
