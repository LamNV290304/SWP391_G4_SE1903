/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import java.io.IOException;
import Dal.*;
import java.io.PrintWriter;
import java.sql.Connection;
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
public class TypeVoucherServlet extends HttpServlet {
   
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
            response.setContentType("text/html;charset=UTF-8");
            String databaseName = (String) request.getSession().getAttribute("databaseName");
            Connection conn = new DBContext(databaseName).getConnection();
            TypeReceiptVoucherDAO thuDAO = new TypeReceiptVoucherDAO(conn);   // DAO cho phiếu thu
            TypePaymentVoucherDAO chiDAO = new TypePaymentVoucherDAO(conn);   // DAO cho phiếu chi
            
            request.setAttribute("typeReceiptList", thuDAO.getAllTypes(1));
            request.setAttribute("typePaymentList", chiDAO.getAllTypes(1));
            
            request.getRequestDispatcher("voucherTypes.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TypeVoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
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
           String action = request.getParameter("action");
String databaseName = (String) request.getSession().getAttribute("databaseName");
        try (Connection conn = new DBContext(databaseName).getConnection()) {
            TypeReceiptVoucherDAO receiptDAO = new TypeReceiptVoucherDAO(conn);
            TypePaymentVoucherDAO paymentDAO = new TypePaymentVoucherDAO(conn);

            switch (action) {
                case "addReceipt" ->  {
                    String name = request.getParameter("nameTypeReceipt");
                    receiptDAO.insertType(name);
                }
                case "deleteReceipt" ->  {
                    int id = Integer.parseInt(request.getParameter("id"));
                    receiptDAO.deleteType(id);
                }
                case "editReceipt" ->  {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String name = request.getParameter("nameTypeReceipt");
                    receiptDAO.updateType(id, name);
                }
                case "addPayment" ->  {
                    String name = request.getParameter("nameTypePayment");
                    paymentDAO.insertType(name);
                }
                case "deletePayment" ->  {
                    int id = Integer.parseInt(request.getParameter("id"));
                    paymentDAO.deleteType(id);
                }
                case "editPayment" ->  {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String name = request.getParameter("nameTypePayment");
                    paymentDAO.updateType(id, name);
                }
                default -> {
                   }
            }

        } catch (Exception e) {
            Logger.getLogger(TypeVoucherServlet.class.getName()).log(Level.SEVERE, null, e);
        }

        // Sau khi thao tác xong, load lại danh sách
        response.sendRedirect("TypeVoucherServlet");
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
