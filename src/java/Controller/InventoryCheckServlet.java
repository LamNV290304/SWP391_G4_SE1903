/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ImportReceiptDAO;
import Dal.ImportReceiptDetailDAO;
import Dal.InventoryCheckDAO;
import Dal.InventoryCheckDetailDAO;
import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.SupplierDAO;
import Dal.TypeImportReceiptDAO;
import Models.Employee;
import Models.ImportReceipt;
import Models.ImportReceiptDetail;
import Models.Inventory;
import Models.InventoryCheck;
import Models.InventoryCheckDetail;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class InventoryCheckServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        DBContext connection = new DBContext("Test");
        InventoryCheckDAO dao = new InventoryCheckDAO(connection.getConnection());
        EmployeeDAO daoEmp = new EmployeeDAO(connection.getConnection());
        ShopDAO daoShop = new ShopDAO(connection.getConnection());

        // 🔍 Lấy tham số lọc
        String shopId = request.getParameter("shopID");
        String empId = request.getParameter("employeeID");
        String fromDate = request.getParameter("fromdate");
        String toDate = request.getParameter("todate");

        // 🔍 Gọi DAO để lọc danh sách
        List<InventoryCheck> listFiltered = dao.filterInventoryChecks(shopId, empId, fromDate, toDate);

        // Gán vào request
        request.setAttribute("listIvt", listFiltered);
        request.setAttribute("listShop", daoShop.getAllShops());
        request.setAttribute("listEmp", daoEmp.listAllEmployeeDTO());

        // Giữ lại giá trị cũ để hiển thị lại trên giao diện
        request.setAttribute("shopID", shopId);
        request.setAttribute("employeeID", empId);
        request.setAttribute("fromdate", fromDate);
        request.setAttribute("todate", toDate);

        // Thông báo nếu có
        String message = request.getParameter("message");
        if ("add_success".equals(message)) {
            request.setAttribute("successMessage", "Thêm phiếu kiểm kê thành công!");
        }

        request.getRequestDispatcher("listInventoryCheck.jsp").forward(request, response);
        /*try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InventoryCheckServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InventoryCheckServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
         */
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
            Logger.getLogger(InventoryCheckServlet.class.getName()).log(Level.SEVERE, null, ex);
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

        if (action != null && receiptIdRaw != null) {
            try (Connection conn = new DBContext("Test").getConnection()) {
                InventoryCheckDAO inventoryCheckDAO = new InventoryCheckDAO(conn);
                InventoryCheckDetailDAO detailDAO = new InventoryCheckDetailDAO(conn);
                InventoryDAO inventoryDAO = new InventoryDAO(conn);
                ProductDAO productDAO = new ProductDAO(conn);
                ShopDAO shopDAO = new ShopDAO(conn);

                int receiptId = Integer.parseInt(receiptIdRaw);

                if (action.equals("delete")) {
                    inventoryCheckDAO.deleteInventoryCheck(receiptId);
                    for (InventoryCheckDetail detail : detailDAO.getDetailsByInventoryCheckID(receiptId)) {

                        // Kiểm tra và cập nhật tồn kho
                        Inventory inv = inventoryDAO.getInventoryByShopAndProduct(detail.getProductID(),
                                inventoryCheckDAO.getInventoryCheckByID(receiptId).getShopID());

                        if (inv != null) {
                            int quantity = 0;
                            if (detail.getQuantitySystem() > detail.getQuantityActual()) {
                                quantity = detail.getQuantitySystem() - detail.getQuantityActual();
                            } else if (detail.getQuantitySystem() < detail.getQuantityActual()) {
                                quantity = detail.getQuantitySystem() - detail.getQuantityActual();
                            }
                            int newQty = inv.getQuantity() + quantity;

                            inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);
                        }
                    }
                    request.getRequestDispatcher("Home.jsp").forward(request, response);
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(InventoryCheckServlet.class.getName()).log(Level.SEVERE, null, ex);
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
