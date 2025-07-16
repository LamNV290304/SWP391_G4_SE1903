/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Models.Inventory;
import Models.Product;
import Models.Shop;
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
 * @author Thai Anh
 */
public class listInventoryHome extends HttpServlet {

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

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Connection connection = new Context.DBContext("Test").getConnection();
        InventoryDAO dao = new InventoryDAO(connection);
        ProductDAO productDAO = new ProductDAO(connection);
        ShopDAO shopDAO = new ShopDAO(connection);

// Lấy danh sách
        List<Product> productList = productDAO.getAllProducts();
        List<Shop> shopList = shopDAO.getAllShops();

// Truyền sang JSP
request.setAttribute("productList", productList);
request.setAttribute("shopList", shopList);
        // Lấy tham số lọc
        String productName = request.getParameter("productName");
        String shopName = request.getParameter("shopName");
        String minQtyRaw = request.getParameter("minQuantity");

        Integer minQuantity = null;
        try {
            if (minQtyRaw != null && !minQtyRaw.isEmpty()) {
                minQuantity = Integer.parseInt(minQtyRaw);
            }
        } catch (NumberFormatException e) {
            minQuantity = null;
        }
        String maxQtyRaw = request.getParameter("maxQuantity");
        Integer maxQuantity = null;
        try {
            if (maxQtyRaw != null && !maxQtyRaw.isEmpty()) {
                maxQuantity = Integer.parseInt(maxQtyRaw);
            }
        } catch (NumberFormatException e) {
            maxQuantity = null;
        }
       List<Inventory> inventoryList = dao.searchInventories(productName, shopName, minQuantity, maxQuantity);
        request.setAttribute("inventoryList", inventoryList);
        request.getRequestDispatcher("listInventory.jsp").forward(request, response);
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
        processRequest(request, response);
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
