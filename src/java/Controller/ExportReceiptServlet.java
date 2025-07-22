/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.*;
import Models.ExportReceipt;
import Models.ExportReceiptDetail;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class ExportReceiptServlet extends HttpServlet {

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
        try {
            response.setContentType("text/html;charset=UTF-8");
            String databaseName = (String) request.getSession().getAttribute("databaseName");
            DBContext connection = new DBContext(databaseName);
            ShopDAO shopDAO = new ShopDAO(connection.getConnection());
            TypeExportReceiptDAO typeDAO = new TypeExportReceiptDAO(connection.getConnection());
            EmployeeDAO employeeDAO = new EmployeeDAO(connection.getConnection());
            ExportReceiptDAO dao = new ExportReceiptDAO(connection.getConnection());
            List<ExportReceipt> list = dao.getAll();
            request.setAttribute("listShop", shopDAO.getAllShops());
            request.setAttribute("Types", typeDAO.getAllTypeExportReceipts(1));
            request.setAttribute("listEmp", employeeDAO.getAllEmployee());
            request.setAttribute("listIE", list);
            String message = request.getParameter("message");
            if (message != null && message.equals("add_success")) {
                request.setAttribute("successMessage", "Thêm phiếu xuất hàng thành công!");
            }
            request.getRequestDispatcher("ExportReceipt.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        try {
            response.setContentType("text/html;charset=UTF-8");
            
            DBContext db = new DBContext(databaseName);
            ExportReceiptDAO dao = new ExportReceiptDAO(db.getConnection());
            
// Lấy filter từ form
String empIdStr = request.getParameter("employeeId");
String shopIdStr = request.getParameter("shopId");
String typeIdStr = request.getParameter("typeId");
String fromDateStr = request.getParameter("fromDate");
String toDateStr = request.getParameter("toDate");

Integer empId = null, shopId = null, typeId = null;
java.util.Date fromDate = null, toDate = null;
String message = request.getParameter("message");
if ("add_success".equals(message)) {
    request.setAttribute("successMessage", "Thêm phiếu xuất hàng thành công!");
}
try {
    if (empIdStr != null && !empIdStr.isEmpty()) {
        empId = Integer.parseInt(empIdStr);
    }
    if (shopIdStr != null && !shopIdStr.isEmpty()) {
        shopId = Integer.parseInt(shopIdStr);
    }
    if (typeIdStr != null && !typeIdStr.isEmpty()) {
        typeId = Integer.parseInt(typeIdStr);
    }
    if (fromDateStr != null && !fromDateStr.isEmpty()) {
        fromDate = java.sql.Date.valueOf(fromDateStr); // yyyy-MM-dd
    }
    if (toDateStr != null && !toDateStr.isEmpty()) {
        toDate = java.sql.Date.valueOf(toDateStr);
    }
} catch (IllegalArgumentException e) {
    e.printStackTrace(); // hoặc ghi log nếu cần
}

// Gọi DAO để lọc
List<ExportReceipt> list = dao.filter(empId, shopId, typeId, fromDate, toDate);

// Gửi dữ liệu về lại JSP
request.setAttribute("listIE", list);
request.setAttribute("listEmp", new EmployeeDAO(db.getConnection()).getAllEmployee());
request.setAttribute("listShop", new ShopDAO(db.getConnection()).getAllShops());
request.setAttribute("Types", new TypeExportReceiptDAO(db.getConnection()).getAllTypeExportReceipts(1));

// Truyền lại giá trị filter cho form giữ nguyên
request.setAttribute("selectedEmpId", empId);
request.setAttribute("selectedShopId", shopId);
request.setAttribute("selectedTypeId", typeId);
request.setAttribute("fromDate", fromDateStr);
request.setAttribute("toDate", toDateStr);

request.getRequestDispatcher("ExportReceipt.jsp").forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptServlet.class.getName()).log(Level.SEVERE, null, ex); 
        }

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
        String action = request.getParameter("action");
        String receiptIdRaw = request.getParameter("receiptId");
String databaseName = (String) request.getSession().getAttribute("databaseName");
        if (action != null && receiptIdRaw != null) {
            try (Connection conn = new DBContext(databaseName).getConnection()) {
                ExportReceiptDAO exportReceiptDAO = new ExportReceiptDAO(conn);
                ExportReceiptDetailDAO detailDAO = new ExportReceiptDetailDAO(conn);
                InventoryDAO inventoryDAO = new InventoryDAO(conn);
                ProductDAO productDAO = new ProductDAO(conn);
                ShopDAO shopDAO = new ShopDAO(conn);

                int receiptId = Integer.parseInt(receiptIdRaw);

                if (action.equals("delete")) {
                    exportReceiptDAO.delete(receiptId);
                    response.sendRedirect("ExportReceiptServlet");
                    return; // ✅ Ngăn servlet chạy tiếp
                } else if (action.equals("edit")) {
                    ExportReceipt receipt = exportReceiptDAO.getByID(receiptId);
                    List<ExportReceiptDetail> detailList = detailDAO.getDetailsByReceiptID(receiptId);
                    EmployeeDAO empDao = new EmployeeDAO(conn);
                    TypeExportReceiptDAO typeExp = new TypeExportReceiptDAO(conn);
                    SupplierDAO supDAO = new SupplierDAO(conn);
                    request.setAttribute("receipt", receipt);
                    request.setAttribute("details", detailList);
                    request.setAttribute("listEmp", empDao.getAllEmployee());
                    request.setAttribute("listShop", shopDAO.getAllShops());
                    request.setAttribute("listType", typeExp.getAllTypeExportReceipts(1));
                    request.setAttribute("listProduct", productDAO.getAllProducts());
                    request.getRequestDispatcher("EditExportReceipt.jsp").forward(request, response);
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
            response.sendRedirect("ExportReceiptServlet"); // hoặc forward nếu cần
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
