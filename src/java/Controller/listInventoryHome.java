/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Dal.InventoryDAO;
import Models.Inventory;
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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

         request.setCharacterEncoding("UTF-8");
response.setContentType("text/html; charset=UTF-8");

           Context.DBContext db = new Context.DBContext("SWP7"); // hoặc dùng constructor mặc định nếu bạn đã sửa
        Connection connection = db.getConnection();
        InventoryDAO dao = new InventoryDAO(connection);
         
        List<Inventory> inventoryList = dao.getAllInventories();
       
        /*BigDecimal cost = BigDecimal.ZERO;
         
        for(Inventory ivt : inventoryList){
          //cost = cost.add(ivt.getProduct().getImportPrice().multiply(BigDecimal.valueOf(ivt.getQuantity())));
         try (PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ThemPhieuNhap</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ThemPhieuNhap at 4 "+ivt.getProduct().toString()+ "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
        }
      */
       // request.setAttribute("totalCost", cost);
        request.setAttribute("inventoryList", inventoryList);
        request.getRequestDispatcher("listInventory.jsp").forward(request, response);
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
