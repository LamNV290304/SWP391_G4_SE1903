/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.EmployeeDto;
import Dal.EmployeeDAO;
import Dal.ShopDAO;
import Models.Shop;
import Utils.Validator;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.List;
import org.apache.tomcat.jakartaee.commons.lang3.Validate;

/**
 *
 * @author Admin
 */
public class ShowEmployeeList extends HttpServlet {

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
            out.println("<title>Servlet ShowEmployeeList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShowEmployeeList at " + request.getContextPath() + "</h1>");
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
        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        String sort = request.getParameter("sort");
        String shopIdParam = request.getParameter("shopId");
        String statusParam = request.getParameter("status");
        String search = request.getParameter("keyword");
        String keyword = Validator.normalizeInput(search);

        Integer shopId = (shopIdParam != null && !shopIdParam.isEmpty()) ? Integer.parseInt(shopIdParam) : null;
        Boolean status = (statusParam != null && !statusParam.isEmpty()) ? statusParam.equals("1") : null;

        try (Connection conn = DBContext.getConnection("ShopDB_TTest")) {
            EmployeeDAO dao = new EmployeeDAO(conn);
            ShopDAO shopDAO = new ShopDAO();

            List<EmployeeDto> employeeList = dao.getEmployeesByPage(page, recordsPerPage, shopId, status, sort, keyword);
            int totalRecords = dao.getTotalEmployeeCount(shopId, status, keyword);
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

            List<Shop> shopList = shopDAO.getAllShops("ShopDB_TTest");

            request.setAttribute("employeeList", employeeList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("shopList", shopList);

            request.getRequestDispatcher("showEmployeeList.jsp").forward(request, response);
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
        processRequest(request, response);
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
