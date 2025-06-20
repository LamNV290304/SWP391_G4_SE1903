/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.SupplierDAO;
import Dal.TypeExportReceiptDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 *
 * @author Thai Anh
 */
public class AddInventoryCheck extends HttpServlet {
   
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
        Connection conn = new DBContext("SWP7").getConnection();
        EmployeeDAO empDao = new EmployeeDAO(conn);
        ShopDAO shopDao = new ShopDAO();
        SupplierDAO supDAO = new SupplierDAO(conn);
        ProductDAO ProDAO = new ProductDAO(conn);
        InventoryDAO ivtDAO = new InventoryDAO(conn);
        request.setAttribute("listEmp", empDao.getAllEmployee());
        request.setAttribute("listSup", supDAO.getAllSuppliers());
        request.setAttribute("listShop", shopDao.getAllShops("SWP7"));
        request.setAttribute("listProduct", ProDAO.getAllProducts());
        request.setAttribute("listIvt", ivtDAO.getAllInventories());
        request.getRequestDispatcher("AddExportReceipt.jsp").forward(request, response);
        /*try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddInventoryCheck</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddInventoryCheck at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
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
        processRequest(request, response);
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
        processRequest(request, response);
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
