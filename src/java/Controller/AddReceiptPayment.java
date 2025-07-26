/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ReceiptVoucherDAO;
import Dal.ShopDAO;
import Dal.TypeReceiptVoucherDAO;
import Dal.CustomerDAO;
import Dal.PaymentMethodDAO;
import Models.ReceiptVoucher;
import Utils.AccessControlUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class AddReceiptPayment extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String databaseName = (String) request.getSession().getAttribute("databaseName");
            if (databaseName == null) {
            response.sendRedirect("SaleSphere");
        }

        if (!AccessControlUtil.hasPermission(request, "AddReceiptPayment ")) {
            response.sendRedirect("loginEmployee.jsp");
            return;
        }
            Connection conn = new DBContext(databaseName).getConnection();
            EmployeeDAO empDao = new EmployeeDAO(conn);
            TypeReceiptVoucherDAO typeDao = new TypeReceiptVoucherDAO(conn);
            ShopDAO shopDao = new ShopDAO(conn);
            CustomerDAO cusDao = new CustomerDAO(conn);
            PaymentMethodDAO pmDao = new PaymentMethodDAO(conn);
            
            request.setAttribute("listEmp", empDao.getAllEmployee());
            request.setAttribute("listShop", shopDao.getAllShops());
            request.setAttribute("listType", typeDao.getAllTypes(1));
            request.setAttribute("listCustomer", cusDao.getAllCustomer());
            request.setAttribute("listPayment", pmDao.getAllPaymentMethods());
            
            String message = request.getParameter("message");
            if ("add_success".equals(message)) {
                request.setAttribute("successMessage", "Thêm phiếu thu thành công!");
            }
            
            request.getRequestDispatcher("ReceiptVoucherServlet").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AddReceiptPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        if (databaseName == null) {
            response.sendRedirect("SaleSphere");
        }

        if (!AccessControlUtil.hasPermission(request, "AddReceiptPayment ")) {
            response.sendRedirect("loginEmployee.jsp");
            return;
        }
         try (Connection conn = new DBContext(databaseName).getConnection()) {
            String shopID_raw = request.getParameter("shopID");
            String employeeID_raw = request.getParameter("employeeID");
            String customerID_raw = request.getParameter("customerID");
            String typeID_raw = request.getParameter("typeID");
            String paymentMethodID_raw = request.getParameter("paymentMethodID");
            String dateStr = request.getParameter("receiptDate");
            String amountRaw = request.getParameter("amount");
            String note = request.getParameter("note");

            // Validate date
            Date receiptDate = null;
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                receiptDate = sdf.parse(dateStr);
            }

            // Build ReceiptVoucher object
            ReceiptVoucher rv = new ReceiptVoucher();
            rv.setShopID(Integer.parseInt(shopID_raw));
            rv.setEmployeeID(Integer.parseInt(employeeID_raw));
            rv.setCustomerID(customerID_raw != null && !customerID_raw.isEmpty() ? Integer.parseInt(customerID_raw) : null);
            rv.setTypeID(Integer.parseInt(typeID_raw));
            rv.setPaymentMethodID(Integer.parseInt(paymentMethodID_raw));
            rv.setReceiptDate(receiptDate);
            rv.setCreatedDate(new Date());
            rv.setNote(note);
            rv.setAmount(new BigDecimal(amountRaw));
            rv.setStatus(true);

            ReceiptVoucherDAO dao = new ReceiptVoucherDAO(conn);
            boolean success = dao.insertReceiptVoucher(rv);

            if (success) {
                response.sendRedirect("AddReceiptPayment?message=add_success");
            } else {
                request.setAttribute("error", "Không thể thêm phiếu thu.");
                processRequest(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý dữ liệu: " + e.getMessage());
            processRequest(request, response);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
