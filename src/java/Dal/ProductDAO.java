/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Category;
import Models.Product;
import Models.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<Product> getAllProducts(int page, int pageSize, String search, String categoryFilter) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.*, c.CategoryName, u.Description as UnitDescription " +
            "FROM Product p " +
            "LEFT JOIN Category c ON p.CategoryID = c.CategoryID " +
            "LEFT JOIN Unit u ON p.UnitID = u.UnitID " +
            "WHERE 1=1 ");
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND p.ProductName LIKE ? ");
        }
        
        if (categoryFilter != null && !categoryFilter.trim().isEmpty()) {
            sql.append("AND p.CategoryID = ? ");
        }
        
        sql.append("ORDER BY p.CreatedDate DESC ");
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            if (search != null && !search.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
            }
            
            if (categoryFilter != null && !categoryFilter.trim().isEmpty()) {
                stmt.setString(paramIndex++, categoryFilter);
            }
            
            stmt.setInt(paramIndex++, (page - 1) * pageSize);
            stmt.setInt(paramIndex, pageSize);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getString("ProductID"));
                product.setProductName(rs.getString("ProductName"));
                product.setCategoryID(rs.getString("CategoryID"));
                product.setUnitID(rs.getString("UnitID"));
                product.setImportPrice(rs.getBigDecimal("ImportPrice"));
                product.setSellingPrice(rs.getBigDecimal("SellingPrice"));
                product.setDescription(rs.getString("Description"));
                product.setStatus(rs.getBoolean("Status"));
                product.setImageUrl(rs.getString("ImageUrl"));
                product.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                product.setCreatedBy(rs.getString("CreatedBy"));
                product.setCategoryName(rs.getString("CategoryName"));
                product.setUnitDescription(rs.getString("UnitDescription"));
                
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
    
    public int getTotalProducts(String search, String categoryFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product WHERE 1=1 ");
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND ProductName LIKE ? ");
        }
        
        if (categoryFilter != null && !categoryFilter.trim().isEmpty()) {
            sql.append("AND CategoryID = ? ");
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            if (search != null && !search.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
            }
            
            if (categoryFilter != null && !categoryFilter.trim().isEmpty()) {
                stmt.setString(paramIndex, categoryFilter);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    public Product getProductById(String productId) {
        String sql = "SELECT p.*, c.CategoryName, u.Description as UnitDescription " +
                    "FROM Product p " +
                    "LEFT JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "LEFT JOIN Unit u ON p.UnitID = u.UnitID " +
                    "WHERE p.ProductID = ?";
        
        try (
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getString("ProductID"));
                product.setProductName(rs.getString("ProductName"));
                product.setCategoryID(rs.getString("CategoryID"));
                product.setUnitID(rs.getString("UnitID"));
                product.setImportPrice(rs.getBigDecimal("ImportPrice"));
                product.setSellingPrice(rs.getBigDecimal("SellingPrice"));
                product.setDescription(rs.getString("Description"));
                product.setStatus(rs.getBoolean("Status"));
                product.setImageUrl(rs.getString("ImageUrl"));
                product.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                product.setCreatedBy(rs.getString("CreatedBy"));
                product.setCategoryName(rs.getString("CategoryName"));
                product.setUnitDescription(rs.getString("UnitDescription"));
                
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO Product (ProductID, ProductName, CategoryID, UnitID, " +
                    "ImportPrice, SellingPrice, Description, Status, ImageUrl, CreatedBy) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, product.getProductID());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getCategoryID());
            stmt.setString(4, product.getUnitID());
            stmt.setBigDecimal(5, product.getImportPrice());
            stmt.setBigDecimal(6, product.getSellingPrice());
            stmt.setString(7, product.getDescription());
            stmt.setBoolean(8, product.isStatus());
            stmt.setString(9, product.getImageUrl());
            stmt.setString(10, product.getCreatedBy());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET ProductName = ?, CategoryID = ?, UnitID = ?, " +
                    "ImportPrice = ?, SellingPrice = ?, Description = ?, Status = ?, ImageUrl = ? " +
                    "WHERE ProductID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategoryID());
            stmt.setString(3, product.getUnitID());
            stmt.setBigDecimal(4, product.getImportPrice());
            stmt.setBigDecimal(5, product.getSellingPrice());
            stmt.setString(6, product.getDescription());
            stmt.setBoolean(7, product.isStatus());
            stmt.setString(8, product.getImageUrl());
            stmt.setString(9, product.getProductID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM Product WHERE ProductID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE Status = 1 ORDER BY CategoryName";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getString("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                category.setDescription(rs.getString("Description"));
                category.setStatus(rs.getBoolean("Status"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }
    
    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT * FROM Unit ORDER BY Description";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnitID(rs.getString("UnitID"));
                unit.setDescription(rs.getString("Description"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return units;
    }
    
    public boolean isProductIdExists(String productId) {
        String sql = "SELECT COUNT(*) FROM Product WHERE ProductID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    public static void main(String[] args) {
        DBContext context = new DBContext("SWP6");
        Connection connection = context.getConnection();
        ProductDAO dao = new ProductDAO(connection);
        System.out.println(dao.getProductById("P001").toString());
    }
}
