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
import java.sql.*;
import Models.*;
import static Utils.PasswordUtils.checkPassword;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    public List<Employee> searchEmployeesByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE FullName LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");  // Tìm kiếm có chứa tên
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

    public List<SalesEmployeeStatisticDto> getSalesStatisticsForSalesEmployees(int employeeId,
            Integer shopId,
            Date startDate,
            Date endDate,
            int currentPage,
            int recordsPerPage) throws SQLException {

        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e.EmployeeID, e.FullName, ");
        sql.append("ISNULL(SUM(ID.Quantity * ID.UnitPrice), 0) AS TotalRevenue, ");
        sql.append("ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, ");
        sql.append("CASE WHEN ISNULL(COUNT(DISTINCT I.InvoiceID), 0) > 0 THEN ISNULL(SUM(ID.Quantity * ID.UnitPrice), 0) / CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ELSE 0 END AS AverageRevenuePerOrder ");
        sql.append("FROM Employee e ");
        sql.append("LEFT JOIN Invoice I ON e.EmployeeID = I.EmployeeID ");
        sql.append("LEFT JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");
        sql.append("WHERE e.EmployeeID = ? "); // Lọc chính xác theo EmployeeID

        List<Object> params = new ArrayList<>();
        params.add(employeeId); // Thêm employeeId vào tham số đầu tiên

        if (shopId != null) {
            sql.append(" AND e.ShopID = ?");
            params.add(shopId);
        }
        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ?");
            params.add(startDate);
            params.add(endDate);
        }

        sql.append(" GROUP BY e.EmployeeID, e.FullName "); // Luôn group by để lấy tổng cho nhân viên đó
        sql.append(" ORDER BY e.EmployeeID "); // Order by cũng chỉ có 1 nhân viên nên không quá quan trọng
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        int offset = (currentPage - 1) * recordsPerPage;
        params.add(offset);
        params.add(recordsPerPage);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    statistics.add(new SalesEmployeeStatisticDto(
                            rs.getInt("EmployeeID"),
                            rs.getString("FullName"),
                            rs.getBigDecimal("TotalRevenue"),
                            rs.getInt("TotalOrders"),
                            rs.getBigDecimal("AverageRevenuePerOrder")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Error getSalesStatisticsForEmployee", ex);
            throw ex;
        }
        return statistics;
    }

    public int getTotalSalesStatisticsCount(Integer employeeIdFilter, Integer shopIdFilter, Date startDate, Date endDate, List<Integer> roleIdFilters) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(DISTINCT E.EmployeeID) ");
        query.append("FROM Employee E ");
        query.append("LEFT JOIN Invoice I ON E.EmployeeID = I.EmployeeID ");
        query.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        // Điều chỉnh để xử lý List of RoleIDs
        if (roleIdFilters != null && !roleIdFilters.isEmpty()) {
            query.append("AND E.RoleID IN (");
            for (int i = 0; i < roleIdFilters.size(); i++) {
                query.append("?");
                if (i < roleIdFilters.size() - 1) {
                    query.append(",");
                }
                params.add(roleIdFilters.get(i));
            }
            query.append(") ");
        }

        if (shopIdFilter != null) {
            query.append("AND E.ShopID = ? ");
            params.add(shopIdFilter);
        }

        if (employeeIdFilter != null) {
            query.append("AND E.EmployeeID = ? ");
            params.add(employeeIdFilter);
        }

        if (startDate != null && endDate != null) {
            query.append("AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(startDate);
            params.add(endDate);
        }

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Error getting total sales statistics count", e);
            throw e;
        }
        return 0;
    }

    public int getTotalSalesStatisticsCountForEmployee(Integer employeeId, Integer shopId, Date startDate, Date endDate) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(E.EmployeeID) "); // Chỉ cần đếm xem có tồn tại nhân viên này không
        query.append("FROM Employee E ");
        query.append("LEFT JOIN Invoice I ON E.EmployeeID = I.EmployeeID "); // Dùng LEFT JOIN để có thể lọc theo InvoiceDate
        query.append("WHERE E.EmployeeID = ? ");

        List<Object> params = new ArrayList<>();
        params.add(employeeId);

        if (shopId != null) {
            query.append("AND E.ShopID = ? ");
            params.add(shopId);
        }

        if (startDate != null && endDate != null) {
            // Điều kiện này sẽ lọc các nhân viên có hóa đơn trong khoảng ngày
            query.append("AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(startDate);
            params.add(endDate);
        }

        // Thêm GROUP BY để đảm bảo COUNT(E.EmployeeID) đếm đúng nếu có join
        // Tuy nhiên, vì WHERE E.EmployeeID = ?, kết quả chỉ có 1 hoặc 0.
        query.append("GROUP BY E.EmployeeID");

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    return 1;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy tổng số lượng thống kê cho nhân viên: ", e);
            throw e;
        }
        return 0;
    }

    public List<SalesEmployeeStatisticDto> getSalesStatistics(
            Integer employeeIdFilter, Integer shopIdFilter, Date startDate, Date endDate,
            int currentPage, int recordsPerPage, List<Integer> roleIdFilters) throws SQLException {

        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e.EmployeeID, e.FullName, ");
        sql.append("ISNULL(SUM(ID.Quantity * ID.UnitPrice), 0) AS TotalRevenue, ");
        sql.append("ISNULL(COUNT(DISTINCT I.InvoiceID), 0) AS TotalOrders, ");
        sql.append("CASE WHEN ISNULL(COUNT(DISTINCT I.InvoiceID), 0) > 0 THEN ISNULL(SUM(ID.Quantity * ID.UnitPrice), 0) / CAST(COUNT(DISTINCT I.InvoiceID) AS DECIMAL(18, 2)) ELSE 0 END AS AverageRevenuePerOrder ");
        sql.append("FROM Employee e ");
        sql.append("LEFT JOIN Invoice I ON e.EmployeeID = I.EmployeeID ");
        sql.append("LEFT JOIN InvoiceDetail ID ON I.InvoiceID = ID.InvoiceID ");
        sql.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        // Điều chỉnh để xử lý List of RoleIDs
        if (roleIdFilters != null && !roleIdFilters.isEmpty()) {
            sql.append(" AND e.RoleID IN (");
            for (int i = 0; i < roleIdFilters.size(); i++) {
                sql.append("?");
                if (i < roleIdFilters.size() - 1) {
                    sql.append(",");
                }
                params.add(roleIdFilters.get(i));
            }
            sql.append(") ");
        }

        if (shopIdFilter != null) {
            sql.append(" AND e.ShopID = ?");
            params.add(shopIdFilter);
        }

        if (employeeIdFilter != null) {
            sql.append(" AND e.EmployeeID = ?");
            params.add(employeeIdFilter);
        }

        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ?");
            params.add(startDate);
            params.add(endDate);
        }

        sql.append(" GROUP BY e.EmployeeID, e.FullName ");
        sql.append(" ORDER BY e.EmployeeID ");
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        int offset = (currentPage - 1) * recordsPerPage;
        params.add(offset);
        params.add(recordsPerPage);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    statistics.add(new SalesEmployeeStatisticDto(
                            rs.getInt("EmployeeID"),
                            rs.getString("FullName"),
                            rs.getBigDecimal("TotalRevenue"),
                            rs.getInt("TotalOrders"),
                            rs.getBigDecimal("AverageRevenuePerOrder")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Error getSalesStatistics", ex);
            throw ex;
        }
        return statistics;
    }

    public List<Employee> getAllStaffAndSelfEmployees(int loggedInEmployeeId, Integer loggedInEmployeeRoleId, Integer loggedInEmployeeShopId) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e.EmployeeID, e.FullName, e.RoleID, r.RoleName "); // Thay r.Name thành r.RoleName nếu cột là RoleName
        sql.append("FROM Employee e JOIN Role r ON e.RoleID = r.RoleID ");
        sql.append("WHERE e.EmployeeID = ? "); // Luôn bao gồm chính người dùng đang đăng nhập

        List<Object> params = new ArrayList<>();
        params.add(loggedInEmployeeId);

        if (loggedInEmployeeRoleId != null && loggedInEmployeeRoleId == 1) { // Admin
            sql.append("OR e.RoleID IN (?, ?) ");
            params.add(2);
            params.add(4);
        } else if (loggedInEmployeeRoleId != null && loggedInEmployeeRoleId == 3 && loggedInEmployeeShopId != null) {
            sql.append("OR (e.ShopID = ? AND e.RoleID IN (?, ?)) ");
            params.add(loggedInEmployeeShopId);
            params.add(2);
            params.add(4);
        }

        sql.append("ORDER BY e.FullName");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        }
        return employees;
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
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Error getting employees by ShopID and RoleID", ex);
            throw ex;
        }
        return employees;
    }

    public int getTotalSalesStatisticsCount(Integer targetEmployeeId, Integer shopId, Date startDate, Date endDate) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(DISTINCT E.EmployeeID) ");
        query.append("FROM Employee E ");
        query.append("LEFT JOIN Invoice I ON E.EmployeeID = I.EmployeeID "); // Dùng LEFT JOIN để đếm cả nhân viên chưa có hóa đơn
        query.append("WHERE E.RoleID = 2 "); // Chỉ Staff

        List<Object> params = new ArrayList<>();

        if (shopId != null) {
            query.append("AND E.ShopID = ? ");
            params.add(shopId);
        }

        if (targetEmployeeId != null) {
            query.append("AND E.EmployeeID = ? ");
            params.add(targetEmployeeId);
        }

        if (startDate != null && endDate != null) {
            query.append("AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(startDate);
            params.add(endDate);
        }

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Error getting total sales statistics count", e);
            throw e;
        }
        return 0;
    }

    public List<Employee> getAllEmployee() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.EmployeeID, e.FullName, e.RoleID, r.Name AS RoleName "
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

        String sql = "SELECT e.EmployeeID, e.Username, e.Password, e.FullName, e.Phone, e.Email, e.Status, e.CreatedDate, e.RoleID, e.ShopID, r.Name AS RoleName "
                + "FROM Employee e JOIN Role r ON e.RoleID = r.RoleID "
                + "WHERE e.Username = ? AND e.Status = 1";

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

            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Error in findEmployeeByUsernameAndPassword", ex);
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
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public static void main(String[] args) {
        // Tạo đối tượng DAO
        DBContext connection = new DBContext("SWP8");
        EmployeeDAO dao = new EmployeeDAO(connection.getConnection());

        // Gọi phương thức getAllEmployee()
    }

}
