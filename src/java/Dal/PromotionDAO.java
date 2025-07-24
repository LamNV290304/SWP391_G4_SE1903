/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.Category;
import Models.Promotion;
import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author duckh
 */
public class PromotionDAO {

    private Connection connection;

    public PromotionDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Promotion> getPromotionsByPage(int pageIndex, int pageSize) throws SQLException {
        List<Promotion> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Promotion p "
                + "JOIN Category c ON p.CategoryID = c.CategoryID "
                + "ORDER BY p.StartDate DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int offset = (pageIndex - 1) * pageSize;
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Promotion p = new Promotion();
                p.setPromotionId(rs.getInt("PromotionID"));
                p.setPromotionName(rs.getString("PromotionName"));
                p.setStartDate(rs.getDate("StartDate"));
                p.setEndDate(rs.getDate("EndDate"));
                p.setStatus(rs.getBoolean("Status"));
                p.setCreatedDate(rs.getTimestamp("CreatedDate"));
                p.setCreatedBy(rs.getString("CreatedBy"));
                p.setDiscountRate(rs.getDouble("DiscountRate"));
                p.setCategoryId(rs.getInt("CategoryID"));
                p.setCategoryName(rs.getString("CategoryName"));
                list.add(p);
            }
        }
        return list;
    }

    public List<Promotion> getPromotionsByCustomerId(int customerId) throws SQLException {
        List<Promotion> list = new ArrayList<>();
        String sql = """
        SELECT p.PromotionID, p.PromotionName, p.StartDate, p.EndDate, p.Status, 
               p.CreatedDate, p.CreatedBy, p.DiscountRate, p.CategoryID
        FROM Promotion p
        INNER JOIN Customer c ON 1 = 1
        WHERE c.CustomerID = ?
          AND GETDATE() BETWEEN p.StartDate AND p.EndDate
          AND p.Status = 1
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Promotion p = new Promotion();
                p.setPromotionId(rs.getInt("PromotionID"));
                p.setPromotionName(rs.getString("PromotionName"));
                p.setStartDate(rs.getDate("StartDate"));
                p.setEndDate(rs.getDate("EndDate"));
                p.setStatus(rs.getBoolean("Status"));
                p.setCreatedDate(rs.getTimestamp("CreatedDate"));
                p.setCreatedBy(rs.getString("CreatedBy"));
                p.setDiscountRate(rs.getDouble("DiscountRate"));
                p.setCategoryId(rs.getInt("CategoryID"));
                list.add(p);
            }
        }

        return list;
    }

    public List<Promotion> getFilteredPromotionsByPage(Integer month, String nameKeyword, int pageIndex, int pageSize) throws SQLException {
        List<Promotion> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName "
                + "FROM Promotion p "
                + "JOIN Category c ON p.CategoryID = c.CategoryID "
                + "WHERE 1=1";
        if (month != null) {
            sql += " AND MONTH(p.startDate) = ?";
        }
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            sql += " AND p.promotionName COLLATE Latin1_General_CI_AI LIKE ?";
        }
        sql += " ORDER BY p.startDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        PreparedStatement ps = connection.prepareStatement(sql);
        int idx = 1;
        if (month != null) {
            ps.setInt(idx++, month);
        }
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            ps.setString(idx++, "%" + nameKeyword + "%");
        }
        ps.setInt(idx++, (pageIndex - 1) * pageSize);
        ps.setInt(idx, pageSize);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Promotion p = new Promotion();
            p.setPromotionId(rs.getInt("PromotionID"));
            p.setPromotionName(rs.getString("PromotionName"));
            p.setStartDate(rs.getDate("StartDate"));
            p.setEndDate(rs.getDate("EndDate"));
            p.setStatus(rs.getBoolean("Status"));
            p.setCreatedDate(rs.getTimestamp("CreatedDate"));
            p.setCreatedBy(rs.getString("CreatedBy"));
            p.setDiscountRate(rs.getDouble("DiscountRate"));
            p.setCategoryId(rs.getInt("CategoryID"));
            p.setCategoryName(rs.getString("CategoryName")); // <-- lấy tên danh mục
            list.add(p);
        }
        return list;
    }

    public int countFilteredPromotions(Integer month, String nameKeyword) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Promotion WHERE 1=1";
        if (month != null) {
            sql += " AND MONTH(startDate) = ?";
        }
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            sql += " AND promotionName COLLATE Latin1_General_CI_AI LIKE ?";
        }

        PreparedStatement ps = connection.prepareStatement(sql);
        int idx = 1;
        if (month != null) {
            ps.setInt(idx++, month);
        }
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            ps.setString(idx++, "%" + nameKeyword + "%");
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName FROM Category";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryID(rs.getInt("CategoryID"));
                c.setCategoryName(rs.getString("CategoryName"));
                list.add(c);
            }
        }
        return list;
    }

    public int getTotalPromotionCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Promotion";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public Promotion getPromotionById(int id) throws SQLException {
        String sql = "SELECT p.*, c.CategoryName FROM Promotion p JOIN Category c ON p.CategoryID = c.CategoryID WHERE p.PromotionID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Promotion p = new Promotion();
                p.setPromotionId(rs.getInt("PromotionID"));
                p.setPromotionName(rs.getString("PromotionName"));
                p.setStartDate(rs.getDate("StartDate"));
                p.setEndDate(rs.getDate("EndDate"));
                p.setStatus(rs.getBoolean("Status"));
                p.setCreatedDate(rs.getTimestamp("CreatedDate"));
                p.setCreatedBy(rs.getString("CreatedBy"));
                p.setDiscountRate(rs.getDouble("DiscountRate"));
                p.setCategoryId(rs.getInt("CategoryID"));
                p.setCategoryName(rs.getString("CategoryName"));
                return p;
            }
        }
        return null;
    }

    public void createPromotion(Promotion p) throws SQLException {
        String sql = "INSERT INTO Promotion (PromotionName, StartDate, EndDate, Status, CreatedDate, CreatedBy, DiscountRate, CategoryID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getPromotionName());
            ps.setDate(2, p.getStartDate());
            ps.setDate(3, p.getEndDate());
            ps.setBoolean(4, p.isStatus());
            ps.setTimestamp(5, p.getCreatedDate());
            ps.setString(6, p.getCreatedBy());
            ps.setDouble(7, p.getDiscountRate());
            ps.setInt(8, p.getCategoryId());
            ps.executeUpdate();
        }
    }

    public Promotion getActivePromotion() throws SQLException {
        String sql = "SELECT TOP 1 p.*, c.CategoryName "
                + "FROM Promotion p "
                + "JOIN Category c ON p.CategoryID = c.CategoryID "
                + "WHERE p.Status = 1 AND p.StartDate <= GETDATE() AND p.EndDate >= GETDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Promotion(
                            rs.getInt("PromotionID"),
                            rs.getString("PromotionName"),
                            rs.getDate("StartDate"),
                            rs.getDate("EndDate"),
                            rs.getBoolean("Status"),
                            rs.getTimestamp("CreatedDate"),
                            rs.getString("CreatedBy"),
                            rs.getDouble("DiscountRate"),
                            rs.getInt("CategoryID"),
                            rs.getString("CategoryName")
                    );
                }
            }
        }
        return null;
    }

    public void updatePromotion(Promotion p) throws SQLException {
        String sql = "UPDATE Promotion SET PromotionName=?, StartDate=?, EndDate=?, Status=?, DiscountRate=?, CategoryID=? WHERE PromotionID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getPromotionName());
            ps.setDate(2, p.getStartDate());
            ps.setDate(3, p.getEndDate());
            ps.setBoolean(4, p.isStatus());
            ps.setDouble(5, p.getDiscountRate());
            ps.setInt(6, p.getCategoryId());
            ps.setInt(7, p.getPromotionId());
            ps.executeUpdate();
        }
    }

    public void deletePromotion(int id) throws SQLException {
        String sql = "DELETE FROM Promotion WHERE PromotionID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
