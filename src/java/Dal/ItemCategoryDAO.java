/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;


import DTO.CategoryItemCountDto;
import java.sql.Connection;
import Models.ItemCategory;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duckh
 */
public class ItemCategoryDAO {

    private Connection connection;

    public ItemCategoryDAO(Connection connection) {
        this.connection = connection;
    }

    public int addCategory(ItemCategory category) throws SQLException {
        String query = "INSERT INTO ItemCategories (CategoryName, Description) VALUES (?, ?)";
        int generatedId = -1;

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    public List<CategoryItemCountDto> getCategoryItemCounts(String categoryNameFilter, String shopNameFilter) throws SQLException {
        List<CategoryItemCountDto> categoryCounts = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.categoryId, c.categoryName, COUNT(si.itemId) AS itemCount ");
        sql.append("FROM ItemCategories c ");
        sql.append("LEFT JOIN ShopItems si ON c.categoryID = si.categoryID ");
        sql.append("LEFT JOIN Shop s ON si.shopID = s.shopID "); 
        sql.append("WHERE 1=1 ");

        if (categoryNameFilter != null && !categoryNameFilter.trim().isEmpty()) {
            sql.append("AND c.categoryName COLLATE Latin1_General_CI_AI LIKE ? ");
        }
        if (shopNameFilter != null && !shopNameFilter.trim().isEmpty()) {
            sql.append("AND s.shopName COLLATE Latin1_General_CI_AI LIKE ? ");
        }

        sql.append("GROUP BY c.categoryId, c.categoryName ");
        sql.append("ORDER BY c.categoryName");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (categoryNameFilter != null && !categoryNameFilter.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + categoryNameFilter + "%");
            }
            if (shopNameFilter != null && !shopNameFilter.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + shopNameFilter + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int categoryId = rs.getInt("categoryId");
                    String categoryName = rs.getString("categoryName");
                    int itemCount = rs.getInt("itemCount");
                    categoryCounts.add(new CategoryItemCountDto(categoryId, categoryName, itemCount));
                }
            }
        }
        return categoryCounts;
    }

    public List<ItemCategory> getAllCategories() throws SQLException {
        List<ItemCategory> categories = new ArrayList<>();
        String query = "SELECT CategoryID, CategoryName, Description FROM ItemCategories ORDER BY CategoryName ASC";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ItemCategory category = new ItemCategory();
                category.setCategoryId(rs.getInt("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                category.setDescription(rs.getString("Description"));
                categories.add(category);
            }
        }
        return categories;
    }

    public ItemCategory getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT CategoryID, CategoryName, Description FROM ItemCategories WHERE CategoryID = ?";
        ItemCategory category = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = new ItemCategory();
                    category.setCategoryId(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    category.setDescription(rs.getString("Description"));
                }
            }
        }
        return category;
    }

    public boolean updateCategory(ItemCategory category) throws SQLException {
        String query = "UPDATE ItemCategories SET CategoryName = ?, Description = ? WHERE CategoryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM ItemCategories WHERE CategoryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        }
    }

   
}
