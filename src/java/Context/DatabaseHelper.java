/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Context;

import Dal.ShopOwnerDAO;
import Models.Employee;
import Models.ShopOwner;
import java.sql.*;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static boolean initializeShopDatabase(String dbName, Employee shopOwner) {
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
                                     RoleID Int PRIMARY KEY,
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
                                     ID INT IDENTITY(1,1) PRIMARY KEY,
                                     Username NVARCHAR(100) NOT NULL UNIQUE,
                                     [Password] NVARCHAR(255) NOT NULL,
                                     FullName NVARCHAR(100),
                                     Email NVARCHAR(100),
                                     Phone NVARCHAR(20),
                                     RoleID NVARCHAR(20) NOT NULL,
                                     ShopID NVARCHAR(20), -- mới thêm
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
                                     ShopID NVARCHAR(20), -- mới thêm
                                     InvoiceDate DATETIME NOT NULL DEFAULT GETDATE(),
                                     TotalAmount DECIMAL(18,2) NOT NULL,
                                     Note NVARCHAR(255),
                                     Status BIT DEFAULT 1,
                                     FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
                                     FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID),
                                     FOREIGN KEY (ShopID) REFERENCES Shop(ShopID)
                                 );

                                 CREATE TABLE InvoiceDetail (
                                     InvoiceDetailID INT IDENTITY PRIMARY KEY,
                                     InvoiceID NVARCHAR(20) NOT NULL,
                                     ProductID NVARCHAR(20) NOT NULL,
                                 	UnitPrice DECIMAL(18,2) NOT NULL,
                                     Quantity INT NOT NULL,
                                     Discount DECIMAL(5,2) DEFAULT 0,
                                 	TotalPrice DECIMAL(18,2) NOT NULL,
                                     FOREIGN KEY (InvoiceID) REFERENCES Invoice(InvoiceID),
                                     FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
                                 );

                                 CREATE TABLE ImportReceipt (
                                     ImportReceiptID INT PRIMARY KEY,
                                     Code NVARCHAR(20) NOT NULL,
                                     SupplierID NVARCHAR(20) NOT NULL,
                                     EmployeeID NVARCHAR(20) NOT NULL,
                                     ShopID NVARCHAR(20), -- mới thêm
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
                                     ShopID NVARCHAR(20), 
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
                                 );
                                 
                                 INSERT INTO Role (RoleID, RoleName, [Description]) VALUES
                                 (1, 'admin', 'Administrator with full access'),
                                 (2, 'Manager', 'Manages operations and staff'),
                                 (3, 'Cashier', 'Handles sales transactions'),
                                 (4, 'Sale', 'Responsible for sales and customer relations');
                                 """;

        try (Connection masterConn = DBContext.getMasterConnection(); Statement stmt = masterConn.createStatement()) {

            stmt.executeUpdate(createDbSQL);
            System.out.println("Database " + dbName + " đã được tạo thành công.");

            try (Connection shopConn = DBContext.getConnection(dbName); Statement shopStmt = shopConn.createStatement()) {
                shopStmt.executeUpdate(createTablesSQL);
                System.out.println("Các bảng đã được tạo thành công trong " + dbName);

                String insertShopOwnerSQL = """
                INSERT INTO Employee ( Username, [Password], FullName, Email, Phone, RoleID, ShopID, Status, CreatedDate, CreatedBy)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)
            """;

                try (PreparedStatement pstmt = shopConn.prepareStatement(insertShopOwnerSQL)) {
                    pstmt.setString(1, shopOwner.getUsername());
                    pstmt.setString(2, shopOwner.getPassword());
                    pstmt.setString(3, shopOwner.getFullname());
                    pstmt.setString(4, shopOwner.getEmail());
                    pstmt.setString(5, shopOwner.getPhone());
                    pstmt.setInt(6, shopOwner.getRoleId());
                    pstmt.setInt(7, shopOwner.getShopId());
                    pstmt.setBoolean(8, shopOwner.isStatus());
                    pstmt.setDate(9, shopOwner.getCreateDate());

                    pstmt.executeUpdate();
                    System.out.println("Người dùng ShopOwner đầu tiên đã được thêm thành công.");
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {

        try {
            String databaseName = "ShopDB_TTest";
            Connection conn = DBContext.getCentralConnection();
            ShopOwnerDAO shopOwnerDAO = new ShopOwnerDAO(conn);
            Date createDate = Date.valueOf(java.time.LocalDate.now());
            
            ShopOwner shopOwner = shopOwnerDAO.getShopOwnerByDatabaseName(databaseName);
            Employee firstEmployee = new Employee(0, 0, 0, shopOwner.getUsername(), shopOwner.getPassword(), shopOwner.getFullname(), shopOwner.getPhone(), shopOwner.getEmail(), true, createDate);
            DatabaseHelper.initializeShopDatabase(databaseName, firstEmployee);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
