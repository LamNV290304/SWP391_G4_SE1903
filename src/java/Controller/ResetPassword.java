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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ResetPassword extends HttpServlet {

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
            out.println("<title>Servlet ResetPassword</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ResetPassword at " + request.getContextPath() + "</h1>");
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
        try {
            String databaseName = request.getParameter("databaseName");
            
            if (databaseName == null) {
                response.sendRedirect("SaleSphere");
            }
            
            if (databaseName.equals("CentralDB")) {
                String email = request.getParameter("email");
                String otp = request.getParameter("otp");

                ShopOwnerDAO dao = new ShopOwnerDAO(DBContext.getCentralConnection());

                dao.markOTPUsed(email, otp);

                request.setAttribute("email", email);
                request.setAttribute("databaseName", databaseName);

                request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            } else {
                String email = request.getParameter("email");
                String otp = request.getParameter("otp");

                EmployeeDAO dao = new EmployeeDAO(DBContext.getConnection(databaseName));

                dao.markOTPUsed(email, otp);

                request.setAttribute("email", email);
                request.setAttribute("databaseName", databaseName);

                request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ResetPassword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ResetPassword.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            String databaseName = request.getParameter("databaseName");
            String email = request.getParameter("email");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
                request.setAttribute("email", email);
                request.setAttribute("databaseName", databaseName);
                request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                return;
            }
            String hashedPassword = PasswordUtils.hashPassword(newPassword);

            if (databaseName.equals("CentralDB")) {
                ShopOwnerDAO shopOwnerDAO = new ShopOwnerDAO(DBContext.getCentralConnection());
                shopOwnerDAO.updatePasswordByEmail(email, hashedPassword);

                ShopOwner owner = shopOwnerDAO.findShopOwnerByUsername(email);

                
                if (!owner.getDatabaseName().equals("CentralDB")) {
                    EmployeeDAO dao = new EmployeeDAO(DBContext.getConnection(owner.getDatabaseName()));
                    dao.updatePasswordByEmail(email, hashedPassword);
                }

                response.sendRedirect("http://localhost:9999/SWP391_G4_SE1903/SaleSphere");

            } else {
                String shopCode = DatabaseHelper.getShopCodeByDatabaseName(databaseName);
                EmployeeDAO dao = new EmployeeDAO(DBContext.getConnection(databaseName));

                Employee employee = dao.findEmployeeByEmail(email);

                if (employee.getRoleId() == 1) {
                    ShopOwnerDAO shopOwnerDAO = new ShopOwnerDAO(DBContext.getCentralConnection());
                    shopOwnerDAO.updatePasswordByEmail(email, hashedPassword);
                }

                System.out.println("Sục sục");
                dao.updatePasswordByEmail(email, hashedPassword);

                response.sendRedirect("http://localhost:9999/SWP391_G4_SE1903/" + shopCode);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ResetPassword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ResetPassword.class.getName()).log(Level.SEVERE, null, ex);
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
