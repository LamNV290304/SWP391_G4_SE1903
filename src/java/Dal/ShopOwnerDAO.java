/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import java.sql.*;
import Models.*;
import static Utils.PasswordUtils.checkPassword;

/**
 *
 * @author Admin
 */
public class ShopOwnerDAO {

    private Connection connection;

    public ShopOwnerDAO(Connection connection) {
        this.connection = connection;
    }

    public ShopOwner findShopOwnerByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM ShopOwners WHERE Username = ? and Status = 1";
        ShopOwner owner = new ShopOwner();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    owner.setId(rs.getInt("Id"));
                    owner.setUsername(rs.getString("Username"));
                    owner.setPassword(rs.getString("Password"));
                    owner.setFullname(rs.getString("Fullname"));
                    owner.setPhone(rs.getString("Phone"));
                    owner.setEmail(rs.getString("Email"));
                    owner.setDatabaseName(rs.getString("DatabaseName"));
                    owner.setShopCode(rs.getString("ShopCode"));
                    owner.setShopName(rs.getString("ShopName"));
                    return owner;
                }
            }
        }
        return owner;
    }

    public ShopOwner findShopOwnerByUsernameForLogin(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM ShopOwners WHERE Username = ?";
        ShopOwner owner = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("Password");

                    if (checkPassword(plainPassword, hashedPasswordFromDB)) {
                        owner = new ShopOwner();
                        owner.setId(rs.getInt("Id"));
                        owner.setUsername(rs.getString("Username"));
                        owner.setFullname(rs.getString("Fullname"));
                        owner.setPhone(rs.getString("Phone"));
                        owner.setEmail(rs.getString("Email"));
                        owner.setDatabaseName(rs.getString("DatabaseName"));
                        owner.setShopCode(rs.getString("ShopCode"));
                        owner.setShopName(rs.getString("ShopName"));
                        owner.setStatus(rs.getBoolean("Status"));
                    }
                }
            }
        }
        return owner;
    }

    public boolean addShopOwner(ShopOwner owner) throws SQLException {
        String sql = "INSERT INTO ShopOwners (Username, Password, Fullname, Phone, Email, Status, DatabaseName, ShopCode, ShopName) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, owner.getUsername());
            stmt.setString(2, owner.getPassword());
            stmt.setString(3, owner.getFullname());
            stmt.setString(4, owner.getPhone());
            stmt.setString(5, owner.getEmail());
            stmt.setBoolean(6, owner.isStatus());
            stmt.setString(7, owner.getDatabaseName());
            stmt.setString(8, owner.getShopCode());
            stmt.setString(9, owner.getShopName());

            stmt.executeUpdate();
            return true;
        }
    }

    public boolean isShopNameExist(String shopName) throws SQLException {
        String sql = "SELECT 1 FROM ShopOwners WHERE ShopName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, shopName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean isUsernameExist(String username) throws SQLException {
        String sql = "SELECT 1 FROM ShopOwners WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean isEmailExist(String email) throws SQLException {
        String sql = "SELECT 1 FROM ShopOwners WHERE Email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean isDatabaseNameExist(String database) throws SQLException {
        String sql = "SELECT 1 FROM ShopOwners WHERE DatabaseName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, database);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean isPhoneExist(String phone) throws SQLException {
        String sql = "SELECT 1 FROM ShopOwners WHERE Phone = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean saveOTP(String email, String otp) {
        String sql = "MERGE INTO OTPs AS target "
                + "USING (SELECT ? AS Email, ? AS OTP, DATEADD(MINUTE, 15, GETDATE()) AS ExpiredAt) AS source "
                + "ON target.Email = source.Email "
                + "WHEN MATCHED THEN UPDATE SET OTP = source.OTP, ExpiredAt = source.ExpiredAt "
                + "WHEN NOT MATCHED THEN INSERT (Email, OTP, ExpiredAt) VALUES (source.Email, source.OTP, source.ExpiredAt);";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyOTP(String email, String otp) {
        String sql = "SELECT * FROM OTPs WHERE Email = ? AND OTP = ? AND ExpiredAt > GETDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatusByEmail(String email) throws SQLException {
        String sql = "UPDATE ShopOwners SET Status = ? WHERE Email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public ShopOwner getShopOwnerByDatabaseName(String databaseName) throws SQLException {
        String sql = "SELECT * FROM ShopOwners WHERE DatabaseName = ? AND Status = 1";
        ShopOwner owner = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, databaseName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    owner = new ShopOwner();
                    owner.setId(rs.getInt("Id"));
                    owner.setUsername(rs.getString("Username"));
                    owner.setPassword(rs.getString("Password"));
                    owner.setFullname(rs.getString("Fullname"));
                    owner.setPhone(rs.getString("Phone"));
                    owner.setEmail(rs.getString("Email"));
                    owner.setDatabaseName(rs.getString("DatabaseName"));
                    owner.setShopCode(rs.getString("ShopCode"));
                    owner.setShopName(rs.getString("ShopName"));
                }
            }
        }
        return owner;
    }

    public void updatePasswordByUsername(String username, String hashedPassword) throws SQLException {
        String sql = "UPDATE ShopOwners SET Password = ? WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    public boolean checkPasswordShopOwner(int ownerId, String plainPassword) throws SQLException {
        String sql = "SELECT Password FROM ShopOwners WHERE Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("Password");
                    return checkPassword(plainPassword, hashedPassword);
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {

    }
}
