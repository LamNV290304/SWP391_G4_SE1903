/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;

import java.sql.Connection;
import java.sql.SQLException;

import DTO.EmployeeDto;
import DTO.SalesEmployeeStatisticDto;
import java.sql.*;
import Models.*;
import Utils.PasswordUtils;
import static Utils.PasswordUtils.checkPassword;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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

    public List<SalesEmployeeStatisticDto> getSalesStatisticsForSalesEmployees() throws SQLException {
        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        String sql = "SELECT "
                + "    E.EmployeeID, "
                + "    E.FullName, "
                + "    SUM(I.TotalAmount) AS TotalRevenue, "
                + "    COUNT(I.InvoiceID) AS TotalOrders "
                + "FROM "
                + "    Employee AS E "
                + "JOIN "
                + "    Invoice AS I ON E.EmployeeID = I.EmployeeID "
                + "WHERE "
                + "    E.RoleID = 2 "
                + "GROUP BY "
                + "    E.EmployeeID, E.FullName "
                + "ORDER BY "
                + "    TotalRevenue DESC";

        try (PreparedStatement ptm = connection.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {
            while (rs.next()) {

                SalesEmployeeStatisticDto stat = new SalesEmployeeStatisticDto(
                        rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getDouble("TotalRevenue"),
                        rs.getInt("TotalOrders")
                );
                statistics.add(stat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);

        }
        return statistics;
    }

    public List<SalesEmployeeStatisticDto> getSalesStatisticsForSalesEmployeesByDateRange(Date startDate, Date endDate) throws SQLException {
        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();
        String sql = "SELECT "
                + "    E.EmployeeID, "
                + "    E.FullName, "
                + "    SUM(I.TotalAmount) AS TotalRevenue, "
                + "    COUNT(I.InvoiceID) AS TotalOrders "
                + "FROM "
                + "    Employee AS E "
                + "JOIN "
                + "    Invoice AS I ON E.EmployeeID = I.EmployeeID "
                + "WHERE "
                + "    E.RoleID = 2 ";

        if (startDate != null) {
            sql += "    AND CAST(I.InvoiceDate AS DATE) >= ? ";
        }
        if (endDate != null) {
            sql += "    AND CAST(I.InvoiceDate AS DATE) <= ? ";
        }

        sql += "GROUP BY "
                + "    E.EmployeeID, E.FullName "
                + "ORDER BY "
                + "    TotalRevenue DESC";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            if (startDate != null) {
                ptm.setDate(paramIndex++, startDate);
            }
            if (endDate != null) {
                ptm.setDate(paramIndex++, endDate);
            }

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    SalesEmployeeStatisticDto stat = new SalesEmployeeStatisticDto(
                            rs.getInt("EmployeeID"),
                            rs.getString("FullName"),
                            rs.getDouble("TotalRevenue"),
                            rs.getInt("TotalOrders")
                    );
                    statistics.add(stat);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, "Error getting sales statistics for sales employees by date range (using CAST)", ex);
            throw ex;
        }
        return statistics;
    }

    public List<Employee> getAllEmployee() {
        List<Employee> l = new ArrayList<>();
        String sql = "SELECT e.[EmployeeID], e.[Username], e.[Password], e.[FullName], e.[Email], e.[Phone], "
                + "e.[RoleID], e.[ShopID], e.[Status], e.[CreatedDate], e.[CreatedBy], "
                + "r.RoleID AS Role_Id, r.RoleName AS Role_Name, r.Description AS Role_Description " // Lấy thông tin từ bảng Role
                + "FROM [dbo].[Employee] AS e "
                + "JOIN [dbo].[Role] AS r ON e.RoleID = r.RoleID"; // JOIN với bảng Role

        try (PreparedStatement ptm = connection.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {
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
                Role role = new Role();
                role.setId(rs.getInt("Role_Id")); 
                role.setName(rs.getString("Role_Name")); 
                role.setDescription(rs.getString("Role_Description")); 
                emp.setRole(role); 

                emp.setShopId(rs.getInt("ShopID"));
              
                l.add(emp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }

    public boolean addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Username, Password, Fullname, Phone, Email, Status, CreateDate, RoleId, ShopId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getUsername());
            stmt.setString(2, employee.getPassword());
            stmt.setString(3, employee.getFullname());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getEmail());
            stmt.setBoolean(6, employee.isStatus());
            stmt.setDate(7, new java.sql.Date(employee.getCreateDate().getTime()));
            stmt.setInt(8, employee.getRole().getId());
            stmt.setInt(9, employee.getShop().getShopID());
            stmt.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + ex.getStackTrace());
            return false;

        }
    }

    public Employee findEmployeeByUsernameAndPassword(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Username = ? and Status = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("Password");

                    // So sánh mật khẩu thô nhập vào với mật khẩu đã mã hóa trong DB
                    if (checkPassword(plainPassword, hashedPasswordFromDB)) {
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
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null; // Không tồn tại user hoặc sai mật khẩu
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
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM Employee e WHERE 1=1 ");
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

    public static void main(String[] args) {
        // Tạo đối tượng DAO
        DBContext connection = new DBContext("SWP8");
        EmployeeDAO dao = new EmployeeDAO(connection.getConnection());

        // Gọi phương thức getAllEmployee()
        List<Employee> employees = dao.getAllEmployee();

        // In kết quả ra màn hình
        for (Employee emp : employees) {
            System.out.println("ID: " + emp.getId());
            System.out.println("Username: " + emp.getUsername());
            System.out.println("Password: " + emp.getPassword());
            System.out.println("Fullname: " + emp.getFullname());
            System.out.println("Phone: " + emp.getPhone());
            System.out.println("Status: " + emp.isStatus());
            System.out.println("Created Date: " + emp.getCreateDate());
            System.out.println("Role ID: " + emp.getRoleId());
            System.out.println("Shop ID: " + emp.getShopId());
            System.out.println("----------");
        }
    }

}
