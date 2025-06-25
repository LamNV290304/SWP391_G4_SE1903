/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ExportReceiptDAO;
import Dal.ExportReceiptDetailDAO;
import Dal.InventoryCheckDAO;
import Dal.InventoryCheckDetailDAO;
import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.SupplierDAO;
import Dal.TypeExportReceiptDAO;
import Models.ExportReceiptDetail;
import Models.Inventory;
import Models.InventoryCheck;
import Models.InventoryCheckDetail;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.math.BigDecimal.valueOf;
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
        request.getRequestDispatcher("AddInventoryCheck.jsp").forward(request, response);
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
        String employeeID = request.getParameter("EmployeeID");
        String shopID_raw = request.getParameter("shopID");
        Integer shopID = Integer.parseInt(shopID_raw);
        String importDateStr = request.getParameter("Date");
        if (employeeID == null || shopID_raw == null || importDateStr == null) {
            request.setAttribute("erroll", "Type,Supplier,Employee,Shop,ImportDate must be not null");
            request.getRequestDispatcher("ErrolReceipt.jsp").forward(request, response);
        }
        
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

        try (Connection conn = new DBContext("SWP7").getConnection()) {
            InventoryCheckDAO inventoryCheckDao = new InventoryCheckDAO(conn);
            InventoryDAO inventoryDAO = new InventoryDAO(conn);

            ProductDAO productDAO = new ProductDAO(conn);
            ShopDAO shopDAO = new ShopDAO();
            // Tạo đối tượng phiếu nhập
            InventoryCheck ivtCheck = new InventoryCheck();
             
            ivtCheck.setEmployeeID(Integer.parseInt(employeeID));
            ivtCheck.setShopID(shopID);
            ivtCheck.setCheckDate(importDate);
            ivtCheck.setNote(note);

            // Thêm phiếu xuất
           inventoryCheckDao.insertInventoryCheck(ivtCheck);
            InventoryCheckDetailDAO ivtCheckDetailDao = new InventoryCheckDetailDAO(conn);
            List<InventoryCheckDetail> listIvtCheckDetail = new ArrayList<>();

            String[] productIDs = request.getParameterValues("productID[]");
            String[] systemQuantitys = request.getParameterValues("systemQuantity[]");
            String[] actualQuantity = request.getParameterValues("actualQuantity[]");
            String[] notes = request.getParameterValues("note[]");

            int size = productIDs.length;
            for (int i = 0; i < size; i++) {
                

                InventoryCheckDetail ivtDetail = new InventoryCheckDetail(
                        inventoryCheckDao.getLatestInventoryCheckID(), 
                        Integer.parseInt(productIDs[i]), Integer.parseInt(systemQuantitys[i]), 
                        Integer.parseInt(actualQuantity[i]), note);
             
                listIvtCheckDetail.add(ivtDetail);

              ivtCheckDetailDao.insertDetail(ivtDetail);
            }

            for (InventoryCheckDetail checkDetail : listIvtCheckDetail) {

                // Kiểm tra và cập nhật tồn kho
                Inventory inv = inventoryDAO.getInventoryByShopAndProduct(
                        checkDetail.getProductID(), Integer.parseInt(shopID_raw));

                if (inv != null) {

                    int newQty = checkDetail.getQuantityActual();
                    inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);

                } else {

                   request.setAttribute("erroll", "Hàng Hóa Không tồn tại trong kho!!");
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
