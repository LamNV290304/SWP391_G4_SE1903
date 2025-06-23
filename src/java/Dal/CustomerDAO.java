/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Customer;
import java.sql.Timestamp;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;

/**
 *
 * @author duckh
 */
public class CustomerDAO {

    private Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Customer> getAllCustomer() {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[Customer]";
        List<Customer> l = new ArrayList<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getBoolean("Status"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getString("CreatedBy"));
                l.add(c);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }

    public Customer getCustomerById(int customerID) {
        String sql = "SELECT [CustomerID]\n"
                + "      ,[CustomerName]\n"
                + "      ,[Phone]\n"
                + "      ,[Email]\n"
                + "      ,[Address]\n"
                + "      ,[Status]\n"
                + "      ,[CreatedDate]\n"
                + "      ,[CreatedBy]\n"
                + "  FROM [dbo].[Customer]\n"
                + "  WHERE CustomerID =?";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, customerID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                return new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getBoolean("Status"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getString("CreatedBy"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int addCustomer(Customer customer) {
        String sql = "INSERT INTO [dbo].[Customer]\n"
                + "           ([CustomerName]\n"
                + "           ,[Phone]\n"
                + "           ,[Email]\n"
                + "           ,[Address]\n"
                + "           ,[Status]\n"
                + "           ,[CreatedDate]\n"
                + "           ,[CreatedBy])\n"
                + "     VALUES\n"
                + "           (?,?,?,?,?,?,?)";
        int customerID = -1;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ptm.setString(1, customer.getCustomerName());
            ptm.setString(2, customer.getPhone());
            ptm.setString(3, customer.getEmail());
            ptm.setString(4, customer.getAddress());
            ptm.setBoolean(5, customer.isStatus());
            ptm.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ptm.setString(7, customer.getCreatedBy());

            int affectedRows = ptm.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = ptm.getGeneratedKeys();
                if (rs.next()) {
                    customerID = rs.getInt(1);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerID;
    }

    public Customer getCustomerByPhone(String phone) {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[Customer]\n"
                + "  WHERE Phone = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, phone);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                return new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getBoolean("Status"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getString("CreatedBy"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getGuestCustomerID() {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[Customer]\n"
                + "  WHERE CustomerName = N'Khách vãng lai'";
        int guestID = -1;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs= ptm.executeQuery();
            while(rs.next()){
                guestID = rs.getInt("CustomerID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return guestID;
    }

    public static void main(String[] args) {
        DBContext connection = new DBContext("SWP1");
        CustomerDAO dao = new CustomerDAO(connection.getConnection());

        List<Customer> l = dao.getAllCustomer();
        for (Customer customer : l) {
            System.out.println(customer.getCustomerName());
        }
    }
}
