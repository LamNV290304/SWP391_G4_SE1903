/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.ExportReceiptDAO;
import Dal.ExportReceiptDetailDAO;
import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Models.ExportReceipt;
import Models.ExportReceiptDetail;
import Models.Inventory;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.math.BigDecimal.valueOf;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class UpdateExceiptReceipt extends HttpServlet {
   
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
            out.println("<title>Servlet UpdateExceiptReceipt</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateExceiptReceipt at " + request.getContextPath () + "</h1>");
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
        String code = request.getParameter("code");
        String employeeID = request.getParameter("EmployeeID");
        String shopID_raw = request.getParameter("shopID");
        Integer shopID = Integer.parseInt(shopID_raw);
        String importDateStr = request.getParameter("Date");
        String exportReceiptId_raw = request.getParameter("ReceiptID");
        if (code == null || employeeID == null || shopID_raw == null || importDateStr == null) {
            request.setAttribute("erroll", "Type,Supplier,Employee,Shop,ImportDate must be not null");
            request.getRequestDispatcher("ErrolReceipt.jsp").forward(request, response);
        }
        int exportReceiptId = Integer.parseInt(exportReceiptId_raw);
        Date importDate = null;
        if (importDateStr != null && !importDateStr.isEmpty()) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                importDate = formatter.parse(importDateStr);
                Date now = new Date(); // lấy thời gian hiện tại

                if (importDate.after(now)) {
                    request.setAttribute("erroll", "Date import Invalid");
                    request.getRequestDispatcher("ErrolReceipt.jsp").forward(request, response);
                }
                // Nếu cần kiểm tra:
            } catch (Exception e) {
                e.printStackTrace(); // hoặc xử lý lỗi
            }
        }

        String note = request.getParameter("note");

        double value = Double.parseDouble(request.getParameter("Total"));
String databaseName = (String) request.getSession().getAttribute("databaseName");
        try (Connection conn = new DBContext(databaseName).getConnection()) {
            ExportReceiptDAO ExreceiptDAO = new ExportReceiptDAO(conn);
            InventoryDAO inventoryDAO = new InventoryDAO(conn);
            ExportReceiptDetailDAO ExportReceipt = new ExportReceiptDetailDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);
            ShopDAO shopDAO = new ShopDAO(conn);
            // Tạo đối tượng phiếu nhập
            ExportReceipt receipt = new ExportReceipt();
            receipt.setTypeID(Integer.parseInt(code));
            receipt.setEmployeeID(Integer.parseInt(employeeID));
            receipt.setShopID(shopID);
            receipt.setReceiptDate(importDate);
            receipt.setNote(note);
            receipt.setTotalAmount(valueOf(value));
            receipt.setStatus(true);
            for (ExportReceiptDetail exportDetail : ExportReceipt.getDetailsByReceiptID(exportReceiptId)) {

                // Kiểm tra và cập nhật tồn kho
                Inventory inv = inventoryDAO.getInventoryByShopAndProduct(Integer.parseInt(exportDetail.getProductID()), Integer.parseInt(shopID_raw));

                if (inv != null) {

                    int newQty = inv.getQuantity() + exportDetail.getQuantity();

                    inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);

                } 
            }
            // Thêm phiếu xuất
            ExreceiptDAO.insert(receipt);
            
            List<ExportReceiptDetail> listExportDetail = new ArrayList<>();
            String[] productIDs = request.getParameterValues("productID[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] prices = request.getParameterValues("price[]");
            String[] notes = request.getParameterValues("note[]");
            int size = productIDs.length;
            for (int i = 0; i < size; i++) {

                ExportReceiptDetail exportDetail = new ExportReceiptDetail(
                        ExreceiptDAO.getNewest().getExportReceiptID(),
                        productIDs[i], Integer.parseInt(quantities[i]), Double.parseDouble(prices[i]), notes[i]);

                listExportDetail.add(exportDetail);

                ExportReceipt.insertDetail(exportDetail);
            }

            for (ExportReceiptDetail exportDetail : listExportDetail) {

                // Kiểm tra và cập nhật tồn kho
                Inventory inv = inventoryDAO.getInventoryByShopAndProduct(Integer.parseInt(exportDetail.getProductID()), Integer.parseInt(shopID_raw));

                if (inv != null) {

                    int newQty = inv.getQuantity() - exportDetail.getQuantity();

                    inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);

                } else {

                    // Tạo mới hàng tồn kho nếu chưa có
                    Inventory newInv = new Inventory();

                    //newInv.setInventoryID("INV" + System.currentTimeMillis()); // ID tạm thời
                    newInv.setProduct(productDAO.getProductById(Integer.parseInt(exportDetail.getProductID())));

                    newInv.setShop(shopDAO.getShopById(shopID));

                    newInv.setQuantity(exportDetail.getQuantity());
                    newInv.setLastUpdated(Timestamp.from(Instant.now()));
                    inventoryDAO.insertInventory(newInv);
                }
            }

            response.sendRedirect("ExportReceipt.jsp");
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
