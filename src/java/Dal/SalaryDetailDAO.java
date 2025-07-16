/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.SalaryDetail;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author ADMIN
 */
public class SalaryDetailDAO {

    private Connection connection;

    public SalaryDetailDAO(Connection connection) {
        this.connection = connection;
    }

    public SalaryDetail getSalaryDetailByID(int salaryDetailID) {
        String sql = "SELECT * FROM SalaryDetail WHERE SalaryDetailID = ?";
        try {
            PreparedStatement pre = connection.prepareStatement(sql);
            pre.setInt(1, salaryDetailID);
            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                SalaryDetail detail = new SalaryDetail();
                detail.setSalaryDetailID(rs.getInt("SalaryDetailID"));
                detail.setSalaryID(rs.getInt("SalaryID"));
                detail.setEmployeeID(rs.getInt("EmployeeID"));
                detail.setBasicSalary(rs.getBigDecimal("BasicSalary"));
                return detail;
            }
        } catch (SQLException e) {
            System.out.println("Error in getSalaryDetailByID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal calculateTotalSalaryForPeriod(java.sql.Date start, java.sql.Date end) {
        String sql = "SELECT SUM(w.WorkHours * s.HourlyRate) AS TotalSalary "
                + "FROM WorkSchedule w "
                + "JOIN SalarySetting s ON w.EmployeeID = s.EmployeeID "
                + "WHERE w.WorkDate BETWEEN ? AND ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setDate(1, start);
            ptm.setDate(2, end);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("TotalSalary");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public Vector<SalaryDetail> getAllSalaryDetail(String sql) {
        Vector<SalaryDetail> listSalaryDetail = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                SalaryDetail p = new SalaryDetail(
                        rs.getInt("SalaryDetailID"),
                        rs.getInt("SalaryID"),
                        rs.getInt("EmployeeID"),
                        rs.getBigDecimal("BasicSalary")
                );
                listSalaryDetail.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listSalaryDetail;
    }

    public int insertSalaryDetail(SalaryDetail p) {
        String sql = "INSERT INTO SalaryDetail (SalaryID, EmployeeID, BasicSalary) VALUES (?, ?, ?)";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getSalaryID());
            ptm.setInt(2, p.getEmployeeID());
            ptm.setBigDecimal(3, p.getBasicSalary());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int updateSalaryDetail(SalaryDetail p) {
        String sql = "UPDATE SalaryDetail SET SalaryID = ?, EmployeeID = ?, BasicSalary = ? WHERE SalaryDetailID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getSalaryID());
            ptm.setInt(2, p.getEmployeeID());
            ptm.setBigDecimal(4, p.getBasicSalary());
            ptm.setInt(5, p.getSalaryDetailID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteSalaryDetail(int salaryDetailID) {
        String sql = "DELETE FROM SalaryDetail WHERE SalaryDetailID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryDetailID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public SalaryDetail searchSalaryDetail(int salaryDetailID) {
        String sql = "SELECT * FROM SalaryDetail WHERE SalaryDetailID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryDetailID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                SalaryDetail p = new SalaryDetail(
                        rs.getInt("SalaryDetailID"),
                        rs.getInt("SalaryID"),
                        rs.getInt("EmployeeID"),
                        rs.getBigDecimal("BasicSalary")
                );
                return p;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Vector<SalaryDetail> getSalaryDetailBySalaryID(int salaryID) {
        String sql = "SELECT * FROM SalaryDetail WHERE SalaryID = ? ORDER BY EmployeeID";
        Vector<SalaryDetail> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                SalaryDetail p = new SalaryDetail(
                        rs.getInt("SalaryDetailID"),
                        rs.getInt("SalaryID"),
                        rs.getInt("EmployeeID"),
                        rs.getBigDecimal("BasicSalary")
                );
                list.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public Vector<SalaryDetail> getSalaryDetailByEmployeeID(int employeeID) {
        String sql = "SELECT * FROM SalaryDetail WHERE EmployeeID = ? ORDER BY SalaryDetailID DESC";
        Vector<SalaryDetail> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, employeeID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                SalaryDetail p = new SalaryDetail(
                        rs.getInt("SalaryDetailID"),
                        rs.getInt("SalaryID"),
                        rs.getInt("EmployeeID"),
                        rs.getBigDecimal("BasicSalary")
                );
                list.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Thêm method để xóa tất cả salary detail theo salary ID
    public int deleteSalaryDetailBySalaryID(int salaryID) {
        String sql = "DELETE FROM SalaryDetail WHERE SalaryID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    // Thêm method để kiểm tra xem employee đã có trong bảng lương chưa
    public boolean isEmployeeInSalary(int salaryID, int employeeID) {
        String sql = "SELECT COUNT(*) FROM SalaryDetail WHERE SalaryID = ? AND EmployeeID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryID);
            ptm.setInt(2, employeeID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Thêm method để tính tổng lương theo salary ID
    public BigDecimal getTotalSalaryBySalaryID(int salaryID) {
        String sql = "SELECT SUM(BasicSalary) as TotalSalary FROM SalaryDetail WHERE SalaryID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("TotalSalary");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    // Thêm method để đếm số lượng nhân viên trong bảng lương
    public int getEmployeeCountBySalaryID(int salaryID) {
        String sql = "SELECT COUNT(*) as EmployeeCount FROM SalaryDetail WHERE SalaryID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return rs.getInt("EmployeeCount");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    // Thêm method để lấy salary detail với thông tin employee (JOIN query)
    public Vector<SalaryDetail> getSalaryDetailWithEmployeeInfo(int salaryID) {
        String sql = "SELECT sd.*, e.FullName, e.Email, e.Phone FROM SalaryDetail sd "
                + "LEFT JOIN Employee e ON sd.EmployeeID = e.EmployeeID "
                + "WHERE sd.SalaryID = ? ORDER BY sd.EmployeeID";
        Vector<SalaryDetail> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                SalaryDetail p = new SalaryDetail(
                        rs.getInt("SalaryDetailID"),
                        rs.getInt("SalaryID"),
                        rs.getInt("EmployeeID"),
                        rs.getBigDecimal("BasicSalary")
                );
                // Có thể thêm thông tin employee vào đây nếu cần
                list.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            DBContext connection = new DBContext("Test");
            SalaryDetailDAO dao = new SalaryDetailDAO(connection.getConnection());
            Vector<SalaryDetail> list = dao.getAllSalaryDetail("SELECT * FROM SalaryDetail");
            for (SalaryDetail s : list) {
                System.out.println("ID: " + s.getSalaryDetailID()
                        + " | SalaryID: " + s.getSalaryID()
                        + " | EmployeeID: " + s.getEmployeeID()
                        + " | BasicSalary: " + s.getBasicSalary());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
