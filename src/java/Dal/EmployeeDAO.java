/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.SQLException;

import DTO.EmployeeDto;
import DTO.SalesEmployeeStatisticDto;
import DTO.SoldProductDetailDto;
import java.sql.*;
import java.util.Date;

import Models.*;
import static Utils.PasswordUtils.checkPassword;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Employee> getEmployeesByRoleAndShop(int shopId, List<Integer> roleIds) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        if (roleIds == null || roleIds.isEmpty()) {
            return employees;
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM Employee WHERE ShopID = ? AND RoleID IN (");
        for (int i = 0; i < roleIds.size(); i++) {
            sql.append("?");
            if (i < roleIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            stmt.setInt(1, shopId);
            for (int i = 0; i < roleIds.size(); i++) {
                stmt.setInt(i + 2, roleIds.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("FullName"));
                    emp.setEmail(rs.getString("Email"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreatedDate"));
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    employees.add(emp);
                }
            }
        }

        return employees;
    }

    public List<SalesEmployeeStatisticDto> getSalesStatisticsByEmployeeIds(
            List<Integer> employeeIds, Integer shopId, Date startDate, Date endDate,
            int currentPage, int recordsPerPage) throws SQLException {

        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        if (employeeIds == null || employeeIds.isEmpty()) {
            return statistics;
        }

        StringBuilder sql = new StringBuilder("SELECT E.EmployeeID, E.FullName, ");
        sql.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) AS TotalRevenue, ");
        sql.append("ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, ");
        sql.append("ISNULL(SUM(ID.Quantity), 0) AS TotalProductsSold, ");
        sql.append("CASE WHEN ISNULL(COUNT(DISTINCT I.InvoiceID), 0) > 0 THEN ");
        sql.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) / CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ");
        sql.append("ELSE 0 END AS AverageRevenuePerOrder ");
        sql.append("FROM Employee E ");
        sql.append("LEFT JOIN Invoice I ON (E.EmployeeID = I.EmployeeID OR E.EmployeeID = I.SaleEmployeeID)");
        sql.append("LEFT JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");
        sql.append("WHERE 1=1 ");
        sql.append("AND I.Status = 1 ");

        if (shopId != null) {
            sql.append("AND E.ShopID = ? ");
        }
        sql.append("AND E.EmployeeID IN (");
        for (int i = 0; i < employeeIds.size(); i++) {
            sql.append("?");
            if (i < employeeIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") ");

        if (startDate != null) {
            sql.append("AND I.InvoiceDate >= ? ");
        }
        if (endDate != null) {
            sql.append("AND I.InvoiceDate < DATEADD(day, 1, ?) ");
        }
        sql.append("GROUP BY E.EmployeeID, E.FullName ");
        sql.append("ORDER BY E.EmployeeID ");
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        System.out.println("SQL getSalesStatisticsByEmployeeIds: " + sql.toString());
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (shopId != null) {
                ps.setInt(paramIndex++, shopId);
            }
            for (Integer empId : employeeIds) {
                ps.setInt(paramIndex++, empId);
            }
            if (startDate != null) {
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(startDate.getTime()));
            }
            if (endDate != null) {
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(endDate.getTime()));
            }

            ps.setInt(paramIndex++, (currentPage - 1) * recordsPerPage);
            ps.setInt(paramIndex++, recordsPerPage);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SalesEmployeeStatisticDto dto = new SalesEmployeeStatisticDto();
                    dto.setEmployeeID(rs.getInt("EmployeeID"));
                    dto.setFullName(rs.getString("FullName"));
                    dto.setTotalRevenue(rs.getBigDecimal("TotalRevenue"));
                    dto.setTotalOrders(rs.getInt("TotalOrders"));
                    dto.setTotalProductsSold(rs.getInt("TotalProductsSold"));
                    dto.setAverageRevenuePerOrder(rs.getBigDecimal("AverageRevenuePerOrder"));
                    statistics.add(dto);
                }
            }
        }
        return statistics;
    }

    public List<SalesEmployeeStatisticDto> getAllSalesStatisticsByEmployeeIdsForExcel(
            List<Integer> employeeIds, Integer shopId, Date startDate, Date endDate) throws SQLException {

        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        if (employeeIds == null || employeeIds.isEmpty()) {
            return statistics;
        }

        StringBuilder sql = new StringBuilder("SELECT E.EmployeeID, E.FullName, ");
        sql.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) AS TotalRevenue, ");
        sql.append("ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, ");
        sql.append("ISNULL(SUM(ID.Quantity), 0) AS TotalProductsSold, ");
        sql.append("CASE WHEN ISNULL(COUNT(DISTINCT I.InvoiceID), 0) > 0 THEN ");
        sql.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) / CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ");
        sql.append("ELSE 0 END AS AverageRevenuePerOrder ");
        sql.append("FROM Employee E ");
        sql.append("LEFT JOIN Invoice I ON E.EmployeeID = I.EmployeeID OR E.EmployeeID = I.SaleEmployeeID ");
        sql.append("LEFT JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");
        sql.append("WHERE I.Status = 1 ");

        if (shopId != null) {
            sql.append("AND E.ShopID = ? ");
        }

        sql.append("AND E.EmployeeID IN (");
        for (int i = 0; i < employeeIds.size(); i++) {
            sql.append("?");
            if (i < employeeIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") ");

        if (startDate != null) {
            sql.append("AND I.InvoiceDate >= ? ");
        }
        if (endDate != null) {
            sql.append("AND I.InvoiceDate < DATEADD(day, 1, ?) ");
        }

        sql.append("GROUP BY E.EmployeeID, E.FullName ");
        sql.append("ORDER BY E.EmployeeID");

        System.out.println("SQL getAllSalesStatisticsByEmployeeIds: " + sql.toString());

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (shopId != null) {
                ps.setInt(paramIndex++, shopId);
            }
            for (Integer empId : employeeIds) {
                ps.setInt(paramIndex++, empId);
            }
            if (startDate != null) {
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(startDate.getTime()));
            }
            if (endDate != null) {
                ps.setTimestamp(paramIndex++, new java.sql.Timestamp(endDate.getTime()));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SalesEmployeeStatisticDto dto = new SalesEmployeeStatisticDto();
                    dto.setEmployeeID(rs.getInt("EmployeeID"));
                    dto.setFullName(rs.getString("FullName"));
                    dto.setTotalRevenue(rs.getBigDecimal("TotalRevenue"));
                    dto.setTotalOrders(rs.getInt("TotalOrders"));
                    dto.setTotalProductsSold(rs.getInt("TotalProductsSold"));
                    dto.setAverageRevenuePerOrder(rs.getBigDecimal("AverageRevenuePerOrder"));
                    statistics.add(dto);
                }
            }
        }
        return statistics;
    }

    public int getTotalSalesStatisticsCountByEmployeeIds(
            List<Integer> employeeIds, Integer shopId, Date startDate, Date endDate) throws SQLException {

        if (employeeIds == null || employeeIds.isEmpty()) {
            return 0;
        }

        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();

        query.append("SELECT COUNT(DISTINCT E.EmployeeID) ");
        query.append("FROM Employee E ");
        query.append("LEFT JOIN Invoice I ON E.EmployeeID = I.EmployeeID OR E.EmployeeID = I.SaleEmployeeID ");
        query.append("LEFT JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");
        query.append("WHERE 1=1 ");
        query.append(" AND I.Status = 1 ");

        if (shopId != null) {
            query.append(" AND E.ShopID = ? ");
            params.add(shopId);
        }

        query.append(" AND E.EmployeeID IN (");
        for (int i = 0; i < employeeIds.size(); i++) {
            query.append("?");
            if (i < employeeIds.size() - 1) {
                query.append(",");
            }
        }
        query.append(") ");
        params.addAll(employeeIds);

        if (startDate != null) {
            query.append(" AND I.InvoiceDate >= ? ");
            params.add(new java.sql.Timestamp(startDate.getTime()));
        }
        if (endDate != null) {
            query.append(" AND I.InvoiceDate <= ? ");
            params.add(new java.sql.Timestamp(endDate.getTime()));
        }

        System.out.println("SQL getTotalSalesStatisticsCountByEmployeeIds: " + query.toString());
        System.out.println("Parameters: " + params.toString());

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof java.util.Date) {
                    ps.setDate(i + 1, new java.sql.Date(((java.util.Date) params.get(i)).getTime()));
                } else {
                    ps.setObject(i + 1, params.get(i));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy tổng số lượng thống kê doanh số: " + e.getMessage(), e);
            throw e;
        }
        return 0;
    }

    public List<Employee> searchEmployeesByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE FullName LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("FullName"));
                    emp.setEmail(rs.getString("Email"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreatedDate"));
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    employees.add(emp);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error searching employees by name: " + ex.getMessage());
            ex.printStackTrace();
        }

        return employees;
    }

    public Employee getEmployeeByID(int id) {
        String sql = "SELECT * FROM Employee WHERE EmployeeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("FullName"));
                    emp.setEmail(rs.getString("Email"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreatedDate"));
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    return emp;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getEmployeeByID: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public List<SoldProductDetailDto> getSoldProductDetailsByEmployee(
            Integer employeeId, Integer shopId, Date startDate, Date endDate,
            int currentPage, int recordsPerPage) throws SQLException {

        List<SoldProductDetailDto> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT P.ProductID, P.ProductName, SUM(ID.Quantity) AS QuantitySold, ");

        sql.append("AVG(ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS UnitPrice, ");
        sql.append("SUM(ID.Quantity * ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS Amount ");
        sql.append("FROM Product P ");
        sql.append("JOIN InvoiceDetail ID ON P.ProductID = ID.ProductID ");
        sql.append("JOIN Invoice I ON ID.InvoiceID = I.InvoiceID ");
        sql.append("WHERE I.SaleEmployeeID = ? ");
        params.add(employeeId);

        sql.append(" AND I.Status = 1 ");

        if (shopId != null) {
            sql.append(" AND I.ShopID = ? ");
            params.add(shopId);
        }

        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(new java.sql.Date(startDate.getTime()));
            params.add(new java.sql.Date(endDate.getTime()));
        }

        sql.append(" GROUP BY P.ProductID, P.ProductName ");

        sql.append(" ORDER BY P.ProductName ");

        int offset = (currentPage - 1) * recordsPerPage;
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(recordsPerPage);

        System.out.println("SQL Product Detail Query (getSoldProductDetailsByEmployee): " + sql.toString());
        System.out.println("Product Detail Parameters: " + params.toString());

        try (PreparedStatement ps = this.connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new SoldProductDetailDto(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getInt("QuantitySold"),
                            rs.getBigDecimal("UnitPrice"),
                            rs.getBigDecimal("Amount")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy chi tiết sản phẩm đã bán: " + ex.getMessage(), ex);
            throw ex;
        }
        return list;
    }

    public int getTotalSalesStatisticsCount(
            Integer employeeIdFilter, Integer shopIdFilter, Date startDate, Date endDate,
            List<Integer> roleIdFilters) throws SQLException {

        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();

        query.append("SELECT COUNT(DISTINCT E.EmployeeID) ");
        query.append("FROM Employee E ");

        query.append("INNER JOIN Invoice I ON (E.EmployeeID = I.EmployeeID OR E.EmployeeID = I.SaleEmployeeID) ");
        query.append("INNER JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");

        query.append("WHERE 1=1 ");
        query.append(" AND I.Status = 1 ");

        if (shopIdFilter != null) {
            query.append(" AND E.ShopID = ? ");
            params.add(shopIdFilter);
        }

        if (employeeIdFilter != null) {
            query.append(" AND E.EmployeeID = ? ");
            params.add(employeeIdFilter);
        } else if (roleIdFilters != null && !roleIdFilters.isEmpty()) {
            query.append(" AND E.RoleID IN (");
            for (int i = 0; i < roleIdFilters.size(); i++) {
                query.append("?");
                if (i < roleIdFilters.size() - 1) {
                    query.append(",");
                }
            }
            query.append(") ");
            params.addAll(roleIdFilters);
        }

        if (startDate != null) {

            query.append(" AND I.InvoiceDate >= ? ");
            params.add(new java.sql.Timestamp(startDate.getTime()));
        }
        if (endDate != null) {
            query.append(" AND I.InvoiceDate < DATEADD(day, 1, ?) ");
            params.add(new java.sql.Timestamp(endDate.getTime()));
        }

        System.out.println("SQL getTotalSalesStatisticsCount (for specific employee/role): " + query.toString());
        System.out.println("Parameters: " + params.toString());
        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof java.util.Date) {
                    ps.setDate(i + 1, new java.sql.Date(((java.util.Date) params.get(i)).getTime()));
                } else {
                    ps.setObject(i + 1, params.get(i));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy tổng số lượng thống kê doanh số: " + e.getMessage(), e);
            throw e;
        }
        return 0;
    }

    public List<SalesEmployeeStatisticDto> getSalesStatisticsBySpecificEmployeeIdsForExcel(
            Integer shopIdFilter, java.util.Date startDate, java.util.Date endDate,
            List<Integer> employeeIdsToFilter) throws SQLException {

        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        StringBuilder sqlSummary = new StringBuilder();
        List<Object> paramsSummary = new ArrayList<>(); // Sử dụng để lưu trữ tham số theo thứ tự

        sqlSummary.append("SELECT e.EmployeeID, e.FullName, ");
        sqlSummary.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) AS TotalRevenue, ");
        sqlSummary.append("ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, ");
        sqlSummary.append("ISNULL(SUM(ID.Quantity), 0) AS TotalProductsSold, ");
        sqlSummary.append("CASE WHEN ISNULL(COUNT(DISTINCT I.InvoiceID), 0) > 0 THEN ");
        sqlSummary.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) / CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ");
        sqlSummary.append("ELSE 0 END AS AverageRevenuePerOrder ");
        sqlSummary.append("FROM Employee e ");

        // Cập nhật JOIN để đảm bảo lọc chính xác hóa đơn có trạng thái = 1
        sqlSummary.append("LEFT JOIN Invoice I ON (e.EmployeeID = I.EmployeeID OR e.EmployeeID = I.SaleEmployeeID) AND I.Status = 1 ");
        sqlSummary.append("LEFT JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");

        sqlSummary.append("WHERE 1=1 ");

        // Lọc theo Shop ID
        if (shopIdFilter != null) {
            sqlSummary.append(" AND e.ShopID = ?");
            paramsSummary.add(shopIdFilter);
        }

        // Lọc theo danh sách Employee ID
        if (employeeIdsToFilter != null && !employeeIdsToFilter.isEmpty()) {
            sqlSummary.append(" AND e.EmployeeID IN ("); // <-- DÙNG e.EmployeeID
            for (int i = 0; i < employeeIdsToFilter.size(); i++) {
                sqlSummary.append("?");
                // Thêm tham số ID vào danh sách paramsSummary
                paramsSummary.add(employeeIdsToFilter.get(i));
                if (i < employeeIdsToFilter.size() - 1) {
                    sqlSummary.append(",");
                }
            }
            sqlSummary.append(") ");
        } else {
            // Xử lý trường hợp danh sách employeeIdsToFilter rỗng hoặc null, 
            // có thể return rỗng hoặc throw exception tùy ý đồ thiết kế.
            // Nếu không có EmployeeID nào để lọc, truy vấn sẽ không trả về gì.
            return new ArrayList<>();
        }

        // Lọc theo ngày bắt đầu (InvoiceDate >= startDate)
        if (startDate != null) {
            sqlSummary.append(" AND I.InvoiceDate >= ?");
            paramsSummary.add(new java.sql.Timestamp(startDate.getTime()));
        }
        // Lọc theo ngày kết thúc (InvoiceDate < endDate + 1 ngày)
        if (endDate != null) {
            sqlSummary.append(" AND I.InvoiceDate < DATEADD(day, 1, ?)");
            paramsSummary.add(new java.sql.Timestamp(endDate.getTime()));
        }

        sqlSummary.append(" GROUP BY e.EmployeeID, e.FullName ");
        sqlSummary.append(" ORDER BY e.EmployeeID "); // Giữ thứ tự nhất quán

        System.out.println("SQL getSalesStatisticsBySpecificEmployeeIdsForExcel: " + sqlSummary.toString());
        System.out.println("Parameters: " + paramsSummary.toString());

        try (PreparedStatement psSummary = this.connection.prepareStatement(sqlSummary.toString())) {
            int paramIndex = 1;
            for (Object param : paramsSummary) {
                if (param instanceof Integer) {
                    psSummary.setInt(paramIndex++, (Integer) param);
                } else if (param instanceof Timestamp) {
                    psSummary.setTimestamp(paramIndex++, (Timestamp) param);
                } else if (param instanceof java.sql.Date) { // Trường hợp bạn truyền java.sql.Date
                    psSummary.setDate(paramIndex++, (java.sql.Date) param);
                } else {
                    // Xử lý các kiểu dữ liệu khác nếu có, hoặc throw exception nếu không mong đợi
                    psSummary.setObject(paramIndex++, param);
                }
            }

            try (ResultSet rsSummary = psSummary.executeQuery()) {
                while (rsSummary.next()) {
                    SalesEmployeeStatisticDto dto = new SalesEmployeeStatisticDto(
                            rsSummary.getInt("EmployeeID"),
                            rsSummary.getString("FullName"),
                            rsSummary.getBigDecimal("TotalRevenue"),
                            rsSummary.getInt("TotalOrders"),
                            rsSummary.getInt("TotalProductsSold"),
                            rsSummary.getBigDecimal("AverageRevenuePerOrder")
                    );
                    statistics.add(dto);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy thống kê tổng hợp doanh số cho Excel theo Employee IDs: " + ex.getMessage(), ex);
            throw ex;
        }
        return statistics;
    }

    public List<SalesEmployeeStatisticDto> getSalesStatistics(
            Integer employeeIdFilter, Integer shopIdFilter, Date startDate, Date endDate,
            int currentPage, int recordsPerPage, List<Integer> roleIdFilters) throws SQLException {

        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        StringBuilder sqlSummary = new StringBuilder();
        List<Object> paramsSummary = new ArrayList<>();

        sqlSummary.append("SELECT e.EmployeeID, e.FullName, ");
        sqlSummary.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) AS TotalRevenue, ");
        sqlSummary.append("ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, ");
        sqlSummary.append("ISNULL(SUM(ID.Quantity), 0) AS TotalProductsSold, ");
        sqlSummary.append("CASE WHEN ISNULL(COUNT(DISTINCT I.InvoiceID), 0) > 0 THEN ");
        sqlSummary.append("ISNULL(SUM(ID.UnitPrice * ID.Quantity * (1 - ISNULL(ID.Discount, 0))), 0) / CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ");
        sqlSummary.append("ELSE 0 END AS AverageRevenuePerOrder ");
        sqlSummary.append("FROM Employee e ");

        sqlSummary.append("LEFT JOIN Invoice I ON (e.EmployeeID = I.EmployeeID OR e.EmployeeID = I.SaleEmployeeID) ");
        sqlSummary.append("LEFT JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");

        sqlSummary.append("WHERE 1=1 ");
        sqlSummary.append(" AND I.Status = 1 ");

        if (shopIdFilter != null) {
            sqlSummary.append(" AND e.ShopID = ?");
            paramsSummary.add(shopIdFilter);
        }

        if (employeeIdFilter != null) {
            sqlSummary.append(" AND e.EmployeeID = ?");
            paramsSummary.add(employeeIdFilter);
        } else if (roleIdFilters != null && !roleIdFilters.isEmpty()) {
            sqlSummary.append(" AND e.RoleID IN (");
            for (int i = 0; i < roleIdFilters.size(); i++) {
                sqlSummary.append("?");
                if (i < roleIdFilters.size() - 1) {
                    sqlSummary.append(",");
                }
            }
            sqlSummary.append(") ");
            paramsSummary.addAll(roleIdFilters);
        }

        if (startDate != null) {
            sqlSummary.append(" AND I.InvoiceDate >= ?");
            paramsSummary.add(new java.sql.Timestamp(startDate.getTime()));
        }
        if (endDate != null) {
            sqlSummary.append(" AND I.InvoiceDate < DATEADD(day, 1, ?)");
            paramsSummary.add(new java.sql.Timestamp(endDate.getTime()));
        }

        sqlSummary.append(" GROUP BY e.EmployeeID, e.FullName ");
        sqlSummary.append(" ORDER BY e.EmployeeID ");

        int offset = (currentPage - 1) * recordsPerPage;
        sqlSummary.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        paramsSummary.add(offset);
        paramsSummary.add(recordsPerPage);

        System.out.println("SQL getSalesStatistics: " + sqlSummary.toString());
        System.out.println("Parameters: " + paramsSummary.toString());

        try (PreparedStatement psSummary = this.connection.prepareStatement(sqlSummary.toString())) {
            for (int i = 0; i < paramsSummary.size(); i++) {
                if (paramsSummary.get(i) instanceof java.util.Date) {
                    psSummary.setDate(i + 1, new java.sql.Date(((java.util.Date) paramsSummary.get(i)).getTime()));
                } else {
                    psSummary.setObject(i + 1, paramsSummary.get(i));
                }
            }
            try (ResultSet rsSummary = psSummary.executeQuery()) {
                while (rsSummary.next()) {
                    SalesEmployeeStatisticDto dto = new SalesEmployeeStatisticDto(
                            rsSummary.getInt("EmployeeID"),
                            rsSummary.getString("FullName"),
                            rsSummary.getBigDecimal("TotalRevenue"),
                            rsSummary.getInt("TotalOrders"),
                            rsSummary.getInt("TotalProductsSold"),
                            rsSummary.getBigDecimal("AverageRevenuePerOrder")
                    );
                    statistics.add(dto);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy thống kê tổng hợp doanh số: " + ex.getMessage(), ex);
            throw ex;
        }
        return statistics;
    }

    public int getTotalPaidInvoicesByCashier(int cashierId, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Invoice "
                + "WHERE Status = 1 AND EmployeeID = ?";

        List<Object> params = new ArrayList<>();
        params.add(cashierId);

        if (startDate != null) {
            sql += " AND InvoiceDate >= ?";
            params.add(new java.sql.Timestamp(startDate.getTime()));
        }

        if (endDate != null) {
            sql += " AND InvoiceDate < DATEADD(day, 1, ?)";
            params.add(new java.sql.Timestamp(endDate.getTime()));
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof java.util.Date) {
                    ps.setTimestamp(i + 1, new java.sql.Timestamp(((Date) params.get(i)).getTime()));
                } else {
                    ps.setObject(i + 1, params.get(i));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Lỗi khi đếm hóa đơn đã thanh toán của Cashier: " + e.getMessage(), e);
            throw e;
        }

        return 0;
    }

    public List<Employee> getEmployeesByShopIdAndRoleId(int shopId, int roleId) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT e.*, r.RoleName FROM Employee e JOIN Role r ON e.RoleID = r.RoleID WHERE e.ShopID = ? AND e.RoleID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setInt(2, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("EmployeeID"));
                    employee.setUsername(rs.getString("Username"));
                    employee.setPassword(rs.getString("Password"));
                    employee.setFullname(rs.getString("FullName"));
                    employee.setEmail(rs.getString("Email"));
                    employee.setPhone(rs.getString("Phone"));
                    employee.setStatus(rs.getBoolean("Status"));
                    employee.setCreateDate(rs.getDate("CreatedDate"));
                    employee.setRoleId(rs.getInt("RoleID"));
                    employee.setShopId(rs.getInt("ShopID"));

                    Role role = new Role();
                    role.setId(rs.getInt("RoleID"));
                    role.setName(rs.getString("RoleName"));
                    employee.setRole(role);

                    employees.add(employee);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class
                    .getName()).log(Level.SEVERE, "Error getting employees by ShopID and RoleID", ex);
            throw ex;
        }
        return employees;
    }

    public List<Employee> getAllEmployee() throws SQLException {

        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.EmployeeID, e.FullName, e.RoleID, r.RoleName AS RoleName "
                + "FROM Employee e JOIN Role r ON e.RoleID = r.RoleID ORDER BY e.FullName";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("EmployeeID"));
                emp.setFullname(rs.getString("FullName"));
                emp.setRoleId(rs.getInt("RoleID"));

                Role role = new Role();
                role.setId(rs.getInt("RoleID"));
                role.setName(rs.getString("RoleName"));
                emp.setRole(role);

                employees.add(emp);
            }
            return employees;
        }
    }

    public List<Employee> getEmployee() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.EmployeeID, e.FullName, e.RoleID, e.ShopID, r.RoleName AS RoleName, s.ShopName "
                + "FROM Employee e "
                + "JOIN Role r ON e.RoleID = r.RoleID "
                + "JOIN Shop s ON s.ShopID = e.ShopID "
                + "ORDER BY e.FullName";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("EmployeeID"));
                emp.setFullname(rs.getString("FullName"));
                emp.setRoleId(rs.getInt("RoleID"));

                Role role = new Role();
                role.setId(rs.getInt("RoleID"));
                role.setName(rs.getString("RoleName")); // đảm bảo RoleName đúng với cột DB
                emp.setRole(role);

                emp.setShopId(rs.getInt("ShopID"));

                employees.add(emp);
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi khi lấy danh sách nhân viên: " + ex.getMessage());
            ex.printStackTrace();
        }
        return employees;
    }

    public boolean addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Username, Password, Fullname, Phone, Email, Status, CreatedDate, RoleId, ShopId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getUsername());
            stmt.setString(2, employee.getPassword());
            stmt.setString(3, employee.getFullname());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getEmail());
            stmt.setBoolean(6, employee.isStatus());
            stmt.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setInt(8, employee.getRoleId());
            stmt.setInt(9, employee.getShopId());
            stmt.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + ex.getStackTrace());
            return false;

        }
    }

    public Employee findEmployeeByUsernameAndPassword(String username, String plainPassword) throws SQLException {
        Employee employee = null;

        String sql = "SELECT e.EmployeeID, e.Username, e.Password, e.FullName, e.Phone, e.Email, e.Status, e.CreatedDate, e.RoleID, e.ShopID, r.RoleName "
                + "FROM Employee e JOIN Role r ON e.RoleID = r.RoleID "
                + "WHERE (e.Username = ? OR e.Email = ?) AND e.Status = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("Password");

                    // So sánh mật khẩu thô nhập vào với mật khẩu đã mã hóa trong DB
                    if (checkPassword(plainPassword, hashedPasswordFromDB)) {
                        employee = new Employee();
                        employee.setId(rs.getInt("EmployeeID"));
                        employee.setUsername(rs.getString("Username"));

                        employee.setFullname(rs.getString("FullName"));
                        employee.setPhone(rs.getString("Phone"));
                        employee.setEmail(rs.getString("Email"));
                        employee.setStatus(rs.getBoolean("Status"));
                        employee.setCreateDate(rs.getDate("CreatedDate"));
                        employee.setRoleId(rs.getInt("RoleID"));
                        employee.setShopId(rs.getInt("ShopID"));

                        Role role = new Role();
                        role.setId(rs.getInt("RoleID"));
                        role.setName(rs.getString("RoleName"));
                        employee.setRole(role);

                    }
                }
            }
        } catch (SQLException ex) {

            Logger.getLogger(EmployeeDAO.class
                    .getName()).log(Level.SEVERE, "Error in findEmployeeByUsernameAndPassword", ex);
            throw ex;
        }
        return employee;
    }

    public Employee findEmployeeByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Email = ? and Status = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setFullname(rs.getString("Fullname"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreatedDate")); // ⚠ Kiểm tra chính xác tên cột trong DB
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    return emp;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null; // Không tồn tại user hoặc sai mật khẩu
    }

    public void updatePasswordByEmail(String email, String hashedPassword) {
        String sql = "UPDATE Employee SET Password = ? WHERE Email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật mật khẩu người dùng", e);
        }
    }

    public List<Employee> getAllEmployeesByShopID(int shopId) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE ShopID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, shopId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("Fullname"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setEmail(rs.getString("Email"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreatedDate"));
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    employees.add(emp);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error retrieving employees by ShopID: " + ex.getMessage());
            ex.printStackTrace();
        }

        return employees;
    }

    public List<EmployeeDto> getEmployeesByPage(int page, int recordsPerPage, Integer shopId, Integer roleId, Boolean status, String sort, String keyword) throws SQLException {
        int offset = (page - 1) * recordsPerPage;
        StringBuilder query = new StringBuilder();
        query.append("SELECT e.EmployeeID, e.FullName, e.Email, e.Phone, e.Status, e.CreatedDate, s.ShopName, r.RoleName ")
                .append("FROM Employee e ")
                .append("JOIN Shop s ON e.ShopID = s.ShopID ")
                .append("JOIN Role r ON e.RoleID = r.RoleID ")
                .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (shopId != null) {
            query.append("AND e.ShopID = ? ");
            params.add(shopId);
        }
        if (roleId != null) {
            query.append("AND e.RoleID = ? ");
            params.add(roleId);
        }
        if (status != null) {
            query.append("AND e.Status = ? ");
            params.add(status);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            query.append("AND (e.FullName LIKE ? OR e.Email LIKE ?) ");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        // Xử lý sắp xếp
        if ("name_asc".equals(sort)) {
            query.append("ORDER BY e.FullName ASC ");
        } else if ("name_desc".equals(sort)) {
            query.append("ORDER BY e.FullName DESC ");
        } else {
            query.append("ORDER BY e.CreatedDate DESC ");
        }

        query.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(recordsPerPage);

        PreparedStatement ps = connection.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        List<EmployeeDto> list = new ArrayList<>();
        while (rs.next()) {
            EmployeeDto e = new EmployeeDto();
            e.setId(rs.getInt("EmployeeID"));
            e.setFullName(rs.getString("FullName"));
            e.setEmail(rs.getString("Email"));
            e.setPhone(rs.getString("Phone"));
            e.setStatus(rs.getBoolean("Status"));
            e.setCreatedDate(rs.getDate("CreatedDate"));
            e.setShopName(rs.getString("ShopName"));
            e.setRole(rs.getString("RoleName"));
            list.add(e);
        }
        return list;
    }

    public int getTotalEmployeeCount(Integer shopId, Integer roleId, Boolean status, String keyword) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM Employee e WHERE 1=1 AND e.RoleID <> 1");
        List<Object> params = new ArrayList<>();

        if (shopId != null) {
            query.append("AND e.ShopID = ? ");
            params.add(shopId);
        }
        if (roleId != null) {
            query.append("AND e.RoleID = ? ");
            params.add(roleId);
        }
        if (status != null) {
            query.append("AND e.Status = ? ");
            params.add(status);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            query.append("AND (e.FullName LIKE ? OR e.Email LIKE ?) ");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        PreparedStatement ps = connection.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public boolean updateEmployeeByUsername(Employee employee) throws SQLException {
        String sql = """
        UPDATE Employee
        SET Fullname = ?, Email = ?, Phone = ?
        WHERE Username = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getFullname());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getUsername());
            stmt.setBoolean(5, employee.isStatus());
            stmt.setInt(6, employee.getRole().getId());
            stmt.setInt(7, employee.getShop().getShopID());
            stmt.setInt(8, employee.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println("Lỗi khi cập nhật hồ sơ: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET FullName = ?, Email = ?, Phone = ?, Status = ?, RoleID = ?, ShopID = ? WHERE EmployeeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employee.getFullname());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getPhone());
            ps.setBoolean(4, employee.isStatus());
            ps.setInt(5, employee.getRoleId());
            ps.setInt(6, employee.getShopId());
            ps.setInt(7, employee.getId());
            ps.executeUpdate();
        }
    }

    public EmployeeDto getEmployeeProfileByUsername(String username) throws SQLException {
        String sql = """
        SELECT e.EmployeeID, e.Fullname, e.Email, e.Username, e.Phone, e.Status, e.CreatedDate,
               s.ShopName, r.RoleName
            FROM Employee e
            LEFT JOIN Shop s ON e.ShopID = s.ShopID
            JOIN Role r ON e.RoleID = r.RoleID
            WHERE e.Username = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDto(
                            rs.getInt("EmployeeID"),
                            rs.getString("Fullname"),
                            rs.getString("Email"),
                            rs.getString("Username"),
                            rs.getString("Phone"),
                            rs.getBoolean("Status"),
                            rs.getDate("CreatedDate"),
                            rs.getString("ShopName"),
                            rs.getString("RoleName")
                    );
                }
            }
        }
        return null;
    }

    public EmployeeDto getEmployeeById(int id) throws SQLException {
        String sql = "SELECT e.EmployeeID, e.FullName, e.Email, e.Phone, e.Status, e.RoleID, e.ShopID, r.RoleName, s.ShopName, e.CreatedDate "
                + "FROM Employee e JOIN Role r ON e.RoleID = r.RoleID JOIN Shop s ON e.ShopID = s.ShopID WHERE e.EmployeeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmployeeDto e = new EmployeeDto();
                    e.setId(rs.getInt("EmployeeID"));
                    e.setFullName(rs.getString("FullName"));
                    e.setEmail(rs.getString("Email"));
                    e.setPhone(rs.getString("Phone"));
                    e.setStatus(rs.getBoolean("Status"));
                    e.setRole(rs.getString("RoleName"));    // có thể thêm thuộc tính roleName
                    e.setShopName(rs.getString("ShopName")); // thêm thuộc tính shopName
                    e.setCreatedDate(rs.getDate("CreatedDate"));
                    return e;
                }
            }
        }
        return null;
    }

    public List<EmployeeDto> listAllEmployeeDTO() throws SQLException {
        List<EmployeeDto> list = new ArrayList<>();

        String sql = """
        SELECT e.EmployeeID, e.Fullname, e.Email, e.Username, e.Phone, e.Status, e.CreatedDate,
               s.ShopName, r.RoleName
        FROM Employee e
        LEFT JOIN Shop s ON e.ShopID = s.ShopID
        JOIN Role r ON e.RoleID = r.RoleID
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EmployeeDto emp = new EmployeeDto();
                emp.setId(rs.getInt("EmployeeID"));
                emp.setFullName(rs.getString("Fullname"));
                emp.setEmail(rs.getString("Email"));
                emp.setUsername(rs.getString("Username"));
                emp.setPhone(rs.getString("Phone"));
                emp.setStatus(rs.getBoolean("Status"));
                emp.setCreatedDate(rs.getDate("CreatedDate"));
                emp.setShopName(rs.getString("ShopName"));
                emp.setRole(rs.getString("RoleName"));
                list.add(emp);

            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM Employee WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Nếu có bản ghi trả về true
            }
        }
    }

    // Kiểm tra phone đã tồn tại chưa
    public boolean isPhoneExists(String phone) throws SQLException {
        String sql = "SELECT 1 FROM Employee WHERE phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Kiểm tra username đã tồn tại chưa
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM Employee WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean updateEmployeeStatus(int id, boolean status) throws SQLException {
        String sql = "UPDATE Employee SET status = ? WHERE EmployeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean checkPasswordEmployee(int employeeId, String plainPassword) throws SQLException {
        String sql = "SELECT Password FROM Employee WHERE EmployeeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("Password");
                    return checkPassword(plainPassword, hashedPassword);
                }
            }
        }
        return false;
    }

    public void updatePassword(int employeeId, String newHashedPassword) throws SQLException {
        String sql = "UPDATE Employee SET Password = ? WHERE EmployeeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newHashedPassword);
            ps.setInt(2, employeeId);
            ps.executeUpdate();
        }
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

    public String getRoleOfEmployee(int employeeId) throws SQLException {
        String roleName = null;
        String sql = "SELECT r.RoleName FROM Employee e JOIN Role r ON e.RoleID = r.RoleID WHERE e.EmployeeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    roleName = rs.getString("RoleName");
                }
            }
        }
        return roleName;
    }

  

}
