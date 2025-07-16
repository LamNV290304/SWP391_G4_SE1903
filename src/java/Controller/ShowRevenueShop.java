/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.InvoiceDAO;
import Dal.ShopOwnerDAO;
import Models.ShopOwner;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import DTO.ShopOwnerRevenuaDto;
import Utils.Validator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ShowRevenueShop extends HttpServlet {

    private static final int PAGE_SIZE = 10;

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
            out.println("<title>Servlet ShowRevenueShop</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShowRevenueShop at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection centralConn = DBContext.getCentralConnection()) {

            // Nhận tham số
            int shopOwnerId = Integer.parseInt(request.getParameter("shopOwnerId"));
            int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            String fromDateRaw = request.getParameter("fromDate");
            String toDateRaw = request.getParameter("toDate");
            String searchName = request.getParameter("searchName");
            searchName = Validator.normalizeInput(searchName);

            Date fromDate = null, toDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (fromDateRaw != null && !fromDateRaw.isEmpty()) {
                java.util.Date parsed = sdf.parse(fromDateRaw);
                fromDate = new java.sql.Date(parsed.getTime());
            }
            if (toDateRaw != null && !toDateRaw.isEmpty()) {
                java.util.Date parsed = sdf.parse(toDateRaw);
                toDate = new java.sql.Date(parsed.getTime());
            }

            ShopOwnerDAO shopOwnerDAO = new ShopOwnerDAO(centralConn);
            ShopOwner shopOwner = shopOwnerDAO.getShopOwnerById(shopOwnerId);
            Connection conn = DBContext.getConnection(shopOwner.getDatabaseName());

            InvoiceDAO invoiceDAO = new InvoiceDAO(conn);
            int totalShops = invoiceDAO.countShopsWithRevenue(fromDate, toDate, searchName);
            int totalPages = (int) Math.ceil((double) totalShops / PAGE_SIZE);

            List<ShopOwnerRevenuaDto> revenueList = invoiceDAO.getShopRevenue(fromDate, toDate, currentPage, PAGE_SIZE, searchName);

            // Truyền sang JSP
            request.setAttribute("shopOwner", shopOwner);
            request.setAttribute("revenueList", revenueList);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("fromDate", fromDateRaw);
            request.setAttribute("toDate", toDateRaw);
            request.setAttribute("searchName", searchName);

            request.getRequestDispatcher("ShopOwner/shopRevenue.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu ngày không hợp lệ");
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
