/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import DTO.ShopTotalValueDto;
import Models.ItemCategory;
import Models.Shop;
import Models.ShopItem;
import Models.Unit;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duckh
 */
public class ShopItemDAO {

    private Connection connection;

    public ShopItemDAO(Connection connection) {
        this.connection = connection;
    }

    public int addItem(ShopItem item) throws SQLException {

        String sql = "INSERT INTO [dbo].[ShopItems]\n"
                + "           ([ItemName]\n"
                + "           ,[CategoryID]\n"
                + "           ,[Quantity]\n"
                + "           ,[UnitID]\n"
                + "           ,[Price]\n"
                + "           ,[ItemDate]\n"
                + "           ,[ShopID]\n"
                + "           ,[Notes])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,GETDATE()\n"
                + "           ,?\n"
                + "           ,?)";
        int generatedId = -1;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int paramIndex = 1;
            ps.setString(paramIndex++, item.getItemName());

            ps.setInt(paramIndex++, item.getCategoryId());
            ps.setInt(paramIndex++, item.getQuantity());
            ps.setInt(paramIndex++, item.getUnitId());
            ps.setBigDecimal(paramIndex++, item.getPrice());

            if (item.getShopId() != null) {
                ps.setInt(paramIndex++, item.getShopId());
            } else {
                ps.setNull(paramIndex++, java.sql.Types.INTEGER);
            }
            ps.setString(paramIndex++, item.getNotes());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (java.sql.ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    public BigDecimal getTotalValueAllShops() {
        BigDecimal totalValue = BigDecimal.ZERO;
        String sql = "SELECT SUM(i.quantity * i.price) AS TotalValue FROM ShopItems i";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                totalValue = rs.getBigDecimal("TotalValue");
                if (totalValue == null) { 
                    totalValue = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ShopItemDAO.class.getName()).log(Level.SEVERE, "Lỗi SQL khi lấy tổng giá trị toàn hệ thống", e);
           
        }
        return totalValue;
    }

    public ShopItem getItemById(int itemId) throws SQLException {

        String sql = "SELECT si.ItemID, si.ItemName, si.CategoryID, ic.CategoryName, "
                + "si.Quantity, si.UnitID, u.Description AS UnitName, si.Price, si.ItemDate, si.ShopID, s.ShopName, "
                + "si.Notes "
                + "FROM ShopItems si "
                + "JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID "
                + "LEFT JOIN Shop s ON si.ShopID = s.ShopID "
                + "LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID "
                + "WHERE si.ItemID = ?";
        ShopItem item = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item = new ShopItem();
                    item.setItemId(rs.getInt("ItemID"));
                    item.setItemName(rs.getString("ItemName"));

                    ItemCategory category = new ItemCategory();
                    category.setCategoryId(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    item.setCategory(category);
                    item.setCategoryId(rs.getInt("CategoryID"));

                    item.setQuantity(rs.getInt("Quantity"));

                    Unit unit = new Unit();
                    unit.setUnitID(rs.getInt("UnitID"));
                    unit.setDescription(rs.getString("UnitName"));
                    item.setUnit(unit);
                    item.setUnitId(rs.getInt("UnitID"));

                    item.setPrice(rs.getBigDecimal("Price"));
                    item.setItemDate(rs.getTimestamp("ItemDate"));

                    if (rs.getObject("ShopID") != null) {
                        Shop shop = new Shop();
                        shop.setShopID(rs.getInt("ShopID"));
                        shop.setShopName(rs.getString("ShopName"));
                        item.setShop(shop);
                        item.setShopId(rs.getInt("ShopID"));
                    }

                    item.setNotes(rs.getString("Notes"));
                }
            }
        }
        return item;
    }

    public int getTotalShopItemCount() throws SQLException {
        String sql = "SELECT COUNT(ItemID) FROM ShopItems";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public BigDecimal getTotalValueByShopId(int shopId) { 
        BigDecimal totalValue = BigDecimal.ZERO; 
        String sql = "SELECT SUM(i.quantity * i.price) AS TotalValue FROM ShopItems i WHERE i.shopId = ?"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, shopId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    totalValue = rs.getBigDecimal("TotalValue"); 
                    if (totalValue == null) { 
                        totalValue = BigDecimal.ZERO;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return totalValue;
    }

    public int countShopItemsByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(si.ItemID) FROM ShopItems si ");
        sqlBuilder.append("JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID ");
        sqlBuilder.append("LEFT JOIN Shop s ON si.ShopID = s.ShopID ");
        sqlBuilder.append("LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID ");
        sqlBuilder.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (startDate != null) {
            sqlBuilder.append("AND CAST(si.ItemDate AS DATE) >= ? ");
            params.add(Timestamp.valueOf(startDate.atStartOfDay()));
        }

        if (endDate != null) {
            sqlBuilder.append("AND CAST(si.ItemDate AS DATE) <= ? ");
            params.add(Timestamp.valueOf(endDate.atTime(23, 59, 59, 999999999)));
        }

        try (PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<ShopItem> getShopItemsByPage(int pageIndex, int pageSize) throws SQLException {
        List<ShopItem> items = new ArrayList<>();

        String sql = "SELECT si.ItemID, si.ItemName, si.CategoryID, ic.CategoryName, "
                + "si.Quantity, si.UnitID, u.Description AS UnitName, si.Price, si.ItemDate, si.ShopID, s.ShopName, "
                + "si.Notes "
                + "FROM ShopItems si "
                + "JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID "
                + "LEFT JOIN Shop s ON si.ShopID = s.ShopID "
                + "LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID "
                + "ORDER BY si.ItemID DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            int offset = (pageIndex - 1) * pageSize;
            ptm.setInt(1, offset);
            ptm.setInt(2, pageSize);

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    ShopItem item = new ShopItem();
                    item.setItemId(rs.getInt("ItemID"));
                    item.setItemName(rs.getString("ItemName"));

                    ItemCategory category = new ItemCategory();
                    category.setCategoryId(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    item.setCategory(category);

                    item.setCategoryId(rs.getInt("CategoryID"));

                    item.setQuantity(rs.getInt("Quantity"));

                    Unit unit = new Unit();
                    unit.setUnitID(rs.getInt("UnitID"));
                    unit.setDescription(rs.getString("UnitName"));
                    item.setUnit(unit);

                    item.setUnitId(rs.getInt("UnitID"));

                    item.setPrice(rs.getBigDecimal("Price"));
                    item.setItemDate(rs.getTimestamp("ItemDate"));

                    if (rs.getObject("ShopID") != null) {
                        Shop shop = new Shop();
                        shop.setShopID(rs.getInt("ShopID"));
                        shop.setShopName(rs.getString("ShopName"));
                        item.setShop(shop);

                        item.setShopId(rs.getInt("ShopID"));
                    } else {
                        item.setShop(null);
                        item.setShopId(null);
                    }

                    item.setNotes(rs.getString("Notes"));
                    items.add(item);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopItemDAO.class
                    .getName()).log(Level.SEVERE, "Lỗi SQL khi phân trang ShopItem", ex);
            throw ex;
        }
        return items;
    }

    public List<ShopItem> getAllItems() throws SQLException {
        List<ShopItem> items = new ArrayList<>();

        String sql = "SELECT si.ItemID, si.ItemName, si.CategoryID, ic.CategoryName, "
                + "si.Quantity, si.UnitID, u.Description AS UnitName, si.Price, si.ItemDate, si.ShopID, s.ShopName, "
                + "si.Notes "
                + "FROM ShopItems si "
                + "JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID "
                + "LEFT JOIN Shop s ON si.ShopID = s.ShopID "
                + "LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID "
                + "ORDER BY si.ItemName ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ShopItem item = new ShopItem();
                item.setItemId(rs.getInt("ItemID"));
                item.setItemName(rs.getString("ItemName"));

                ItemCategory category = new ItemCategory();
                category.setCategoryId(rs.getInt("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                item.setCategory(category);

                item.setQuantity(rs.getInt("Quantity"));

                Unit unit = new Unit();
                unit.setUnitID(rs.getInt("UnitID"));
                unit.setDescription(rs.getString("UnitName"));
                item.setUnit(unit);

                item.setPrice(rs.getBigDecimal("Price"));
                item.setItemDate(rs.getTimestamp("ItemDate"));

                if (rs.getObject("ShopID") != null) {
                    Shop shop = new Shop();
                    shop.setShopID(rs.getInt("ShopID"));
                    shop.setShopName(rs.getString("ShopName"));
                    item.setShop(shop);
                }

                item.setNotes(rs.getString("Notes"));

                items.add(item);
            }
        }
        return items;
    }

    public List<ShopItem> searchShopItemsByKey(String key) throws SQLException {
        List<ShopItem> items = new ArrayList<>();
        String sql = "SELECT si.ItemID, si.ItemName, si.CategoryID, ic.CategoryName, "
                + "si.Quantity, si.UnitID, u.Description AS UnitName, si.Price, si.ItemDate, si.ShopID, s.ShopName, "
                + "si.Notes "
                + "FROM ShopItems si "
                + "JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID "
                + "LEFT JOIN Shop s ON si.ShopID = s.ShopID "
                + "LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID "
                + "WHERE CAST(si.ItemID AS VARCHAR) LIKE ? OR si.ItemName COLLATE Latin1_General_CI_AI LIKE ? "
                + "ORDER BY si.ItemID DESC";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setString(1, "%" + key + "%");
            ptm.setString(2, "%" + key + "%");

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    ShopItem item = new ShopItem();
                    item.setItemId(rs.getInt("ItemID"));
                    item.setItemName(rs.getString("ItemName"));

                    ItemCategory category = new ItemCategory();
                    category.setCategoryId(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    item.setCategory(category);
                    item.setCategoryId(rs.getInt("CategoryID"));

                    item.setQuantity(rs.getInt("Quantity"));

                    Unit unit = new Unit();
                    unit.setUnitID(rs.getInt("UnitID"));
                    unit.setDescription(rs.getString("UnitName"));
                    item.setUnit(unit);
                    item.setUnitId(rs.getInt("UnitID"));

                    item.setPrice(rs.getBigDecimal("Price"));
                    item.setItemDate(rs.getTimestamp("ItemDate"));

                    if (rs.getObject("ShopID") != null) {
                        Shop shop = new Shop();
                        shop.setShopID(rs.getInt("ShopID"));
                        shop.setShopName(rs.getString("ShopName"));
                        item.setShop(shop);
                        item.setShopId(rs.getInt("ShopID"));
                    } else {
                        item.setShop(null);
                        item.setShopId(null);
                    }

                    item.setNotes(rs.getString("Notes"));
                    items.add(item);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopItemDAO.class
                    .getName()).log(Level.SEVERE, "Lỗi SQL khi tìm kiếm ShopItem theo từ khóa", ex);
            throw ex;
        }
        return items;
    }

    public List<ShopItem> getShopItemsByDateRange(LocalDate startDate, LocalDate endDate, int pageIndex, int pageSize) throws SQLException {
        List<ShopItem> items = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT si.ItemID, si.ItemName, si.CategoryID, ic.CategoryName, "
                + "si.Quantity, si.UnitID, u.Description AS UnitName, si.Price, si.ItemDate, si.ShopID, s.ShopName, "
                + "si.Notes "
                + "FROM ShopItems si "
                + "JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID "
                + "LEFT JOIN Shop s ON si.ShopID = s.ShopID "
                + "LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID "
                + "WHERE 1=1 ");

        if (startDate != null) {
            sqlBuilder.append(" AND CAST(si.ItemDate AS DATE) >= ?");
        }
        if (endDate != null) {
            sqlBuilder.append(" AND CAST(si.ItemDate AS DATE) <= ?");
        }

        sqlBuilder.append(" ORDER BY si.ItemDate DESC ");
        sqlBuilder.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement pstmt = connection.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            if (startDate != null) {

                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(startDate.atStartOfDay()));
            }
            if (endDate != null) {

                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(endDate.atTime(23, 59, 59, 999999999)));
            }

            int offset = (pageIndex - 1) * pageSize;
            pstmt.setInt(paramIndex++, offset);
            pstmt.setInt(paramIndex++, pageSize);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ShopItem item = new ShopItem();
                    item.setItemId(rs.getInt("ItemID"));
                    item.setItemName(rs.getString("ItemName"));

                    ItemCategory category = new ItemCategory();
                    category.setCategoryId(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    item.setCategory(category);
                    item.setCategoryId(rs.getInt("CategoryID"));

                    item.setQuantity(rs.getInt("Quantity"));

                    Unit unit = new Unit();
                    unit.setUnitID(rs.getInt("UnitID"));
                    unit.setDescription(rs.getString("UnitName"));
                    item.setUnit(unit);
                    item.setUnitId(rs.getInt("UnitID"));

                    item.setPrice(rs.getBigDecimal("Price"));
                    item.setItemDate(rs.getTimestamp("ItemDate"));

                    if (rs.getObject("ShopID") != null) {
                        Shop shop = new Shop();
                        shop.setShopID(rs.getInt("ShopID"));
                        shop.setShopName(rs.getString("ShopName"));
                        item.setShop(shop);
                        item.setShopId(rs.getInt("ShopID"));
                    } else {
                        item.setShop(null);
                        item.setShopId(null);
                    }

                    item.setNotes(rs.getString("Notes"));
                    items.add(item);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopItemDAO.class
                    .getName()).log(Level.SEVERE, "Lỗi SQL khi tìm kiếm ShopItem theo phạm vi ngày", ex);
            throw ex;
        }
        return items;
    }

    public List<ShopItem> searchShopItemsByKeyWithPagination(String key, int pageIndex, int pageSize) throws SQLException {
        List<ShopItem> items = new ArrayList<>();
        String sql = "SELECT si.ItemID, si.ItemName, si.CategoryID, ic.CategoryName, "
                + "si.Quantity, si.UnitID, u.Description AS UnitName, si.Price, si.ItemDate, si.ShopID, s.ShopName, "
                + "si.Notes "
                + "FROM ShopItems si "
                + "JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID "
                + "LEFT JOIN Shop s ON si.ShopID = s.ShopID "
                + "LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID "
                + "WHERE CAST(si.ItemID AS VARCHAR) LIKE ? OR si.ItemName COLLATE Latin1_General_CI_AI LIKE ? "
                + "ORDER BY si.ItemID DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setString(1, "%" + key + "%");
            ptm.setString(2, "%" + key + "%");

            int offset = (pageIndex - 1) * pageSize;
            ptm.setInt(3, offset);
            ptm.setInt(4, pageSize);

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    ShopItem item = new ShopItem();
                    item.setItemId(rs.getInt("ItemID"));
                    item.setItemName(rs.getString("ItemName"));

                    ItemCategory category = new ItemCategory();
                    category.setCategoryId(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    item.setCategory(category);
                    item.setCategoryId(rs.getInt("CategoryID"));

                    item.setQuantity(rs.getInt("Quantity"));

                    Unit unit = new Unit();
                    unit.setUnitID(rs.getInt("UnitID"));
                    unit.setDescription(rs.getString("UnitName"));
                    item.setUnit(unit);
                    item.setUnitId(rs.getInt("UnitID"));

                    item.setPrice(rs.getBigDecimal("Price"));
                    item.setItemDate(rs.getTimestamp("ItemDate"));

                    if (rs.getObject("ShopID") != null) {
                        Shop shop = new Shop();
                        shop.setShopID(rs.getInt("ShopID"));
                        shop.setShopName(rs.getString("ShopName"));
                        item.setShop(shop);
                        item.setShopId(rs.getInt("ShopID"));
                    } else {
                        item.setShop(null);
                        item.setShopId(null);
                    }

                    item.setNotes(rs.getString("Notes"));
                    items.add(item);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopItemDAO.class
                    .getName()).log(Level.SEVERE, "Lỗi SQL khi tìm kiếm ShopItem theo từ khóa với phân trang", ex);
            throw ex;
        }
        return items;
    }

    public boolean updateItemQuantity(int itemId, int newQuantity, BigDecimal newPrice) throws SQLException {
        String sql = "UPDATE ShopItems SET Quantity = ?,Price = ?, ItemDate = GETDATE() WHERE ItemID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setBigDecimal(2, newPrice);
            ps.setInt(3, itemId);

            return ps.executeUpdate() > 0;
        }
    }

    public ShopItem getExistingItem(String itemName, int categoryId, int unitId, Integer shopId) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT si.ItemID, si.ItemName, si.CategoryID, ic.CategoryName, "
                + "si.Quantity, si.UnitID, u.Description AS UnitName, si.Price, si.ItemDate, si.ShopID, s.ShopName, "
                + "si.Notes "
                + "FROM ShopItems si "
                + "JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID "
                + "LEFT JOIN Shop s ON si.ShopID = s.ShopID "
                + "LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID "
                + "WHERE si.ItemName = ? AND si.CategoryID = ? AND si.UnitID = ?");

        if (shopId != null) {
            sql.append(" AND si.ShopID = ?");
        } else {
            sql.append(" AND si.ShopID IS NULL");
        }

        ShopItem item = null;
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setString(paramIndex++, itemName);

            ps.setInt(paramIndex++, categoryId);
            ps.setInt(paramIndex++, unitId);
            if (shopId != null) {
                ps.setInt(paramIndex++, shopId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    item = new ShopItem();
                    item.setItemId(rs.getInt("ItemID"));
                    item.setItemName(rs.getString("ItemName"));

                    ItemCategory category = new ItemCategory();
                    category.setCategoryId(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    item.setCategory(category);
                    item.setCategoryId(rs.getInt("CategoryID"));

                    item.setQuantity(rs.getInt("Quantity"));

                    Unit unit = new Unit();
                    unit.setUnitID(rs.getInt("UnitID"));
                    unit.setDescription(rs.getString("UnitName"));
                    item.setUnit(unit);
                    item.setUnitId(rs.getInt("UnitID"));

                    item.setPrice(rs.getBigDecimal("Price"));
                    item.setItemDate(rs.getTimestamp("ItemDate"));

                    if (rs.getObject("ShopID") != null) {
                        Shop shop = new Shop();
                        shop.setShopID(rs.getInt("ShopID"));
                        shop.setShopName(rs.getString("ShopName"));
                        item.setShop(shop);
                        item.setShopId(rs.getInt("ShopID"));
                    } else {
                        item.setShop(null);
                        item.setShopId(null);
                    }
                    item.setNotes(rs.getString("Notes"));
                }
            }
        }
        return item;
    }

    public int countShopItemsByName(String itemNameKeyword) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(si.ItemID) FROM ShopItems si ");
        sqlBuilder.append("JOIN ItemCategories ic ON si.CategoryID = ic.CategoryID ");
        sqlBuilder.append("LEFT JOIN Shop s ON si.ShopID = s.ShopID ");
        sqlBuilder.append("LEFT JOIN [dbo].[Unit] u ON si.UnitID = u.UnitID ");
        sqlBuilder.append("WHERE si.ItemName LIKE ? ");

        try (PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            ps.setString(1, "%" + itemNameKeyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean updateItem(ShopItem item) throws SQLException {

        String sql = "UPDATE ShopItems SET ItemName = ?, CategoryID = ?, "
                + "Quantity = ?, UnitID = ?, Price = ?, ItemDate = ?, ShopID = ?, Notes = ? "
                + "WHERE ItemID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, item.getItemName());

            ps.setInt(2, item.getCategoryId());
            ps.setInt(3, item.getQuantity());

            ps.setInt(4, item.getUnitId());

            if (item.getPrice() != null) {
                ps.setBigDecimal(5, item.getPrice());
            } else {
                ps.setNull(5, java.sql.Types.DECIMAL);
            }

            if (item.getItemDate() != null) {
                ps.setTimestamp(6, item.getItemDate());
            } else {
                ps.setNull(6, java.sql.Types.TIMESTAMP);
            }

            if (item.getShopId() != null) {
                ps.setInt(7, item.getShopId());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            ps.setString(8, item.getNotes());
            ps.setInt(9, item.getItemId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteItem(int itemId) throws SQLException {
        String query = "DELETE FROM ShopItems WHERE ItemID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<ShopTotalValueDto> getShopTotalValues(String categoryNameFilter, String shopNameFilter) throws SQLException {
        List<ShopTotalValueDto> shopValues = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.shopID, s.shopName, ISNULL(SUM(si.quantity * si.price), 0) AS totalValue ");
        sql.append("FROM Shop s ");
        sql.append("LEFT JOIN ShopItems si ON s.shopID = si.shopID ");
        sql.append("LEFT JOIN ItemCategories ic ON si.categoryID = ic.categoryID "); 
        sql.append("WHERE 1=1 ");

        if (categoryNameFilter != null && !categoryNameFilter.trim().isEmpty()) {
            sql.append("AND ic.categoryName COLLATE Latin1_General_CI_AI LIKE ? ");
        }
        if (shopNameFilter != null && !shopNameFilter.trim().isEmpty()) {
            sql.append("AND s.shopName COLLATE Latin1_General_CI_AI LIKE ? ");
        }

        sql.append("GROUP BY s.shopID, s.shopName ");
        sql.append("ORDER BY s.shopName");

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
                    int shopId = rs.getInt("shopID");
                    String shopName = rs.getString("shopName");

                    BigDecimal totalValue = rs.getBigDecimal("totalValue");
                    shopValues.add(new ShopTotalValueDto(shopId, shopName, totalValue));
                }
            }
        }
        return shopValues;
    }

}
