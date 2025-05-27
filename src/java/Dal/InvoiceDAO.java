/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.Invoice;
import java.sql.Connection;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 *
 * @author duckh
 */
public class InvoiceDAO {

    private Connection connection;

    public InvoiceDAO(Connection connection) {
        this.connection = connection;
    }

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

    public boolean deleteInvoice(String invoiceID) {
        Invoice invoice = searchInvoice(invoiceID);
        if (invoice == null) {
            System.out.println("Không tìm thấy hóa đơn: " + invoiceID);
            return false;
        }

        if (invoice.isStatus()) {
            System.out.println("Không thể xóa hóa đơn đã thanh toán");
            return false;
        }
        String deleteSql = "Delete\n"
                + "  FROM [dbo].[Invoice]\n"
                + "  Where InvoiceID = ?";

        try {
            PreparedStatement deletePtm = connection.prepareStatement(deleteSql);
            deletePtm.setString(1, invoiceID);
            int row = deletePtm.executeUpdate();
            return row > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public Invoice searchInvoice(String invoiceID) {
        String sql = "SELECT * FROM Invoice WHERE InvoiceID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, invoiceID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return new Invoice(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getTimestamp(5),
                        rs.getDouble(6), rs.getString(7), rs.getBoolean(8));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Vector<Invoice> searchInvoices(String invoiceID, String customerID, String employeeID, String shopID) {
        Vector<Invoice> list = new Vector<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Invoice WHERE 1=1");

        if (invoiceID != null && !invoiceID.isEmpty()) {
            sql.append(" AND InvoiceID LIKE ?");
        }
        if (customerID != null && !customerID.isEmpty()) {
            sql.append(" AND CustomerID LIKE ?");
        }
        if (employeeID != null && !employeeID.isEmpty()) {
            sql.append(" AND EmployeeID LIKE ?");
        }
        if (shopID != null && !shopID.isEmpty()) {
            sql.append(" AND ShopID LIKE ?");
        }

        try {
            PreparedStatement ptm = connection.prepareStatement(sql.toString());
            int index = 1;
            if (invoiceID != null && !invoiceID.isEmpty()) {
                ptm.setString(index++, "%" + invoiceID + "%");
            }
            if (customerID != null && !customerID.isEmpty()) {
                ptm.setString(index++, "%" + customerID + "%");
            }
            if (employeeID != null && !employeeID.isEmpty()) {
                ptm.setString(index++, "%" + employeeID + "%");
            }
            if (shopID != null && !shopID.isEmpty()) {
                ptm.setString(index++, "%" + shopID + "%");
            }

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

    public void updateInvoice(Invoice i) {
        String sql = "UPDATE [dbo].[Invoice]\n"
                + "   SET \n"
                + "      [CustomerID] = ?\n"
                + "      ,[EmployeeID] = ?\n"
                + "      ,[ShopID] = ?\n"
                + "      ,[InvoiceDate] = ?\n"
                + "      ,[TotalAmount] = ?\n"
                + "      ,[Note] = ?\n"
                + "      ,[Status] = ?\n"
                + " WHERE InvoiceID =?";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, i.getCustomerID());
            ptm.setString(2, i.getEmployeeID());
            ptm.setString(3, i.getShopID());
            ptm.setTimestamp(4, i.getInvoiceDate());
            ptm.setDouble(5, i.getTotalAmount());
            ptm.setString(6, i.getNote());
            ptm.setBoolean(7, i.isStatus());
            ptm.setString(8, i.getInvoiceID());

            ptm.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    public static void main(String[] args) {
        DBContext connection = new DBContext("SaleSphere");
        InvoiceDAO dao = new InvoiceDAO(connection.getConnection());

//        Invoice newInvoice = new Invoice(
//                "INV10020",
//                "CUST004",
//                "EMP002",
//                "SHOP001",
//                Timestamp.from(Instant.now()),
//                2000000.0,
//                "Mua quần áo mùa đông",
//                false);
//        dao.addInvoice(newInvoice);
        String sql = "SELECT *\n"
                + "  FROM [dbo].[Invoice]";
        for (Invoice inv : dao.getAllInvoices(sql)) {
            System.out.printf("%s  %s | %s | %s | %s | %.2f | %s | %b%n",
                    inv.getInvoiceID(),
                    inv.getCustomerID(),
                    inv.getEmployeeID(),
                    inv.getShopID(),
                    inv.getInvoiceDate(),
                    inv.getTotalAmount(),
                    inv.getNote(),
                    inv.isStatus());
        }
        //update hoa don
        Invoice invoiceToUpdate = dao.searchInvoice("INV1003");
        if (invoiceToUpdate != null) {
            System.out.println("Tìm thấy hóa đơn: " + invoiceToUpdate.getInvoiceID());
            dao.updateInvoice(new Invoice(invoiceToUpdate.getInvoiceID(),
                    "CUST003", "EMP001", "SHOP002", Timestamp.from(Instant.now()), 500000.0, "Mua áo khoác", false));
            System.out.println("Đã update hóa đơn.");
            Invoice updatedInvoice = dao.searchInvoice("INV1003");
            System.out.printf("%s  %s | %s | %s | %s | %.2f | %s | %b%n",
                    updatedInvoice.getInvoiceID(),
                    updatedInvoice.getCustomerID(),
                    updatedInvoice.getEmployeeID(),
                    updatedInvoice.getShopID(),
                    updatedInvoice.getInvoiceDate(),
                    updatedInvoice.getTotalAmount(),
                    updatedInvoice.getNote(),
                    updatedInvoice.isStatus());
        } else {
            System.out.println("Cập nhật hóa đơn thất bại.");
        }

//            //delete hoa don
//            Invoice i = dao.searchInvoice("INV10020");
//            if (i != null) {
//                 dao.deleteInvoice(i.getInvoiceID());
//                
//                    System.out.println("Xóa đơn thành công.");
//                    Vector<Invoice> deletedInvoice = dao.getAllInvoices(sql);
//                    for (Invoice inv : deletedInvoice) {
//                        System.out.printf("%s  %s | %s | %s | %s | %.2f | %s | %b%n",
//                                inv.getInvoiceID(),
//                                inv.getCustomerID(),
//                                inv.getEmployeeID(),
//                                inv.getShopID(),
//                                inv.getInvoiceDate(),
//                                inv.getTotalAmount(),
//                                inv.getNote(),
//                                inv.isStatus());
//                    }
//                
//            } else {
//                System.out.println("không tìm thấy hóa đơn.");
//            }
    }
}
