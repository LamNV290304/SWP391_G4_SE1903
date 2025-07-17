/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.*;
import Models.Category;
import Models.Unit;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Thai Anh
 */
public class ListCategoryUnitServlet extends HttpServlet {
   
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
            out.println("<title>Servlet ListCategoryUnitServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListCategoryUnitServlet at " + request.getContextPath () + "</h1>");
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
         DBContext db = new DBContext("Test");
        CategoryDAO categoryDAO = new CategoryDAO(db.getConnection());
        UnitDAO unitDAO = new UnitDAO(db.getConnection());

        List<Category> categoryList = categoryDAO.getAllCategories();
        List<Unit> unitList = unitDAO.getAllActiveUnits();

        request.setAttribute("categoryList", categoryList);
        request.setAttribute("unitList", unitList);

        request.getRequestDispatcher("listCategoryUnit.jsp").forward(request, response);
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

    DBContext db = new DBContext("Test");
    CategoryDAO categoryDAO = new CategoryDAO(db.getConnection());
    UnitDAO unitDAO = new UnitDAO(db.getConnection());

    if ("addCategory".equals(action)) {
        String name = request.getParameter("categoryName");
        String description = request.getParameter("description");
        Category c = new Category();
        c.setCategoryName(name);
        c.setDescription(description);
        c.setStatus(true);
        categoryDAO.addCategory(c);
    } else if ("addUnit".equals(action)) {
        String description = request.getParameter("description");
        Unit u = new Unit();
        u.setDescription(description);
        u.setStatus(1);
        unitDAO.addUnit(u);
    }
    if ("updateCategory".equals(action)) {
    int id = Integer.parseInt(request.getParameter("categoryID"));
    String name = request.getParameter("categoryName");
    String desc = request.getParameter("description");

    Category c = new Category();
    c.setCategoryID(id);
    c.setCategoryName(name);
    c.setDescription(desc);
    c.setStatus(true);
    categoryDAO.updateCategory(c);
}
else if ("updateUnit".equals(action)) {
    int id = Integer.parseInt(request.getParameter("unitID"));
    String desc = request.getParameter("description");

    Unit u = new Unit();
    u.setUnitID(id);
    u.setDescription(desc);
    unitDAO.updateUnit(u);
}
    if ("deleteCategory".equals(action)) {
    int categoryID = Integer.parseInt(request.getParameter("categoryID"));
    categoryDAO.deleteCategory(categoryID); // gọi DAO xử lý
}
else if ("deleteUnit".equals(action)) {
    int unitID = Integer.parseInt(request.getParameter("unitID"));
    unitDAO.deactivateUnit(unitID); // gọi DAO xử lý
}
    response.sendRedirect("ListCategoryUnitServlet");
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
