/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import DTO.EmployeeDto;
import java.sql.*;
import Models.*;
import static Utils.PasswordUtils.checkPassword;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Username, Password, Fullname, Phone, Email, Status, CreateDate, RoleId, ShopId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getUsername());
            stmt.setString(2, employee.getPassword());
            stmt.setString(3, employee.getFullname());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getEmail());  // thêm email ở vị trí thứ 5
            stmt.setBoolean(6, employee.isStatus());
            stmt.setDate(7, new java.sql.Date(employee.getCreateDate().getTime()));
            stmt.setInt(8, employee.getRole().getId());
            stmt.setString(9, employee.getShop().getShopID());
            stmt.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + ex.getStackTrace());
            return false;
        }
    }

    public Employee findEmployeeByUsernameAndPassword(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Username = ?";
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

    public static void main(String[] args) throws ClassNotFoundException {

        try (Connection conn = DBContext.getConnection("ShopDB_TTest")) {
            EmployeeDAO dao = new EmployeeDAO(conn);

            // 🔢 Test getTotalEmployeeCount()
            //int total = dao.getTotalEmployeeCount();
            //System.out.println("🧮 Tổng số nhân viên: " + total);
            // 🔁 Test getEmployeesByPage(page, size)
            EmployeeDto employee = dao.getEmployeeById(5);

            //List<EmployeeDto> list = dao.getEmployeesByPage(page, size);
            System.out.println(employee);
        } catch (SQLException ex) {
            System.err.println("❌ Lỗi kết nối SQL: " + ex.getMessage());
        }
    }
}
