package Dal;

import Context.DBContext;
import Models.WorkSchedule;
import java.sql.Connection;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.SQLException;

public class WorkScheduleDAO {

    private Connection connection;

    public WorkScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    public Vector<WorkSchedule> getAllWorkSchedule(String sql) {
        Vector<WorkSchedule> listWorkSchedule = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                WorkSchedule p = new WorkSchedule(
                        rs.getInt("WorkScheduleID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getInt("ShiftID"),
                        rs.getDate("WorkDate"),
                        rs.getInt("Status"),
                        rs.getString("Note"),
                        rs.getDate("CreatedDate"),
                        rs.getString("CreatedBy")
                );
                listWorkSchedule.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listWorkSchedule;
    }

    public int insertWorkSchedule(WorkSchedule p) {
        String sql = "INSERT INTO [dbo].[WorkSchedule] "
                + "([EmployeeID], [ShopID], [ShiftID], [WorkDate], [Status], [Note], [CreatedDate], [CreatedBy]) "
                + "VALUES (?, ?, ?, ?, ?, ?, GETDATE(), ?)";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getEmployeeID());
            ptm.setInt(2, p.getShopID());
            ptm.setInt(3, p.getShiftID());
            ptm.setDate(4, new java.sql.Date(p.getWorkDate().getTime()));
            ptm.setInt(5, p.getStatus());
            ptm.setString(6, p.getNote());
            ptm.setString(7, p.getCreatedBy());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int updateWorkSchedule(WorkSchedule p) {
        String sql = "UPDATE [dbo].[WorkSchedule] SET "
                + "[EmployeeID] = ?, "
                + "[ShopID] = ?, "
                + "[ShiftID] = ?, "
                + "[WorkDate] = ?, "
                + "[Status] = ?, "
                + "[Note] = ? "
                + "WHERE [WorkScheduleID] = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, p.getEmployeeID());
            ptm.setInt(2, p.getShopID());
            ptm.setInt(3, p.getShiftID());
            ptm.setDate(4, new java.sql.Date(p.getWorkDate().getTime()));
            ptm.setInt(5, p.getStatus());
            ptm.setString(6, p.getNote());
            ptm.setInt(7, p.getWorkScheduleID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteWorkSchedule(int WorkScheduleID) {
        String sql = "DELETE FROM [dbo].[WorkSchedule] WHERE WorkScheduleID = ?";
        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, WorkScheduleID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public boolean isScheduleExist(int employeeID, String workDate, int shiftID) {
        String sql = "SELECT COUNT(*) FROM [dbo].[WorkSchedule] "
                + "WHERE [EmployeeID] = ? AND [WorkDate] = ? AND [ShiftID] = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, employeeID);
            ptm.setString(2, workDate);
            ptm.setInt(3, shiftID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public WorkSchedule getWorkScheduleById(int workScheduleID) {
        String sql = "SELECT * FROM [dbo].[WorkSchedule] WHERE [WorkScheduleID] = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, workScheduleID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return new WorkSchedule(
                        rs.getInt("WorkScheduleID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getInt("ShiftID"),
                        rs.getDate("WorkDate"),
                        rs.getInt("Status"),
                        rs.getString("Note"),
                        rs.getDate("CreatedDate"),
                        rs.getString("CreatedBy")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Vector<WorkSchedule> getWorkScheduleByEmployee(int employeeID) {
        String sql = "SELECT * FROM [dbo].[WorkSchedule] WHERE [EmployeeID] = ? ORDER BY [WorkDate]";
        Vector<WorkSchedule> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, employeeID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                WorkSchedule p = new WorkSchedule(
                        rs.getInt("WorkScheduleID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getInt("ShiftID"),
                        rs.getDate("WorkDate"),
                        rs.getInt("Status"),
                        rs.getString("Note"),
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

    public Vector<WorkSchedule> getWorkScheduleByDate(String startDate, String endDate) {
        String sql = "SELECT * FROM [dbo].[WorkSchedule] "
                + "WHERE [WorkDate] BETWEEN ? AND ? ORDER BY [WorkDate], [EmployeeID]";
        Vector<WorkSchedule> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, startDate);
            ptm.setString(2, endDate);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                WorkSchedule p = new WorkSchedule(
                        rs.getInt("WorkScheduleID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getInt("ShiftID"),
                        rs.getDate("WorkDate"),
                        rs.getInt("Status"),
                        rs.getString("Note"),
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

    public Vector<WorkSchedule> getWorkScheduleByDateAndEmployee(int employeeID, String startDate, String endDate) {
        String sql = "SELECT * FROM [dbo].[WorkSchedule] "
                + "WHERE [EmployeeID] = ? AND [WorkDate] BETWEEN ? AND ? ORDER BY [WorkDate]";
        Vector<WorkSchedule> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, employeeID);
            ptm.setString(2, startDate);
            ptm.setString(3, endDate);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                WorkSchedule p = new WorkSchedule(
                        rs.getInt("WorkScheduleID"),
                        rs.getInt("EmployeeID"),
                        rs.getInt("ShopID"),
                        rs.getInt("ShiftID"),
                        rs.getDate("WorkDate"),
                        rs.getInt("Status"),
                        rs.getString("Note"),
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
        DBContext connection = new DBContext("Test");
        WorkScheduleDAO workScheduleDAO = new WorkScheduleDAO(connection.getConnection());
        try {
            

            String sql = "SELECT * FROM WorkSchedule";
            Vector<WorkSchedule> list = workScheduleDAO.getAllWorkSchedule(sql);

            // In kết quả
            for (WorkSchedule w : list) {
                System.out.println("ID: " + w.getWorkScheduleID()
                        + ", Employee: " + w.getEmployeeID()
                        + ", Shop: " + w.getShopID()
                        + ", Shift: " + w.getShiftID()
                        + ", Date: " + w.getWorkDate()
                        + ", Status: " + w.getStatus()
                        + ", Note: " + w.getNote());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
