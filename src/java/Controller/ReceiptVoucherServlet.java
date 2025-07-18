/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.*;
import Models.ReceiptVoucher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class ReceiptVoucherServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            String databaseName = (String) request.getSession().getAttribute("databaseName");
            Connection conn = new DBContext(databaseName).getConnection();
            ReceiptVoucherDAO dao = new ReceiptVoucherDAO(conn);
            ShopDAO shopDAO = new ShopDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            CustomerDAO customerDAO = new CustomerDAO(conn);
            TypeReceiptVoucherDAO typeReceipt = new TypeReceiptVoucherDAO(conn);
            PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO(conn);
            List<ReceiptVoucher> vouchers = dao.getAllReceiptVouchers();
            request.setAttribute("receiptVouchers", vouchers);
            request.setAttribute("shops", shopDAO.getAllShops());
            request.setAttribute("employees", employeeDAO.getAllEmployee());
            request.setAttribute("customers", customerDAO.getAllCustomer());
            request.setAttribute("types", typeReceipt.getAllTypes(1));
            request.setAttribute("paymentMethods", paymentMethodDAO.getAllPaymentMethods());
            request.getRequestDispatcher("listReceiptVoucher.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
String databaseName = (String) request.getSession().getAttribute("databaseName");
        try (Connection conn = new DBContext(databaseName).getConnection()) {

            // Lấy các giá trị lọc từ request
            Integer shopID = parseInteger(request.getParameter("shopID"));
            Integer employeeID = parseInteger(request.getParameter("employeeID"));
            Integer typeID = parseInteger(request.getParameter("typeID"));
            Integer paymentMethodID = parseInteger(request.getParameter("paymentMethodID"));
            BigDecimal minAmount = parseBigDecimal(request.getParameter("minAmount"));
            BigDecimal maxAmount = parseBigDecimal(request.getParameter("maxAmount"));
            Date fromDate = parseDate(request.getParameter("fromDate"));
            Date toDate = parseDate(request.getParameter("toDate"));

            // DAO
            ReceiptVoucherDAO dao = new ReceiptVoucherDAO(conn);
            ShopDAO shopDAO = new ShopDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            CustomerDAO customerDAO = new CustomerDAO(conn);
            TypeReceiptVoucherDAO typeDAO = new TypeReceiptVoucherDAO(conn);
            PaymentMethodDAO paymentDAO = new PaymentMethodDAO(conn);

            List<ReceiptVoucher> vouchers = dao.filterReceiptVouchers(
                    shopID, employeeID, typeID, minAmount, maxAmount, fromDate, toDate, paymentMethodID
            );

            // Truyền data cho JSP
            request.setAttribute("receiptVouchers", vouchers);
            request.setAttribute("shops", shopDAO.getAllShops());
            request.setAttribute("employees", employeeDAO.getAllEmployee());
            request.setAttribute("customers", customerDAO.getAllCustomer());
            request.setAttribute("types", typeDAO.getAllTypes(1));
            request.setAttribute("paymentMethods", paymentDAO.getAllPaymentMethods());

            // Trả lại dữ liệu lọc để giữ nguyên trên form
            request.setAttribute("paramShopID", shopID);
            request.setAttribute("paramEmployeeID", employeeID);
            request.setAttribute("paramTypeID", typeID);
            request.setAttribute("paramPaymentMethodID", paymentMethodID);
            request.setAttribute("paramMinAmount", minAmount);
            request.setAttribute("paramMaxAmount", maxAmount);
            request.setAttribute("paramFromDate", request.getParameter("fromDate"));
            request.setAttribute("paramToDate", request.getParameter("toDate"));

            request.getRequestDispatcher("listReceiptVoucher.jsp").forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ReceiptVoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        String action = request.getParameter("action");
String databaseName = (String) request.getSession().getAttribute("databaseName");
        try (Connection conn = new DBContext(databaseName).getConnection()) {
            ReceiptVoucherDAO dao = new ReceiptVoucherDAO(conn);

            if ("update".equals(action)) {
                ReceiptVoucher rv = extractReceiptVoucher(request);
                dao.updateReceiptVoucher(rv);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("receiptVoucherID"));
                dao.deleteReceiptVoucher(id);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

        response.sendRedirect("ReceiptVoucherServlet");
    }

    private ReceiptVoucher extractReceiptVoucher(HttpServletRequest request) throws Exception {
        ReceiptVoucher rv = new ReceiptVoucher();

        rv.setReceiptVoucherID(Integer.parseInt(request.getParameter("receiptVoucherID")));
        rv.setShopID(Integer.parseInt(request.getParameter("shopID")));
        rv.setEmployeeID(Integer.parseInt(request.getParameter("employeeID")));

        String custStr = request.getParameter("customerID");
        if (custStr != null && !custStr.isEmpty()) {
            rv.setCustomerID(Integer.parseInt(custStr));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        rv.setReceiptDate(sdf.parse(request.getParameter("receiptDate")));

        rv.setAmount(new BigDecimal(request.getParameter("amount")));
        rv.setNote(request.getParameter("note"));
        rv.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        rv.setCreatedDate(new Date()); // Cập nhật createdDate hiện tại
        rv.setTypeID(Integer.parseInt(request.getParameter("typeID")));
        rv.setPaymentMethodID(Integer.parseInt(request.getParameter("paymentMethodID")));

        return rv;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private Integer parseInteger(String value) {
        try {
            return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return (value != null && !value.isEmpty()) ? new BigDecimal(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private java.util.Date parseDate(String value) {
        try {
            return (value != null && !value.isEmpty()) ? new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value) : null;
        } catch (java.text.ParseException e) {
            return null;
        }
    }

    private ReceiptVoucher extractReceiptVoucherFromRequest(HttpServletRequest request, boolean isUpdate) {
        ReceiptVoucher rv = new ReceiptVoucher();

        if (isUpdate) {
            rv.setReceiptVoucherID(Integer.parseInt(request.getParameter("id")));
        }

        rv.setReceiptDate(java.sql.Date.valueOf(request.getParameter("receiptDate")));
        rv.setShopID(Integer.parseInt(request.getParameter("shopID")));
        rv.setEmployeeID(Integer.parseInt(request.getParameter("employeeID")));

        String customerID = request.getParameter("customerID");
        rv.setCustomerID(customerID == null || customerID.isEmpty() ? null : Integer.parseInt(customerID));

        rv.setAmount(new BigDecimal(request.getParameter("amount"))); 
        rv.setNote(request.getParameter("note"));
        rv.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        rv.setTypeID(Integer.parseInt(request.getParameter("typeID")));
        rv.setPaymentMethodID(Integer.parseInt(request.getParameter("paymentMethodID")));

        return rv;
    }
}
