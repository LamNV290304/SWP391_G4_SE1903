package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */


import Context.DBContext;
import Dal.*;
import Models.*;
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
public class ImportReceiptServlet extends HttpServlet {
   
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
        DBContext connection = new DBContext("SWP7");
    ImportReceiptDAO dao = new ImportReceiptDAO(connection.getConnection());
    ImportReceiptDetailDAO detailDao = new ImportReceiptDetailDAO(connection.getConnection());
    List<ImportReceipt> list = dao.getAllImportReceipts();
    request.setAttribute("listDetail", detailDao.getAllDetails());
    request.setAttribute("listIR", list);
    request.getRequestDispatcher("ImportReceipt.jsp").forward(request, response);
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
         String action = request.getParameter("action");
    String receiptIdRaw = request.getParameter("receiptId");

    if (action != null && receiptIdRaw != null) {
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            ImportReceiptDAO importReceiptDAO = new ImportReceiptDAO(conn);
            ImportReceiptDetailDAO detailDAO = new ImportReceiptDetailDAO(conn);
            InventoryDAO inventoryDAO = new InventoryDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);
            ShopDAO shopDAO = new ShopDAO();

            int receiptId = Integer.parseInt(receiptIdRaw);

            if (action.equals("delete")) {
                importReceiptDAO.deleteImportReceipt(receiptId);
                request.getRequestDispatcher("ImportReceiptServlet").forward(request, response);
                return; // ✅ Ngăn servlet chạy tiếp
            } else if (action.equals("edit")) {
                ImportReceipt receipt = importReceiptDAO.getImportReceiptByID(receiptId);
                List<ImportReceiptDetail> detailList = detailDAO.getDetailsByReceiptID(receiptId);

                EmployeeDAO empDao = new EmployeeDAO(conn);
                TypeImportReceiptDAO typeImp = new TypeImportReceiptDAO(conn);
                SupplierDAO supDAO = new SupplierDAO(conn);

                request.setAttribute("receipt", receipt);
                request.setAttribute("details", detailList);
                request.setAttribute("listEmp", empDao.getAllEmployee());
                request.setAttribute("listSup", supDAO.getAllSuppliers());
                request.setAttribute("listShop", shopDAO.getAllShops("SWP7"));
                request.setAttribute("listType", typeImp.getAllTypeImportReceipts());
                request.setAttribute("listProduct", productDAO.getAllProducts());

                request.getRequestDispatcher("EditImportReceipt.jsp").forward(request, response);
                return; // ✅ Ngăn servlet chạy tiếp
            }
        } catch (Exception e) {
            // Log lỗi ra console
            System.err.println("Lỗi xử lý: " + e.getMessage());
            e.printStackTrace();

            // Set lỗi và forward sang trang báo lỗi
            request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("ImportReceipt.jsp").forward(request, response);
            return; // ✅ Ngăn servlet chạy tiếp
        }
    } else {
        // Nếu thiếu tham số thì chuyển hướng hoặc báo lỗi
        response.sendRedirect("ImportReceiptServlet"); // hoặc forward nếu cần
    }
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
