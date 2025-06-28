/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Noti;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class NotiDAO {

    private Connection connection;

    public NotiDAO(Connection connection) {
        this.connection = connection;
    }

    public Vector<Noti> getAllNoti(String sql) {

        Vector<Noti> listNoti = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Noti p = new Noti(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getDate(6),
                        rs.getInt(7));

                listNoti.add(p);
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return listNoti;
    }

    public int insertNoti(Noti p) {
        String sql = "INSERT INTO [dbo].[Noti]\n"
                + "           ([Title]\n"
                + "           ,[Message]\n"
                + "           ,[Link]\n"
                + "           ,[ReceiverEmployeeID]\n"
                + "           ,[CreatedDate]\n"
                + "           ,[IsRead])\n"
                + "     VALUES (?, ?, ?, ?, GETDATE(),?)";

        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getTitle());
            ptm.setString(2, p.getMessage());
            ptm.setString(3, p.getLink());
            ptm.setInt(4, p.getReceiverEmployeeID());
            ptm.setInt(5, p.getIsRead());

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int updateNotiIsRead(int NotiID, int IsRead) {
        int result = 0;
        try {
            String sql = "UPDATE Noti SET IsRead = ? WHERE NotiID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, IsRead);
            ps.setInt(2, NotiID);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<Integer, Integer> MapListNotiDate() {
        String sql = "SELECT TOP 5\n"
                + "    NotiID,\n"
                + "    DATEDIFF(MINUTE, CreatedDate, GETDATE()) AS MinutesSinceCreated\n"
                + "	FROM Noti\n"
                + "	ORDER BY CreatedDate DESC;";
        Map<Integer, Integer> map = new HashMap<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
        DBContext connection = new DBContext("SWP8");
        // Tạo đối tượng DAO
        NotiDAO dao = new NotiDAO(connection.getConnection());

        String sql = "SELECT [NotiID]\n"
                + "      ,[Title]\n"
                + "      ,[Message]\n"
                + "      ,[Link]\n"
                + "      ,[ReceiverEmployeeID]\n"
                + "      ,[CreatedDate]\n"
                + "      ,[IsRead]\n"
                + "  FROM [dbo].[Noti]";

        Vector<Noti> notis = dao.getAllNoti(sql);

        System.out.println(notis.size());

        for (Noti n : notis) {
            System.out.println("ID: " + n.getNotiID());
            System.out.println("Tiêu đề: " + n.getTitle());
            System.out.println("Nội dung: " + n.getMessage());
            System.out.println("Link: " + n.getLink());
            System.out.println("Người nhận ID: " + n.getReceiverEmployeeID());
            System.out.println("Ngày tạo: " + n.getCreatedDate());
            System.out.println("Đã đọc: " + (n.getIsRead() == 1 ? "Đã đọc" : "Chưa đọc"));
            System.out.println("---------------------------");
        }
    }

}
