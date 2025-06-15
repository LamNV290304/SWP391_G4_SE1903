/*
         * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
         * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.InventoryDAO;
import Dal.InvoiceDAO;
import Dal.InvoiceDetailDAO;
import Dal.ShopDAO;
import Models.Invoice;
import Models.InvoiceDetail;
import Models.Shop;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.System.Logger;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duckh
 */
public class InvoiceServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    InvoiceDAO idao = new InvoiceDAO(connection.getConnection());
    InvoiceDetailDAO idetail = new InvoiceDetailDAO(connection.getConnection());
    InventoryDAO inventoryDAO = new InventoryDAO(connection.getConnection());
    ShopDAO sDAO = new ShopDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    private String sql = "SELECT * FROM [dbo].[Invoice]";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            listInvoices(request, response);
            return;
        }
        switch (action) {
            case "listDetail":
                listInvoiceDetail(request, response);
                break;
            default:
                listInvoices(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            listInvoices(request, response);
            return;
        }
        switch (action) {
            case "add":
                addInvoice(request, response);
                break;
            case "delete":
                deleteInvoice(request, response);
                break;
            case "update":
                updateInvoice(request, response);
                break;
            case "search":
                searchInvoice(request, response);
                break;
            case "addDetail":
                addInvoiceDetail(request, response);
                break;
            case "updateDetail":
                updateInvoiceDetail(request, response);
                break;
            default:
                listInvoices(request, response);
                break;
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void listInvoices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pageIndexParam = request.getParameter("page");
        int pageIndex = 1;
        if (pageIndexParam != null) {
            try {
                pageIndex = Integer.parseInt(pageIndexParam);
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }
        int pageSize = 5;
        int totalInvoices = idao.getTotalInvoiceCount();

        //tinh tong so trang can thiet de hien thi tat ca hoa don: Math.ceil la lam tron len
        int totalPages = (int) Math.ceil((double) totalInvoices / pageSize);

        List<Invoice> invoices = idao.getInvoicesByPage(pageIndex, pageSize);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("invoiceList", invoices);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("Invoice.jsp").forward(request, response);
    }

    private void updateInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String invoiceID = request.getParameter("invoiceID");
        String customerID = request.getParameter("customerID");
        String employee = request.getParameter("employeeID");
        String shopID = request.getParameter("shopID");
        Invoice oldInvoice = idao.searchInvoice(invoiceID);

        Timestamp invoiceDateTimestamp = oldInvoice.getInvoiceDate();

        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        String note = request.getParameter("note");
        boolean status = Boolean.parseBoolean(request.getParameter("status"));

        Invoice invoice = new Invoice(invoiceID, customerID, employee, shopID, invoiceDateTimestamp, totalAmount, note, status);
        boolean result = idao.updateInvoice(invoice);
        if (result) {
            System.out.println("Cập nhật thành công.");
        } else {
            System.out.println("Cập nhật thất bại.");
        }
        response.sendRedirect("InvoiceServlet");

    }

    private void updateInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String invoiceID = request.getParameter("invoiceID").trim();

            String productID = request.getParameter("productID").trim();
            String unitPriceStr = request.getParameter("unitPrice").trim();
            String quantityStr = request.getParameter("quantity").trim();
            String discountStr = request.getParameter("discount");
            String invoiceDetailIDParam = request.getParameter("invoiceDetailID");
            if (unitPriceStr == null || unitPriceStr.isEmpty()
                    || quantityStr == null || quantityStr.isEmpty()
                    || discountStr == null || discountStr.isEmpty()
                    || invoiceDetailIDParam == null || invoiceDetailIDParam.isEmpty()) {

                listInvoiceDetail(request, response);
                return;
            }

            double unitPrice = Double.parseDouble(unitPriceStr);
            int quantity = Integer.parseInt(quantityStr);
            double discount = Double.parseDouble(discountStr);
            int invoiceDetailID = Integer.parseInt(invoiceDetailIDParam);
            InvoiceDetail lay = idetail.getInvoiceDetailByInvoiceDetailID(invoiceDetailID);
            if (lay == null) {

                response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
                return;
            }
            String shopID = lay.getShopID();

            InvoiceDetail updatedDetail = new InvoiceDetail(invoiceDetailID, invoiceID, productID, unitPrice, quantity, discount);
            updatedDetail.setShopID(shopID);

            InvoiceDetail existingDetail = idetail.getInvoiceDetailByInvoiceDetailID(invoiceDetailID);
            if (existingDetail == null) {
                request.setAttribute("errorMessage", "Chi tiết hóa đơn không tồn tại.");
                listInvoiceDetail(request, response);
                return;
            }
            boolean updated = idetail.updateInvoiceDetail(updatedDetail, inventoryDAO);
            if (!updated) {

                request.setAttribute("errorMessage", "Cập nhật thất bại.");
                listInvoiceDetail(request, response);
                return;
            }

            List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);
            double totalAmount = 0;
            for (InvoiceDetail d : details) {
                totalAmount += d.getTotalPrice();
            }
            Invoice invoice = idao.searchInvoice(invoiceID);
            invoice.setTotalAmount(totalAmount);
            idao.updateInvoice(invoice);

            response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ.");
            listInvoiceDetail(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra.");
            listInvoiceDetail(request, response);
        }
    }

    private void addInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        String productID = request.getParameter("productID");
        String shopID = request.getParameter("shopID");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
        String discountStr = request.getParameter("discount");

        Double discount = 0.0;
        if (discountStr != null && !discountStr.trim().isEmpty()) {
            discount = Double.parseDouble(discountStr);
        }

        InvoiceDetail detail = new InvoiceDetail(invoiceID, productID, unitPrice, quantity, discount);
        boolean success = idetail.addInvoiceDetailAndUpdateInventory(detail, shopID);

        if (!success) {
            response.sendRedirect("InvoiceServlet?action=addDetailForm&invoiceID=" + invoiceID);
            return;
        }

        List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);
        double totalAmount = 0;
        for (InvoiceDetail d : details) {
            totalAmount += d.getTotalPrice();
        }
        Invoice invoice = idao.searchInvoice(invoiceID);
        invoice.setTotalAmount(totalAmount);
        idao.updateInvoice(invoice);

        response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
    }

    private void addInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        String customerID = request.getParameter("customerID");
        String employeeID = request.getParameter("employeeID");
        String shopID = request.getParameter("shopID");
        Timestamp invoiceDate = new Timestamp(System.currentTimeMillis()); // ngày hiện tại
        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        String note = request.getParameter("note");
        boolean status = Boolean.parseBoolean(request.getParameter("status"));

        Invoice newInvoice = new Invoice(invoiceID, customerID, employeeID, shopID, invoiceDate, totalAmount, note, status);
        idao.addInvoice(newInvoice);

        List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);

        request.setAttribute("selectedInvoice", newInvoice);
        request.setAttribute("invoiceDetails", details);
        request.getRequestDispatcher("Invoice.jsp").forward(request, response);

    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID");
        if (invoiceID == null || invoiceID.trim().isEmpty()) {
            response.sendRedirect("InvoiceServlet");
            return;
        }
        Invoice invoice = idao.searchInvoice(invoiceID);
        if (invoice == null) {
            // Nếu không tìm thấy, chuyển về danh sách
            response.sendRedirect("InvoiceServlet");
            return;
        }
        if (invoice.isStatus()) {
            request.setAttribute("errorMessage", "Không thể xóa hóa đơn đã thanh toán!");
            listInvoices(request, response);
        } else {

            idao.deleteInvoice(invoiceID);
            response.sendRedirect("InvoiceServlet");
        }
    }

    private void searchInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceID = request.getParameter("invoiceID"); // Đây là key tìm kiếm

        List<Invoice> invoices;

        if (invoiceID == null || invoiceID.trim().isEmpty()) {

            response.sendRedirect("InvoiceServlet");
            return;
        }
        invoices = idao.searchInvoiceByKey(invoiceID);
        request.setAttribute("data", invoiceID);
        request.setAttribute("invoiceList", invoices); // Đặt tên thuộc tính phù hợp với JSP của bạn
        request.getRequestDispatcher("Invoice.jsp").forward(request, response);
    }

    private void listInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String invoiceID = request.getParameter("invoiceID");
        if (invoiceID == null) {
            response.sendRedirect("InvoiceServlet");
            return;
        }
        Invoice invoice = idao.searchInvoice(invoiceID);
        if (invoice == null) {
            response.sendRedirect("InvoiceServlet");
            return;
        }

        // Lấy thông tin Shop và Customer từ Invoice
        String shopID = invoice.getShopID();
        Shop selectedShop = null;
        try {
            selectedShop = sDAO.getShopByID(shopID, "SWP1"); // Vẫn cần ShopDAO và getShopById
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể lấy thông tin cửa hàng.");
        }
        List<InvoiceDetail> detail = idetail.getDetailByInvoiceID(invoiceID);
        if (detail == null) {
            detail = new ArrayList<>();
        }
        String editDetailIDParam = request.getParameter("editDetailID");
        if (editDetailIDParam != null && !editDetailIDParam.isEmpty()) {
            try {
               
                int editDetailID = Integer.parseInt(editDetailIDParam);
                request.setAttribute("editDetailID", editDetailID); 
            } catch (NumberFormatException e) {
                request.setAttribute("editDetailID", null); 
            }
        }
        double totalAmount = 0;
        for (InvoiceDetail d : detail) {
            totalAmount += d.getTotalPrice();
        }

        invoice.setTotalAmount(totalAmount);
        idao.updateInvoice(invoice);

        request.setAttribute("selectedInvoice", invoice);
        request.setAttribute("invoiceDetails", detail);
        request.setAttribute("selectedShop", selectedShop);

        request.getRequestDispatcher("InvoiceDetail.jsp").forward(request, response);

    }
    //    private void deleteInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
    //        throws ServletException, IOException {
    //    String invoiceDetailIDStr = request.getParameter("invoiceDetailID");
    //    String invoiceID = request.getParameter("invoiceID");
    //
    //    if (invoiceDetailIDStr == null || invoiceID == null) {
    //        response.sendRedirect("InvoiceServlet");
    //        return;
    //    }
    //
    //    int invoiceDetailID = Integer.parseInt(invoiceDetailIDStr);
    //
    //    boolean success = idetail.deleteInvoiceDetailAndUpdateInventory(invoiceDetailID);
    //
    //    // Cập nhật lại totalAmount của hóa đơn
    //    List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);
    //    double totalAmount = 0;
    //    for (InvoiceDetail d : details) {
    //        totalAmount += d.getTotalPrice();
    //    }
    //
    //    Invoice invoice = idao.searchInvoice(invoiceID);
    //    if (invoice != null) {
    //        invoice.setTotalAmount(totalAmount);
    //        idao.updateInvoice(invoice);
    //    }
    //
    //    response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
    //}

}
