/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ExportReceiptDAO;
import Dal.ExportReceiptDetailDAO;
import Dal.ImportReceiptDAO;
import Dal.ImportReceiptDetailDAO;
import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.SupplierDAO;
import Dal.TypeExportReceiptDAO;
import Dal.TypeImportReceiptDAO;
import Models.ExportReceipt;
import Models.ExportReceiptDetail;
import Models.ImportReceipt;
import Models.ImportReceiptDetail;
import Models.Inventory;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class AddExportReceipt extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * =======
     *
     * /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. >>>>>>> Stashed changes
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String databaseName = (String) request.getSession().getAttribute("databaseName");
            Connection conn = new DBContext(databaseName).getConnection();
            response.setContentType("text/html;charset=UTF-8");

            EmployeeDAO empDao = new EmployeeDAO(conn);
            TypeExportReceiptDAO typeImp = new TypeExportReceiptDAO(conn);
            ShopDAO shopDao = new ShopDAO(conn);
            SupplierDAO supDAO = new SupplierDAO(conn);
            ProductDAO ProDAO = new ProductDAO(conn);
            InventoryDAO ivtDAO = new InventoryDAO(conn);
            request.setAttribute("listEmp", empDao.getAllEmployee());
            request.setAttribute("listSup", supDAO.getAllSuppliers());
            request.setAttribute("listShop", shopDao.getAllShops());
            request.setAttribute("listType", typeImp.getAllTypeExportReceipts(1));
            request.setAttribute("listProduct", ProDAO.getAllProducts());
            request.setAttribute("listIvt", ivtDAO.getAllInventories());
            String message = request.getParameter("message");
            if (message != null && message.equals("add_success")) {
                request.setAttribute("successMessage", "Thêm phiếu xuất hàng thành công!");
            }
            request.getRequestDispatcher("AddExportReceipt.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AddExportReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * =======
     * request.getRequestDispatcher("AddExportReceipt.jsp").forward(request,
     * response); } * //
     * <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
     * /**
     * Handles the HTTP <code>GET</code> method. >>>>>>> Stashed changes
     *
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
     *
     * ======= throws ServletException, IOException { processRequest(request,
     * response); } * /** Handles the HTTP <code>POST</code> method. >>>>>>>
     * Stashed changes
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        String code = request.getParameter("code");
        String employeeID = request.getParameter("EmployeeID");
        String shopID_raw = request.getParameter("shopID");
        Integer shopID = Integer.parseInt(shopID_raw);
        String importDateStr = request.getParameter("Date");
        if (code == null || employeeID == null || shopID_raw == null || importDateStr == null) {
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

        double value = Double.parseDouble(request.getParameter("Total"));

        try (Connection conn = new DBContext(databaseName).getConnection()) {
            ExportReceiptDAO ExreceiptDAO = new ExportReceiptDAO(conn);
            InventoryDAO inventoryDAO = new InventoryDAO(conn);

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

            // Thêm phiếu xuất
            ExreceiptDAO.insert(receipt);
            ExportReceiptDetailDAO ExportReceipt = new ExportReceiptDetailDAO(conn);
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
    int productId = Integer.parseInt(exportDetail.getProductID());
    int exportQty = exportDetail.getQuantity();

    // Lấy danh sách inventory sắp xếp từ cũ đến mới
    List<Inventory> inventoryList = inventoryDAO.getListInventoryBy(productId, shopID);

    for (int i = 0; i < size && exportQty > 0; i++) {
        Inventory inv = inventoryList.get(i);
        int currentQty = inv.getQuantity();

        if (exportQty >= currentQty) {
            // Trừ hết số lượng hiện có
            inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), 0);
            exportQty -= currentQty;
        } else {
            // Trừ một phần, còn lại giữ nguyên
            inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), currentQty - exportQty);
            exportQty = 0;
        }

        // Nếu đây là bản ghi cuối cùng và vẫn còn số lượng cần trừ => cho phép âm
        if (i == size - 1 && exportQty > 0) {
            int newQty = -exportQty;
            inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);
            exportQty = 0;
        }
    }
}

            //Tạo thông báo

            response.sendRedirect("ExportReceiptServlet?message=add_success");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi thêm phiếu nhập: " + e.getMessage());
            request.getRequestDispatcher("add_import_receipt.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * ======= throws ServletException, IOException { processRequest(request,
     * response); }
     *
     * /**
     * Returns a short description of the servlet. >>>>>>> Stashed changes
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
