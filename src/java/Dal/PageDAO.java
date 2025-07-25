/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import java.sql.*;

import Models.Pages;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class PageDAO {

    private Connection connection;

    public PageDAO(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Pages> getAllPages() throws SQLException {
        Map<String, Pages> pages = new LinkedHashMap<>();

        String sql = "SELECT * FROM Pages ORDER BY TRY_CAST([Group] AS INT);";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pages page = new Pages();
                page.setPageCode(rs.getString("PageCode"));
                page.setPagePath(rs.getString("PagePath"));
                page.setDisplayName(rs.getString("DisplayName"));

                pages.put(page.getPageCode(), page);
            }
        }
        return pages;
    }
}
