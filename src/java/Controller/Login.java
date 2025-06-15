/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Models.Employee;
import java.sql.Connection;
import java.io.IOException;
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
public class Login extends HttpServlet {

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
        request.getRequestDispatcher("Home.jsp").forward(request, response);
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
            String username = request.getParameter("email-username");
            String password = request.getParameter("password");

            String databaseName = (String) request.getSession().getAttribute("databaseName");
            Connection con = DBContext.getConnection(databaseName);

            EmployeeDAO employeeDAO = new EmployeeDAO(con);
            Employee employee = employeeDAO.findEmployeeByUsernameAndPassword(username, password);

            if (employee == null) {
                request.setAttribute("error", "Wrong password or username");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

            request.getSession().setAttribute("Employee", employee);
            request.getRequestDispatcher("Home.jsp").forward(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
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
            String username = request.getParameter("email-username");
            String password = request.getParameter("password");

            String databaseName = (String) request.getSession().getAttribute("databaseName");
            Connection con = DBContext.getConnection(databaseName);

            if (databaseName.equals("")){
                
            }
            EmployeeDAO employeeDAO = new EmployeeDAO(con);
            Employee employee = employeeDAO.findEmployeeByUsernameAndPassword(username, password);

            if (employee == null) {
                request.setAttribute("error", "Wrong password or username");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

            request.getSession().setAttribute("Employee", employee);
            request.getRequestDispatcher("Home.jsp").forward(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
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
