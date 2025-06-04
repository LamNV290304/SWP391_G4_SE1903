/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.InvoiceDAO;
import Dal.InvoiceDetailDAO;
import Models.Invoice;
import Models.InvoiceDetail;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duckh
 */
public class InvoiceServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    InvoiceDAO idao = new InvoiceDAO(connection.getConnection());
    InvoiceDetailDAO idetail = new InvoiceDetailDAO(connection.getConnection());

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
            case "listDetail":
                listInvoiceDetail(request, response);
                break;
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
            case "addDetail":
                addInvoiceDetail(request, response);
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
        List<Invoice> invoices = idao.getAllInvoices("SELECT * FROM Invoice");
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

    private void showAddDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        if (invoiceID == null) {
            response.sendRedirect("InvoiceServlet");
            return;
        }
        request.setAttribute("invoiceID", invoiceID);
        request.getRequestDispatcher("AddInvoiceDetail.jsp").forward(request, response);
    }

    private void addInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        String productID = request.getParameter("productID");
        String shopID = request.getParameter("shopID");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
        String discountStr = request.getParameter("discount");

        Double discount = 0.0;
        if (discountStr != null && !discountStr.trim().isEmpty()) {
            discount = Double.parseDouble(discountStr);
        }

        InvoiceDetail detail = new InvoiceDetail(invoiceID, productID, unitPrice, quantity, discount);
        boolean success = idetail.addInvoiceDetailAndUpdateInventory(detail, shopID);

        if (!success) {
            response.sendRedirect("InvoiceServlet?action=addDetailForm&invoiceID=" + invoiceID);
            return;
        }

        List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);
        double totalAmount = 0;
        for (InvoiceDetail d : details) {
            totalAmount += d.getTotalPrice();
        }
        Invoice invoice = idao.searchInvoice(invoiceID);
        invoice.setTotalAmount(totalAmount);
        idao.updateInvoice(invoice);

        response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
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

        List<Invoice> invoices = idao.getAllInvoices(sql);

        List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);

        request.setAttribute("invoiceList", invoices);
        request.setAttribute("selectedInvoice", newInvoice);
        request.setAttribute("invoiceDetails", details);

        response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        idao.deleteInvoice(invoiceID);
        response.sendRedirect("InvoiceServlet");
    }

    private void listInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        if (invoiceID == null) {
            response.sendRedirect("InvoiceServlet");
            return;
        }
        Invoice invoice = idao.searchInvoice(invoiceID);
        if (invoice == null) {
            response.sendRedirect("InvoiceServlet");
            return;
        }
        List<InvoiceDetail> detail = idetail.getDetailByInvoiceID(invoiceID);
        if (detail == null) {
            InvoiceDetail empty = new InvoiceDetail();
            empty.setInvoiceID(invoiceID);
            detail = new ArrayList<>();
            detail.add(empty);
        }
        double totalAmount = 0;
        for (InvoiceDetail d : detail) {
            totalAmount += d.getTotalPrice();
        }

        invoice.setTotalAmount(totalAmount);
        idao.updateInvoice(invoice);
        List<Invoice> invoices = idao.getAllInvoices(sql);

        request.setAttribute("invoiceList", invoices);
        request.setAttribute("selectedInvoice", invoice);
        request.setAttribute("invoiceDetails", detail);

        request.getRequestDispatcher("Invoice.jsp").forward(request, response);

    }

    public void saveInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        String productID = request.getParameter("productID");
        String shopID = request.getParameter("shopID");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
        String discountStr = request.getParameter("discount");

        Double discount = 0.0;
        if (discountStr != null && !discountStr.trim().isEmpty()) {
            discount = Double.parseDouble(discountStr);
        }
        InvoiceDetail detail = new InvoiceDetail(invoiceID, productID, unitPrice, quantity, discount);
    }

}
