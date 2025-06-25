/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Context.DatabaseHelper;
import DTO.EmployeeDto;
import Dal.EmployeeDAO;
import Dal.RoleDAO;
import Dal.ShopDAO;
import Models.Employee;
import Models.Role;
import Models.Shop;
import Utils.MailUtil;
import static Utils.PasswordUtils.hashPassword;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class AddEmployee extends HttpServlet {

    private final String password = "123abc@A";

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
            out.println("<title>Servlet AddEmployee</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddEmployee at " + request.getContextPath() + "</h1>");
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
        String databaseName = (String) request.getSession().getAttribute("databaseName");

        try (Connection conn = DBContext.getConnection(databaseName)) {
            ShopDAO shopDAO = new ShopDAO();
            RoleDAO roleDAO = new RoleDAO(conn);

            List<Shop> shopList = shopDAO.getAllShops(databaseName);
            List<Role> roleList = roleDAO.getAllRoles();
            request.setAttribute("shopList", shopList);
            request.setAttribute("roleList", roleList);

            request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi truy xuất danh sách nhân viên");
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
            String databaseName = (String) request.getSession().getAttribute("databaseName");

            Connection conn = DBContext.getConnection(databaseName);
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String username = request.getParameter("username");
            int shopId = Integer.parseInt(request.getParameter("shopId"));
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            boolean status = "1".equals(request.getParameter("status"));

            EmployeeDAO dao = new EmployeeDAO(conn);

            if (dao.isEmailExists(email)) {
                request.setAttribute("error", "Email đã tồn tại!");
                doGet(request, response);
                return;
            }

            if (dao.isPhoneExists(phone)) {
                request.setAttribute("error", "Số điện thoại đã tồn tại!");
                doGet(request, response);
                return;
            }

            if (dao.isUsernameExists(username)) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
                doGet(request, response);
                return;
            }
            Employee e = new Employee();
            e.setFullname(fullName);
            e.setEmail(email);
            e.setPhone(phone);
            e.setUsername(username);
            e.setPassword(hashPassword(password));
            e.setShopId(shopId);
            e.setRoleId(roleId);
            e.setStatus(status);
            
            String shopCode = DatabaseHelper.getShopCodeByDatabaseName(databaseName);
            
            String link = "http://localhost:9999/SWP391_G4_SE1903/" + shopCode;

            try {
                dao.addEmployee(e);
                MailUtil.sendAccountCredentials(email, username, password, link);
            } catch (SQLException ex) {
                Logger.getLogger(AddEmployee.class.getName()).log(Level.SEVERE, null, ex);
            }

            response.sendRedirect("ShowEmployeeList");
        } catch (NumberFormatException ex) {
            throw new ServletException(ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddEmployee.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddEmployee.class.getName()).log(Level.SEVERE, null, ex);
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
