/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.InvoiceDAO;
import Models.Invoice;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Vector;

/**
 *
 * @author duckh
 */
public class InvoiceServlet extends HttpServlet {
    DBContext connection = new DBContext("SaleSphere");
    InvoiceDAO idao = new InvoiceDAO(connection.getConnection());
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    private String sql = "SELECT * FROM [dbo].[Invoice]";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            listInvoices(request, response);
            return;
        }
        switch (action) {
            default:
                listInvoices(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            listInvoices(request, response);
            return;
        }
        switch (action) {
            case "add":
                addInvoice(request, response);
                break;
            case "delete":
                deleteInvoice(request, response);
                break;
            case "update":
                updateInvoice(request, response);
                break;
            default:
                listInvoices(request, response);
                break;
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void listInvoices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Vector<Invoice> invoices = idao.getAllInvoices("SELECT * FROM Invoice");
        request.setAttribute("invoiceList", invoices);
        request.getRequestDispatcher("Invoice.jsp").forward(request, response);
    }

    private void updateInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
            String invoiceID = request.getParameter("invoiceID");
            String customerID = request.getParameter("customerID");
            String employee = request.getParameter("employeeID");
            String shopID = request.getParameter("shopID");
            Invoice oldInvoice = idao.searchInvoice(invoiceID);

            Timestamp invoiceDateTimestamp = oldInvoice.getInvoiceDate();

            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
            String note = request.getParameter("note");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            Invoice invoice = new Invoice(invoiceID, customerID, employee, shopID, invoiceDateTimestamp, totalAmount, note, status);
            idao.updateInvoice(invoice);
            response.sendRedirect("InvoiceServlet");
        
    }


    private void addInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        String customerID = request.getParameter("customerID");
        String employeeID = request.getParameter("employeeID");
        String shopID = request.getParameter("shopID");
        Timestamp invoiceDate = new Timestamp(System.currentTimeMillis()); // ngày hiện tại
        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        String note = request.getParameter("note");
        boolean status = Boolean.parseBoolean(request.getParameter("status"));

        Invoice newInvoice = new Invoice(invoiceID, customerID, employeeID, shopID, invoiceDate, totalAmount, note, status);
        idao.addInvoice(newInvoice);

        response.sendRedirect("InvoiceServlet");
    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        idao.deleteInvoice(invoiceID);
        response.sendRedirect("InvoiceServlet");
    }

}
    