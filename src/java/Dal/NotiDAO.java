/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.Noti;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.ResultSet;
/**
 *
 * @author ADMIN
 */
public class NotiDAO {

    private Connection connection;

    public NotiDAO(Connection connection) {
        this.connection = connection;
    }
    
    public Vector<Noti> getAllNoti(String sql)  {

        Vector<Noti> listNoti = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Noti p = new Noti(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getDate(5),
                        rs.getInt(6));

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
                + "     VALUES (?, ?, ?, ?, ?)";

        int n = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getTitle());
            ptm.setString(2, p.getMessage());
            ptm.setString(3, p.getLink());
            ptm.setInt(4, p.getReceiverEmployeeID());
            ptm.setDate(5, new java.sql.Date(p.getCreatedDate().getTime()));
            ptm.setInt(6, p.getIsRead());

            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

}
