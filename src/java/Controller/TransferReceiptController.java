/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Dal.TransferReceiptDAO;
import Models.TransferReceipt;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "TransferReceipt", urlPatterns = {"/TransferReceipt"})
public class TransferReceiptController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String SQL = "SELECT * FROM [dbo].[[TransferReceipt]]";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        
        TransferReceiptDAO dao = new TransferReceiptDAO();
        String service = request.getParameter("service");
        if (service == null) {
            service = "listTransferReceipt";
        }
        if (service.equals("updateProduct")) {
            String submit = request.getParameter("submit");
            if (submit == null) {//show UpdateProduct.jsp
                String TransferReceiptID = request.getParameter("TransferReceiptID");
                TransferReceipt p = dao.searchTransferReceipt(TransferReceiptID);

                request.setAttribute("p", p);
                request.getRequestDispatcher("jsp/UpdateProductJSTL.jsp").forward(request, response);
            } else {
                String TransferReceiptID = request.getParameter("TransferReceiptID"),
                        ProductID = request.getParameter("ProductID"),
                        FromInventoryID = request.getParameter("FromInventoryID"),
                        ToInventoryID = request.getParameter("ToInventoryID");
                int Quantity = Integer.parseInt(request.getParameter("Quantity"));
                Date TransferDate = Date.valueOf(request.getParameter("TransferDate"));
                String Note = request.getParameter("Note");
                TransferReceipt p = new TransferReceipt(TransferReceiptID, ProductID, FromInventoryID, ToInventoryID, Quantity, TransferDate, Note);

                dao.updateTransferReceipt(p);
//                Vector<Products> list = dao.getAllProduct(SQL);
//                request.setAttribute("data", list);
//                request.setAttribute("pageTitle", "Product Manager");
//                request.setAttribute("tableTitle", "List of Product");
//                request.getRequestDispatcher("jsp/ProductJSP.jsp").forward(request, response);

                response.sendRedirect("TransferReceiptController");
            }
        }
        
        
        
        if (service.equals("addTransferReceipt")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                request.getRequestDispatcher("jsp/InsertTransferReceipt.jsp").forward(request, response);
            } else {
                String TransferReceiptID = request.getParameter("TransferReceiptID"),
                        ProductID = request.getParameter("ProductID"),
                        FromInventoryID = request.getParameter("FromInventoryID"),
                        ToInventoryID = request.getParameter("ToInventoryID");
                int Quantity = Integer.parseInt(request.getParameter("Quantity"));
                Date TransferDate = Date.valueOf(request.getParameter("TransferDate"));
                String Note = request.getParameter("Note");
                TransferReceipt p = new TransferReceipt(TransferReceiptID, ProductID, FromInventoryID, ToInventoryID, Quantity, TransferDate, Note);

                dao.insertTransferReceipt(p);
                Vector<TransferReceipt> list = dao.getAllTransferReceipt(SQL);
                request.setAttribute("data", list);
                request.setAttribute("pageTitle", "TransferReceipt Manager");
                request.setAttribute("tableTitle", "List of TransferReceipt");
                request.getRequestDispatcher("TransferReceiptHTML/ListTransferReceipt.jsp").forward(request, response);

//            response.sendRedirect("ServletProduct_JSP");
            }

        }

        if (service.equals("listTransferReceipt")) {
            String submit = request.getParameter("submit");
            //Call Models
            Vector<TransferReceipt> list;
            if (submit == null) {
                list = dao.getAllTransferReceipt(SQL);
            } else {
                String name = request.getParameter("TransferReceiptID");
                list = dao.getAllTransferReceipt("SELECT *\n"
                        + "FROM TransferReceipt\n"
                        + "WHERE TransferReceiptID like N'%" + name + "%'");
            }
            //Set data for view
            request.setAttribute("data", list);
            request.setAttribute("pageTitle", "TransferReceipt Manager");
            request.setAttribute("tableTitle", "List of TransferReceipt");
            //Select view
            request.getRequestDispatcher("TransferReceiptHTML/ListTransferReceipt.jsp").forward(request, response);

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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TransferReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TransferReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
