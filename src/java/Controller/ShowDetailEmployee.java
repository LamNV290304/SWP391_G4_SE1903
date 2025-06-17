/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.EmployeeDto;
import Dal.*;
import Models.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ShowDetailEmployee extends HttpServlet {

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
            out.println("<title>Servlet ShowDetailEmployee</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShowDetailEmployee at " + request.getContextPath() + "</h1>");
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
            int id = Integer.parseInt(request.getParameter("id"));

            Connection conn = DBContext.getConnection("ShopDB_TTest");
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            RoleDAO roleDAO = new RoleDAO(conn);
            ShopDAO shopDAO = new ShopDAO();

            EmployeeDto employee = employeeDAO.getEmployeeById(id);
            List<Role> roleList = roleDAO.getAllRoles();
            List<Shop> shopList = shopDAO.getAllShops("ShopDB_TTest");

            request.setAttribute("employee", employee);
            request.setAttribute("roleList", roleList);
            request.setAttribute("shopList", shopList);

            request.getRequestDispatcher("showEmployeeDetails.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy nhân viên.");
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
            Connection conn = DBContext.getConnection("ShopDB_TTest");

            int id = Integer.parseInt(request.getParameter("id"));
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            boolean status = "1".equals(request.getParameter("status"));
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            int shopId = Integer.parseInt(request.getParameter("shopId"));

            Employee employee = new Employee();
            employee.setId(id);
            employee.setFullname(fullName);
            employee.setEmail(email);
            employee.setPhone(phone);
            employee.setStatus(status);
            employee.setRoleId(roleId);
            employee.setShopId(shopId);

            EmployeeDAO dao = new EmployeeDAO(conn);
            dao.updateEmployee(employee);

            response.sendRedirect("ShowDetailEmployee?id=" + id);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi cập nhật nhân viên.");
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
