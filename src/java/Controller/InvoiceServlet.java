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
import Dal.VATRateDAO;
import Models.Customer;
import Models.Employee;
import Models.Invoice;
import Models.InvoiceDetail;
import Models.Product;
import Models.Inventory;
import Models.Shop;
import Models.VATRate;
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
    VATRateDAO vatRateDAO = new VATRateDAO(connection.getConnection());

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
            case "completeInvoice":
                completeInvoice(request, response);
                break;
//            case "update":
//                updateInvoice(request, response);
//                break;
            case "addDetail":
                addInvoiceDetail(request, response);
                break;
            case "checkCustomerPhone":
                processCheckCustomerPhone(request, response);
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

    private void setGuestCustomerAttributes(HttpServletRequest request) {
        request.setAttribute("customerID", cDAO.getGuestCustomerID());
        request.setAttribute("customerPhone", "");
        request.setAttribute("customerName", "Khách vãng lai");
        request.setAttribute("customerEmail", "");
        request.setAttribute("customerAddress", "");
        request.setAttribute("customerFieldsReadonly", false);
        request.setAttribute("customerExists", false);
    }

    private void processCheckCustomerPhone(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String invoiceIdParam = request.getParameter("invoiceID");
        Invoice selectedInvoice = null;
        int invoiceID = -1;

        try {
            if (invoiceIdParam != null && !invoiceIdParam.trim().isEmpty()) {
                invoiceID = Integer.parseInt(invoiceIdParam);
                selectedInvoice = idao.searchInvoice(invoiceID);
                if (selectedInvoice == null) {
                    request.setAttribute("errorMessage", "Lỗi: Không tìm thấy hóa đơn với ID đã cung cấp.");
                }
            } else {
                request.setAttribute("errorMessage", "Lỗi: ID hóa đơn không hợp lệ hoặc bị thiếu.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Lỗi: ID hóa đơn không hợp lệ.");
        }

        String customerPhone = request.getParameter("customerPhone");
        request.setAttribute("customerPhone", customerPhone != null ? customerPhone : "");

        int defaultCustomerId = cDAO.getGuestCustomerID();

        int customerToSetInInvoice = defaultCustomerId;
        Customer foundCustomer = null;
        boolean shouldShowAddCustomerButton = false;
        if (customerPhone != null && !customerPhone.trim().isEmpty()) {
            foundCustomer = cDAO.getCustomerByPhone(customerPhone.trim());
            if (foundCustomer != null) {
                customerToSetInInvoice = foundCustomer.getCustomerID();
                request.setAttribute("successMessage", "Đã tìm thấy khách hàng: " + foundCustomer.getCustomerName());
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy khách hàng. Bạn có thể thêm khách hàng mới hoặc tiếp tục với Khách vãng lai.");
                shouldShowAddCustomerButton = true;
            }
        } else {
            request.setAttribute("phoneCheckMessage", "Vui lòng nhập số điện thoại hoặc chọn Khách vãng lai.");
        }
        request.setAttribute("showAddCustomerButton", shouldShowAddCustomerButton);
        if (selectedInvoice != null) {
            selectedInvoice.setCustomerID(customerToSetInInvoice);
            boolean updatedInDb = idao.updateInvoiceCustomer(selectedInvoice);
            if (!updatedInDb) {
                request.setAttribute("errorMessage", "Lỗi: Không thể cập nhật khách hàng cho hóa đơn trong cơ sở dữ liệu.");
            }
        }

        loadCustomerInfoForInvoiceForm(request, selectedInvoice);

        if (selectedInvoice != null) {
            request.setAttribute("selectedInvoice", selectedInvoice);
            List<InvoiceDetail> invoiceDetails = idetail.getDetailByInvoiceID(selectedInvoice.getInvoiceID());
            request.setAttribute("invoiceDetails", invoiceDetails);

            List<Inventory> inventories = inventoryDAO.getAllInventoriesInStore(selectedInvoice.getShopID());
            request.setAttribute("inventories", inventories);

            List<Product> products = pDAO.getAllProducts();
            request.setAttribute("products", products);
        }

        request.getRequestDispatcher("invoiceForm.jsp").forward(request, response);
    }

    private void loadCustomerInfoForInvoiceForm(HttpServletRequest request, Invoice selectedInvoice) {
        int defaultCustomerId = cDAO.getGuestCustomerID();
        request.setAttribute("defaultCustomerId", defaultCustomerId);

        if (selectedInvoice != null) {
            int currentCustomerID = selectedInvoice.getCustomerID();
            Customer currentCustomer = cDAO.getCustomerById(currentCustomerID);

            if (currentCustomer != null) {
                request.setAttribute("customerPhone", currentCustomer.getPhone());
                request.setAttribute("customerName", currentCustomer.getCustomerName());
                request.setAttribute("customerEmail", currentCustomer.getEmail());
                request.setAttribute("customerAddress", currentCustomer.getAddress());
                request.setAttribute("customerID", currentCustomer.getCustomerID());

                request.setAttribute("customerFieldsReadonly", currentCustomerID != defaultCustomerId);
                request.setAttribute("customerExists", currentCustomerID != defaultCustomerId);

            } else {

                setGuestCustomerAttributes(request);
            }
        } else {

            setGuestCustomerAttributes(request);
        }
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
            loadCustomerInfoForInvoiceForm(request, selectedInvoice);
            int shopID = selectedInvoice.getShopID();
            if (shopIDParam != null && !shopIDParam.isEmpty()) {
                shopID = Integer.parseInt(shopIDParam);
            }
            loadCustomerInfoForInvoiceForm(request, selectedInvoice);
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
            request.setAttribute("employees", eDAO.getAllEmployee());
            request.setAttribute("allShops", sDAO.getAllShops("SWP1"));
            List<VATRate> vatRatesList = vatRateDAO.getAllVATRates();
            request.setAttribute("vatRatesList", vatRatesList);
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

//    private void updateInvoice(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            int invoiceID = Integer.parseInt(request.getParameter("invoiceID"));
//            int customerID = Integer.parseInt(request.getParameter("customerID"));
//            int employeeID = Integer.parseInt(request.getParameter("employeeID"));
//            int shopID = Integer.parseInt(request.getParameter("shopID"));
//
//            Invoice oldInvoice = idao.searchInvoice(invoiceID);
//
//            if (oldInvoice == null) {
//                request.setAttribute("errorMessage", "Không tìm thấy hóa đơn để cập nhật.");
//                listInvoices(request, response);
//                return;
//            }
//            Timestamp invoiceDateTimestamp = oldInvoice.getInvoiceDate();
//            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
//            String note = request.getParameter("note");
//            boolean status = Boolean.parseBoolean(request.getParameter("status"));
//            Invoice invoice = new Invoice(invoiceID, customerID, employeeID, shopID, invoiceDateTimestamp, totalAmount, note, status);
//            boolean result = idao.updateInvoice(invoice);
//
//            if (result) {
//                System.out.println("Cập nhật thành công.");
//                request.setAttribute("successMessage", "Cập nhật hóa đơn thành công!");
//            } else {
//                System.out.println("Cập nhật thất bại.");
//                request.setAttribute("errorMessage", "Cập nhật hóa đơn thất bại!");
//            }
//
//            listInvoices(request, response);
//        } catch (NumberFormatException e) {
//            request.setAttribute("errorMessage", "Dữ liệu ID không hợp lệ. Vui lòng kiểm tra lại.");
//            listInvoices(request, response);
//        } catch (Exception e) {
//            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi cập nhật hóa đơn: " + e.getMessage());
//            listInvoices(request, response);
//        }
//    }
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
                showManageInvoiceDetailForm(request, response);
                return;
            }

            BigDecimal unitPrice = new BigDecimal(unitPriceStr); 
            int quantity = Integer.parseInt(quantityStr);
            double discount = Double.parseDouble(discountStr); 

            InvoiceDetail oldDetail = idetail.getInvoiceDetailByInvoiceDetailID(invoiceDetailID);
            if (oldDetail == null) {
                request.setAttribute("errorMessage", "Chi tiết hóa đơn không tồn tại.");
                request.setAttribute("invoiceID", invoiceIdParam);
                showManageInvoiceDetailForm(request, response);
                return;
            }
            int shopID = oldDetail.getShopID();

            InvoiceDetail updatedDetail = new InvoiceDetail(invoiceDetailID, invoiceID, productID, unitPrice, quantity, discount);
            updatedDetail.setShopID(shopID);

            boolean updated = idetail.updateInvoiceDetail(updatedDetail, inventoryDAO);

            if (updated) {

                try {
                    updateInvoiceTotals(request, invoiceID);

                } catch (Exception e) {

                    e.printStackTrace();
                    request.setAttribute("errorMessage", "Cập nhật tổng tiền hóa đơn thất bại: " + e.getMessage());
                }

            } else {
                request.setAttribute("errorMessage", "Cập nhật chi tiết hóa đơn thất bại hoặc không đủ số lượng sản phẩm trong kho.");
            }

            request.setAttribute("invoiceID", invoiceIdParam);

            response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=manageInvoiceDetails&invoiceID=" + invoiceID + "&customerID=" + request.getParameter("customerID"));
            return; // Kết thúc xử lý
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại: " + e.getMessage());
            request.setAttribute("invoiceID", invoiceIdParam);
            showManageInvoiceDetailForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi cập nhật chi tiết hóa đơn: " + e.getMessage());
            request.setAttribute("invoiceID", invoiceIdParam);
            showManageInvoiceDetailForm(request, response);
        }
    }

    private void updateInvoiceTotals(HttpServletRequest request, int invoiceID) throws Exception {
        Invoice invoice = idao.searchInvoice(invoiceID);

        if (invoice != null) {
            List<InvoiceDetail> currentInvoiceDetails = idetail.getDetailByInvoiceID(invoiceID);
            double totalAmountBeforeVAT = 0; 

            for (InvoiceDetail d : currentInvoiceDetails) {
             
                totalAmountBeforeVAT += d.getTotalPrice();
            }

            VATRate selectedVatRate = vatRateDAO.getVATRateById(invoice.getVatRateID());
            double vatPercentage = 0.0;
            if (selectedVatRate != null && selectedVatRate.getRate() != null) {
              
                vatPercentage = selectedVatRate.getRate().doubleValue();
            } else {
                System.err.println("Warning: VAT Rate not found for Invoice ID: " + invoiceID + ". Using 0% VAT.");
               
            }

            double vatAmount = totalAmountBeforeVAT * vatPercentage;
            vatAmount = Math.round(vatAmount * 100.0) / 100.0; 

            double finalTotalAmount = totalAmountBeforeVAT + vatAmount;
            finalTotalAmount = Math.round(finalTotalAmount * 100.0) / 100.0; 

            invoice.setTotalAmount(BigDecimal.valueOf(finalTotalAmount));
            invoice.setVatAmount(BigDecimal.valueOf(vatAmount));
            idao.updateInvoice(invoice);

//            request.getSession().setAttribute("successMessage", "Cập nhật tổng tiền hóa đơn thành công!");
        } else {
            System.err.println("Error: Invoice with ID " + invoiceID + " not found when trying to update totals.");
            request.setAttribute("errorMessage", "Không tìm thấy hóa đơn để cập nhật tổng tiền.");
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
        String customerIDParam = request.getParameter("customerID");
        if (invoiceIDParam == null || productIDParam == null || quantityStr == null || unitPriceStr == null || shopIDParam == null) {
            request.setAttribute("errorMessage", "Thông tin chi tiết hóa đơn bị thiếu.");
            request.setAttribute("invoiceID", invoiceIDParam);
            showManageInvoiceDetailForm(request, response);
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
           
                updateInvoiceTotals(request, invoiceID); 

                response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=manageInvoiceDetails&invoiceID=" + invoiceID + "&customerID=" + customerIDParam);
                return; 
            } else {
                request.setAttribute("errorMessage", "Thêm chi tiết hóa đơn thất bại hoặc không đủ số lượng sản phẩm trong kho.");
                request.setAttribute("invoiceID", invoiceIDParam);
                showManageInvoiceDetailForm(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ (số): " + e.getMessage());
            request.setAttribute("invoiceID", invoiceIDParam);
            showManageInvoiceDetailForm(request, response);
        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Lỗi hệ thống xảy ra khi thêm chi tiết: " + e.getMessage());
            request.setAttribute("invoiceID", invoiceIDParam);
            showManageInvoiceDetailForm(request, response);
        }
    }

    private void addInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorMessage = null;
        String successMessage = null;

        List<Employee> employees = null;
        List<Shop> allShops = null;
        List<VATRate> vatRatesList = null;
        try {
            employees = eDAO.getAllEmployee();
            allShops = sDAO.getAllShops("SWP1");
            vatRatesList = vatRateDAO.getAllVATRates();
        } catch (Exception daoEx) {
            System.err.println("Lỗi khi tải dữ liệu dropdown: " + daoEx.getMessage());
            daoEx.printStackTrace();
            errorMessage = "Không thể tải dữ liệu cần thiết cho form. Vui lòng thử lại sau.";
        }

        request.setAttribute("employees", employees);
        request.setAttribute("allShops", allShops);
        request.setAttribute("vatRatesList", vatRatesList);

        try {
            
            int defaultCustomerId = cDAO.getGuestCustomerID();
            if (defaultCustomerId == -1) {
                errorMessage = "Lỗi: Không tìm thấy ID khách vãng lai trong cơ sở dữ liệu.";
             
            }

            String customerIDParam = request.getParameter("customerID");
            int customerID = defaultCustomerId; 
            if (customerIDParam != null && !customerIDParam.isEmpty()) {
                try {
                    customerID = Integer.parseInt(customerIDParam);
                } catch (NumberFormatException e) {
                    System.err.println("Cảnh báo: customerID không phải số, sử dụng mặc định. Chi tiết: " + customerIDParam);
                }
            }
        
            request.setAttribute("param_customerID", customerIDParam);

            String employeeIDParam = request.getParameter("employeeID");
            int employeeID = -1; 
            if (employeeIDParam == null || employeeIDParam.isEmpty()) {
                errorMessage = "Nhân viên không được để trống.";
            } else {
                try {
                    employeeID = Integer.parseInt(employeeIDParam);
                } catch (NumberFormatException e) {
                    errorMessage = "Mã nhân viên không hợp lệ. Vui lòng chọn lại.";
                    System.err.println("NumberFormatException for employeeID: " + employeeIDParam);
                }
            }
           
            request.setAttribute("param_employeeID", employeeIDParam);

            String shopIDParam = request.getParameter("shopID");
            int shopID = -1;
            if (shopIDParam == null || shopIDParam.isEmpty()) {
                errorMessage = "Cửa hàng không được để trống.";
            } else {
                try {
                    shopID = Integer.parseInt(shopIDParam);
                } catch (NumberFormatException e) {
                    errorMessage = "Mã cửa hàng không hợp lệ. Vui lòng chọn lại.";
                    System.err.println("NumberFormatException for shopID: " + shopIDParam);
                }
            }
      
            request.setAttribute("param_shopID", shopIDParam);

            // Xử lý vatRateID
            String vatRateIDParam = request.getParameter("vatRateID");
            int vatRateID = -1;
            if (vatRateIDParam == null || vatRateIDParam.isEmpty()) {
                errorMessage = "Tỷ lệ VAT không được để trống.";
            } else {
                try {
                    vatRateID = Integer.parseInt(vatRateIDParam);
                } catch (NumberFormatException e) {
                    errorMessage = "Mã tỷ lệ VAT không hợp lệ. Vui lòng chọn lại.";
                    System.err.println("NumberFormatException for vatRateID: " + vatRateIDParam);
                }
            }
            request.setAttribute("param_vatRateID", vatRateIDParam);

            String note = request.getParameter("note");
            request.setAttribute("param_note", note); 

            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                request.getRequestDispatcher("addInvoice.jsp").forward(request, response);
                return;
            }

            Invoice newInvoice = new Invoice();
            newInvoice.setCustomerID(customerID);
            newInvoice.setEmployeeID(employeeID);
            newInvoice.setShopID(shopID);
            newInvoice.setInvoiceDate(Timestamp.from(Instant.now()));
            newInvoice.setTotalAmount(BigDecimal.ZERO); 
            newInvoice.setNote(note);
            newInvoice.setStatus(false);
            newInvoice.setVatRateID(vatRateID);
            newInvoice.setVatAmount(BigDecimal.ZERO); 

            int generatedInvoiceID = idao.addInvoice(newInvoice);

            if (generatedInvoiceID > 0) {
                successMessage = "Hóa đơn đã được thêm thành công! Vui lòng thêm chi tiết.";
                response.sendRedirect("InvoiceServlet?action=manageInvoiceDetails&invoiceID=" + generatedInvoiceID);
                return; 
            } else {
                errorMessage = "Không thể thêm hóa đơn vào cơ sở dữ liệu. Vui lòng thử lại.";
          
            }

        } catch (Exception e) { 
            errorMessage = "Đã xảy ra lỗi hệ thống khi thêm hóa đơn: " + e.getMessage();
            e.printStackTrace();
        }

        // --- Phần này chỉ chạy nếu có lỗi và không có redirect ---
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("successMessage", successMessage); // Có thể có successMessage nếu redirect không thành công (ít xảy ra)
        request.getRequestDispatcher("addInvoice.jsp").forward(request, response);
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
        String editDetailIdParam = request.getParameter("editDetailID");
        String invoiceIdParam = request.getParameter("invoiceID");
        String newCustomerIDParam = request.getParameter("newCustomerID");
        try {
            if (invoiceIdParam == null || invoiceIdParam.isEmpty()) {
                request.setAttribute("errorMessage", "Mã hóa đơn không hợp lệ.");
                listInvoices(request, response);
                return;
            }
            int invoiceID = Integer.parseInt(invoiceIdParam);
            Invoice selectedInvoice = idao.searchInvoice(invoiceID);

            if (selectedInvoice == null) {
                request.setAttribute("errorMessage", "Không tìm thấy hóa đơn cần quản lý.");
                listInvoices(request, response);
                return;
            }
            if (newCustomerIDParam != null && !newCustomerIDParam.isEmpty()) {
                try {
                    int newCustomerID = Integer.parseInt(newCustomerIDParam);

                    if (selectedInvoice.getCustomerID() == 0 || selectedInvoice.getCustomerID() != newCustomerID) {
                        selectedInvoice.setCustomerID(newCustomerID);
                        boolean updated = idao.updateInvoice(selectedInvoice);
                        if (updated) {
                            request.setAttribute("successMessage", "Đã thêm khách hàng mới và cập nhật hóa đơn.");
                        } else {
                            request.setAttribute("errorMessage", "Không thể cập nhật khách hàng mới cho hóa đơn.");
                        }
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "ID khách hàng mới không hợp lệ.");
                }
            }

            if (editDetailIdParam != null && !editDetailIdParam.isEmpty()) {
                try {
                    int editDetailID = Integer.parseInt(editDetailIdParam);
                    request.setAttribute("editDetailID", editDetailID);
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "ID chi tiết hóa đơn không hợp lệ.");

                }
            }
            loadCustomerInfoForInvoiceForm(request, selectedInvoice);

            List<Inventory> inventories = inventoryDAO.getAllInventoriesInStore(selectedInvoice.getShopID());
            request.setAttribute("inventories", inventories);

            List<Product> products = pDAO.getAllProducts();
            request.setAttribute("products", products);

            request.setAttribute("selectedInvoice", selectedInvoice);
            request.setAttribute("invoiceDetails", idetail.getDetailByInvoiceID(invoiceID));

            request.getRequestDispatcher("invoiceForm.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Mã hóa đơn không hợp lệ.");
            listInvoices(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải form quản lý chi tiết hóa đơn: " + e.getMessage());
            listInvoices(request, response);
        }
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

    private void completeInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIdParam = request.getParameter("invoiceID");

        if (invoiceIdParam == null || invoiceIdParam.trim().isEmpty()) {
       
            request.getSession().setAttribute("errorMessage", "Mã hóa đơn không hợp lệ để hoàn tất.");
            response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=list"); // Redirect về trang danh sách
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIdParam.trim());
            Invoice invoice = idao.searchInvoice(invoiceID);

            if (invoice == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy hóa đơn cần hoàn tất.");
                response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=list");
                return;
            }

            if (invoice.isStatus()) { 
                request.getSession().setAttribute("errorMessage", "Hóa đơn này đã được hoàn tất rồi.");
                response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=list");
                return;
            }

            try {
        
                updateInvoiceTotals(request, invoiceID);
             
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("errorMessage", "Hoàn tất hóa đơn thất bại do lỗi tính toán tổng tiền: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=list");
                return;
            }

            invoice.setStatus(true);
            boolean updated = idao.updateInvoice(invoice);

            if (updated) {
                request.getSession().setAttribute("successMessage", "Hóa đơn #" + invoiceID + " đã được hoàn tất thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Hoàn tất hóa đơn #" + invoiceID + " thất bại.");
            }
            response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=listDetail&invoiceID=" + invoiceID);
            return;

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID hóa đơn không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Lỗi hệ thống khi hoàn tất hóa đơn: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/InvoiceServlet?action=list");
        }
    }

    private void listInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceIDParam = request.getParameter("invoiceID");
        if (invoiceIDParam == null || invoiceIDParam.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Không tìm thấy mã hóa đơn để hiển thị chi tiết. Vui lòng chọn một hóa đơn.");
            response.sendRedirect("InvoiceServlet?action=list");
            return;
        }
        try {
            int invoiceID = Integer.parseInt(invoiceIDParam);
            Invoice invoice = idao.searchInvoice(invoiceID);

            if (invoice == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + invoiceID + ".");
                response.sendRedirect("InvoiceServlet?action=list");
                return;
            }

            try {
                updateInvoiceTotals(request, invoiceID);
          
                invoice = idao.searchInvoice(invoiceID); 
                if (invoice == null) {
                    request.getSession().setAttribute("errorMessage", "Không tìm thấy hóa đơn sau khi cập nhật tổng tiền.");
                    response.sendRedirect("InvoiceServlet?action=list");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("errorMessage", "Lỗi khi cập nhật tổng tiền hóa đơn: " + e.getMessage());
                response.sendRedirect("InvoiceServlet?action=list");
                return;
            }
    
            List<InvoiceDetail> details = idetail.getDetailByInvoiceID(invoiceID);
            if (details == null) {
                details = new ArrayList<>();
            }

            Customer selectedCustomer = null;
            if (invoice.getCustomerID() > 0) {
                selectedCustomer = cDAO.getCustomerById(invoice.getCustomerID());
            }

            int shopID = invoice.getShopID();
            List<Inventory> inventoriesInShop = inventoryDAO.getAllInventoriesInStore(shopID);
            List<Product> products = pDAO.getAllProducts();
            List<Customer> customers = cDAO.getAllCustomer();
            List<Employee> employees = eDAO.getAllEmployee();

            Shop selectedShop = null;
            try {
                selectedShop = sDAO.getShopByID(shopID, "SWP1");
            } catch (Exception e) {
                System.err.println("Lỗi khi lấy thông tin cửa hàng: " + e.getMessage());
                request.setAttribute("warningMessage", "Không thể lấy thông tin cửa hàng liên quan đến hóa đơn.");
            }

            String editDetailIDParam = request.getParameter("editDetailID");
            if (editDetailIDParam != null && !editDetailIDParam.isEmpty()) {
                try {
                    int editDetailID = Integer.parseInt(editDetailIDParam);
                    request.setAttribute("editDetailID", editDetailID);
                } catch (NumberFormatException e) {
                    System.err.println("ID chi tiết hóa đơn chỉnh sửa không hợp lệ: " + editDetailIDParam);
                }
            }

            request.setAttribute("selectedInvoice", invoice); 
            request.setAttribute("invoiceDetails", details);
            request.setAttribute("selectedCustomer", selectedCustomer);
            request.setAttribute("inventories", inventoriesInShop);
            request.setAttribute("products", products);
            request.setAttribute("customers", customers);
            request.setAttribute("employees", employees);
            request.setAttribute("selectedShop", selectedShop);

            request.getRequestDispatcher("InvoiceDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Mã hóa đơn không hợp lệ.");
            response.sendRedirect("InvoiceServlet?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Lỗi hệ thống khi hiển thị chi tiết hóa đơn: " + e.getMessage());
            response.sendRedirect("InvoiceServlet?action=list");
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
