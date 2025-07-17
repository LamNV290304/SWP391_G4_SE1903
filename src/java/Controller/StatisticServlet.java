/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DTO.SalesEmployeeStatisticDto;
import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ShopDAO;
import Models.Employee;
import Models.ShopOwner;
import Models.Shop;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duckh
 */
public class StatisticServlet extends HttpServlet {

    private DBContext dbContext;
    private EmployeeDAO eDAO;
    private ShopDAO sDAO;
    private static final int DEFAULT_RECORDS_PER_PAGE = 5;
    private static final Logger LOGGER = Logger.getLogger(StatisticServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();

        dbContext = new DBContext("SWP1");
        eDAO = new EmployeeDAO(dbContext.getConnection());
        sDAO = new ShopDAO();

    }

    @Override
    public void destroy() {
        super.destroy();
        // Giữ nguyên comment về đóng kết nối, tùy thuộc vào DBContext
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Logic chung nếu có
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userRoleName = null;

        ShopOwner loggedInShopOwner = (ShopOwner) session.getAttribute("shopOwner");
        if (loggedInShopOwner != null) {
            userRoleName = "ShopOwner";
        } else {
            Employee loggedInEmployee = (Employee) session.getAttribute("Employee");
            if (loggedInEmployee != null && loggedInEmployee.getRole() != null) {
                userRoleName = loggedInEmployee.getRole().getName();
            }
        }

        // Redirect to login if user is not authenticated or role is unknown
        if (userRoleName == null || "Unknown".equalsIgnoreCase(userRoleName) || userRoleName.isEmpty()) {
            response.sendRedirect("login.jsp"); // Or a more appropriate login path
            return;
        }

        String action = request.getParameter("action");

        // Handle specific actions like Excel export
        if ("exportExcel".equals(action)) {
//            exportSalesStatisticsToExcel(request, response);
        } else {
            try {
                // Dispatch to role-specific handlers
                switch (userRoleName) {
                    case "Admin":
                        handleAdminRequest(request, response);
                        break;
                    case "ShopOwner":
                        handleShopOwnerRequest(request, response);
                        break;
                    case "Cashier":
                        handleCashierRequest(request, response);
                        break;
                    case "Staff":
                        handleStaffRequest(request, response);
                        break;
                    default:
                        // If role is recognized but not handled, or not allowed
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này.");
                        return;
                }
                // Forward to the JSP page to display statistics
                request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error during statistics retrieval for role: " + userRoleName, e);
                request.setAttribute("errorMessage", "Đã xảy ra lỗi cơ sở dữ liệu: " + e.getMessage());
                request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected error during statistics retrieval for role: " + userRoleName, e);
                request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
                request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // --- Các phương thức handle cho từng vai trò ---
    private void handleAdminRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        // Retrieve and parse request parameters
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String shopIdParam = request.getParameter("shopId");
        String employeeIdParam = request.getParameter("employeeId");
        String pageParam = request.getParameter("page");

        // Explicit date parsing for startDate
        Date startDate = null;
        try {
            if (startDateParam != null && !startDateParam.isEmpty()) {
                startDate = Date.valueOf(startDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày bắt đầu không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Explicit date parsing for endDate
        Date endDate = null;
        try {
            if (endDateParam != null && !endDateParam.isEmpty()) {
                endDate = Date.valueOf(endDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày kết thúc không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Explicit integer parsing for filterShopId
        Integer filterShopId = null;
        if (shopIdParam != null && !shopIdParam.isEmpty() && !"all".equals(shopIdParam)) {
            try {
                filterShopId = Integer.parseInt(shopIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số cửa hàng không hợp lệ.");
            }
        }

        // Explicit integer parsing for targetEmployeeId
        Integer targetEmployeeId = null;
        if (employeeIdParam != null && !employeeIdParam.isEmpty() && !"all".equals(employeeIdParam)) {
            try {
                targetEmployeeId = Integer.parseInt(employeeIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số nhân viên không hợp lệ.");
            }
        }

        // Fetch all shops for the filter dropdown
        List<Shop> allShops = sDAO.getAllShops("SWP1"); // "SWP1" should ideally be dynamic or from config
        request.setAttribute("allShops", allShops);

        // Define allowed roles for sales statistics (Admin can see all relevant roles)
        List<Integer> allowedRoleIds = List.of(1, 2, 3, 4); // Example: Admin, ShopOwner, Cashier, Staff

        // Get filterable employees for the dropdown based on selected shop and allowed roles
        List<SalesEmployeeStatisticDto> filterableEmployeeDTOs = eDAO.getSalesStatistics(
                null, filterShopId, null, null, 1, Integer.MAX_VALUE, allowedRoleIds);

        List<Employee> filterableEmployees = new ArrayList<>();
        for (SalesEmployeeStatisticDto dto : filterableEmployeeDTOs) {
            Employee emp = new Employee();
            emp.setId(dto.getEmployeeID());
            emp.setFullname(dto.getFullName());
            filterableEmployees.add(emp);
        }
        request.setAttribute("allEmployees", filterableEmployees);

        // Validate if the selected employee is valid for the current filters
        if (targetEmployeeId != null && !isEmployeeInSalesStatisticList(targetEmployeeId, filterableEmployeeDTOs)) {
            request.setAttribute("errorMessage", "Nhân viên được chọn không tồn tại hoặc không thuộc các vai trò được xem.");
            targetEmployeeId = null; // Reset if invalid
        }

        // Explicit integer parsing for currentPage (pagination)
        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số trang không hợp lệ.");
                currentPage = 1; // Default to first page on error
            }
        }
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;

        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId, filterShopId, startDate, endDate, allowedRoleIds);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Adjust current page to be within valid range
        currentPage = Math.max(1, Math.min(currentPage, totalPages > 0 ? totalPages : 1));

        // Fetch sales statistics based on filters and pagination
        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId, filterShopId, startDate, endDate, currentPage, recordsPerPage, allowedRoleIds);

        // Set attributes for JSP
        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "Admin");
        request.setAttribute("statisticTitle", "Thống kê Doanh số Toàn hệ thống (Admin)");
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);

        // Retain filter values in the request for sticky forms
        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("selectedShopId", shopIdParam);
        request.setAttribute("selectedEmployeeId", employeeIdParam);
    }

    /**
     * Handles requests for the ShopOwner role. Filters statistics by the shop
     * owned by the logged-in ShopOwner. Allows further filtering by date range
     * and employees within their shop.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    private void handleShopOwnerRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        ShopOwner loggedInShopOwner = (ShopOwner) session.getAttribute("shopOwner");

        // Ensure ShopOwner is logged in
        if (loggedInShopOwner == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get the shop ID associated with the ShopOwner
        Shop shopOfOwner = sDAO.getShopByName(loggedInShopOwner.getShopName(), loggedInShopOwner.getDatabaseName());
        Integer shopId = (shopOfOwner != null) ? shopOfOwner.getShopID() : null;

        if (shopId == null) {
            request.setAttribute("errorMessage", "Không tìm thấy thông tin cửa hàng cho ShopOwner này. Vui lòng kiểm tra lại cấu hình ShopOwner.");
            return;
        }

        // Retrieve and parse request parameters
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String selectedEmployeeIdParam = request.getParameter("employeeId");
        String pageParam = request.getParameter("page");

        // Explicit date parsing for startDate
        Date startDate = null;
        try {
            if (startDateParam != null && !startDateParam.isEmpty()) {
                startDate = Date.valueOf(startDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày bắt đầu không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Explicit date parsing for endDate
        Date endDate = null;
        try {
            if (endDateParam != null && !endDateParam.isEmpty()) {
                endDate = Date.valueOf(endDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày kết thúc không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Explicit integer parsing for targetEmployeeId
        Integer targetEmployeeId = null;
        if (selectedEmployeeIdParam != null && !selectedEmployeeIdParam.isEmpty() && !"all".equals(selectedEmployeeIdParam)) {
            try {
                targetEmployeeId = Integer.parseInt(selectedEmployeeIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số nhân viên không hợp lệ.");
            }
        }

        // Allowed roles for ShopOwner (Cashier and Staff within their shop)
        List<Integer> allowedRoleIds = List.of(3, 4); // Example: Cashier, Staff

        // Get filterable employees (only those in the owner's shop with allowed roles)
        List<SalesEmployeeStatisticDto> filterableEmployeeDTOs = eDAO.getSalesStatistics(
                null, shopId, null, null, 1, Integer.MAX_VALUE, allowedRoleIds);

        List<Employee> filterableEmployees = new ArrayList<>();
        for (SalesEmployeeStatisticDto dto : filterableEmployeeDTOs) {
            Employee emp = new Employee();
            emp.setId(dto.getEmployeeID());
            emp.setFullname(dto.getFullName());
            filterableEmployees.add(emp);
        }
        request.setAttribute("filterableEmployees", filterableEmployees);

        // Validate if the selected employee is valid for the current filters
        if (targetEmployeeId != null && !isEmployeeInSalesStatisticList(targetEmployeeId, filterableEmployeeDTOs)) {
            request.setAttribute("errorMessage", "Nhân viên được chọn không thuộc cửa hàng của bạn hoặc không phải là nhân viên bán hàng/thu ngân.");
            targetEmployeeId = null; // Reset if invalid
        }

        // Explicit integer parsing for currentPage (pagination)
        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số trang không hợp lệ.");
                currentPage = 1; // Default to first page on error
            }
        }
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;

        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId, shopId, startDate, endDate, allowedRoleIds);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        currentPage = Math.max(1, Math.min(currentPage, totalPages > 0 ? totalPages : 1));

        // Fetch sales statistics
        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId, shopId, startDate, endDate, currentPage, recordsPerPage, allowedRoleIds);

        // Set attributes for JSP
        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "ShopOwner");
        request.setAttribute("statisticTitle", "Thống kê Doanh số Bán hàng (Chủ cửa hàng)");
        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("selectedEmployeeId", selectedEmployeeIdParam);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);
    }

    /**
     * Handles requests for the Cashier role. Cashiers can only view statistics
     * for employees within their own shop, including themselves. They can
     * filter by date range and other employees in their shop.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    private void handleCashierRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        Employee loggedInCashier = (Employee) session.getAttribute("Employee");

        // Validate logged-in cashier information
        if (loggedInCashier == null || loggedInCashier.getId() == 0 || loggedInCashier.getShopId() == 0) {
            request.setAttribute("errorMessage", "Thông tin nhân viên thu ngân không khả dụng hoặc không có thông tin cửa hàng.");
            return;
        }

        Integer cashierShopId = loggedInCashier.getShopId();
        // Integer loggedInCashierId = loggedInCashier.getId(); // Not directly used as a filter here but could be.

        // Retrieve and parse request parameters
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String selectedEmployeeIdParam = request.getParameter("employeeId");
        String pageParam = request.getParameter("page");

        // Explicit date parsing for startDate
        Date startDate = null;
        try {
            if (startDateParam != null && !startDateParam.isEmpty()) {
                startDate = Date.valueOf(startDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày bắt đầu không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Explicit date parsing for endDate
        Date endDate = null;
        try {
            if (endDateParam != null && !endDateParam.isEmpty()) {
                endDate = Date.valueOf(endDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày kết thúc không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Explicit integer parsing for targetEmployeeId
        Integer targetEmployeeId = null;
        if (selectedEmployeeIdParam != null && !selectedEmployeeIdParam.isEmpty() && !"all".equals(selectedEmployeeIdParam)) {
            try {
                targetEmployeeId = Integer.parseInt(selectedEmployeeIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số nhân viên không hợp lệ.");
            }
        }

        // Allowed roles for Cashier's view (other cashiers and staff in their shop)
        List<Integer> allowedRoleIds = List.of(3, 4); // Example: Cashier, Staff

        // Get filterable employees within the cashier's shop and allowed roles
        List<SalesEmployeeStatisticDto> filterableEmployeeDTOs = eDAO.getSalesStatistics(
                null, cashierShopId, null, null, 1, Integer.MAX_VALUE, allowedRoleIds);

        List<Employee> filterableEmployees = new ArrayList<>();
        for (SalesEmployeeStatisticDto dto : filterableEmployeeDTOs) {
            Employee emp = new Employee();
            emp.setId(dto.getEmployeeID());
            emp.setFullname(dto.getFullName());
            filterableEmployees.add(emp);
        }
        request.setAttribute("filterableEmployees", filterableEmployees);

        // Validate selected employee
        if (targetEmployeeId != null && !isEmployeeInSalesStatisticList(targetEmployeeId, filterableEmployeeDTOs)) {
            request.setAttribute("errorMessage", "Nhân viên được chọn không tồn tại hoặc không thuộc cửa hàng của bạn/không phải nhân viên bán hàng.");
            targetEmployeeId = null; // Reset if invalid
        }

        // Explicit integer parsing for currentPage (pagination)
        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số trang không hợp lệ.");
                currentPage = 1; // Default to first page on error
            }
        }
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;

        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId, cashierShopId, startDate, endDate, allowedRoleIds);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        currentPage = Math.max(1, Math.min(currentPage, totalPages > 0 ? totalPages : 1));

        // Fetch sales statistics
        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId, cashierShopId, startDate, endDate, currentPage, recordsPerPage, allowedRoleIds);

        // Set attributes for JSP
        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "Cashier");
        request.setAttribute("statisticTitle", "Thống kê Doanh số (Thu ngân)");
        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("selectedEmployeeId", selectedEmployeeIdParam);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);
    }

    /**
     * Handles requests for the Staff role. Staff can only view their own sales
     * statistics. Filters by date range.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If a database access error occurs.
     */
    private void handleStaffRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        Employee loggedInStaff = (Employee) session.getAttribute("Employee");

        // Validate logged-in staff information
        if (loggedInStaff == null || loggedInStaff.getId() == 0 || loggedInStaff.getShopId() == 0) {
            request.setAttribute("errorMessage", "Thông tin nhân viên không khả dụng. Vui lòng đăng nhập lại.");
            return;
        }

        Integer targetEmployeeId = loggedInStaff.getId();
        Integer shopId = loggedInStaff.getShopId();

        // Retrieve and parse date parameters
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String pageParam = request.getParameter("page");

        // Explicit date parsing for startDate
        Date startDate = null;
        try {
            if (startDateParam != null && !startDateParam.isEmpty()) {
                startDate = Date.valueOf(startDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày bắt đầu không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Explicit date parsing for endDate
        Date endDate = null;
        try {
            if (endDateParam != null && !endDateParam.isEmpty()) {
                endDate = Date.valueOf(endDateParam);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày kết thúc không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        // Allowed role for Staff's view (only themselves)
        List<Integer> allowedRoleIds = List.of(4); // Example: Staff role ID

        // Explicit integer parsing for currentPage (pagination)
        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số trang không hợp lệ.");
                currentPage = 1; // Default to first page on error
            }
        }
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;

        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId, shopId, startDate, endDate, allowedRoleIds);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        currentPage = Math.max(1, Math.min(currentPage, totalPages > 0 ? totalPages : 1));

        // Fetch sales statistics for the logged-in staff
        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId, shopId, startDate, endDate, currentPage, recordsPerPage, allowedRoleIds);

        // Set attributes for JSP
        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "Staff");
        request.setAttribute("statisticTitle", "Thống kê Doanh số Cá nhân");
        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);

        // For staff, the employee dropdown should only show themselves
        List<Employee> filterableEmployees = new ArrayList<>();
        Employee staffForDropdown = new Employee();
        staffForDropdown.setId(loggedInStaff.getId());
        staffForDropdown.setFullname(loggedInStaff.getFullname());
        filterableEmployees.add(staffForDropdown);
        request.setAttribute("filterableEmployees", filterableEmployees);
        request.setAttribute("selectedEmployeeId", String.valueOf(loggedInStaff.getId())); // Pre-select their ID

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);
    }

    /**
     * Checks if a given employee ID exists in a list of
     * SalesEmployeeStatisticDto objects. This is useful for validating selected
     * employee filters against the allowed list.
     *
     * @param employeeId The employee ID to check.
     * @param dtoList The list of SalesEmployeeStatisticDto objects.
     * @return true if the employee ID is found in the list, false otherwise.
     */
    private boolean isEmployeeInSalesStatisticList(int employeeId, List<SalesEmployeeStatisticDto> dtoList) {
        if (dtoList == null) {
            return false;
        }
        for (SalesEmployeeStatisticDto dto : dtoList) {
            if (dto.getEmployeeID() == employeeId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý thống kê doanh số";
    }
}
