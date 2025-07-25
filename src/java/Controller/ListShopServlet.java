/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.ShopDAO;
import Models.Shop;
import Utils.AccessControlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.List;

/**
 *
 * @author Thai Anh
 */
public class ListShopServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ListShopServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListShopServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         try {
             String databaseName = (String) request.getSession().getAttribute("databaseName");
            Connection conn = new DBContext(databaseName).getConnection();
            if (databaseName == null) {
            response.sendRedirect("SaleSphere");
        }

        if (!AccessControlUtil.hasPermission(request, "AddEmployee")) {
            response.sendRedirect("loginEmployee.jsp");
            return;
        }
            if (databaseName == null) {
            response.sendRedirect("SaleSphere");
        }

        if (!AccessControlUtil.hasPermission(request, "AddEmployee")) {
            response.sendRedirect("loginEmployee.jsp");
            return;
        }
            ShopDAO shopDAO = new ShopDAO(conn);
            List<Shop> list = shopDAO.getAllShops();
             
            request.setAttribute("listShop", list);
            request.getRequestDispatcher("listShop.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải danh sách cửa hàng.");
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
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
        Connection conn = new DBContext(databaseName).getConnection();
        ShopDAO shopDAO = new ShopDAO(conn);

        String shopIDStr = request.getParameter("shopID"); // chỉ có khi update hoặc delete
        String shopName = request.getParameter("shopName");
        String shopCode = request.getParameter("shopCode");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String createdDate = request.getParameter("createdDate");
        String statusStr = request.getParameter("status");

        if (shopName != null && shopCode != null && address != null && phone != null && createdDate != null) {
            // Xử lý thêm hoặc sửa
            boolean isUpdate = (shopIDStr != null && !shopIDStr.isEmpty());
            boolean status = Boolean.parseBoolean(statusStr);
            Shop shop = new Shop();

            shop.setShopName(shopName);
            shop.setShopCode(shopCode);
            shop.setAddress(address);
            shop.setPhone(phone);
            shop.setCreatedDate(Date.valueOf(createdDate));
            shop.setStatus(status);

            if (isUpdate) {
                int shopID = Integer.parseInt(shopIDStr);
                shop.setShopID(shopID);
                shopDAO.updateShop(shop);
            } else {
                shopDAO.createShop(shop);
            }

        } else if (shopIDStr != null && (shopName == null || shopName.isEmpty())) {
            // Nếu chỉ có shopID và không có các trường khác thì là xóa
            int shopID = Integer.parseInt(shopIDStr);
            shopDAO.deleteShop(shopID);
        }

        response.sendRedirect("ListShopServlet");

    } catch (Exception ex) {
        ex.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi xử lý cửa hàng.");
    }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
