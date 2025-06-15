/*
         * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
         * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.CustomerDAO;
import Dal.EmployeeDAO;
import Dal.InventoryDAO;
import Dal.InvoiceDAO;
import Dal.InvoiceDetailDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Models.Customer;
import Models.Employee;
import Models.Invoice;
import Models.InvoiceDetail;
import Models.Product;
import Models.Shop;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

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
    EmployeeDAO eDAO = new EmployeeDAO(connection.getConnection());
    ProductDAO pDAO = new ProductDAO(connection.getConnection());
    CustomerDAO cDAO = new CustomerDAO(connection.getConnection());

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
            case "list":
                listInvoices(request, response);
                break;
            case "listDetail":
                listInvoiceDetail(request, response);
                break;
            case "showAddForm":
                showAddInvoiceForm(request, response);
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

    private void showAddInvoiceForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Employee> employees = eDAO.getAllEmployee();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("addInvoice.jsp").forward(request, response);
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
        List<Customer> customers = cDAO.getAllCustomer();
        List<Invoice> invoices = idao.getInvoicesByPage(pageIndex, pageSize);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("customers", customers);

        request.setAttribute("invoiceList", invoices);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("listInvoice.jsp").forward(request, response);
    }

    private void updateInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            int invoiceID = Integer.parseInt(request.getParameter("invoiceID"));
            int customerID = Integer.parseInt(request.getParameter("customerID"));
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));
            int shopID = Integer.parseInt(request.getParameter("shopID"));

            Invoice oldInvoice = idao.searchInvoice(invoiceID);

            if (oldInvoice == null) {

                request.setAttribute("errorMessage", "Không tìm thấy hóa đơn để cập nhật.");
                listInvoices(request, response);
                return;
            }

            Timestamp invoiceDateTimestamp = oldInvoice.getInvoiceDate();

            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
            String note = request.getParameter("note");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            // Tạo đối tượng Invoice mới với các ID là int
            Invoice invoice = new Invoice(invoiceID, customerID, employeeID, shopID, invoiceDateTimestamp, totalAmount, note, status);
            boolean result = idao.updateInvoice(invoice);

            if (result) {
                System.out.println("Cập nhật thành công.");
            } else {
                System.out.println("Cập nhật thất bại.");
            }
            response.sendRedirect("InvoiceServlet?action=list");
        } catch (NumberFormatException e) {

            request.setAttribute("errorMessage", "Dữ liệu ID không hợp lệ. Vui lòng kiểm tra lại.");
            listInvoices(request, response);
        } catch (Exception e) {

            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi cập nhật hóa đơn.");
            listInvoices(request, response);
        }
    }

    private void updateInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            int invoiceID = Integer.parseInt(request.getParameter("invoiceID").trim());
            int productID = Integer.parseInt(request.getParameter("productID").trim()); // Giả sử ProductID là int
            String unitPriceStr = request.getParameter("unitPrice").trim();
            String quantityStr = request.getParameter("quantity").trim();
            String discountStr = request.getParameter("discount").trim(); // Thêm .trim()
            int invoiceDetailID = Integer.parseInt(request.getParameter("invoiceDetailID"));
            if (unitPriceStr.isEmpty() || quantityStr.isEmpty() || discountStr.isEmpty()) {
                request.setAttribute("errorMessage", "Dữ liệu chi tiết hóa đơn bị thiếu.");
                listInvoiceDetail(request, response);
                return;
            }

            double unitPrice = Double.parseDouble(unitPriceStr);
            int quantity = Integer.parseInt(quantityStr);
            double discount = Double.parseDouble(discountStr);

            InvoiceDetail lay = idetail.getInvoiceDetailByInvoiceDetailID(invoiceDetailID);
            if (lay == null) {
                response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);

                return;
            }
            int shopID = lay.getShopID();

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
            request.setAttribute("invoiceID", request.getParameter("invoiceID"));
            listInvoiceDetail(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra.");
            request.setAttribute("invoiceID", request.getParameter("invoiceID"));
            listInvoiceDetail(request, response);
        }
    }

    private void addInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIDParam = request.getParameter("invoiceID"); // Lấy dưới dạng String trước
        String productIDParam = request.getParameter("productID");
        String shopIDParam = request.getParameter("shopID");
        String quantityStr = request.getParameter("quantity");
        String unitPriceStr = request.getParameter("unitPrice");
        String discountStr = request.getParameter("discount");

        if (invoiceIDParam == null || productIDParam == null || quantityStr == null || unitPriceStr == null || shopIDParam == null) {
            request.setAttribute("errorMessage", "Thông tin chi tiết hóa đơn bị thiếu.");
            request.setAttribute("invoiceID", invoiceIDParam); // Giữ nguyên String để forward
            listInvoiceDetail(request, response);
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIDParam.trim());
            int productID = Integer.parseInt(productIDParam.trim());
            int shopID = Integer.parseInt(shopIDParam.trim());
            int quantity = Integer.parseInt(quantityStr.trim());
            double unitPrice = Double.parseDouble(unitPriceStr.trim());
            Double discount = 0.0;
            if (discountStr != null && !discountStr.trim().isEmpty()) {
                discount = Double.parseDouble(discountStr);
            }

            InvoiceDetail detail = new InvoiceDetail(invoiceID, productID, unitPrice, quantity, discount);

            detail.setShopID(shopID);

            boolean success = idetail.addInvoiceDetailAndUpdateInventory(detail, shopID);

            if (!success) {
                request.setAttribute("errorMessage", "Thêm chi tiết hóa đơn thất bại hoặc không đủ số lượng sản phẩm trong kho.");

                request.setAttribute("invoiceID", invoiceID);
                listInvoiceDetail(request, response);
                return;
            }

            // Cập nhật tổng tiền của hóa đơn chính
            List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);
            double totalAmount = 0;
            for (InvoiceDetail d : details) {
                totalAmount += d.getTotalPrice();
            }
            Invoice invoice = idao.searchInvoice(invoiceID);
            if (invoice != null) {
                invoice.setTotalAmount(totalAmount);
                idao.updateInvoice(invoice);
            }

            // Chuyển hướng về trang chi tiết hóa đơn (đã cập nhật)
            response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ (số).");
            // Giữ lại invoiceID để hiển thị đúng trang chi tiết
            request.setAttribute("invoiceID", invoiceIDParam);
            listInvoiceDetail(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi thêm chi tiết.");
            // Giữ lại invoiceID để hiển thị đúng trang chi tiết
            request.setAttribute("selected", e);
            request.setAttribute("invoiceID", invoiceIDParam);
            listInvoiceDetail(request, response);
        }
    }

    private void addInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int customerID = Integer.parseInt(request.getParameter("customerID"));
        int employeeID = Integer.parseInt(request.getParameter("employeeID"));
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        Timestamp invoiceDate = new Timestamp(System.currentTimeMillis());
        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        String note = request.getParameter("note");
        boolean status = Boolean.parseBoolean(request.getParameter("status"));

        Invoice newInvoice = new Invoice(customerID, employeeID, shopID, invoiceDate, totalAmount, note, status);
        idao.addInvoice(newInvoice);

        List<Employee> e = eDAO.getAllEmployee();

        request.setAttribute("selectedInvoice", newInvoice);

        request.getRequestDispatcher("InvoiceDetaiul.jsp").forward(request, response);
//        response.sendRedirect("InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);

    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIDParam = request.getParameter("invoiceID");
        if (invoiceIDParam == null || invoiceIDParam.trim().isEmpty()) {
            response.sendRedirect("InvoiceServlet");
            return;
        }
        try {
            int invoiceID = Integer.parseInt(invoiceIDParam);
            Invoice invoice = idao.searchInvoice(invoiceID);
            if (invoice == null) {
                response.sendRedirect("InvoiceServlet");
                return;
            }
            if (invoice.isStatus()) {
                request.setAttribute("errorMessage", "Không thể xóa hóa đơn đã thanh toán!");
                listInvoices(request, response);
            } else {
                boolean deleted = idao.deleteInvoice(invoiceID);
                if (deleted) {
                    response.sendRedirect("InvoiceServlet?action=list");
                } else {
                    request.setAttribute("errorMessage", "Xóa hóa đơn thất bại.");
                    listInvoices(request, response);
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID hóa đơn không hợp lệ.");
            listInvoices(request, response);
        } catch (Exception e) {

            request.setAttribute("errorMessage", "Lỗi hệ thống khi xóa hóa đơn.");
            listInvoices(request, response);
        }
    }

    private void searchInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchKey = request.getParameter("invoiceID"); // Đây là key tìm kiếm

        List<Invoice> invoices;

        if (searchKey == null || searchKey.trim().isEmpty()) {

            response.sendRedirect("InvoiceServlet?action=list");
            return;
        }
        invoices = idao.searchInvoiceByKey(searchKey);
        request.setAttribute("data", searchKey);
        request.setAttribute("invoiceList", invoices); // Đặt tên thuộc tính phù hợp với JSP của bạn
        request.getRequestDispatcher("listInvoice.jsp").forward(request, response);
    }

    private void listInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String invoiceIDParam = request.getParameter("invoiceID");
        if (invoiceIDParam == null || invoiceIDParam.trim().isEmpty()) {
            response.sendRedirect("InvoiceServlet?action=list");
            return;
        }
        try {
            int invoiceID = Integer.parseInt(invoiceIDParam);
            Invoice invoice = idao.searchInvoice(invoiceID);
            if (invoice == null) {
                response.sendRedirect("InvoiceServlet?action=list");
                return;
            }

            List<Employee> em = eDAO.getAllEmployee();
            int shopID = invoice.getShopID();
            Shop selectedShop = null;
            try {
                selectedShop = sDAO.getShopByID(shopID, "SW1");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Không thể lấy thông tin cửa hàng.");
            }

            List<Product> products = null;
            try {
                products = pDAO.getAllProducts();
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Không thể lấy danh sách sản phẩm: " + e.getMessage());
                products = new ArrayList<>();
            }

            List<InvoiceDetail> detail = idetail.getDetailByInvoiceID(invoiceID);
            if (detail == null) {
                detail = new ArrayList<>();
            }

 
            List<Customer> customers = cDAO.getAllCustomer();

            String editDetailIDParam = request.getParameter("editDetailID");
            if (editDetailIDParam != null && !editDetailIDParam.isEmpty()) {
                try {
                    int editDetailID = Integer.parseInt(editDetailIDParam);
                    request.setAttribute("editDetailID", editDetailID);
                } catch (NumberFormatException e) {
                    request.setAttribute("editDetailID", null);
                }
            }

            Customer selectedCustomer = null;       
            if (invoice.getCustomerID() >0) { 
                selectedCustomer = cDAO.getCustomerById(invoice.getCustomerID());
            }

            double totalAmount = 0;
            for (InvoiceDetail d : detail) {
                totalAmount += d.getTotalPrice();
            }

            invoice.setTotalAmount(totalAmount);
            idao.updateInvoice(invoice);

            request.setAttribute("employees", em);
            request.setAttribute("customers", customers); // Bạn có thể giữ dòng này nếu cần danh sách khách hàng cho mục đích khác

            // --- THÊM DÒNG NÀY ĐỂ TRUYỀN ĐỐI TƯỢNG selectedCustomer VÀO REQUEST ---
            request.setAttribute("selectedCustomer", selectedCustomer);
            // ---------------------------------------------------------------------

            request.setAttribute("selectedInvoice", invoice);
            request.setAttribute("invoiceDetails", detail);
            request.setAttribute("selectedShop", selectedShop);
            request.setAttribute("products", products);

            request.getRequestDispatcher("InvoiceDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID hóa đơn không hợp lệ.");
            response.sendRedirect("InvoiceServlet?action=list");
        } catch (Exception e) {
            // Log lỗi chi tiết hơn trong môi trường thực tế
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống khi tải chi tiết hóa đơn: " + e.getMessage());
            response.sendRedirect("InvoiceServlet?action=list");
        }
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

