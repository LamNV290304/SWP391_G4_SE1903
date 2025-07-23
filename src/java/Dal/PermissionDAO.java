/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class PermissionDAO {

    private Connection connection;

    public PermissionDAO(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Boolean> getGrantedPageMapByRoleId(int roleId) throws SQLException {
        Map<String, Boolean> permissionMap = new HashMap<>();

        String sql = "SELECT PageCode, IsPermission FROM Permissions WHERE RoleId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String pageId = rs.getString("PageCode");
                    boolean isGranted = rs.getBoolean("IsPermission");

                    permissionMap.put(pageId, isGranted);
                }
            }
        }

        return permissionMap;
    }
}
