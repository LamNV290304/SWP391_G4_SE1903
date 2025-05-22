/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Context;
import java.sql.*;
/**
 *
 * @author Admin
 */
public final class DatabaseHelper {

    private static final int MAX_DB_NAME_LENGTH = 50;
    private static final String DB_NAME_PREFIX = "ShopDB_";

    private DatabaseHelper() {}

    public static String generateSafeDatabaseName(String raw) {
        if (raw == null || raw.isEmpty()) {
            throw new IllegalArgumentException("Tên thô không được null hoặc rỗng.");
        }

        String sanitized = raw.replaceAll("[^a-zA-Z0-9_]", "");

        if (sanitized.length() > MAX_DB_NAME_LENGTH) {
            sanitized = sanitized.substring(0, MAX_DB_NAME_LENGTH);
        }

        return DB_NAME_PREFIX + sanitized;
    }

    public static boolean isValidDatabaseName(String dbName) {
        return dbName != null && dbName.matches("^[a-zA-Z_][a-zA-Z0-9_]{0,49}$");
    }

    public static String escapeSqlLike(String input) {
        if (input == null) return null;
        return input.replace("[", "[[]")
                    .replace("%", "[%]")
                    .replace("_", "[_]")
                    .replace("'", "''");
    }

    public static String getDatabaseNameByShopCode(String shopCode) {
        String sql = "SELECT DatabaseName FROM ShopOwners WHERE ShopCode = ?";
        try (Connection conn = DBContext.getCentralConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shopCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("database_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
