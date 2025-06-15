/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import java.sql.*;
import Context.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Models.*;
import Dal.*;
import Utils.*;

/**
 *
 * @author Admin
 */
public class Register extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Register</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Register at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        request.setCharacterEncoding("UTF-8");
        try {
            DBContext dbContext = new DBContext("CentralDB");
            Connection conn = dbContext.getConnection();
            ShopOwnerDAO shopOwnerDAO = new ShopOwnerDAO(conn);

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String repassword = request.getParameter("re-password");
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String shopName = request.getParameter("shopName");

            if (!password.equals(repassword)) {
                request.setAttribute("error", "Password và Re-password không khớp!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (shopOwnerDAO.isUsernameExist(username)) {
                request.setAttribute("error", "Username đã tồn tại, vui lòng chọn username khác!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            if (shopOwnerDAO.isPhoneExist(phone)) {
                request.setAttribute("error", "Số điện thoại đã tồn tại, vui lòng chọn username khác!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (shopOwnerDAO.isShopNameExist(shopName)) {
                request.setAttribute("error", "Shop đã tồn tại, vui lòng chọn tên khác!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (shopOwnerDAO.isEmailExist(email)) {
                request.setAttribute("error", "Email đã tồn tại, vui lòng chọn tên khác!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            String otp = DatabaseHelper.generateOTP();

            String databaseName = DatabaseHelper.generateSafeDatabaseName(shopName);
            String shopCode = DatabaseHelper.generateShopCode(shopName);
            
            if (shopOwnerDAO.isDatabaseNameExist(databaseName)) {
                request.setAttribute("error", "Shop đã tồn tại, vui lòng chọn tên khác!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            String hasPassword = PasswordUtils.hashPassword(password);
            ShopOwner shopOwner = new ShopOwner(databaseName, shopCode, shopName, 0, username, hasPassword, fullname, phone, email, false, null);
            
            shopOwnerDAO.addShopOwner(shopOwner);

            shopOwnerDAO.saveOTP(email, otp);

            MailUtil.sendCode(email, otp);

            request.setAttribute("email", email);
            request.setAttribute("shopCode", shopCode);
            request.setAttribute("databaseName", databaseName);
            request.getRequestDispatcher("verificationOTP.jsp").forward(request, response);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + ex.getStackTrace());
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
