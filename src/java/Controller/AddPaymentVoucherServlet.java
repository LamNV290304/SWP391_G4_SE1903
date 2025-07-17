/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.*;
import Models.PaymentVoucher;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class AddPaymentVoucherServlet extends HttpServlet {

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
        try {
            Connection conn = new DBContext("Test").getConnection();
            EmployeeDAO empDao = new EmployeeDAO(conn);
            TypeReceiptVoucherDAO typeDao = new TypeReceiptVoucherDAO(conn);
            ShopDAO shopDao = new ShopDAO(conn);
            CustomerDAO cusDao = new CustomerDAO(conn);
            PaymentMethodDAO pmDao = new PaymentMethodDAO(conn);
            SupplierDAO supDAO = new SupplierDAO(conn);
            request.setAttribute("listEmp", empDao.getAllEmployee());
            request.setAttribute("listShop", shopDao.getAllShops());
            request.setAttribute("listType", typeDao.getAllTypes(1));
            request.setAttribute("listCustomer", cusDao.getAllCustomer());
            request.setAttribute("listPayment", pmDao.getAllPaymentMethods());
            request.setAttribute("listSupplier", supDAO.getAllSuppliers());
            String message = request.getParameter("message");
            if ("add_success".equals(message)) {
                request.setAttribute("successMessage", "Thêm phiếu chi thành công!");
            }

            request.getRequestDispatcher("AddPaymentVoucher.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AddReceiptPayment.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
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
        try (Connection conn = new DBContext("Test").getConnection()) {
            int shopID = Integer.parseInt(request.getParameter("shopID"));
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));

            String supplierIDStr = request.getParameter("supplierID");
            Integer supplierID = (supplierIDStr != null && !supplierIDStr.isEmpty()) ? Integer.parseInt(supplierIDStr) : null;

            String paymentDateStr = request.getParameter("paymentDate");
            java.util.Date paymentDate = java.sql.Timestamp.valueOf(paymentDateStr.replace("T", " ") + ":00");

            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            String note = request.getParameter("note");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            int typeID = Integer.parseInt(request.getParameter("typeID"));
            int paymentMethodID = Integer.parseInt(request.getParameter("paymentMethodID"));

            // Tạo đối tượng phiếu chi
            PaymentVoucher pv = new PaymentVoucher();
            pv.setShopID(shopID);
            pv.setEmployeeID(employeeID);
            pv.setSupplierID(supplierID);
            pv.setPaymentDate(paymentDate);
            pv.setAmount(amount);
            pv.setNote(note);
            pv.setStatus(status);
            pv.setCreatedDate(new java.util.Date());
            pv.setTypeID(typeID);
            pv.setPaymentMethodID(paymentMethodID);

            // Gọi DAO để thêm
            PaymentVoucherDAO dao = new PaymentVoucherDAO(conn);
            boolean inserted = dao.insertPaymentVoucher(pv);

            if (inserted) {
                response.sendRedirect("PaymentVoucherServlet");
            } else {
                request.setAttribute("errorMessage", "Không thể thêm phiếu chi. Vui lòng thử lại.");
                doGet(request, response); // Trả về form với thông báo lỗi
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            doGet(request, response); // Trả lại form nếu có lỗi
        }
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

}
