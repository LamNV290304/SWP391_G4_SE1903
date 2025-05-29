/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Context;

import java.sql.*;
import java.security.SecureRandom;

/**
 *
 * @author Admin
 */
public final class DatabaseHelper {

    private static final SecureRandom random = new SecureRandom();
    private static final int MAX_DB_NAME_LENGTH = 50;
    private static final String DB_NAME_PREFIX = "ShopDB_";

    private DatabaseHelper() {
    }

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
        if (input == null) {
            return null;
        }
        return input.replace("[", "[[]")
                .replace("%", "[%]")
                .replace("_", "[_]")
                .replace("'", "''");
    }

    public static String getDatabaseNameByShopCode(String shopCode) {
        String sql = "SELECT DatabaseName FROM ShopOwners WHERE ShopCode = ?";
        try (Connection conn = DBContext.getCentralConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shopCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("DatabaseName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getShopNameByShopCode(String shopCode) {
        String sql = "SELECT ShopName FROM ShopOwners WHERE ShopCode = ?";
        try (Connection conn = DBContext.getCentralConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shopCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("ShopName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateShopCode(String shopName) {
        if (shopName == null || shopName.isEmpty()) {
            throw new IllegalArgumentException("Shop name không được null hoặc rỗng.");
        }

        // Loại bỏ dấu tiếng Việt (nếu cần), viết thường và loại bỏ khoảng trắng
        String normalized = java.text.Normalizer.normalize(shopName, java.text.Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // Bỏ dấu tiếng Việt
                .toLowerCase()
                .replaceAll("[^a-z0-9]", ""); // Loại bỏ ký tự đặc biệt và khoảng trắng

        return normalized;
    }

    public static String generateOTP() {
        int otp = random.nextInt(1000000); // Từ 0 đến 999999
        return String.format("%06d", otp); // Đảm bảo đủ 6 chữ số, thêm số 0 ở đầu nếu cần
    }

    public static boolean initializeShopDatabase(String dbName) {
        String createDbSQL = "CREATE DATABASE " + dbName;

        // Các câu lệnh tạo bảng
        String createTablesSQL = """
                                 CREATE TABLE Category (
                                     CategoryID NVARCHAR(20) NOT NULL PRIMARY KEY,
                                     CategoryName NVARCHAR(100) NOT NULL,
                                     [Description] NVARCHAR(255),
                                     Status BIT DEFAULT 1 
                                 );
                                 
                                 CREATE TABLE Unit (
                                     UnitID NVARCHAR(20) PRIMARY KEY,
                                     [Description] NVARCHAR(255)
                                 );
                                 
                                 
                                 CREATE TABLE Role (
                                     RoleID NVARCHAR(20) PRIMARY KEY,
                                     RoleName NVARCHAR(100) NOT NULL,
                                     [Description] NVARCHAR(255)
                                 );
                                 
                                 CREATE TABLE Shop (
                                     ShopID NVARCHAR(20) PRIMARY KEY,
                                     ShopName NVARCHAR(100) NOT NULL,
                                     [Address] NVARCHAR(255),
                                     Phone NVARCHAR(20),
                                     Email NVARCHAR(100),
                                     Status BIT DEFAULT 1,
                                     CreatedDate DATETIME DEFAULT GETDATE(),
                                     CreatedBy NVARCHAR(100)
                                 );
                                 
                                 CREATE TABLE Customer (
                                     CustomerID NVARCHAR(20) PRIMARY KEY,
                                     CustomerName NVARCHAR(100) NOT NULL,
                                     Phone NVARCHAR(20),
                                     Email NVARCHAR(100),
                                     [Address] NVARCHAR(255),
                                     Status BIT DEFAULT 1,
                                     CreatedDate DATETIME DEFAULT GETDATE(),
                                     CreatedBy NVARCHAR(100)
                                 );
                                 
                                 CREATE TABLE Supplier (
                                     SupplierID NVARCHAR(20) PRIMARY KEY,
                                     SupplierName NVARCHAR(100) NOT NULL,
                                     Phone NVARCHAR(20),
                                     Email NVARCHAR(100),
                                     [Address] NVARCHAR(255),
                                     Status BIT DEFAULT 1,
                                     CreatedDate DATETIME DEFAULT GETDATE(),
                                     CreatedBy NVARCHAR(100)
                                 );
                                 
                                 CREATE TABLE Employee (
                                     EmployeeID NVARCHAR(20) PRIMARY KEY,
                                     Username NVARCHAR(100) NOT NULL UNIQUE,
                                     [Password] NVARCHAR(255) NOT NULL,
                                     FullName NVARCHAR(100),
                                     Email NVARCHAR(100),
                                     Phone NVARCHAR(20),
                                     RoleID NVARCHAR(20) NOT NULL,
                                     ShopID NVARCHAR(20), -- m\u1edbi th\u00eam
                                     Status BIT DEFAULT 1,
                                     CreatedDate DATETIME DEFAULT GETDATE(),
                                     CreatedBy NVARCHAR(100),
                                     FOREIGN KEY (RoleID) REFERENCES Role(RoleID),
                                     FOREIGN KEY (ShopID) REFERENCES Shop(ShopID)
                                 );
                                 
                                 CREATE TABLE Product (
                                     ProductID NVARCHAR(20) PRIMARY KEY,
                                     ProductName NVARCHAR(100) NOT NULL,
                                     CategoryID NVARCHAR(20) NOT NULL,
                                     UnitID NVARCHAR(20) NOT NULL,
                                     Price DECIMAL(18,2) NOT NULL,
                                     [Description] NVARCHAR(255),
                                     Status BIT DEFAULT 1,
                                     CreatedDate DATETIME DEFAULT GETDATE(),
                                     CreatedBy NVARCHAR(100),
                                     FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID),
                                     FOREIGN KEY (UnitID) REFERENCES Unit(UnitID)
                                 );
                                 
                                 CREATE TABLE Invoice (
                                     InvoiceID NVARCHAR(20) PRIMARY KEY,
                                     CustomerID NVARCHAR(20) NOT NULL,
                                     EmployeeID NVARCHAR(20) NOT NULL,
                                     ShopID NVARCHAR(20), -- m\u1edbi th\u00eam
                                     InvoiceDate DATETIME NOT NULL DEFAULT GETDATE(),
                                     TotalAmount DECIMAL(18,2) NOT NULL,
                                     Note NVARCHAR(255),
                                     Status BIT DEFAULT 1,
                                     FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
                                     FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID),
                                     FOREIGN KEY (ShopID) REFERENCES Shop(ShopID)
                                 );
                                 
                                 CREATE TABLE InvoiceDetail (
                                     InvoiceDetailID INT PRIMARY KEY,
                                     InvoiceID NVARCHAR(20) NOT NULL,
                                     ProductID NVARCHAR(20) NOT NULL,
                                     Quantity INT NOT NULL,
                                     Discount DECIMAL(5,2) DEFAULT 0,
                                     FOREIGN KEY (InvoiceID) REFERENCES Invoice(InvoiceID),
                                     FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
                                 );
                                 
                                 CREATE TABLE ImportReceipt (
                                     ImportReceiptID INT PRIMARY KEY,
                                     Code NVARCHAR(20) NOT NULL,
                                     SupplierID NVARCHAR(20) NOT NULL,
                                     EmployeeID NVARCHAR(20) NOT NULL,
                                     ShopID NVARCHAR(20), -- m\u1edbi th\u00eam
                                     ReceiptDate DATETIME DEFAULT GETDATE(),
                                     TotalAmount DECIMAL(18,2) NOT NULL,
                                     Note NVARCHAR(255),
                                     Status BIT DEFAULT 1,
                                     FOREIGN KEY (SupplierID) REFERENCES Supplier(SupplierID),
                                     FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID),
                                     FOREIGN KEY (ShopID) REFERENCES Shop(ShopID)
                                 );
                                 
                                 CREATE TABLE ImportReceiptDetail (
                                     ImportReceiptDetailID INT NOT NULL PRIMARY KEY,
                                     ImportReceiptID INT NOT NULL,
                                     ProductID NVARCHAR(20) NOT NULL,
                                     Quantity INT NOT NULL,
                                     Price DECIMAL(18,2) NOT NULL,
                                     FOREIGN KEY (ImportReceiptID) REFERENCES ImportReceipt(ImportReceiptID),
                                     FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
                                 );
                                 
                                 CREATE TABLE Inventory (
                                     InventoryID NVARCHAR(20) PRIMARY KEY,
                                     ProductID NVARCHAR(20) NOT NULL,
                                     ShopID NVARCHAR(20), -- m\u1edbi th\u00eam
                                     Quantity INT NOT NULL DEFAULT 0,
                                     LastUpdated DATETIME DEFAULT GETDATE(),
                                     FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
                                     FOREIGN KEY (ShopID) REFERENCES Shop(ShopID)
                                 );
                                 
                                 CREATE TABLE TransferReceipt (
                                     TransferReceiptID NVARCHAR(20) PRIMARY KEY,
                                     ProductID NVARCHAR(20) NOT NULL,
                                     FromInventoryID NVARCHAR(20) NOT NULL,
                                     ToInventoryID NVARCHAR(20) NOT NULL,
                                     Quantity INT NOT NULL,
                                     TransferDate DATETIME DEFAULT GETDATE(),
                                     Note NVARCHAR(255),
                                     FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
                                     FOREIGN KEY (FromInventoryID) REFERENCES Inventory(InventoryID),
                                     FOREIGN KEY (ToInventoryID) REFERENCES Inventory(InventoryID)
                                 );""";

        try (Connection masterConn = DBContext.getMasterConnection(); Statement stmt = masterConn.createStatement()) {

            stmt.executeUpdate(createDbSQL);
            System.out.println("Database " + dbName + " đã được tạo thành công.");

            try (Connection shopConn = DBContext.getConnection(dbName); Statement shopStmt = shopConn.createStatement()) {
                shopStmt.executeUpdate(createTablesSQL);
                System.out.println("Các bảng đã được tạo thành công trong " + dbName);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
