/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import java.sql.*;
import Models.*;
import static Utils.PasswordUtils.checkPassword;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT * FROM ShopOwners WHERE (Username = ? OR Email = ?) and Status = 1";
        ShopOwner owner = new ShopOwner();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);

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
                    owner.setTaxNumber(rs.getString("TaxNumber"));
                    return owner;
                }
            }
        }
        return owner;
    }

    public ShopOwner findShopOwnerByUsernameForLogin(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM ShopOwners WHERE (Username = ? OR Email = ?)";
        ShopOwner owner = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);

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
                        owner.setTaxNumber(rs.getString("TaxNumber"));
                        owner.setStatus(rs.getBoolean("Status"));
                    }
                }
            }
        }
        return owner;
    }

    public boolean addShopOwner(ShopOwner owner) throws SQLException {
        String sql = "INSERT INTO ShopOwners (Username, Password, Fullname, Phone, Email, Status, DatabaseName, ShopCode, ShopName, TaxNumber) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setString(10, owner.getTaxNumber());

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

    public ShopOwner getShopOwnerById(int id) throws SQLException {
        String sql = "SELECT * FROM ShopOwners WHERE Id = ? AND Status = 1";
        ShopOwner owner = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
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
                    owner.setTaxNumber(rs.getString("TaxNumber"));
                    owner.setStatus(rs.getBoolean("Status"));
                }
            }
        }
        return owner;
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

    public void updatePasswordByEmail(String email, String hashedPassword) throws SQLException {
        String sql = "UPDATE ShopOwners SET Password = ? WHERE Email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
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

    public List<ShopOwner> getAllWithPagination(String search, int offset, int limit) throws SQLException {
        List<ShopOwner> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ShopOwners WHERE 1=1 AND Id <> 1 ");

        if (search != null && !search.isBlank()) {
            sql.append("AND (FullName LIKE ? OR Email LIKE ?) ");
        }

        sql.append("ORDER BY Id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;

            if (search != null && !search.isBlank()) {
                String keyword = "%" + search + "%";
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }

            ps.setInt(index++, offset);
            ps.setInt(index, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShopOwner owner = new ShopOwner();
                    owner.setId(rs.getInt("Id"));
                    owner.setEmail(rs.getString("Email"));
                    owner.setFullname(rs.getString("FullName"));
                    owner.setDatabaseName(rs.getString("DatabaseName"));
                    owner.setCreateDate(rs.getDate("CreateAt"));
                    owner.setShopName(rs.getString("ShopName"));
                    owner.setPhone(rs.getString("Phone"));
                    owner.setTaxNumber(rs.getString("TaxNumber"));
                    owner.setStatus(rs.getBoolean("Status"));
                    list.add(owner);
                }
            }
        }

        return list;
    }

    public int countAll(String search) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ShopOwners WHERE 1=1 ");
        if (search != null && !search.isBlank()) {
            sql.append("AND (Name LIKE ? OR Email LIKE ?)");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (search != null && !search.isBlank()) {
                String keyword = "%" + search + "%";
                ps.setString(index++, keyword);
                ps.setString(index, keyword);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void upsertOTP(String email, String otp, Timestamp expiredAt) {
        String sql = """
        MERGE OTPs AS target
        USING (SELECT ? AS Email) AS source
        ON target.Email = source.Email
        WHEN MATCHED THEN
            UPDATE SET 
                OTP = ?, 
                ExpiredAt = ?, 
                Status = 0
        WHEN NOT MATCHED THEN
            INSERT (Email, OTP, ExpiredAt, Status)
            VALUES (?, ?, ?, 0);
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // For source.Email
            stmt.setString(1, email);
            // For UPDATE
            stmt.setString(2, otp);
            stmt.setTimestamp(3, expiredAt);
            // For INSERT
            stmt.setString(4, email);
            stmt.setString(5, otp);
            stmt.setTimestamp(6, expiredAt);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thực hiện upsert OTP", e);
        }
    }

    public void markOTPUsed(String email, String otp) {
        String sql = "UPDATE OTPs SET Status = 1 WHERE Email = ? AND OTP = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, otp);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateShopOwnerProfile(String username, String shopName, String email, String phone, String taxNum) {
        String sql = "UPDATE ShopOwners SET ShopName = ?, Email = ?, Phone = ?, TaxNumber = ? WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, shopName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, taxNum);
            ps.setString(5, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ShopOwner getShopOwnerByUsername(String username) {
        String sql = "SELECT * FROM ShopOwners WHERE Username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ShopOwner shopOwner = new ShopOwner();
                shopOwner.setId(rs.getInt("Id"));
                shopOwner.setUsername(rs.getString("Username"));
                shopOwner.setFullname(rs.getString("Fullname"));
                shopOwner.setEmail(rs.getString("Email"));
                shopOwner.setPhone(rs.getString("Phone"));
                shopOwner.setShopName(rs.getString("ShopName"));
                shopOwner.setTaxNumber(rs.getString("TaxNumber"));
                shopOwner.setStatus(rs.getBoolean("Status"));
                return shopOwner;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        try (Connection conn = DBContext.getCentralConnection()) {

            ShopOwnerDAO dao = new ShopOwnerDAO(conn);
            String search = ""; // hoặc thử với từ khóa như "Nguyễn", "gmail" v.v.
            int offset = 0;
            int limit = 10;

            List<ShopOwner> shopOwners = dao.getAllWithPagination(search, offset, limit);

            System.out.println("==== Danh sách ShopOwner (trừ ID = 1) ====");
            for (ShopOwner owner : shopOwners) {
                System.out.println("ID: " + owner.getId());
                System.out.println("Email: " + owner.getEmail());
                System.out.println("Họ tên: " + owner.getFullname());
                System.out.println("DB: " + owner.getDatabaseName());
                System.out.println("Ngày tạo: " + owner.getCreateDate());
                System.out.println("Shop Name: " + owner.getShopName());
                System.out.println("SĐT: " + owner.getPhone());
                System.out.println("Trạng thái: " + (owner.isStatus() ? "Đang hoạt động" : "Đã khóa"));
                System.out.println("--------------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
