/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Category;
import Models.Product;
import Models.Unit;
import java.math.BigDecimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {

    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT [ProductID]\n"
                + "      ,[ProductName]\n"
                + "      ,[CategoryID]\n"
                + "      ,[UnitID]\n"
                + "      ,[SellingPrice]\n"
                + "      ,[Description]\n"
                + "      ,[Status]\n"
                + "      ,[CreatedDate]\n"
                + "      ,[CreatedBy]\n"
                + "  FROM [dbo].[Product]";
        List<Product> l = new ArrayList<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Product pr = new Product(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("CategoryID"),
                        rs.getString("UnitID"),
                        rs.getBigDecimal("SellingPrice"),
                        rs.getString("Description"),
                        rs.getBoolean("Status"),
                        rs.getTimestamp("CreatedDate").toLocalDateTime(),
                        rs.getString("CreatedBy"));

                l.add(pr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }
  
    public Vector<Product> getProduct(String sql) {
        Vector<Product> vector = new Vector<>();
        try (PreparedStatement pre = connection.prepareStatement(sql); ResultSet rs = pre.executeQuery()) {

            while (rs.next()) {
                int productID = rs.getInt("productID");
                String productName = rs.getString("productName");
                String categoryID = rs.getString("categoryID");
                String unitID = rs.getString("unitID");
                BigDecimal importPrice = rs.getBigDecimal("importPrice");
                BigDecimal sellingPrice = rs.getBigDecimal("sellingPrice");
                String description = rs.getString("description");
                boolean status = rs.getBoolean("status");
                String imageUrl = rs.getString("imageUrl");
                String createdBy = rs.getString("createdBy");

                Product pro = new Product(productID, productName, categoryID, unitID,
                        importPrice, sellingPrice, description,
                        status, imageUrl, createdBy);
                vector.add(pro);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public List<Product> getAllProducts(int page, int pageSize, String search, String categoryFilter) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.CategoryName, u.Description as UnitDescription "
                + "FROM Product p "
                + "LEFT JOIN Category c ON p.CategoryID = c.CategoryID "
                + "LEFT JOIN Unit u ON p.UnitID = u.UnitID "
                + "WHERE 1=1 ");

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
                product.setProductID(rs.getInt("ProductID")); // Changed to int
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

    public Product getProductById(int productId) {
        String sql = "SELECT p.*, c.CategoryName, u.Description as UnitDescription "
                + "FROM Product p "
                + "LEFT JOIN Category c ON p.CategoryID = c.CategoryID "
                + "LEFT JOIN Unit u ON p.UnitID = u.UnitID "
                + "WHERE p.ProductID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
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

        String sql = "INSERT INTO Product (ProductName, CategoryID, UnitID, "
                + "ImportPrice, SellingPrice, Description, Status, ImageUrl, CreatedBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategoryID());
            stmt.setString(3, product.getUnitID());
            stmt.setBigDecimal(4, product.getImportPrice());
            stmt.setBigDecimal(5, product.getSellingPrice());
            stmt.setString(6, product.getDescription());
            stmt.setBoolean(7, product.isStatus());
            stmt.setString(8, product.getImageUrl());
            stmt.setString(9, product.getCreatedBy());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET ProductName = ?, CategoryID = ?, UnitID = ?, "
                + "ImportPrice = ?, SellingPrice = ?, Description = ?, Status = ?, ImageUrl = ? "
                + "WHERE ProductID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getCategoryID());
            stmt.setString(3, product.getUnitID());
            stmt.setBigDecimal(4, product.getImportPrice());
            stmt.setBigDecimal(5, product.getSellingPrice());
            stmt.setString(6, product.getDescription());
            stmt.setBoolean(7, product.isStatus());
            stmt.setString(8, product.getImageUrl());
            stmt.setInt(9, product.getProductID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM Product WHERE ProductID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE Status = 1 ORDER BY CategoryName";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
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

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnitID(rs.getInt("UnitID"));
                unit.setDescription(rs.getString("Description"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return units;
    }

    public static void main(String[] args) {
        DBContext connection = new DBContext("SWP1");
        ProductDAO pDAO = new ProductDAO(connection.getConnection());
        List<Product> p = pDAO.getAllProducts();
        for (Product product : p) {
            System.out.println(product.getProductName());
        }
    }

}
