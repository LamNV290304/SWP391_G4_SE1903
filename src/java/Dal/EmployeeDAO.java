/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import DTO.EmployeeDto;
import java.sql.*;
import Models.*;
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

    public Employee findEmployeeByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Username = ? AND Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("EmployeeID"));
                    emp.setUsername(rs.getString("Username"));
                    emp.setPassword(rs.getString("Password"));
                    emp.setFullname(rs.getString("Fullname"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setStatus(rs.getBoolean("Status"));
                    emp.setCreateDate(rs.getDate("CreateDate"));
                    emp.setRoleId(rs.getInt("RoleID"));
                    emp.setShopId(rs.getInt("ShopID"));
                    return emp;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy hoặc lỗi
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

    public List<EmployeeDto> getEmployeesByPage(int page, int recordsPerPage, Integer shopId, Boolean status, String sort, String keyword) throws SQLException {
        int offset = (page - 1) * recordsPerPage;
        StringBuilder query = new StringBuilder();
        query.append("SELECT e.EmployeeID, e.FullName, e.Email, e.Status, e.CreatedDate, s.ShopName ")
                .append("FROM Employee e JOIN Shop s ON e.ShopID = s.ShopID WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (shopId != null) {
            query.append("AND e.ShopID = ? ");
            params.add(shopId);
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

        // Xử lý sort
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
            e.setStatus(rs.getBoolean("Status"));
            e.setCreatedDate(rs.getDate("CreatedDate"));
            e.setShopName(rs.getString("ShopName"));
            list.add(e);
        }
        return list;
    }

    public int getTotalEmployeeCount(Integer shopId, Boolean status, String keyword) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM Employee e WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (shopId != null) {
            query.append("AND e.ShopID = ? ");
            params.add(shopId);
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
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET Password = ?, Fullname = ?, Phone = ?, Email = ?, Status = ?, RoleId = ?, ShopId = ? WHERE EmployeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getPassword());
            stmt.setString(2, employee.getFullname());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getEmail());
            stmt.setBoolean(5, employee.isStatus());
            stmt.setInt(6, employee.getRole().getId());
            stmt.setString(7, employee.getShop().getShopID());
            stmt.setInt(8, employee.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println("Update failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {

        try (Connection conn = DBContext.getConnection("ShopDB_TTest")) {
            EmployeeDAO dao = new EmployeeDAO(conn);

            // 🔢 Test getTotalEmployeeCount()
            //int total = dao.getTotalEmployeeCount();
            //System.out.println("🧮 Tổng số nhân viên: " + total);
            // 🔁 Test getEmployeesByPage(page, size)
            int page = 1;
            int size = 5;

            //List<EmployeeDto> list = dao.getEmployeesByPage(page, size);
            System.out.println("📋 Danh sách nhân viên (trang " + page + "):");
//            for (EmployeeDto e : list) {
//                System.out.println("- " + e.getFullName() + " | " + e.getEmail()
//                        + " | Shop: " + e.getShopName()
//                        + " | Status: " + (e.isStatus() ? "Active" : "Inactive"));
//            }

        } catch (SQLException ex) {
            System.err.println("❌ Lỗi kết nối SQL: " + ex.getMessage());
        }
    }
}
