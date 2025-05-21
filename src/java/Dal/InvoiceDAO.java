/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.Invoice;
import dal.DBContext;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.time.Instant;

/**
 *
 * @author duckh
 */
public class InvoiceDAO extends DBContext {

    public Vector<Invoice> getAllInvoices(String sql) {
        Vector<Invoice> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(new Invoice(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getTimestamp(5),
                        rs.getDouble(6),
                        rs.getString(7),
                        rs.getBoolean(8)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void addInvoice(Invoice i) {
        String sql = "INSERT INTO [dbo].[Invoice]\n"
                + "           ([InvoiceID]\n"
                + "           ,[CustomerID]\n"
                + "           ,[EmployeeID]\n"
                + "           ,[ShopID]\n"
                + "           ,[InvoiceDate]\n"
                + "           ,[TotalAmount]\n"
                + "           ,[Note]\n"
                + "           ,[Status])\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setString(1, i.getInvoiceID());
            ptm.setString(2, i.getCustomerID());
            ptm.setString(3, i.getEmployeeID());
            ptm.setString(4, i.getShopID());
            ptm.setTimestamp(5, Timestamp.from(Instant.now()));
            ptm.setDouble(6, i.getTotalAmount());
            ptm.setString(7, i.getNote());
            ptm.setBoolean(8, i.isStatus());
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        InvoiceDAO dao = new InvoiceDAO();

        Invoice newInvoice = new Invoice(
                "INV1003",
                "CUST004",
                "EMP002",
                "SHOP001",
                Timestamp.from(Instant.now()),
                2000000.0,
                "Mua quần áo mùa đông",
                true);
        dao.addInvoice(newInvoice);
        String sql = "SELECT *\n"
                + "  FROM [dbo].[Invoice]";
        Vector<Invoice> list = dao.getAllInvoices(sql);
        for (Invoice invoice : list) {
            System.out.println(invoice.getCustomerID());
        }
    }

}
