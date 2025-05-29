/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.ProductDAO;
import Models.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;

/**
 *
 * @author Admin
 */
public class ProductController extends HttpServlet {

    DBContext connection = new DBContext("swp2");
    ProductDAO dao = new ProductDAO(connection.getConnection());

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

        Vector<Product> vector = new Vector<>();
        vector = dao.getProduct("select* from Product");
        request.setAttribute("vector", vector);
        request.getRequestDispatcher("/jsp/Product.jsp").forward(request, response);

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
        String service = request.getParameter("service");

        if (service.equals("Delete")) {
            String pId = request.getParameter("pId");
            int n = dao.deleteProduct(pId, "swp2");
            String message = "";
                
                if (n == 0) {
                    message = "delete failed";
                    request.getSession().setAttribute("message", message);
                    doGet(request, response);
                } else if (n == -1) {
                    message = "foreign key constrain";
                    request.getSession().setAttribute("message", message);
                    doGet(request, response);
                } else {
                    doGet(request, response);
                }
        }

        if (service.equals("Update")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                String pId = request.getParameter("pId");
                Vector<Product> vector = dao.getProduct("select* from Product where ProductID ='" + pId + "'");
                request.setAttribute("vector", vector);
                request.getRequestDispatcher("/jsp/UpdateProduct.jsp").forward(request, response);
            } else {
                String pId = request.getParameter("ProductID");
                String ProductName = request.getParameter("ProductName");
                String CategoryID = request.getParameter("CategoryID");
                String UnitID = request.getParameter("UnitID");
                double Price = Double.parseDouble(request.getParameter("Price"));
                String Description = request.getParameter("Description");
                boolean Status = request.getParameter("Status").equals("1") ? true : false;
                String CreatedDate = request.getParameter("CreatedDate");
                String CreatedBy = request.getParameter("CreatedBy");
                Product pro = new Product(pId, ProductName, CategoryID, UnitID, Price, Description, Status, CreatedDate, CreatedBy);
                dao.updateProduct(pro);
                doGet(request, response);
            }
        }

        if (service.equals("Insert")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                request.getRequestDispatcher("/jsp/InsertProduct.jsp").forward(request, response);
            } else {
                String ProductID = request.getParameter("ProductID");
                String ProductName = request.getParameter("ProductName");
                String CategoryID = request.getParameter("CategoryID");
                String UnitID = request.getParameter("UnitID");
                double Price = Double.parseDouble(request.getParameter("Price"));
                String Description = request.getParameter("Description");
                boolean Status = request.getParameter("Status").equals("1") ? true : false;
                String CreatedDate = request.getParameter("CreatedDate");
                String CreatedBy = request.getParameter("CreatedBy");
                Product pro = new Product(ProductID, ProductName, CategoryID, UnitID, Price, Description, Status, CreatedDate, CreatedBy);
                dao.addProduct(pro);
                doGet(request, response);

            }
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
