/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Context.DatabaseHelper;
import Dal.EmployeeDAO;
import Dal.ShopOwnerDAO;
import Models.Employee;
import Models.ShopOwner;
import Utils.PasswordUtils;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ChangePasswordShopOwner extends HttpServlet {

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
            throws ServletException, IOException, ClassNotFoundException {

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
        request.getRequestDispatcher("ShopOwner/changePasswordCentral.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        ShopOwner loggedInOwner = (ShopOwner) session.getAttribute("shopOwner");

        if (loggedInOwner == null) {
            response.sendRedirect("SaleSphere");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("errorMessage", "Mật khẩu mới và xác nhận không khớp.");
            request.getRequestDispatcher("ShopOwner/changePasswordCentral.jsp").forward(request, response);
            return;
        }

        try (Connection conn = new DBContext("CentralDB").getConnection()) {
            ShopOwnerDAO dao = new ShopOwnerDAO(conn);

            // Kiểm tra mật khẩu hiện tại
            boolean isCorrect = dao.checkPasswordShopOwner(loggedInOwner.getId(), currentPassword);
            if (!isCorrect) {
                session.setAttribute("errorMessage", "Mật khẩu hiện tại không đúng.");
                request.getRequestDispatcher("ShopOwner/changePasswordCentral.jsp").forward(request, response);
                return;
            }

            String hashedPassword = PasswordUtils.hashPassword(newPassword);
            dao.updatePasswordByUsername(loggedInOwner.getUsername(), hashedPassword);

            if (loggedInOwner.getDatabaseName().equals("CentralDB")) {
                session.setAttribute("successMessage", "Đổi mật khẩu thành công!");
                request.getRequestDispatcher("ShopOwner/changePasswordCentral.jsp").forward(request, response);
                return;
            }
            Connection shopConn = Context.DBContext.getConnection(loggedInOwner.getDatabaseName());
            EmployeeDAO shopDAO = new EmployeeDAO(shopConn);

            shopDAO.updatePassword(loggedInOwner.getId(), hashedPassword);

            session.setAttribute("successMessage", "Đổi mật khẩu thành công!");
            request.getRequestDispatcher("ShopOwner/changePasswordCentral.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau.");
            request.getRequestDispatcher("ShopOwner/changePasswordCentral.jsp").forward(request, response);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.setAttribute("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau.");
            request.getRequestDispatcher("ShopOwner/changePasswordCentral.jsp").forward(request, response);
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
