/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Salary;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author ADMIN
 */
public class SalaryDAO {

    private Connection connection;

    public SalaryDAO(Connection connection) {
        this.connection = connection;
    }

    public BigDecimal getHourlyRateByEmployeeID(int empId) {
        String sql = "SELECT HourlyRate FROM SalarySetting WHERE EmployeeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal rate = rs.getBigDecimal("HourlyRate");
                    return rate != null ? rate : BigDecimal.ZERO;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getWorkHoursOfEmployeeInPeriod(int empId, Date start, Date end) {
        String sql = "SELECT SUM(WorkHours) AS TotalHours FROM WorkSchedule "
                + "WHERE EmployeeID = ? AND WorkDate BETWEEN ? AND ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setDate(2, new java.sql.Date(start.getTime()));
            ps.setDate(3, new java.sql.Date(end.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("TotalHours");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public int insertSalaryAndReturnID(Salary s) {
        String sql = "INSERT INTO Salary (SalaryName, SalaryPeriod, WorkPeriodStart, WorkPeriodEnd, TotalSalary, Status, CreatedDate, CreatedBy) "
                + "OUTPUT INSERTED.SalaryID "
                + "VALUES (?, ?, ?, ?, ?, ?, GETDATE(), ?)";
        int salaryID = -1;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, s.getSalaryName());
            ptm.setString(2, s.getSalaryPeriod());
            ptm.setDate(3, (Date) s.getWorkPeriodStart());
            ptm.setDate(4, (Date) s.getWorkPeriodEnd());
            ptm.setBigDecimal(5, s.getTotalSalary());
            ptm.setString(6, s.getStatus());
            ptm.setString(7, s.getCreatedBy());

            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                salaryID = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return salaryID;
    }

    public int updateTotalSalary(int salaryID, BigDecimal total) {
        String sql = "UPDATE Salary SET TotalSalary = ? WHERE SalaryID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setBigDecimal(1, total);
            ptm.setInt(2, salaryID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public Vector<Salary> getAllSalary(String sql) {
        Vector<Salary> listSalary = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Salary p = new Salary(
                        rs.getInt("SalaryID"),
                        rs.getString("SalaryName"),
                        rs.getString("SalaryPeriod"),
                        rs.getDate("WorkPeriodStart"),
                        rs.getDate("WorkPeriodEnd"),
                        rs.getBigDecimal("TotalSalary"),
                        rs.getString("Status"),
                        rs.getDate("CreatedDate"),
                        rs.getString("CreatedBy")
                );
                listSalary.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listSalary;
    }

    public int insertSalary(Salary p) {
        String sql = "INSERT INTO Salary (SalaryName, SalaryPeriod, WorkPeriodStart, WorkPeriodEnd, TotalSalary, Status, CreatedDate, CreatedBy) VALUES (?, ?, ?, ?, ?, ?, GETDATE(),?)";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getSalaryName());
            ptm.setString(2, p.getSalaryPeriod());
            ptm.setDate(3, new java.sql.Date(p.getWorkPeriodStart().getTime()));
            ptm.setDate(4, new java.sql.Date(p.getWorkPeriodEnd().getTime()));
            ptm.setBigDecimal(5, p.getTotalSalary());
            ptm.setString(6, p.getStatus());
            ptm.setString(7, p.getCreatedBy());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int updateSalary(Salary p) {
        String sql = "UPDATE Salary SET SalaryName = ?, SalaryPeriod = ?, WorkPeriodStart = ?, WorkPeriodEnd = ?, TotalSalary = ?, Status = ? WHERE SalaryID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getSalaryName());
            ptm.setString(2, p.getSalaryPeriod());
            ptm.setDate(3, new java.sql.Date(p.getWorkPeriodStart().getTime()));
            ptm.setDate(4, new java.sql.Date(p.getWorkPeriodEnd().getTime()));
            ptm.setBigDecimal(5, p.getTotalSalary());
            ptm.setString(6, p.getStatus());
            ptm.setInt(7, p.getSalaryID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteSalary(int salaryID) {
        String sql = "DELETE FROM Salary WHERE SalaryID = ?";
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

    public Salary searchSalary(int salaryID) {
        String sql = "SELECT * FROM Salary WHERE SalaryID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, salaryID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                Salary p = new Salary(
                        rs.getInt("SalaryID"),
                        rs.getString("SalaryName"),
                        rs.getString("SalaryPeriod"),
                        rs.getDate("WorkPeriodStart"),
                        rs.getDate("WorkPeriodEnd"),
                        rs.getBigDecimal("TotalSalary"),
                        rs.getString("Status"),
                        rs.getDate("CreatedDate"),
                        rs.getString("CreatedBy")
                );
                return p;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Vector<Salary> getSalaryByStatus(String status) {
        String sql = "SELECT * FROM Salary WHERE Status = ? ORDER BY WorkPeriodStart DESC";
        Vector<Salary> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, status);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Salary p = new Salary(
                        rs.getInt("SalaryID"),
                        rs.getString("SalaryName"),
                        rs.getString("SalaryPeriod"),
                        rs.getDate("WorkPeriodStart"),
                        rs.getDate("WorkPeriodEnd"),
                        rs.getBigDecimal("TotalSalary"),
                        rs.getString("Status"),
                        rs.getDate("CreatedDate"),
                        rs.getString("CreatedBy")
                );
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
            SalaryDAO dao = new SalaryDAO(connection.getConnection());
            Vector<Salary> list = dao.getAllSalary("SELECT * FROM Salary");
            for (Salary s : list) {
                System.out.println("ID: " + s.getSalaryID()
                        + " | Name: " + s.getSalaryName()
                        + " | Period: " + s.getSalaryPeriod()
                        + " | Start: " + s.getWorkPeriodStart()
                        + " | End: " + s.getWorkPeriodEnd()
                        + " | Total: " + s.getTotalSalary()
                        + " | Status: " + s.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
