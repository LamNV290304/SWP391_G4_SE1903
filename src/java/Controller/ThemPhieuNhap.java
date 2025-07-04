/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.ImportReceiptDAO;
import Dal.ImportReceiptDetailDAO;
import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Models.ImportReceipt;
import Models.ImportReceiptDetail;
import Models.Inventory;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Thai Anh
 */
public class ThemPhieuNhap extends HttpServlet {
   
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
            out.println("<title>Servlet ThemPhieuNhap</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ThemPhieuNhap at " + request.getContextPath () + "</h1>");
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
          try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ThemPhieuNhap</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Hoan thanh 1"+"</h1>");
            out.println("</body>");
            out.println("</html>");
        }
          int importReceiptID = Integer.parseInt(request.getParameter("importReceiptID"));
        String code = request.getParameter("code");
        String supplierID = request.getParameter("SupplierID");
        String employeeID = request.getParameter("EmployeeID");
        String shopID = request.getParameter("shopID");
        String importDateStr = request.getParameter("Date");

Date importDate = null;
if (importDateStr != null && !importDateStr.isEmpty()) {
    try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        importDate = formatter.parse(importDateStr);
        
        // Nếu cần kiểm tra:
        System.out.println("Ngày nhập hàng (java.util.Date): " + importDate);
    } catch (Exception e) {
        e.printStackTrace(); // hoặc xử lý lỗi
    }
}

        String note= request.getParameter("note");
          
        double value= Double.parseDouble(request.getParameter("Total"));
    
        try (Connection conn = new DBContext("SWP4").getConnection()) {
            
            ImportReceiptDAO receiptDAO = new ImportReceiptDAO(conn);
            InventoryDAO inventoryDAO = new InventoryDAO(conn);
            
         ImportReceiptDetailDAO receiptDetailDAO = new ImportReceiptDetailDAO(conn);
         
ProductDAO productDAO = new ProductDAO(conn);
ShopDAO shopDAO = new ShopDAO();
            // Tạo đối tượng phiếu nhập
            ImportReceipt receipt = new ImportReceipt();
            receipt.setImportReceiptID(importReceiptID);
            receipt.setCode(code); // Tên sản phẩm không cần ở đây
            receipt.setSupplierID(supplierID);  
            receipt.setEmployeeID(employeeID);// Tên cửa hàng không cần ở đây
            receipt.setShopID(shopID);
            receipt.setReceiptDate(importDate);
            receipt.setNote(note);
            receipt.setTotalAmount(value);
            receipt.setStatus(true );
  
            // Thêm phiếu nhập
            receiptDAO.insertImportReceipt(receipt);
            
            List<ImportReceiptDetail> listImportDetail = new ArrayList<>();
             
            String[] importReceiptDetailIDs = request.getParameterValues("importReceiptDetailID[]");
            
String[] importReceiptIDs = request.getParameterValues("importReceiptID[]");
String[] productIDs = request.getParameterValues("productID[]");
String[] quantities = request.getParameterValues("quantity[]");
String[] prices = request.getParameterValues("price[]");
String[] notes = request.getParameterValues("note[]");

int size= importReceiptDetailIDs.length;
for(int i=0;i<size;i++){
    ImportReceiptDetail importDetail = new ImportReceiptDetail(Integer.parseInt(importReceiptDetailIDs[i]), 
            Integer.parseInt(importReceiptIDs[i]), 
            productIDs[i],  Integer.parseInt(quantities[i]), Double.parseDouble(prices[i]), notes[i]);
    listImportDetail.add(importDetail);
      
    receiptDetailDAO.insertDetail(importDetail);

}

for(ImportReceiptDetail importDetail : listImportDetail){
   
     // Kiểm tra và cập nhật tồn kho
            Inventory inv = inventoryDAO.getInventoryByShopAndProduct( importDetail.getProductID(),shopID);
            
          
            if (inv != null) {
                 
                      int newQty = inv.getQuantity() + importDetail.getQuantity();
                 
                inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);
                 
            } else {
                 
                // Tạo mới hàng tồn kho nếu chưa có
                Inventory newInv = new Inventory();
              
                newInv.setInventoryID("INV" + System.currentTimeMillis()); // ID tạm thời
                  try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ThemPhieuNhap</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Hoan thanh 2"+"</h1>");
            out.println("</body>");
            out.println("</html>");
        }
                newInv.setProduct(productDAO.getProductById(importDetail.getProductID()));
                
                newInv.setShop(shopDAO.getShopByID(shopID, "SWP4"));
                newInv.setQuantity(importDetail.getQuantity());
                newInv.setLastUpdated(Timestamp.from(Instant.now()));
                inventoryDAO.insertInventory(newInv); 
            }
}
           

            response.sendRedirect("ImportReceipt.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi thêm phiếu nhập: " + e.getMessage());
            request.getRequestDispatcher("add_import_receipt.jsp").forward(request, response);
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
