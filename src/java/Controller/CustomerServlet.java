/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.CustomerDAO;
import Models.Customer;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 *
 * @author duckh
 */
public class CustomerServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    private CustomerDAO cDAO = new CustomerDAO(connection.getConnection());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "list":
                listCustomers(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "deactivate":
                deactivateCustomer(request, response);
                break;
            case "showCreateForm":
                showCreateForm(request, response);
                break;
            case "activate":
                activateCustomer(request, response);
                break;
            default:
                listCustomers(request, response);
                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "addOrUpdate";
        }

        switch (action) {
            case "addCustomer":
                addCustomer(request, response);
                break;
            case "updateCustomer":
                UpdateCustomer(request, response);
                break;
            case "deleteCustomer":
                deleteCustomer(request, response);
                break;

            default:
                listCustomers(request, response);
                break;
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phone = request.getParameter("phone");
        String invoiceID = request.getParameter("invoiceID");
        request.setAttribute("customerPhone", phone);
        if (invoiceID != null && !invoiceID.isEmpty()) {
            request.setAttribute("returnInvoiceID", invoiceID);
        }

        request.getRequestDispatcher("customerForm.jsp").forward(request, response);

    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchKeyword = request.getParameter("searchKeyword");
        List<Customer> customers;
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            customers = cDAO.searchCustomers(searchKeyword);
        } else {
            customers = cDAO.getAllCustomer();
        }
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("customer_list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("customerForm.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer existingCustomer = cDAO.getCustomerById(id);
        request.setAttribute("customer", existingCustomer);
        request.getRequestDispatcher("/customerForm.jsp").forward(request, response);
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("customerName");
            String phone = request.getParameter("customerPhone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            // Lấy invoiceID để quay lại hóa đơn
            String returnInvoiceID = request.getParameter("returnInvoiceID");

            Timestamp createdDate = Timestamp.from(Instant.now());
            String createdBy = (String) request.getAttribute("username"); //
            if (createdBy == null || createdBy.isEmpty()) {
                createdBy = "System";
            }

            Customer newCustomer = new Customer();
            newCustomer.setCustomerName(name);
            newCustomer.setPhone(phone);
            newCustomer.setEmail(email);
            newCustomer.setAddress(address);
            newCustomer.setStatus(true);
            newCustomer.setCreatedDate(createdDate);
            newCustomer.setCreatedBy(createdBy);

            int newCustomerID = cDAO.addCustomer(newCustomer);
            if (newCustomerID != -1) {
                request.setAttribute("successMessage", "Thêm khách hàng thành công! ID: " + newCustomerID);

                if (returnInvoiceID != null && !returnInvoiceID.isEmpty()) {

                    response.sendRedirect(request.getContextPath()
                            + "/InvoiceServlet?action=manageInvoiceDetails&invoiceID=" + returnInvoiceID
                            + "&newCustomerID=" + newCustomerID);
                } else {

                    response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
                }
            } else {

                request.setAttribute("customerName", name);
                request.setAttribute("customerPhone", phone);
                request.setAttribute("customerEmail", email);
                request.setAttribute("customerAddress", address);
                request.setAttribute("returnInvoiceID", returnInvoiceID);

                request.setAttribute("errorMessage", "Thêm khách hàng thất bại: Số điện thoại **" + phone + "** đã tồn tại. Vui lòng sử dụng số khác.");
                request.getRequestDispatcher("/customerForm.jsp").forward(request, response);
            }
        } catch (Exception e) {

            request.setAttribute("customerName", request.getParameter("customerName"));
            request.setAttribute("customerPhone", request.getParameter("phone"));
            request.setAttribute("customerEmail", request.getParameter("email"));
            request.setAttribute("customerAddress", request.getParameter("address"));
            request.setAttribute("returnInvoiceID", request.getParameter("returnInvoiceID"));

            request.setAttribute("errorMessage", "Đã xảy ra lỗi hệ thống khi thêm khách hàng mới: " + e.getMessage());
            request.getRequestDispatcher("/customerForm.jsp").forward(request, response);
        }
    }

    private void UpdateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerID = Integer.parseInt(request.getParameter("customerID"));
            String name = request.getParameter("customerName");
            String phone = request.getParameter("customerPhone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");

            Customer existingCustomer = cDAO.getCustomerById(customerID);
            if (existingCustomer == null) {
                request.setAttribute("errorMessage", "Không tìm thấy khách hàng cần cập nhật.");
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
                return;
            }

            Customer customerToUpdate = new Customer();
            customerToUpdate.setCustomerID(customerID);
            customerToUpdate.setCustomerName(name);
            customerToUpdate.setPhone(phone);
            customerToUpdate.setEmail(email);
            customerToUpdate.setAddress(address);

            customerToUpdate.setStatus(existingCustomer.isStatus());
            customerToUpdate.setCreatedDate(existingCustomer.getCreatedDate());
            customerToUpdate.setCreatedBy(existingCustomer.getCreatedBy());

            boolean updated = cDAO.updateCustomer(customerToUpdate);
            if (updated) {
                request.setAttribute("successMessage", "Cập nhật khách hàng thành công!");
            } else {
                request.setAttribute("errorMessage", "Cập nhật khách hàng thất bại.");
            }
        } catch (NumberFormatException e) {

            request.setAttribute("errorMessage", "ID khách hàng không hợp lệ khi cập nhật.");
        } catch (Exception e) {

            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật khách hàng.");
        }
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerIDParam = request.getParameter("id");
        if (customerIDParam == null || customerIDParam.trim().isEmpty()) {
            request.setAttribute("errorMessage", "ID khách hàng không được để trống.");
            listCustomers(request, response);
            return;
        }
        try {
            int customerID = Integer.parseInt(customerIDParam);
            Customer customer = cDAO.getCustomerById(customerID);
            if (customer == null) {
                request.setAttribute("errorMessage", "Không tìm thấy khách hàng để xóa.");
                listCustomers(request, response);
                return;
            }
            boolean deleted = cDAO.deleteCustomer(customerID);
            if (deleted) {
                request.setAttribute("successMessage", "Khách hàng đã được xóa vĩnh viễn!");
                listCustomers(request, response);
            } else {
                request.setAttribute("errorMessage", "Xóa khách hàng thất bại.");
                listCustomers(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID khách hàng không hợp lệ.");
            listCustomers(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống khi xóa khách hàng: " + e.getMessage());
            listCustomers(request, response);
        }
    }

    private void deactivateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean deactivated = cDAO.updateCustomerStatus(id, false);
        if (deactivated) {
            request.setAttribute("message", "Vô hiệu hóa khách hàng thành công!");
        } else {
            request.setAttribute("errorMessage", "Vô hiệu hóa khách hàng thất bại.");
        }
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
    }

    private void activateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean activated = cDAO.updateCustomerStatus(id, true);
        if (activated) {
            request.setAttribute("message", "Kích hoạt khách hàng thành công!");
        } else {
            request.setAttribute("errorMessage", "Kích hoạt khách hàng thất bại.");
        }
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
    }
}
