/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.ShopOwnerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import Context.DatabaseHelper;
import Utils.MailUtil;

/**
 *
 * @author Admin
 */
public class Verify extends HttpServlet {

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
            out.println("<title>Servlet Verify</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Verify at " + request.getContextPath() + "</h1>");
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
        String email = (String) request.getAttribute("email");
        String databaseName = (String) request.getAttribute("databaseName");
        String shopCode = (String) request.getAttribute("shopCode");

        request.setAttribute("email", email);
        request.setAttribute("shopCode", shopCode);
        request.setAttribute("databaseName", databaseName);
        request.getRequestDispatcher("verificationOTP.jsp").forward(request, response);
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
        String otp = request.getParameter("otp");
        String email = request.getParameter("email");
        String databaseName = request.getParameter("databaseName");
        String shopCode = request.getParameter("shopCode");

        DBContext dbContext = new DBContext("CentralDB");
        Connection conn = dbContext.getConnection();
        ShopOwnerDAO shopOwnerDAO = new ShopOwnerDAO(conn);

        if (!shopOwnerDAO.verifyOTP(email, otp)) {
            request.setAttribute("error", "OTP không đúng");
            request.setAttribute("databaseName", databaseName);
            request.setAttribute("email", email);
            request.getRequestDispatcher("verificationOTP.jsp").forward(request, response);
            return;
        }

        DatabaseHelper.initializeShopDatabase(databaseName);
        
        String link = "http://localhost:9999/SWP391_G4_SE1903/" + shopCode;

        MailUtil.sendLink(email, link);
        
        response.sendRedirect("successRegister.jsp");
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
