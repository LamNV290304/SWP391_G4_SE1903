/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.Role;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class RoleDAO extends DBContext{

    public List<Role> getAllRole() {
        List<Role> roles = new ArrayList<>();
        try {
            String sql = "Select * From Role";
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("ID"));
                role.setName(rs.getString("Name"));
                roles.add(role);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return roles;
    }
}
