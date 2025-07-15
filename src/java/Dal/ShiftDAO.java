/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Shift;
import Models.WorkSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.Time;

/**
 *
 * @author ADMIN
 */
public class ShiftDAO {

    private Connection connection;

    public ShiftDAO(Connection connection) {
        this.connection = connection;
    }

    public Vector<Shift> getAllShift(String sql) {
        Vector<Shift> listShift = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                // Updated to include salary field (assuming it's the 6th column)
                Shift p = new Shift(rs.getInt(1), // shiftID
                        rs.getString(2), // shiftName
                        rs.getTime(3).toLocalTime(), // startTime
                        rs.getTime(4).toLocalTime(), // endTime
                        rs.getString(5), // description
                        rs.getInt(6));
                listShift.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listShift;
    }

    public int insertShift(Shift p) {
        String sql = "INSERT INTO Shift (ShiftName, StartTime, EndTime, Description, Salary, NumberOfEmployees) VALUES (?, ?, ?, ?, ?)";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getShiftName());
            ptm.setTime(2, Time.valueOf(p.getStartTime()));
            ptm.setTime(3, Time.valueOf(p.getEndTime()));
            ptm.setString(4, p.getDescription());
            ptm.setInt(5, p.getNumberOfEmployees());

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteShift(int ShiftID) {
        String sql = "DELETE FROM [dbo].[Shift] WHERE ShiftID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, ShiftID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public Shift searchShift(int ShiftID) {
        String sql = "SELECT * FROM [dbo].[Shift]\n"
                + "Where ShiftID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, ShiftID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                Shift p = new Shift(rs.getInt(1),
                        rs.getString(2),
                        rs.getTime(3).toLocalTime(),
                        rs.getTime(4).toLocalTime(),
                        rs.getString(5),
                        rs.getInt(6)
                );

                return p;
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            DBContext connection = new DBContext("Test");
            // Gọi DAO
            ShiftDAO dao = new ShiftDAO(connection.getConnection());
            // Gọi hàm lấy tất cả Shift - Updated SQL to include Salary
            Vector<Shift> list = dao.getAllShift("SELECT ShiftID, ShiftName, StartTime, EndTime, Description,NumberOfEmployees FROM Shift");
            // In kết quả
            for (Shift s : list) {
                System.out.println("ID: " + s.getShiftID()
                        + " | Name: " + s.getShiftName()
                        + " | Start: " + s.getStartTime()
                        + " | End: " + s.getEndTime()
                        + " | Desc: " + s.getDescription()
                        + " | NumberOfEmployees: " + s.getNumberOfEmployees());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
