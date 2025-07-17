/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.ProductDAO;
import Dal.UnitDAO;
import Models.Category;
import Models.Product;
import Models.Unit;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Thai Anh
 */
public class ListProductServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet ListProductServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListProductServlet at " + request.getContextPath () + "</h1>");
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
          
        // Nhận tham số lọc từ request
        String search = request.getParameter("search");
        String categoryID = request.getParameter("categoryID");
        String statusParam = request.getParameter("status");
        String minImportPriceStr = request.getParameter("minImportPrice");
        String maxImportPriceStr = request.getParameter("maxImportPrice");
        String minSellingPriceStr = request.getParameter("minSellingPrice");
        String maxSellingPriceStr = request.getParameter("maxSellingPrice");

        Boolean status = null;
        if (statusParam != null && !statusParam.isEmpty()) {
            status = Boolean.parseBoolean(statusParam);
        }

        BigDecimal minImportPrice = parseBigDecimal(minImportPriceStr);
        BigDecimal maxImportPrice = parseBigDecimal(maxImportPriceStr);
        BigDecimal minSellingPrice = parseBigDecimal(minSellingPriceStr);
        BigDecimal maxSellingPrice = parseBigDecimal(maxSellingPriceStr);

        // Kết nối DB và gọi DAO
        DBContext db = new DBContext("Test");
        ProductDAO productDAO = new ProductDAO(db.getConnection());
        UnitDAO unitDAO = new UnitDAO(db.getConnection());
        List<Product> productList = productDAO.getAllProductsFiltered(
            search, categoryID, status,
            minImportPrice, maxImportPrice,
            minSellingPrice, maxSellingPrice
        );

        List<Category> categoryList = productDAO.getAllCategories();
        List<Unit> unitList = unitDAO.getAllActiveUnits();
        // Gửi dữ liệu về JSP
        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("unitList", unitList);

        // Forward
        request.getRequestDispatcher("listProduct.jsp").forward(request, response);
    } 
 private BigDecimal parseBigDecimal(String val) {
        try {
            if (val != null && !val.trim().isEmpty()) {
                return new BigDecimal(val.trim());
            }
        } catch (NumberFormatException e) {
            // Có thể log lỗi nếu cần
        }
        return null;
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
                
        String action = request.getParameter("action");
        if ("update".equals(action)) {
               int productID = Integer.parseInt(request.getParameter("productID"));
            String productName = request.getParameter("productName");
            String categoryID = request.getParameter("categoryID");
            BigDecimal importPrice = new BigDecimal(request.getParameter("importPrice"));
            BigDecimal sellingPrice = new BigDecimal(request.getParameter("sellingPrice"));
            String description = request.getParameter("description");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
          String unitID = request.getParameter("unitID");

            Product product = new Product();
            product.setProductID(productID);
            product.setProductName(productName);
            product.setCategoryID(categoryID);
            product.setImportPrice(importPrice);
            product.setSellingPrice(sellingPrice);
            product.setDescription(description);
            product.setStatus(status);
            product.setUnitID(unitID);
            DBContext dBContext = new DBContext("Test");
            ProductDAO productDAO = new ProductDAO(dBContext.getConnection());
            productDAO.updateProduct(product);
            response.sendRedirect("ListProductServlet");
        } else if ("delete".equals(action)) {
              int productID = Integer.parseInt(request.getParameter("productID"));
              DBContext dBContext = new DBContext("Test");
            ProductDAO productDAO = new ProductDAO(dBContext.getConnection());
            productDAO.deleteProduct(productID);

        // Sau khi update hoặc delete, redirect lại về list
        response.sendRedirect("ListProductServlet");
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
