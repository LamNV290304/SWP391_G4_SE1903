/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DTO.SalesEmployeeStatisticDto;
import Context.DBContext;
import DTO.SoldProductDetailDto;
import Dal.EmployeeDAO;
import Dal.ExportStatisticDAO;
import Dal.InvoiceDAO;
import Dal.InvoiceDetailDAO;
import Dal.ShopDAO;
import Models.Employee;
import Models.ShopOwner;
import Utils.ExcelExporter;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author duckh
 */
public class StatisticServlet extends HttpServlet {

    private DBContext dbContext;
    private InvoiceDAO iDAO;
    private EmployeeDAO eDAO;
    private InvoiceDetailDAO idetailDAO;
    private ShopDAO sDAO;
    private ExportStatisticDAO exDAO;

    private static final Logger LOGGER = Logger.getLogger(StatisticServlet.class.getName());
    private static final int DEFAULT_RECORDS_PER_PAGE = 5;

    public boolean init(HttpServletRequest request, HttpServletResponse response) {
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        if (databaseName == null) {
            try {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        DBContext connection = new DBContext(databaseName);
        idetailDAO = new InvoiceDetailDAO(connection.getConnection());
        iDAO = new InvoiceDAO(connection.getConnection());
        eDAO = new EmployeeDAO(connection.getConnection());
        sDAO = new ShopDAO(connection.getConnection());
        exDAO = new ExportStatisticDAO(connection.getConnection());
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        // Giữ nguyên comment về đóng kết nối, tùy thuộc vào DBContext
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!init(request, response)) {
            return;
        }
        String action = request.getParameter("action");
        if ("export".equalsIgnoreCase(action)) {
            try {
                exportEmployeeStatisticExcel(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(StatisticServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        HttpSession session = request.getSession();
        String userRoleName = null;

        Employee loggedInEmployee = (Employee) session.getAttribute("Employee");
        if (loggedInEmployee != null && loggedInEmployee.getRole() != null) {
            userRoleName = loggedInEmployee.getRole().getName();
        }

        // Handle specific actions like Excel export
//        if ("exportExcel".equals(action)) {
////            exportSalesStatisticsToExcel(request, response);
//        } else {
        try {
            // Dispatch to role-specific handlers
            switch (userRoleName) {

                case "Manager":
                    handleManagerRequest(request, response);
                    break;
                case "Cashier":
                    handleCashierRequest(request, response);
                    break;
                case "Sale":
                    handleSaleRequest(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này.");
                    return;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during statistics retrieval for role: " + userRoleName, e);
            request.setAttribute("errorMessage", "Đã xảy ra lỗi cơ sở dữ liệu: " + e.getMessage());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during statistics retrieval for role: " + userRoleName, e);
            request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());

        }
//        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    // --- Các phương thức handle cho từng vai trò ---

    private void handleManagerRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        Employee loggedInManager = (Employee) session.getAttribute("Employee");

        if (loggedInManager == null || loggedInManager.getShopId() == 0
                || loggedInManager.getRole() == null
                || !"Manager".equals(loggedInManager.getRole().getName())) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer shopId = loggedInManager.getShopId();

        String selectedEmployeeIdParam = request.getParameter("employeeId");
        String pageParam = request.getParameter("page");
        String selectedMonthParam = request.getParameter("selectedMonth");
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        Timestamp startDate = null;
        Timestamp endDate = null;

        try {
            if (startDateParam != null && !startDateParam.isEmpty() && endDateParam != null && !endDateParam.isEmpty()) {
                startDate = Timestamp.valueOf(startDateParam + " 00:00:00");
                endDate = Timestamp.valueOf(endDateParam + " 23:59:59");
            } else if (selectedMonthParam != null && !selectedMonthParam.isEmpty()) {
                int month = Integer.parseInt(selectedMonthParam);
                int currentYear = LocalDate.now().getYear(); // Lấy năm hiện tại
                YearMonth yearMonth = YearMonth.of(currentYear, month);
                startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
                endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));
            } else {
                // Mặc định: 1 tháng trước đến hiện tại
                LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
                startDateParam = oneMonthAgo.toString();
                startDate = Timestamp.valueOf(oneMonthAgo.atStartOfDay());

                LocalDate today = LocalDate.now();
                endDateParam = today.toString();
                endDate = Timestamp.valueOf(today.atTime(23, 59, 59));
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ.");
            // Đảm bảo truyền đủ các tham số, bao gồm selectedMonthParam
            setCommonRequestAttributes(request, loggedInManager, new ArrayList<>(), BigDecimal.ZERO, 0, 1, 1, 0, startDateParam, endDateParam, selectedMonthParam);
            request.setAttribute("statisticType", "managerSummary"); // Giữ nguyên loại thống kê
            request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            return;
        }

        // Lấy tất cả Sale + Cashier trong shop
        List<Employee> allowedEmployees = eDAO.getEmployeesByRoleAndShop(shopId, List.of(3, 4));
        request.setAttribute("filterableEmployees", allowedEmployees);

        if (selectedEmployeeIdParam == null || selectedEmployeeIdParam.isEmpty()) {
            selectedEmployeeIdParam = "all";
        }
        request.setAttribute("selectedEmployeeId", selectedEmployeeIdParam);
        request.setAttribute("selectedMonth", selectedMonthParam); // Đã có

        int currentPage = 1;
        try {
            if (pageParam != null) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        int totalRecords = 1;
        int totalPages = 0;
        List<SalesEmployeeStatisticDto> statistics = new ArrayList<>();

        if ("all".equals(selectedEmployeeIdParam)) {

            List<Integer> employeeIds = new ArrayList<>();
            employeeIds.add(loggedInManager.getId());
            for (Employee emp : allowedEmployees) {
                employeeIds.add(emp.getId());

            }

            totalRecords = eDAO.getTotalSalesStatisticsCountByEmployeeIds(employeeIds, shopId, startDate, endDate);

            totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_RECORDS_PER_PAGE);
            currentPage = Math.max(1, Math.min(currentPage, totalPages));
            statistics = eDAO.getSalesStatisticsByEmployeeIds(employeeIds, shopId, startDate, endDate, currentPage, DEFAULT_RECORDS_PER_PAGE);

        } else {

            try {
                int employeeId = Integer.parseInt(selectedEmployeeIdParam);

                Employee selected = allowedEmployees.stream()
                        .filter(emp -> emp.getId() == employeeId)
                        .findFirst()
                        .orElse(null);

                if (selected != null) {

                    int roleId = selected.getRoleId();
                    totalRecords = eDAO.getTotalSalesStatisticsCount(employeeId, shopId, startDate, endDate, List.of(roleId));

                    totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_RECORDS_PER_PAGE);
                    currentPage = Math.max(1, Math.min(currentPage, totalPages));
                    statistics = eDAO.getSalesStatistics(employeeId, shopId, startDate, endDate, currentPage, DEFAULT_RECORDS_PER_PAGE, List.of(roleId));
                    if (roleId == 4) {

                        List<SoldProductDetailDto> productSaleStatistics = idetailDAO.getSoldProductDetailsByEmployee(
                                employeeId, shopId, startDate, endDate, currentPage, 5);

                        BigDecimal overallTotalAmountSold = idetailDAO.getTotalAmountSoldForAllProductsByEmployee(employeeId, shopId, startDate, endDate);
                        int overallTotalQuantitySold = idetailDAO.getTotalQuantitySoldForAllProductsByEmployee(employeeId, shopId, startDate, endDate);

                        setCommonRequestAttributesForSale(request, selected, productSaleStatistics, overallTotalAmountSold, overallTotalQuantitySold,
                                currentPage, totalPages, totalRecords, startDateParam, endDateParam, selectedMonthParam);
                        request.setAttribute("userRole", "Cashier");
                        request.setAttribute("statisticType", "saleProductDetail");
                        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);

                    } else {
                        request.setAttribute("statisticType", "cashierSummary");
                    }
                } else {
                    request.setAttribute("errorMessage", "Nhân viên được chọn không hợp lệ hoặc không thuộc cửa hàng.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số nhân viên không hợp lệ: " + selectedEmployeeIdParam);
            }
        }

        BigDecimal totalSoldRevenue = idetailDAO.getTotalProductRevenueByShop(shopId, startDate, endDate);
        int totalSoldProducts = idetailDAO.getTotalSoldProductsByShop(shopId);

        setCommonRequestAttributes(request, loggedInManager, statistics, totalSoldRevenue, totalSoldProducts,
                currentPage, totalPages, totalRecords, startDateParam, endDateParam, selectedMonthParam);

        request.setAttribute("userRole", "Manager");
        request.setAttribute("statisticType", "managerSummary");
        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
    }

    private void handleCashierRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        Employee loggedInCashier = (Employee) session.getAttribute("Employee");

        if (loggedInCashier == null || loggedInCashier.getId() == 0 || loggedInCashier.getShopId() == 0) {
            request.setAttribute("errorMessage", "Thông tin nhân viên thu ngân không khả dụng hoặc không có thông tin cửa hàng.");
            request.setAttribute("statisticType", "cashierSummary");
            request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            return;
        }

        Integer shopId = loggedInCashier.getShopId();
        int currentPage = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ex) {
            currentPage = 1;
        }
        int totalPages = 1;
        int totalRecords = 0;
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String selectedMonthParam = request.getParameter("selectedMonth");
        Timestamp startDate = null;
        Timestamp endDate = null;

        try {
            if (selectedMonthParam != null && !selectedMonthParam.isEmpty()) {

                int month = Integer.parseInt(selectedMonthParam);
                int currentYear = LocalDate.now().getYear();
                YearMonth yearMonth = YearMonth.of(currentYear, month);
                startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
                endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

                startDateParam = null;
                endDateParam = null;

            } else if (startDateParam != null && !startDateParam.isEmpty() && endDateParam != null && !endDateParam.isEmpty()) {

                startDate = Timestamp.valueOf(startDateParam + " 00:00:00");
                endDate = Timestamp.valueOf(endDateParam + " 23:59:59");
            } else {

                LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
                startDateParam = oneMonthAgo.toString();
                startDate = Timestamp.valueOf(oneMonthAgo.atStartOfDay());

                LocalDate today = LocalDate.now();
                endDateParam = today.toString();
                endDate = Timestamp.valueOf(today.atTime(23, 59, 59));
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ.");

            setCommonRequestAttributes(request, loggedInCashier, new ArrayList<>(), BigDecimal.ZERO, 0, 1, 1, 0, startDateParam, endDateParam, selectedMonthParam);
            request.setAttribute("statisticType", "cashierSummary");
            request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            return;
        }

        List<Employee> salesEmployeesInShop = eDAO.getEmployeesByRoleAndShop(shopId, List.of(4));
        List<Employee> filterableEmployees = new ArrayList<>();
        filterableEmployees.add(loggedInCashier);
        for (Employee emp : salesEmployeesInShop) {
            if (emp.getId() != loggedInCashier.getId()) {
                filterableEmployees.add(emp);
            }
        }
        request.setAttribute("filterableEmployees", filterableEmployees);

        String selectedEmployeeIdParam = request.getParameter("employeeId");
        if (selectedEmployeeIdParam == null || selectedEmployeeIdParam.isEmpty()) {
            selectedEmployeeIdParam = "all";
        }
        request.setAttribute("selectedEmployeeId", selectedEmployeeIdParam);
        request.setAttribute("selectedMonth", selectedMonthParam);

        List<SalesEmployeeStatisticDto> salesStatistics = new ArrayList<>();
        if ("all".equals(selectedEmployeeIdParam)) {

            List<Integer> employeeIds = new ArrayList<>();
            employeeIds.add(loggedInCashier.getId());
            for (Employee emp : salesEmployeesInShop) {
                employeeIds.add(emp.getId());
            }
            totalRecords = eDAO.getTotalSalesStatisticsCountByEmployeeIds(employeeIds, shopId, startDate, endDate);

            totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_RECORDS_PER_PAGE);
            currentPage = Math.max(1, Math.min(currentPage, totalPages));
            salesStatistics = eDAO.getSalesStatisticsByEmployeeIds(employeeIds, shopId, startDate, endDate, currentPage, DEFAULT_RECORDS_PER_PAGE);

        } else {
            try {
                int employeeId = Integer.parseInt(selectedEmployeeIdParam);

                Employee selected = filterableEmployees.stream()
                        .filter(emp -> emp.getId() == employeeId)
                        .findFirst()
                        .orElse(null);

                if (selected != null) {

                    int roleId = selected.getRoleId();
                    totalRecords = eDAO.getTotalSalesStatisticsCount(employeeId, shopId, startDate, endDate, List.of(roleId));

                    totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_RECORDS_PER_PAGE);
                    currentPage = Math.max(1, Math.min(currentPage, totalPages));
                    salesStatistics = eDAO.getSalesStatistics(employeeId, shopId, startDate, endDate, currentPage, DEFAULT_RECORDS_PER_PAGE, List.of(roleId));
                    if (roleId == 4) {

                        List<SoldProductDetailDto> productSaleStatistics = idetailDAO.getSoldProductDetailsByEmployee(
                                employeeId, shopId, startDate, endDate, currentPage, DEFAULT_RECORDS_PER_PAGE);

                        BigDecimal overallTotalAmountSold = idetailDAO.getTotalAmountSoldForAllProductsByEmployee(employeeId, shopId, startDate, endDate);
                        int overallTotalQuantitySold = idetailDAO.getTotalQuantitySoldForAllProductsByEmployee(employeeId, shopId, startDate, endDate);

                        setCommonRequestAttributesForSale(request, selected, productSaleStatistics, overallTotalAmountSold, overallTotalQuantitySold,
                                currentPage, totalPages, totalRecords, startDateParam, endDateParam, selectedMonthParam);
                        request.setAttribute("userRole", "Cashier");
                        request.setAttribute("statisticType", "saleProductDetail");
                        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);

                    } else {
                        request.setAttribute("statisticType", "cashierSummary");
                    }
                } else {
                    request.setAttribute("errorMessage", "Nhân viên được chọn không hợp lệ hoặc không thuộc cửa hàng.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số nhân viên không hợp lệ: " + selectedEmployeeIdParam);
            }
        }

        BigDecimal totalSoldRevenue = idetailDAO.getTotalProductRevenueByShop(shopId, startDate, endDate);
        int totalSoldProducts = idetailDAO.getTotalSoldProductsByShop(shopId);

        setCommonRequestAttributes(request, loggedInCashier, salesStatistics, totalSoldRevenue, totalSoldProducts,
                currentPage, totalPages, totalRecords, startDateParam, endDateParam, selectedMonthParam);
        if (request.getAttribute("statisticType") == null) {
            request.setAttribute("statisticType", "cashierSummary");
        }
        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
    }

    private void setCommonRequestAttributes(HttpServletRequest request, Employee loggedInCashier,
            List<SalesEmployeeStatisticDto> salesStatistics,
            BigDecimal totalSoldRevenue, int totalOrders,
            int currentPage, int totalPages, int totalRecords,
            String startDateParam, String endDateParam, String selectedMonthParam) {
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalSoldRevenue", totalSoldRevenue);

        request.setAttribute("salesStatistics", salesStatistics != null ? salesStatistics : new ArrayList<>());

        request.setAttribute("userRole", loggedInCashier != null && loggedInCashier.getRole() != null ? loggedInCashier.getRole().getName() : "Unknown");

        request.setAttribute("statisticTitle", "Thống kê Doanh số của " + loggedInCashier.getFullname());
        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("selectedMonth", selectedMonthParam);
        request.setAttribute("recordsPerPage", DEFAULT_RECORDS_PER_PAGE);
        request.setAttribute("totalRecords", totalRecords);

    }

    private void setCommonRequestAttributesForSale(HttpServletRequest request, Employee loggedInEmployee,
            List<SoldProductDetailDto> productSaleStatistics,
            BigDecimal totalSoldRevenue, int totalQuantitySold,
            int currentPage, int totalPages, int totalRecords,
            String startDateParam, String endDateParam, String selectedMonthParam) {

        request.setAttribute("totalSoldRevenue", totalSoldRevenue != null ? totalSoldRevenue : BigDecimal.ZERO);
        request.setAttribute("totalQuantitySold", totalQuantitySold);

        request.setAttribute("productSaleStatistics", productSaleStatistics != null ? productSaleStatistics : new ArrayList<>());
        request.setAttribute("userRole", loggedInEmployee != null && loggedInEmployee.getRole() != null ? loggedInEmployee.getRole().getName() : "Unknown");

        request.setAttribute("statisticType", "saleProductDetail");
        request.setAttribute("statisticTitle", "Thống kê Sản phẩm đã bán của " + loggedInEmployee.getFullname());
        request.setAttribute("selectedMonth", selectedMonthParam);
        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", DEFAULT_RECORDS_PER_PAGE);
        request.setAttribute("totalRecords", totalRecords);
    }

    private void handleSaleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        Employee loggedInEmployee = (Employee) session.getAttribute("Employee");

        if (loggedInEmployee == null
                || loggedInEmployee.getRole() == null
                || !"Sale".equals(loggedInEmployee.getRole().getName())) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer employeeId = loggedInEmployee.getId();
        Integer shopId = loggedInEmployee.getShopId();

        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String pageParam = request.getParameter("page");
        String recordsPerPageParam = request.getParameter("recordsPerPage");
        String selectedMonthParam = request.getParameter("selectedMonth");

        Timestamp startDate = null;
        Timestamp endDate = null;

        try {
            if (selectedMonthParam != null && !selectedMonthParam.isEmpty()) {

                int month = Integer.parseInt(selectedMonthParam);
                int currentYear = LocalDate.now().getYear();
                YearMonth yearMonth = YearMonth.of(currentYear, month);
                startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
                endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

                startDateParam = null;
                endDateParam = null;

            } else if (startDateParam != null && !startDateParam.isEmpty() && endDateParam != null && !endDateParam.isEmpty()) {

                startDate = Timestamp.valueOf(startDateParam + " 00:00:00");
                endDate = Timestamp.valueOf(endDateParam + " 23:59:59");
            } else {

                LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
                startDateParam = oneMonthAgo.toString();
                startDate = Timestamp.valueOf(oneMonthAgo.atStartOfDay());

                LocalDate today = LocalDate.now();
                endDateParam = today.toString();
                endDate = Timestamp.valueOf(today.atTime(23, 59, 59));
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ.");

            setCommonRequestAttributesForSale(request, loggedInEmployee, new ArrayList<>(), BigDecimal.ZERO, 0, 1, 1, 0, startDateParam, endDateParam, selectedMonthParam); // <-- THÊM selectedMonthParam
            request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            return;
        }

        List<Employee> filterableEmployees = new ArrayList<>();
        filterableEmployees.add(loggedInEmployee);
        request.setAttribute("filterableEmployees", filterableEmployees);
        request.setAttribute("selectedEmployeeId", String.valueOf(employeeId));
        request.setAttribute("selectedMonth", selectedMonthParam);

        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Tham số trang không hợp lệ.");
                currentPage = 1;
            }
        }

        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        if (recordsPerPageParam != null && !recordsPerPageParam.isEmpty()) {
            try {
                recordsPerPage = Integer.parseInt(recordsPerPageParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        int totalRecords = idetailDAO.getTotalSoldProductDetailsCountByEmployee(
                employeeId, shopId, startDate, endDate);

        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        currentPage = Math.max(1, Math.min(currentPage, totalPages > 0 ? totalPages : 1));

        List<SoldProductDetailDto> productSaleStatistics = idetailDAO.getSoldProductDetailsByEmployee(
                employeeId, shopId, startDate, endDate, currentPage, recordsPerPage);

        BigDecimal overallTotalAmountSold = idetailDAO.getTotalAmountSoldForAllProductsByEmployee(employeeId, shopId, startDate, endDate);
        int overallTotalQuantitySold = idetailDAO.getTotalQuantitySoldForAllProductsByEmployee(employeeId, shopId, startDate, endDate);

        setCommonRequestAttributesForSale(request, loggedInEmployee, productSaleStatistics, overallTotalAmountSold, overallTotalQuantitySold,
                currentPage, totalPages, totalRecords, startDateParam, endDateParam, selectedMonthParam);

        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
    }

    private boolean isEmployeeInEmployeeList2(int employeeId, List<SalesEmployeeStatisticDto> employeeList) {
        for (SalesEmployeeStatisticDto dto : employeeList) {
            if (dto.getEmployeeID() == employeeId) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmployeeInEmployeeList(int employeeId, List<Employee> employees) {
        if (employees == null) {
            return false;
        }
        for (Employee e : employees) {
            if (e.getId() == employeeId) {
                return true;
            }
        }
        return false;
    }

    private void exportEmployeeStatisticExcel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        Employee loggedInEmployee = (Employee) session.getAttribute("Employee");

        if (loggedInEmployee == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int currentEmployeeId = loggedInEmployee.getId();
        int shopId = loggedInEmployee.getShopId();
        String role = loggedInEmployee.getRole().getName();

        String statisticType = request.getParameter("statisticType");
        String selectedEmployeeIdParam = request.getParameter("selectedEmployeeId");
        Integer selectedEmployeeId = null;

        List<Integer> employeeIdsForExport = new ArrayList<>();

        if (selectedEmployeeIdParam != null && selectedEmployeeIdParam.equalsIgnoreCase("all")) {
            // Nếu chọn "All", lấy tất cả nhân viên có vai trò 3 và 4 trong shop
            List<Employee> salesCashierEmployeesInShop = eDAO.getEmployeesByRoleAndShop(shopId, List.of(3, 4)); // Giả sử 3: Cashier, 4: Sale
            for (Employee emp : salesCashierEmployeesInShop) {
                employeeIdsForExport.add(emp.getId());
            }
        } else if (selectedEmployeeIdParam != null && !selectedEmployeeIdParam.isEmpty()) {
            try {
                selectedEmployeeId = Integer.parseInt(selectedEmployeeIdParam);
                employeeIdsForExport.add(selectedEmployeeId);
            } catch (NumberFormatException e) {
                // Nếu có lỗi parse hoặc không có tham số, mặc định là nhân viên hiện tại
                employeeIdsForExport.add(currentEmployeeId);
            }
        } else {
            // Mặc định, chỉ export của nhân viên hiện tại nếu không có lựa chọn cụ thể
            employeeIdsForExport.add(currentEmployeeId);
        }
        String selectedMonthParam = request.getParameter("selectedMonth");
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");

        Timestamp startDate, endDate;
        try {
            if (selectedMonthParam != null && !selectedMonthParam.isEmpty()) {

                int month = Integer.parseInt(selectedMonthParam);
                int currentYear = LocalDate.now().getYear();
                YearMonth yearMonth = YearMonth.of(currentYear, month);
                startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
                endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));
            } else if (startDateParam != null && !startDateParam.isEmpty() && endDateParam != null && !endDateParam.isEmpty()) {

                startDate = Timestamp.valueOf(LocalDate.parse(startDateParam).atStartOfDay());
                endDate = Timestamp.valueOf(LocalDate.parse(endDateParam).atTime(23, 59, 59));
            } else {

                startDate = Timestamp.valueOf(LocalDate.now().minusMonths(1).atStartOfDay());
                endDate = Timestamp.valueOf(LocalDate.now().atTime(23, 59, 59));
            }
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng ngày không hợp lệ.");
            return;
        }
        List<Employee> allowedEmployees = eDAO.getEmployeesByRoleAndShop(shopId, List.of(3, 4));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Sales_Statistics.xlsx");
        try (OutputStream out = response.getOutputStream()) {
            if ("all".equalsIgnoreCase(statisticType) || ("Manager".equalsIgnoreCase(role))) {

                Set<Integer> uniqueEmployeeIds = new HashSet<>();

                uniqueEmployeeIds.add(loggedInEmployee.getId());

                List<Employee> salesEmployeesInShop = eDAO.getEmployeesByRoleAndShop(shopId, List.of(3, 4));
                for (Employee emp : salesEmployeesInShop) {
                    uniqueEmployeeIds.add(emp.getId());
                }

          

                List<SalesEmployeeStatisticDto> allStats = eDAO.getSalesStatisticsBySpecificEmployeeIdsForExcel(
                        shopId, startDate, endDate, employeeIdsForExport);

                ExcelExporter.exportAllStatistics(allStats, out);

            } else if ("cashierSummary".equalsIgnoreCase(statisticType)
                    || ("Cashier".equalsIgnoreCase(role) && statisticType == null)) {

                if (selectedEmployeeId == null) {
                    selectedEmployeeId = currentEmployeeId;
                }

                List<SalesEmployeeStatisticDto> statisticList = exDAO.getCashierSummaryStatistics(
                        selectedEmployeeId, startDate, endDate, shopId);

                BigDecimal totalRevenue = idetailDAO.getTotalProductRevenueByShop(shopId, startDate, endDate);
                int totalSoldProducts = idetailDAO.getTotalSoldProductsByShop(shopId);

                ExcelExporter.exportCashierSummaryStatistics(statisticList, totalRevenue, totalSoldProducts, out);

            } else if ("saleProductDetail".equalsIgnoreCase(statisticType)
                    || ("Sale".equalsIgnoreCase(role) && statisticType == null)) {

                if (selectedEmployeeId == null) {
                    selectedEmployeeId = currentEmployeeId;
                }

                List<SoldProductDetailDto> soldList = exDAO.getSoldProductDetailsForExport(
                        selectedEmployeeId, shopId, startDate, endDate);

                int totalQuantity = idetailDAO.getTotalQuantitySoldForAllProductsByEmployee(
                        selectedEmployeeId, shopId, startDate, endDate);

                BigDecimal totalAmount = idetailDAO.getTotalAmountSoldForAllProductsByEmployee(
                        selectedEmployeeId, shopId, startDate, endDate);

                ExcelExporter.exportSaleProductStatistics(soldList, totalQuantity, totalAmount, out);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Loại thống kê không hợp lệ hoặc vai trò không được hỗ trợ.");
            }

        } catch (Exception e) {
            Logger.getLogger(StatisticServlet.class.getName()).log(Level.SEVERE, "Lỗi khi xuất file Excel", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tạo file Excel.");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý thống kê doanh số";
    }
}
