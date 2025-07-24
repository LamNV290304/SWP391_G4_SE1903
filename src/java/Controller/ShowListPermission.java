/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import java.sql.*;
import Context.DBContext;
import Dal.PageDAO;
import Dal.PermissionDAO;
import Dal.RoleDAO;
import Models.Pages;
import Models.Role;
import Models.ShopOwner;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class ShowListPermission extends HttpServlet {

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
            out.println("<title>Servlet ShowListPermission</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShowListPermission at " + request.getContextPath() + "</h1>");
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
            ShopOwner shopOwner = (ShopOwner) request.getSession().getAttribute("shopOwner");
            if (shopOwner == null || shopOwner.getId() != 1) {
                response.sendRedirect(request.getContextPath() + "/SaleSphere");
                return;
            }

            Connection conn = DBContext.getCentralConnection();
            RoleDAO roleDAO = new RoleDAO(conn);
            PageDAO pageDAO = new PageDAO(conn);
            PermissionDAO permissionDAO = new PermissionDAO(conn);

            // Lấy danh sách role (trừ Admin)
            List<Role> roles = roleDAO.getAllRoles();
            request.setAttribute("roles", roles);

            // Lấy danh sách tất cả page
            Map<String, Pages> pages = pageDAO.getAllPages();
            request.setAttribute("pages", pages);

            // Lấy roleId từ param nếu có
            String roleIdStr = request.getParameter("roleId");
            if (roleIdStr != null && !roleIdStr.isEmpty()) {
                int roleId = Integer.parseInt(roleIdStr);
                Map<String, Boolean> grantedPageCodes = permissionDAO.getGrantedPageMapByRoleId(roleId);
                request.setAttribute("selectedRoleId", roleId);
                request.setAttribute("grantedPages", grantedPageCodes);
            }

            request.getRequestDispatcher("ShopOwner/authorize.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error500.jsp");
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
            ShopOwner shopOwner = (ShopOwner) request.getSession().getAttribute("shopOwner");
            if (shopOwner == null || shopOwner.getId() != 1) {
                response.sendRedirect(request.getContextPath() + "/SaleSphere");
                return;
            }

            int roleId = Integer.parseInt(request.getParameter("roleId"));
            String[] grantedPageCodes = request.getParameterValues("pageIds"); // Có thể null

            Connection conn = DBContext.getCentralConnection();
            PermissionDAO permissionDAO = new PermissionDAO(conn);
            PageDAO pageDAO = new PageDAO(conn);

            Map<String, Pages> allPages = pageDAO.getAllPages();

            Set<String> grantedSet = new HashSet<>();
            if (grantedPageCodes != null) {
                grantedSet.addAll(Arrays.asList(grantedPageCodes));
            }

            for (String pageCode : allPages.keySet()) {
                boolean isGranted = grantedSet.contains(pageCode);
                permissionDAO.updatePermission(roleId, pageCode, isGranted);
            }

            request.getSession().setAttribute("flash_success", "Cập nhật phân quyền thành công!");
            response.sendRedirect("ShowListPermission?roleId=" + roleId);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("flash_fail", "Đã xảy ra lỗi khi cập nhật quyền!");
            response.sendRedirect("ShowListPermission");
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
