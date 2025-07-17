/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Models.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Thai Anh
 */
public class CategoryDAO {
     private Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryID(rs.getInt("CategoryID"));
                c.setCategoryName(rs.getString("CategoryName"));
                c.setDescription(rs.getString("Description"));
                c.setStatus(rs.getBoolean("Status"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Category> getActiveCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE Status = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryID(rs.getInt("CategoryID"));
                c.setCategoryName(rs.getString("CategoryName"));
                c.setDescription(rs.getString("Description"));
                c.setStatus(rs.getBoolean("Status"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Category getCategoryByID(int id) {
        String sql = "SELECT * FROM Category WHERE CategoryID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category c = new Category();
                    c.setCategoryID(rs.getInt("CategoryID"));
                    c.setCategoryName(rs.getString("CategoryName"));
                    c.setDescription(rs.getString("Description"));
                    c.setStatus(rs.getBoolean("Status"));
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addCategory(Category c) {
        String sql = "INSERT INTO Category (CategoryName, Description, Status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCategoryName());
            ps.setString(2, c.getDescription());
            ps.setBoolean(3, c.isStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCategory(Category c) {
        String sql = "UPDATE Category SET CategoryName = ?, Description = ?, Status = ? WHERE CategoryID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCategoryName());
            ps.setString(2, c.getDescription());
            ps.setBoolean(3, c.isStatus());
            ps.setInt(4, c.getCategoryID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(int id) {
        String sql = "UPDATE Category SET Status = 0 WHERE CategoryID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
