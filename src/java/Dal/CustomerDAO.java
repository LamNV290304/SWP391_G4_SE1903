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
        String sql = "SELECT CustomerID, CustomerName, Phone, Email, Address, Status, CreatedDate, CreatedBy "
                + "FROM [dbo].[Customer] WHERE CustomerID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, customerID);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean updateCustomerStatus(int customerID, boolean status) {
        String sql = "UPDATE [dbo].[Customer] SET Status = ? WHERE CustomerID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setBoolean(1, status); 
            ptm.setInt(2, customerID);
            int affectedRows = ptm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Customer> searchCustomers(String keyword) {
        String sql = "SELECT * FROM [dbo].[Customer] "
                + "WHERE CustomerName  COLLATE Latin1_General_CI_AI LIKE ? OR Phone LIKE ? OR Email LIKE ?";
        List<Customer> customerList = new ArrayList<>();
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {

            ptm.setString(1, "%" + keyword + "%");
            ptm.setString(2, "%" + keyword + "%");
            ptm.setString(3, "%" + keyword + "%");

            try (ResultSet rs = ptm.executeQuery()) {
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
                    customerList.add(c);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerList;
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

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE [dbo].[Customer] SET CustomerName = ?, Phone = ?, Email = ?, Address = ?, Status = ?, CreatedDate = ?, CreatedBy = ? WHERE CustomerID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setString(1, customer.getCustomerName());
            ptm.setString(2, customer.getPhone());
            ptm.setString(3, customer.getEmail());
            ptm.setString(4, customer.getAddress());
            ptm.setBoolean(5, customer.isStatus());
            ptm.setTimestamp(6, customer.getCreatedDate());
            ptm.setString(7, customer.getCreatedBy());
            ptm.setInt(8, customer.getCustomerID());
            int affectedRows = ptm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
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
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                guestID = rs.getInt("CustomerID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return guestID;
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customer WHERE CustomerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        DBContext connection = new DBContext("SWP1");
        CustomerDAO dao = new CustomerDAO(connection.getConnection());

//        List<Customer> l = dao.searchCustomers("1269");
//        for (Customer customer : l) {
//            System.out.println(customer.getCustomerName());
//        }
        System.out.println("--- Bắt đầu Test Case 3: Xóa khách hàng ---");

      
        int idToDelete=31;
    
            boolean deleted = dao.deleteCustomer(idToDelete);

            if (deleted) {
                System.out.println("Test Case 3 thành công: Đã xóa khách hàng với ID: " + idToDelete);

                Customer checkCustomer = dao.getCustomerById(idToDelete);
                if (checkCustomer == null) {
                    System.out.println("Test Case 3: Kiểm tra lại thành công - Khách hàng ID " + idToDelete + " không còn tồn tại.");
                } else {
                    System.out.println("Test Case 3 LỖI: Khách hàng ID " + idToDelete + " vẫn tồn tại sau khi xóa.");
                }
            } else {
                System.out.println("Test Case 3 THẤT BẠI: Không thể xóa khách hàng với ID: " + idToDelete);
                
            }
        }

    }
