/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Models.Employee;
import Context.DBContext;
import Dal.*;
import Models.Shop;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Home extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String fromDateParam = request.getParameter("from");
    String toDateParam = request.getParameter("to");

    Date fromDate = (fromDateParam != null && !fromDateParam.isEmpty())
            ? Date.valueOf(fromDateParam)
            : Date.valueOf(java.time.LocalDate.now().withDayOfMonth(1));
    Date toDate = (toDateParam != null && !toDateParam.isEmpty())
            ? Date.valueOf(toDateParam)
            : Date.valueOf(java.time.LocalDate.now());

    try {
         String databaseName = (String) request.getSession().getAttribute("databaseName");
        DBContext db = new DBContext(databaseName);
        String shopIdRaw = request.getParameter("shopId");
        int shopId = (shopIdRaw != null && !shopIdRaw.isEmpty()) ? Integer.parseInt(shopIdRaw) : 0;
        request.setAttribute("shopId", shopId);

        ProductDAO productDAO = new ProductDAO(db.getConnection());
        ReceiptVoucherDAO receiptDAO = new ReceiptVoucherDAO(db.getConnection());
        PaymentVoucherDAO paymentDAO = new PaymentVoucherDAO(db.getConnection());
        ShopDAO shopDAO = new ShopDAO(db.getConnection());
        InvoiceDAO invoiceDAO = new InvoiceDAO(db.getConnection());
        CustomerDAO customerDAO= new CustomerDAO(db.getConnection());
        InventoryDAO inventoryDAO = new InventoryDAO(db.getConnection());
        ImportReceiptDAO importDao = new ImportReceiptDAO(db.getConnection());
        
        int totalProducts = productDAO.getAllProducts().size();
        int totalShops = shopDAO.getAllShops().size();
        List<Shop> shops = shopDAO.getAllShops();
        double totalIncome = invoiceDAO.getTotalAmountDateRange(shopId,fromDate, toDate);
        int invoiceCount = invoiceDAO.countInvoiceByDateRange(fromDate, toDate,shopId);
        double avgPerInvoice =totalIncome/invoiceCount;
        int customerCount = customerDAO.getCustomersByShopAndDateRange(shopId, fromDate, toDate).size();
        double totalSalesAmount = invoiceDAO.getTotalAmountDateRange(shopId,fromDate, toDate);
        double avgPerCustomer = customerCount == 0 ? 0 : totalSalesAmount / customerCount;
        BigDecimal totalInventory = inventoryDAO.getTotalQuantityByShop(shopId);
        int outOfStock = inventoryDAO.countProductsInStockByShop(shopId, -9999,1);
        int belowThreshold = inventoryDAO.countProductsInStockByShop(shopId, 0,10);
        int aboveThreshold = inventoryDAO.countProductsInStockByShop(shopId, 0,100);
        double materialCost = importDao.getImportAmountByShopAndDateRange(shopId,fromDate, toDate);
double materialCostPercent = 100.0 * materialCost / totalSalesAmount;
materialCostPercent = Math.round(materialCostPercent * 100.0) / 100.0;
        
        double totalExpense = paymentDAO.getTotalExpense(fromDate, toDate);
        int totalPayments = paymentDAO.countPaymentVouchers(fromDate, toDate);
        
        int totalSoldQuantity = invoiceDAO.getTotalQuantityByShopAndDateRange(0, fromDate, toDate);
        
        

        request.setAttribute("shops", shops);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalShops", totalShops);
        request.setAttribute("totalIncome", totalIncome);
        request.setAttribute("invoiceCount", invoiceCount);
        request.setAttribute("avgPerInvoice", avgPerInvoice);
        request.setAttribute("customerCount", customerCount);
        request.setAttribute("avgPerCustomer", avgPerCustomer);
        request.setAttribute("totalInventory", totalInventory);
        request.setAttribute("outOfStock", outOfStock);
        request.setAttribute("belowThreshold", belowThreshold);
        request.setAttribute("aboveThreshold", aboveThreshold);
        request.setAttribute("materialCost", materialCost);
        request.setAttribute("materialCostPercent", materialCostPercent);
        
        
        
        
        request.setAttribute("totalPayments", totalPayments);
        request.setAttribute("totalSalesAmount", totalSalesAmount);
        request.setAttribute("totalSoldQuantity", totalSoldQuantity);
        
        request.setAttribute("discount", 0); // placeholder
        request.setAttribute("revenueChange", invoiceDAO.getRevenueChangePercent(fromDate, toDate,shopId));
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);

        request.getRequestDispatcher("Home.jsp").forward(request, response);

    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(500, "Lỗi xử lý dữ liệu trang chủ");
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
        doGet(request, response);
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
