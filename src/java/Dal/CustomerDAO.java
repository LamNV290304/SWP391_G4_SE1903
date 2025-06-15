/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Customer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static void main(String[] args) {
        DBContext connection = new DBContext("SWP1");
        CustomerDAO dao = new CustomerDAO(connection.getConnection());

        List<Customer> l = dao.getAllCustomer();
        for (Customer customer : l) {
            System.out.println(customer.getCustomerName());
        }
    }
}
