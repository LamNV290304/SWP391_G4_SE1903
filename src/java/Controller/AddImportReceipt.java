///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//
//package Controller;
//
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import Dal.ImportReceiptDAO;
//import Dal.InventoryDAO;
//import Models.ImportReceipt;
//import Models.Inventory;
//import Models.Product;
//import Models.Shop;
//import Context.DBContext;
//import Dal.EmployeeDAO;
//import Dal.ImportReceiptDetailDAO;
//import Dal.ProductDAO;
//import Dal.ShopDAO;
//import Dal.SupplierDAO;
//import Dal.TypeImportReceiptDAO;
//import Models.ImportReceiptDetail;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
///**
// *
// * @author Thai Anh
// */
//public class AddImportReceipt extends HttpServlet {
//   
//    /** 
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//    throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        Connection conn = new DBContext("SWP6").getConnection();
//        EmployeeDAO empDao = new EmployeeDAO(conn);
//        TypeImportReceiptDAO typeImp = new TypeImportReceiptDAO(conn);
//        ShopDAO shopDao = new ShopDAO();
//        SupplierDAO supDAO = new SupplierDAO(conn);
//        request.setAttribute("listSup", supDAO.getAllSuppliers());
//        request.setAttribute("listShop", shopDao.getAllShops("SWP6"));
//        request.setAttribute("listType", typeImp.getAllTypeImportReceipts());
//        request.getRequestDispatcher("AddImportReceipt.jsp").forward(request, response);
//    } 
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /** 
//     * Handles the HTTP <code>GET</code> method.
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//    throws ServletException, IOException {
//        processRequest(request, response);
//    } 
//
//    /** 
//     * Handles the HTTP <code>POST</code> method.
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//    throws ServletException, IOException {
//     
//          int importReceiptID = Integer.parseInt(request.getParameter("importReceiptID"));
//        String code = request.getParameter("code");
//        String supplierID = request.getParameter("SupplierID");
//        String employeeID = request.getParameter("EmployeeID");
//        String shopID = request.getParameter("shopID");
//        String importDateStr = request.getParameter("Date");
//
//Date importDate = null;
//if (importDateStr != null && !importDateStr.isEmpty()) {
//    try {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        importDate = formatter.parse(importDateStr);
//        
//        // Nếu cần kiểm tra:
//        System.out.println("Ngày nhập hàng (java.util.Date): " + importDate);
//    } catch (Exception e) {
//        e.printStackTrace(); // hoặc xử lý lỗi
//    }
//}
//
//        String note= request.getParameter("note");
//          
//        double value= Double.parseDouble(request.getParameter("Total"));
//    
//        try (Connection conn = new DBContext("SWP6").getConnection()) {
//            
//            ImportReceiptDAO receiptDAO = new ImportReceiptDAO(conn);
//            InventoryDAO inventoryDAO = new InventoryDAO(conn);
//            
//         ImportReceiptDetailDAO receiptDetailDAO = new ImportReceiptDetailDAO(conn);
//         
//ProductDAO productDAO = new ProductDAO(conn);
//ShopDAO shopDAO = new ShopDAO();
//            // Tạo đối tượng phiếu nhập
//            ImportReceipt receipt = new ImportReceipt();
//            receipt.setImportReceiptID(importReceiptID);
//            receipt.setCode(code); // Tên sản phẩm không cần ở đây
//            receipt.setSupplierID(supplierID);  
//            receipt.setEmployeeID(employeeID);// Tên cửa hàng không cần ở đây
//            receipt.setShopID(shopID);
//            receipt.setReceiptDate(importDate);
//            receipt.setNote(note);
//            receipt.setTotalAmount(value);
//            receipt.setStatus(true );
//   
//            // Thêm phiếu nhập
//            receiptDAO.insertImportReceipt(receipt);
//            
//            List<ImportReceiptDetail> listImportDetail = new ArrayList<>();
//             
//            String[] importReceiptDetailIDs = request.getParameterValues("importReceiptDetailID[]");
//            
//String[] importReceiptIDs = request.getParameterValues("importReceiptID[]");
//String[] productIDs = request.getParameterValues("productID[]");
//String[] quantities = request.getParameterValues("quantity[]");
//String[] prices = request.getParameterValues("price[]");
//String[] notes = request.getParameterValues("note[]");
//
//int size= importReceiptDetailIDs.length;
//for(int i=0;i<size;i++){
//    ImportReceiptDetail importDetail = new ImportReceiptDetail(Integer.parseInt(importReceiptDetailIDs[i]), 
//            Integer.parseInt(importReceiptIDs[i]), 
//            productIDs[i],  Integer.parseInt(quantities[i]), Double.parseDouble(prices[i]), notes[i]);
//    listImportDetail.add(importDetail);
//      
//    receiptDetailDAO.insertDetail(importDetail);
//
//}
//
//for(ImportReceiptDetail importDetail : listImportDetail){
//   
//     // Kiểm tra và cập nhật tồn kho
//            Inventory inv = inventoryDAO.getInventoryByShopAndProduct( importDetail.getProductID(),shopID);
//            
//          
//            if (inv != null) {
//                 
//                      int newQty = inv.getQuantity() + importDetail.getQuantity();
//                 
//                inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);
//                
//            } else {
//                 
//                // Tạo mới hàng tồn kho nếu chưa có
//                Inventory newInv = new Inventory();
//              
//                newInv.setInventoryID("INV" + System.currentTimeMillis()); // ID tạm thời
//                  
//                newInv.setProduct(productDAO.getProductById(importDetail.getProductID()));
//                
//                newInv.setShop(shopDAO.getShopByID(shopID, "SWP6"));
//                
//                newInv.setQuantity(importDetail.getQuantity());
//                newInv.setLastUpdated(Timestamp.from(Instant.now()));
//                inventoryDAO.insertInventory(newInv); 
//            }
//}
//           
//
//            response.sendRedirect("ImportReceipt.jsp");
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Lỗi khi thêm phiếu nhập: " + e.getMessage());
//            request.getRequestDispatcher("add_import_receipt.jsp").forward(request, response);
//        }
//    }
//
//    /** 
//     * Returns a short description of the servlet.
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}

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
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import static java.time.LocalDate.now;
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
               
                // Sau khi xóa chi tiết, xóa phiếu nhập
                importReceiptDAO.deleteImportReceipt(receiptId);

                // Chuyển hướng lại trang danh sách
                request.getRequestDispatcher("ImportReceiptServlet").forward(request, response);

            } else if (action.equals("edit")) {
                // Lấy thông tin phiếu nhập
                ImportReceipt receipt = importReceiptDAO.getImportReceiptByID(receiptId);
                List<ImportReceiptDetail> detailList = detailDAO.getDetailsByReceiptID(receiptId);

                // Lấy dữ liệu hỗ trợ để show form
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

                // Chuyển đến trang chỉnh sửa
                request.getRequestDispatcher("EditImportReceipt.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("ImportReceipt.jsp").forward(request, response);
            return;
        }
    }

    // Nếu không phải edit/delete thì hiển thị form thêm mới (mặc định)
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
        String shopID_raw = request.getParameter("shopID");
        String importDateStr = request.getParameter("Date");
if(code==null || supplierID==null || employeeID ==null || shopID_raw ==null || importDateStr ==null){
    request.setAttribute("erroll", "Type,Supplier,Employee,Shop,ImportDate must be not null");
    request.getRequestDispatcher("ErrolReceipt.jsp").forward(request, response);
}
Integer shopID = Integer.parseInt(shopID_raw);
Date importDate = null;
if (importDateStr != null && !importDateStr.isEmpty()) {
    try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        importDate = formatter.parse(importDateStr);
       Date now = new Date(); // lấy thời gian hiện tại

        if (importDate.after(now)) {
            request.setAttribute("erroll", "Date Invalid");
            request.getRequestDispatcher("ErrolReceipt.jsp").forward(request, response);
        }
        // Nếu cần kiểm tra:
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
            receipt.setShopID(shopID_raw);
            receipt.setReceiptDate(importDate);
            receipt.setNote(note);
            receipt.setTotalAmount(value);
            receipt.setStatus(true );
   
            // Thêm phiếu nhập
            receiptDAO.insertImportReceipt(receipt);
            
            List<ImportReceiptDetail> listImportDetail = new ArrayList<>();
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
            Inventory inv = inventoryDAO.getInventoryByShopAndProduct( Integer.parseInt(importDetail.getProductID()),Integer.parseInt(shopID_raw));
            
          
            if (inv != null) {
                 
                      int newQty = inv.getQuantity() + importDetail.getQuantity();
                 
                inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);
                
            } else {
                 
                // Tạo mới hàng tồn kho nếu chưa có
                Inventory newInv = new Inventory();
              
                //newInv.setInventoryID("INV" + System.currentTimeMillis()); // ID tạm thời
                  
                newInv.setProduct(productDAO.getProductById(Integer.parseInt(importDetail.getProductID())));
                
                newInv.setShop(shopDAO.getShopByID(shopID, "SWP7"));
                
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
