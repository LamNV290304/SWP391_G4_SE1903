/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Dal.ImportReceiptDAO;
import Dal.InventoryDAO;
import Models.ImportReceipt;
import Models.Inventory;
import Models.Product;
import Models.Shop;
import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ImportReceiptDetailDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.SupplierDAO;
import Dal.TypeImportReceiptDAO;
import Models.ImportReceiptDetail;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
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
public class AddImportReceipt extends HttpServlet {
   
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
        TypeImportReceiptDAO typeImp = new TypeImportReceiptDAO(conn);
        ShopDAO shopDao = new ShopDAO();
        SupplierDAO supDAO = new SupplierDAO(conn);
        ProductDAO ProDAO = new ProductDAO(conn);
       
        request.setAttribute("listEmp", empDao.getAllEmployee());
        request.setAttribute("listSup", supDAO.getAllSuppliers());
        request.setAttribute("listShop", shopDao.getAllShops("SWP7"));
        
        request.setAttribute("listType", typeImp.getAllTypeImportReceipts());
         
        request.setAttribute("listProduct", ProDAO.getAllProducts());
        
        request.getRequestDispatcher("AddImportReceipt.jsp").forward(request, response);
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
    
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            
            ImportReceiptDAO receiptDAO = new ImportReceiptDAO(conn);
            InventoryDAO inventoryDAO = new InventoryDAO(conn);
           
         ImportReceiptDetailDAO receiptDetailDAO = new ImportReceiptDetailDAO(conn);
         
ProductDAO productDAO = new ProductDAO(conn);
ShopDAO shopDAO = new ShopDAO();
            // Tạo đối tượng phiếu nhập
            ImportReceipt receipt = new ImportReceipt();
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
              try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Test</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Test at 1</h1>");
            out.println("</body>");
            out.println("</html>");
        }
            
String[] productIDs = request.getParameterValues("productID[]");
String[] quantities = request.getParameterValues("quantity[]");
String[] prices = request.getParameterValues("price[]");
String[] notes = request.getParameterValues("note[]");

int size= productIDs.length;
for(int i=0;i<size;i++){
    ImportReceiptDetail importDetail = new ImportReceiptDetail( 
            receiptDetailDAO.getImportReceiptIDByInfo(receipt), 
            productIDs[i],  Integer.parseInt(quantities[i]), Double.parseDouble(prices[i]), notes[i]);
    listImportDetail.add(importDetail);
      
    receiptDetailDAO.insertDetail(importDetail);

}

for(ImportReceiptDetail importDetail : listImportDetail){
   
     // Kiểm tra và cập nhật tồn kho
            Inventory inv = inventoryDAO.getInventoryByShopAndProduct( Integer.parseInt(importDetail.getProductID()),Integer.parseInt(shopID));
            
          
            if (inv != null) {
                 
                      int newQty = inv.getQuantity() + importDetail.getQuantity();
                 
                inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);
                
            } else {
                 
                // Tạo mới hàng tồn kho nếu chưa có
                Inventory newInv = new Inventory();
              
                //newInv.setInventoryID("INV" + System.currentTimeMillis()); // ID tạm thời
                  
                newInv.setProduct(productDAO.getProductById(Integer.parseInt(importDetail.getProductID())));
                
                newInv.setShop(shopDAO.getShopByID(shopID, "SWP6"));
                
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
