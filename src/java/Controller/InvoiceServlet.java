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
import Models.Inventory;
import Models.Shop;
import Utils.JspStringRender;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Utils.MailSender;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.sql.Date;

import java.util.List;
import javax.mail.MessagingException;

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
            case "search":
                searchInvoice(request, response);
                break;
            case "searchByDate":
                searchInvoicesByDateRange(request, response);
                break;
            case "showAddForm":
                showAddInvoiceForm(request, response);
                break;
            case "sendInvoiceEmail":
                sendInvoiceEmail(request, response);
                break;
            case "manageInvoiceDetails":
                showManageInvoiceDetailForm(request, response);
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
            case "addDetail":
                addInvoiceDetail(request, response);
                break;

            case "updateDetail":
                updateInvoiceDetail(request, response);
                break;
            case "selectProductForPrice":
                selectProductForPrice(request, response);
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
    private void searchInvoicesByDateRange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");

        Date startDate = null;
        Date endDate = null;

        try {
            if (startDateParam != null && !startDateParam.isEmpty()) {
                startDate = Date.valueOf(startDateParam);
            }
            if (endDateParam != null && !endDateParam.isEmpty()) {
                endDate = Date.valueOf(endDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
            listInvoices(request, response);
            return;
        }

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

        int totalInvoices = idao.getTotalInvoiceCount_UsingCastInSQL(startDate, endDate);
        int totalPages = (int) Math.ceil((double) totalInvoices / pageSize);

        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > totalPages && totalPages > 0) {
            pageIndex = totalPages;
        }
        if (totalInvoices == 0) {
            pageIndex = 1;
        }

        List<Invoice> invoicesForCurrentPage = idao.getInvoicesByDateRange_UsingCastInSQL(startDate, endDate, pageIndex, pageSize);

        // --- Kết thúc logic phân trang ---
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalInvoices);
        request.setAttribute("invoiceList", invoicesForCurrentPage);
        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("searchType", "date");

        request.getRequestDispatcher("listInvoice.jsp").forward(request, response);
    }

    private void selectProductForPrice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIdParam = request.getParameter("invoiceID");
        String productIDStr = request.getParameter("productID");
        String shopIDParam = request.getParameter("shopID");

        try {
            if (invoiceIdParam == null || invoiceIdParam.isEmpty()) {
                request.setAttribute("errorMessage", "Mã hóa đơn không hợp lệ.");
                showManageInvoiceDetailForm(request, response);
                return;
            }
            int invoiceID = Integer.parseInt(invoiceIdParam);
            Invoice selectedInvoice = idao.searchInvoice(invoiceID);
            if (selectedInvoice == null) {
                request.setAttribute("errorMessage", "Không tìm thấy hóa đơn cần quản lý.");
                listInvoices(request, response);
                return;
            }
            request.setAttribute("selectedInvoice", selectedInvoice);

            int shopID = selectedInvoice.getShopID();
            if (shopIDParam != null && !shopIDParam.isEmpty()) {
                shopID = Integer.parseInt(shopIDParam);
            }

            if (productIDStr != null && !productIDStr.isEmpty()) {
                int productID = Integer.parseInt(productIDStr);
                Product selectedProduct = pDAO.getProductById(productID);
                if (selectedProduct != null) {

                    request.setAttribute("selectedUnitPrice", selectedProduct.getSellingPrice());
                } else {
                    request.setAttribute("errorMessage", "Không tìm thấy sản phẩm với ID: " + productID);
                }
                request.setAttribute("paramProductID", productIDStr);
                request.setAttribute("paramQuantity", request.getParameter("quantity"));
                request.setAttribute("paramDiscount", request.getParameter("discount"));

            }

            List<InvoiceDetail> invoiceDetails = idetail.getDetailByInvoiceID(invoiceID);
            List<Product> products = pDAO.getAllProducts();
            List<Inventory> inventories = inventoryDAO.getAllInventoriesInStore(shopID);

            request.setAttribute("invoiceDetails", invoiceDetails);
            request.setAttribute("products", products);
            request.setAttribute("inventories", inventories);

            request.getRequestDispatcher("invoiceForm.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu ID sản phẩm, hóa đơn hoặc cửa hàng không hợp lệ.");
            e.printStackTrace();
            if (invoiceIdParam != null && !invoiceIdParam.isEmpty()) {
                request.setAttribute("invoiceID", invoiceIdParam);
                showManageInvoiceDetailForm(request, response);
            } else {
                listInvoices(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi lấy giá sản phẩm: " + e.getMessage());
            if (invoiceIdParam != null && !invoiceIdParam.isEmpty()) {
                request.setAttribute("invoiceID", invoiceIdParam);
                showManageInvoiceDetailForm(request, response);
            } else {
                listInvoices(request, response);
            }
        }
    }

    private void showAddInvoiceForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("customers", cDAO.getAllCustomer());
            request.setAttribute("allShops", sDAO.getAllShops("SWP1"));
            request.getRequestDispatcher("addInvoice.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải form thêm hóa đơn: " + e.getMessage());
            request.getRequestDispatcher("listInvoice.jsp").forward(request, response); 
        }
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
        int totalPages = (int) Math.ceil((double) totalInvoices / pageSize);

        List<Customer> customers = cDAO.getAllCustomer();
        List<Employee> employees = eDAO.getAllEmployee();
        List<Shop> allShops = sDAO.getAllShops("SWP1");
        List<Invoice> invoices = idao.getInvoicesByPage(pageIndex, pageSize);

        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("customers", customers);
        request.setAttribute("employees", employees);
        request.setAttribute("allShops", allShops);
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
            Invoice invoice = new Invoice(invoiceID, customerID, employeeID, shopID, invoiceDateTimestamp, totalAmount, note, status);
            boolean result = idao.updateInvoice(invoice);

            if (result) {
                System.out.println("Cập nhật thành công.");
                request.setAttribute("successMessage", "Cập nhật hóa đơn thành công!");
            } else {
                System.out.println("Cập nhật thất bại.");
                request.setAttribute("errorMessage", "Cập nhật hóa đơn thất bại!");
            }

            listInvoices(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu ID không hợp lệ. Vui lòng kiểm tra lại.");
            listInvoices(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi cập nhật hóa đơn: " + e.getMessage());
            listInvoices(request, response);
        }
    }

    private void updateInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIdParam = request.getParameter("invoiceID");
        if (invoiceIdParam == null || invoiceIdParam.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Mã hóa đơn không tồn tại.");
            listInvoices(request, response);
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIdParam.trim());
            int productID = Integer.parseInt(request.getParameter("productID").trim());
            String unitPriceStr = request.getParameter("unitPrice").trim();
            String quantityStr = request.getParameter("quantity").trim();
            String discountStr = request.getParameter("discount").trim();
            int invoiceDetailID = Integer.parseInt(request.getParameter("invoiceDetailID"));
            if (unitPriceStr.isEmpty() || quantityStr.isEmpty() || discountStr.isEmpty()) {
                request.setAttribute("errorMessage", "Dữ liệu chi tiết hóa đơn bị thiếu.");
                request.setAttribute("invoiceID", invoiceIdParam);
                listInvoiceDetail(request, response);
                return;
            }

            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            int quantity = Integer.parseInt(quantityStr);
            double discount = Double.parseDouble(discountStr);

            InvoiceDetail lay = idetail.getInvoiceDetailByInvoiceDetailID(invoiceDetailID);
            if (lay == null) {
                request.setAttribute("errorMessage", "Chi tiết hóa đơn không tồn tại.");
                request.setAttribute("invoiceID", invoiceIdParam);
                listInvoiceDetail(request, response);
                return;
            }
            int shopID = lay.getShopID();
            InvoiceDetail updatedDetail = new InvoiceDetail(invoiceDetailID, invoiceID, productID, unitPrice, quantity, discount);
            updatedDetail.setShopID(shopID);

            boolean updated = idetail.updateInvoiceDetail(updatedDetail, inventoryDAO);

            if (updated) {
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
                request.setAttribute("successMessage", "Cập nhật chi tiết hóa đơn thành công!");
            } else {
                request.setAttribute("errorMessage", "Cập nhật chi tiết hóa đơn thất bại.");
            }

            request.setAttribute("invoiceID", invoiceIdParam);
            listInvoiceDetail(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
            request.setAttribute("invoiceID", invoiceIdParam);
            listInvoiceDetail(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi cập nhật chi tiết hóa đơn: " + e.getMessage());
            request.setAttribute("invoiceID", invoiceIdParam);
            listInvoiceDetail(request, response);
        }
    }

    private void addInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIDParam = request.getParameter("invoiceID");
        String productIDParam = request.getParameter("productID");
        String shopIDParam = request.getParameter("shopID");
        String quantityStr = request.getParameter("quantity");
        String unitPriceStr = request.getParameter("unitPrice");
        String discountStr = request.getParameter("discount");

        if (invoiceIDParam == null || productIDParam == null || quantityStr == null || unitPriceStr == null || shopIDParam == null) {
            request.setAttribute("errorMessage", "Thông tin chi tiết hóa đơn bị thiếu.");
            request.setAttribute("invoiceID", invoiceIDParam);
            listInvoiceDetail(request, response);
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIDParam.trim());
            int productID = Integer.parseInt(productIDParam.trim());
            int shopID = Integer.parseInt(shopIDParam.trim());
            int quantity = Integer.parseInt(quantityStr.trim());
            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            Double discount = 0.0;
            if (discountStr != null && !discountStr.trim().isEmpty()) {
                discount = Double.parseDouble(discountStr);
            }
            InvoiceDetail detail = new InvoiceDetail(invoiceID, productID, unitPrice, quantity, discount);
            detail.setShopID(shopID);

            boolean success = idetail.addInvoiceDetailAndUpdateInventory(detail, shopID);

            if (success) {

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
                request.setAttribute("successMessage", "Thêm chi tiết hóa đơn thành công!");
            } else {
                request.setAttribute("errorMessage", "Thêm chi tiết hóa đơn thất bại hoặc không đủ số lượng sản phẩm trong kho.");
            }
            request.setAttribute("invoiceID", invoiceIDParam);
            listInvoiceDetail(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ (số).");
            request.setAttribute("invoiceID", invoiceIDParam);
            listInvoiceDetail(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi thêm chi tiết: " + e.getMessage());
            request.setAttribute("invoiceID", invoiceIDParam);
            listInvoiceDetail(request, response);
        }
    }

    private void addInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int customerID = Integer.parseInt(request.getParameter("customerID"));
            int employeeID = 1;
            int shopID = Integer.parseInt(request.getParameter("shopID"));
            Timestamp invoiceDate = Timestamp.from(Instant.now());
            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
            String note = request.getParameter("note");
            boolean status = false;

            Invoice newInvoice = new Invoice(customerID, employeeID, shopID, invoiceDate, totalAmount, note, status);
            int generatedInvoiceID = idao.addInvoice(newInvoice);

            if (generatedInvoiceID > 0) {
                request.setAttribute("successMessage", "Hóa đơn đã được thêm thành công! Vui lòng thêm chi tiết.");
                response.sendRedirect("InvoiceServlet?action=manageInvoiceDetails&invoiceID=" + generatedInvoiceID);
                return;
            } else {
                request.setAttribute("errorMessage", "Không thể thêm hóa đơn. Vui lòng thử lại.");
                request.setAttribute("customers", cDAO.getAllCustomer());
                request.setAttribute("allShops", sDAO.getAllShops("SWP1"));
                request.getRequestDispatcher("addInvoice.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Lỗi định dạng dữ liệu: Mã khách hàng, cửa hàng hoặc tổng tiền phải là số.");
            request.setAttribute("customers", cDAO.getAllCustomer());
            request.setAttribute("allShops", sDAO.getAllShops("SWP1"));

            request.getRequestDispatcher("addInvoice.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi thêm hóa đơn: " + e.getMessage());
            try {
                request.setAttribute("customers", cDAO.getAllCustomer());
                request.setAttribute("allShops", sDAO.getAllShops("SWP1"));
            } catch (Exception daoEx) {
                System.err.println("Error fetching dropdown data on error in general catch: " + daoEx.getMessage());
            }
            request.getRequestDispatcher("addInvoice.jsp").forward(request, response);
        }
    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response)
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
                request.setAttribute("errorMessage", "Không tìm thấy hóa đơn để xóa.");
                listInvoices(request, response);
                return;
            }
            if (invoice.isStatus()) {
                request.setAttribute("errorMessage", "Không thể xóa hóa đơn đã thanh toán!");
                listInvoices(request, response);
            } else {
                boolean deleted = idao.deleteInvoice(invoiceID);
                if (deleted) {
                    request.setAttribute("successMessage", "Hóa đơn đã được xóa thành công!");
                    listInvoices(request, response);
                } else {
                    request.setAttribute("errorMessage", "Xóa hóa đơn thất bại.");
                    listInvoices(request, response);
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID hóa đơn không hợp lệ.");
            listInvoices(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống khi xóa hóa đơn: " + e.getMessage());
            listInvoices(request, response);
        }
    }

    private void showManageInvoiceDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIdPR = request.getParameter("invoiceID");
        if (invoiceIdPR == null || invoiceIdPR.isEmpty()) {
            response.sendRedirect("InvoiceServlet?action=list");
            return;
        }
        int invoiceID = Integer.parseInt(invoiceIdPR);
        Invoice selectedInvoice = idao.searchInvoice(invoiceID);
        if (selectedInvoice == null) {
            request.setAttribute("errorMessage", "Không tìm thấy hóa đơn với mã: " + invoiceID);
            listInvoices(request, response);
            return;
        }
        List<InvoiceDetail> invoiceDetails = idetail.getDetailByInvoiceID(invoiceID);
        List<Product> products = pDAO.getAllProducts();
        List<Inventory> inventories = inventoryDAO.getAllInventoriesInStore(selectedInvoice.getShopID());
        request.setAttribute("selectedInvoice", selectedInvoice);
        request.setAttribute("invoiceDetails", invoiceDetails);
        request.setAttribute("products", products);
        request.setAttribute("inventories", inventories);

        String editDetailID = request.getParameter("editDetailID");
        if (editDetailID != null && !editDetailID.isEmpty()) {
            request.setAttribute("editDetailID", Integer.parseInt(editDetailID));
        }
        request.getRequestDispatcher("invoiceForm.jsp").forward(request, response);
    }

    private void searchInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchKey = request.getParameter("invoiceID");
        if (searchKey == null || searchKey.trim().isEmpty()) {
            searchKey = request.getParameter("searchQuery");
        }

        if (searchKey == null || searchKey.trim().isEmpty()) {

            response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=list");
            return;
        }
        searchKey = searchKey.trim();

        List<Invoice> fullSearchResults = idao.searchInvoiceByKey(searchKey);

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

        int totalInvoices = fullSearchResults.size();

        int totalPages = (int) Math.ceil((double) totalInvoices / pageSize);

        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > totalPages && totalPages > 0) {
            pageIndex = totalPages;
        }

        if (totalInvoices == 0) {
            pageIndex = 1;
        }

        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalInvoices);

        List<Invoice> invoicesForCurrentPage;
        if (startIndex < endIndex) {
            invoicesForCurrentPage = fullSearchResults.subList(startIndex, endIndex);
        } else {
            invoicesForCurrentPage = new ArrayList<>();
        }

        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("invoiceList", invoicesForCurrentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalInvoices);
        request.setAttribute("searchQuery", searchKey);

        request.getRequestDispatcher("listInvoice.jsp").forward(request, response);
    }

    private void listInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIDParam = request.getParameter("invoiceID");
        if (invoiceIDParam == null || invoiceIDParam.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Không tìm thấy mã hóa đơn để hiển thị chi tiết.");
            request.getRequestDispatcher("/listInvoice.jsp").forward(request, response);
            return;
        }
        try {
            int invoiceID = Integer.parseInt(invoiceIDParam);
            Invoice invoice = idao.searchInvoice(invoiceID);
            if (invoice == null) {
                request.setAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + invoiceID);
                request.getRequestDispatcher("/listInvoice.jsp").forward(request, response);
                return;
            }

            List<Employee> em = eDAO.getAllEmployee();
            int shopID = invoice.getShopID();
            List<Inventory> inventoriesInShop = inventoryDAO.getAllInventoriesInStore(shopID);
            Shop selectedShop = null;
            try {
                selectedShop = sDAO.getShopByID(shopID, "SWP1");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Không thể lấy thông tin cửa hàng.");
            }
            request.setAttribute("inventories", inventoriesInShop);
            List<Product> products = pDAO.getAllProducts();
            request.setAttribute("products", products);

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
            if (invoice.getCustomerID() > 0) {
                selectedCustomer = cDAO.getCustomerById(invoice.getCustomerID());
            }
            double totalAmount = 0;
            for (InvoiceDetail d : detail) {
                totalAmount += d.getTotalPrice();
            }
            invoice.setTotalAmount(totalAmount);
            idao.updateInvoice(invoice);

//            request.setAttribute("employees", em);
            request.setAttribute("customers", customers);
            request.setAttribute("selectedCustomer", selectedCustomer);
            request.setAttribute("selectedInvoice", invoice);
            request.setAttribute("invoiceDetails", detail);
            request.setAttribute("selectedShop", selectedShop);

            request.getRequestDispatcher("InvoiceDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID hóa đơn không hợp lệ.");
            request.getRequestDispatcher("/listInvoice.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống khi tải chi tiết hóa đơn: " + e.getMessage());
            request.getRequestDispatcher("/listInvoice.jsp").forward(request, response);
        }
    }

    private void sendInvoiceEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIdStr = request.getParameter("invoiceID");

        request.setAttribute("invoiceID", invoiceIdStr);

        if (invoiceIdStr == null || invoiceIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Không tìm thấy mã hóa đơn để gửi email.");
            listInvoiceDetail(request, response);
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIdStr);
            Invoice selectedInvoice = idao.searchInvoice(invoiceID);
            if (selectedInvoice == null) {
                request.setAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + invoiceID);
                listInvoiceDetail(request, response);
                return;
            }
            Customer selectedCustomer = cDAO.getCustomerById(selectedInvoice.getCustomerID());
            Shop selectedShop = sDAO.getShopByID(selectedInvoice.getShopID(), "SWP1");
            List<InvoiceDetail> invoiceDetails = idetail.getDetailByInvoiceID(invoiceID);
            List<Product> products = pDAO.getAllProducts();
            List<Inventory> inventoriesInShop = inventoryDAO.getAllInventoriesInStore(selectedInvoice.getShopID());
            request.setAttribute("selectedInvoice", selectedInvoice);
            request.setAttribute("selectedCustomer", selectedCustomer);
            request.setAttribute("invoiceDetails", invoiceDetails);
            request.setAttribute("selectedShop", selectedShop);
            request.setAttribute("products", products);
            request.setAttribute("inventories", inventoriesInShop);
            if (selectedCustomer == null || selectedCustomer.getEmail() == null || selectedCustomer.getEmail().isEmpty()) {
                request.setAttribute("errorMessage", "Không tìm thấy email khách hàng hoặc khách hàng không có email để gửi hóa đơn.");
                listInvoiceDetail(request, response);
                return;
            }
            String emailHtmlContent = JspStringRender.renderJspToString(request, response, "/invoiceEmailTemplate.jsp");
            if (emailHtmlContent == null || emailHtmlContent.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Lỗi: Nội dung hóa đơn trống rỗng sau khi render. Email không được gửi.");
                listInvoiceDetail(request, response);
                return;
            }
            String subject = "Hóa đơn bán hàng từ " + (selectedShop != null ? selectedShop.getShopName() : "Cửa hàng của bạn") + " - #" + invoiceID;
            MailSender emailSender = new MailSender();
            emailSender.sendInvoiceMail(selectedCustomer.getEmail(), subject, emailHtmlContent);
            request.setAttribute("successMessage", "Hóa đơn đã được gửi đến email khách hàng thành công!");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Mã hóa đơn không hợp lệ.");
            e.printStackTrace();
        } catch (MessagingException e) {
            request.setAttribute("errorMessage", "Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý hóa đơn hoặc gửi email: " + e.getMessage());
        } finally {
            if (invoiceIdStr != null && !invoiceIdStr.isEmpty()) {
                request.setAttribute("invoiceID", invoiceIdStr);
            }
            listInvoiceDetail(request, response);
        }
    }
}
