/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        Map<String, Boolean> permissionMap = new LinkedHashMap<>();

        String sql = "SELECT p.PageCode, p.IsPermission FROM Permissions p JOIN Pages pg ON p.PageCode = pg.PageCode WHERE p.RoleId = ? ORDER BY TRY_CAST(pg.[Group] AS INT);";

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

    public void updatePermission(int roleId, String pageCode, boolean isPermission) throws SQLException {
        String sqlUpdate = "UPDATE Permissions SET IsPermission = ? WHERE RoleId = ? AND PageCode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {
            stmt.setBoolean(1, isPermission);
            stmt.setInt(2, roleId);
            stmt.setString(3, pageCode);
            stmt.executeUpdate();
        }
    }
    
    public static void main(String[] args) throws ClassNotFoundException {
        try (Connection conn = DBContext.getCentralConnection()) {
            PermissionDAO dao = new PermissionDAO(conn);

            int testRoleId = 1;
            Map<String, Boolean> permissionMap = dao.getGrantedPageMapByRoleId(testRoleId);

            System.out.println("Danh sách quyền của RoleId = " + testRoleId);
            System.out.println("PageCode\t\tGranted?");
            System.out.println("-------------------------------------");
            for (Map.Entry<String, Boolean> entry : permissionMap.entrySet()) {
                System.out.printf("%-20s %s\n", entry.getKey(), entry.getValue() ? "✅" : "❌");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
