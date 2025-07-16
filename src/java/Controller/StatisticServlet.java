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

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duckh
 */
public class StatisticServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    EmployeeDAO eDAO = new EmployeeDAO(connection.getConnection());
    ShopDAO sDAO = new ShopDAO();
    private static final int DEFAULT_RECORDS_PER_PAGE = 5;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

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
            if (loggedInEmployee != null) {
                userRoleName = loggedInEmployee.getRole() != null ? loggedInEmployee.getRole().getName() : "Unknown";
            }
        }

        if (userRoleName == null || "Unknown".equalsIgnoreCase(userRoleName)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("exportExcel".equals(action)) {
//            exportSalesStatisticsToExcel(request, response);
        } else {

            try {
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
                    default:
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này.");
                        return;
                }
                request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
                request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userRoleName = null;

        ShopOwner loggedInShopOwner = (ShopOwner) session.getAttribute("shopOwner");
        if (loggedInShopOwner != null) {
            userRoleName = "ShopOwner";
        } else {
            Employee loggedInEmployee = (Employee) session.getAttribute("Employee");
            if (loggedInEmployee != null) {
                userRoleName = loggedInEmployee.getRole() != null ? loggedInEmployee.getRole().getName() : "Unknown";
            }
        }

        if (userRoleName == null || "Unknown".equalsIgnoreCase(userRoleName)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("exportExcel".equals(action)) {
//            exportSalesStatisticsToExcel(request, response);
        } else {

            doGet(request, response);
        }
    }

    // Trong StatisticServlet.java
    private void handleAdminRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String shopIdParam = request.getParameter("shopId");
        String employeeIdParam = request.getParameter("employeeId");

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
            e.printStackTrace();
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        Integer filterShopId = null;
        if (shopIdParam != null && !shopIdParam.isEmpty() && !"all".equals(shopIdParam)) {
            try {
                filterShopId = Integer.parseInt(shopIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID cửa hàng không hợp lệ.");
            }
        }

        Integer targetEmployeeId = null;
        if (employeeIdParam != null && !employeeIdParam.isEmpty() && !"all".equals(employeeIdParam)) {
            try {
                targetEmployeeId = Integer.parseInt(employeeIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID nhân viên không hợp lệ.");
            }
        }

        // Admin có thể xem tất cả các cửa hàng
        List<Shop> allShops = sDAO.getAllShops("SWP1");
        request.setAttribute("allShops", allShops);

        // Admin được phép xem thống kê của Staff (RoleID = 2), Cashier (RoleID = 4) và Shop Owner (RoleID = 3)
        List<Integer> allowedRoleIds = new ArrayList<>();
        allowedRoleIds.add(1); // Admin (nếu bạn muốn Admin xem doanh số của các Admin khác)
        allowedRoleIds.add(2); // Staff
        allowedRoleIds.add(3); // Shop Owner
        allowedRoleIds.add(4); // Cashier

        // Lấy danh sách tất cả nhân viên (Staff, Cashier, Shop Owner) có thể lọc cho dropdown
        List<SalesEmployeeStatisticDto> filterableEmployeeDTOs = eDAO.getSalesStatistics(
                null, // No specific employee filter here
                filterShopId, // Apply shop filter if selected by Admin
                null, null, // No date filter for getting the list of employees
                1, Integer.MAX_VALUE, // Get all records
                allowedRoleIds // Lọc theo tất cả các vai trò được phép
        );

        List<Employee> filterableEmployees = new ArrayList<>();
        for (SalesEmployeeStatisticDto dto : filterableEmployeeDTOs) {
            Employee emp = new Employee();
            emp.setId(dto.getEmployeeID());
            emp.setFullname(dto.getFullName());
            // Thêm các thuộc tính khác nếu cần hiển thị trong dropdown (ví dụ: RoleName, ShopName)
            filterableEmployees.add(emp);
        }
        request.setAttribute("allEmployees", filterableEmployees); // Use this for the employee filter dropdown

        // Validate selected employee if a filter is applied
        if (targetEmployeeId != null) {
            boolean isValidEmployee = false;
            for (Employee emp : filterableEmployees) {
                if (emp.getId() == targetEmployeeId) {
                    isValidEmployee = true;
                    break;
                }
            }
            if (!isValidEmployee) {
                request.setAttribute("errorMessage", "Nhân viên được chọn không tồn tại hoặc không phải là nhân viên thuộc các vai trò được xem.");
                targetEmployeeId = null; // Reset to null if invalid
            }
        }

        int currentPage;
        String pageParam = request.getParameter("page");
        try {
            currentPage = (pageParam != null && !pageParam.isEmpty()) ? Math.max(1, Integer.parseInt(pageParam)) : 1;
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;

        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId,
                filterShopId,
                startDate,
                endDate,
                allowedRoleIds // Lọc theo tất cả các vai trò được phép
        );
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId,
                filterShopId,
                startDate,
                endDate,
                currentPage,
                recordsPerPage,
                allowedRoleIds // Lọc theo tất cả các vai trò được phép
        );

        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "Admin");
        request.setAttribute("statisticTitle", "Thống kê Doanh số Toàn hệ thống (Admin)");

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);

        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("selectedShopId", shopIdParam);
        request.setAttribute("selectedEmployeeId", employeeIdParam);

        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
    }

    // Trong StatisticServlet.java
    private void handleShopOwnerRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        ShopOwner loggedInShopOwner = (ShopOwner) session.getAttribute("shopOwner"); // Giả sử đây là đối tượng ShopOwner của bạn
        if (loggedInShopOwner == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        System.out.println("DEBUG: LoggedInShopOwner ShopName: " + loggedInShopOwner.getShopName());
        System.out.println("DEBUG: LoggedInShopOwner DatabaseName: " + loggedInShopOwner.getDatabaseName());
        Shop shopOfOwner = sDAO.getShopByName(loggedInShopOwner.getShopName(), loggedInShopOwner.getDatabaseName());
        System.out.println("DEBUG: ShopDAO.getShopByName trả về: " + (shopOfOwner != null ? shopOfOwner.getShopID() : "null"));
        Integer shopId = (shopOfOwner != null) ? shopOfOwner.getShopID() : null;

        if (shopId == null) {
            request.setAttribute("errorMessage", "Không tìm thấy thông tin cửa hàng cho ShopOwner này. Vui lòng kiểm tra lại cấu hình ShopOwner.");
            request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
            return;
        }

        int currentPage = 1;
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        String pageStr = request.getParameter("page");
        String recordsPerPageStr = request.getParameter("recordsPerPage");
        try {
            if (pageStr != null && !pageStr.isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
            }
            if (recordsPerPageStr != null && !recordsPerPageStr.isEmpty()) {
                recordsPerPage = Integer.parseInt(recordsPerPageStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Tham số phân trang không hợp lệ.");
        }

        Date startDate = null;
        Date endDate = null;
        Integer targetEmployeeId = null;
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String selectedEmployeeIdParam = request.getParameter("employeeId");

        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = Date.valueOf(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = Date.valueOf(endDateStr);
            }
            if (selectedEmployeeIdParam != null && !selectedEmployeeIdParam.isEmpty() && !selectedEmployeeIdParam.equals("all")) {
                targetEmployeeId = Integer.parseInt(selectedEmployeeIdParam);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Tham số ngày hoặc ID nhân viên không hợp lệ.");
        }

        // Shop Owner được phép xem thống kê của Staff (RoleID = 2) và Cashier (RoleID = 4)
        List<Integer> allowedRoleIds = new ArrayList<>();
        allowedRoleIds.add(2); // Staff
        allowedRoleIds.add(4); // Cashier
        // Nếu Shop Owner cũng có doanh số cá nhân (như một Employee) và bạn muốn hiển thị, thêm RoleID của Shop Owner vào đây.
        // Ví dụ: allowedRoleIds.add(3); // RoleID 3 là ShopOwner

        // Lấy tất cả nhân viên Staff và Cashier trong cửa hàng của ShopOwner cho dropdown lọc
        List<SalesEmployeeStatisticDto> filterableEmployeeDTOs = eDAO.getSalesStatistics(
                null, // Không lọc theo EmployeeID cụ thể ở đây
                shopId, // Chỉ các nhân viên trong cửa hàng của ShopOwner
                null, null, // Không lọc theo ngày
                1, Integer.MAX_VALUE, // Lấy tất cả
                allowedRoleIds // Lọc Staff và Cashier
        );

        List<Employee> filterableEmployees = new ArrayList<>();
        for (SalesEmployeeStatisticDto dto : filterableEmployeeDTOs) {
            Employee emp = new Employee();
            emp.setId(dto.getEmployeeID());
            emp.setFullname(dto.getFullName());
            filterableEmployees.add(emp);
        }
        request.setAttribute("filterableEmployees", filterableEmployees); // Danh sách nhân viên có thể lọc

        // Kiểm tra tính hợp lệ của nhân viên được chọn (nếu có)
        if (targetEmployeeId != null) {
            boolean isValidEmployee = false;
            for (Employee emp : filterableEmployees) {
                if (emp.getId() == targetEmployeeId) {
                    isValidEmployee = true;
                    break;
                }
            }
            if (!isValidEmployee) {
                request.setAttribute("errorMessage", "Nhân viên được chọn không thuộc cửa hàng của bạn hoặc không phải là nhân viên bán hàng/thu ngân.");
                targetEmployeeId = null; // Đặt lại về null để hiển thị tất cả nếu có lỗi
            }
        }

        // Lấy tổng số bản ghi thống kê
        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId,
                shopId,
                startDate,
                endDate,
                allowedRoleIds
        );
        int noOfPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (currentPage > noOfPages && noOfPages > 0) {
            currentPage = noOfPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId,
                shopId,
                startDate,
                endDate,
                currentPage,
                recordsPerPage,
                allowedRoleIds
        );

        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "ShopOwner");
        request.setAttribute("statisticTitle", "Thống kê Doanh số Bán hàng (Chủ cửa hàng)");

        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
        request.setAttribute("selectedEmployeeId", targetEmployeeId != null ? String.valueOf(targetEmployeeId) : "all");

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
    }

    // Trong StatisticServlet.java
    private void handleCashierRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        // Ensure that loggedInUser is correctly cast to Employee for Cashier
        Employee loggedInCashier = (Employee) session.getAttribute("loggedInUser");

        if (loggedInCashier == null || loggedInCashier.getId() == 0 || loggedInCashier.getShopId() == 0) {
            request.setAttribute("errorMessage", "Thông tin nhân viên thu ngân không khả dụng hoặc không có thông tin cửa hàng.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        Integer cashierShopId = loggedInCashier.getShopId(); // Cashier only views within their shop

        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String selectedEmployeeIdParam = request.getParameter("employeeId"); // Cashier can filter by employee

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
            e.printStackTrace();
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        Integer targetEmployeeId = null;
        if (selectedEmployeeIdParam != null && !selectedEmployeeIdParam.isEmpty() && !"all".equals(selectedEmployeeIdParam)) {
            try {
                targetEmployeeId = Integer.parseInt(selectedEmployeeIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID nhân viên không hợp lệ.");
            }
        }

        // Cashier is allowed to see statistics for Staff (RoleID = 2) and themselves (Cashier - RoleID = 4)
        List<Integer> allowedRoleIds = new ArrayList<>();
        allowedRoleIds.add(2); // Staff
        allowedRoleIds.add(4); // Cashier

        // Get list of Staff and the Cashier themselves for the employee filter dropdown
        List<SalesEmployeeStatisticDto> filterableEmployeeDTOs = eDAO.getSalesStatistics(
                null, // No specific employee filter here
                cashierShopId, // Only employees within the Cashier's shop
                null, null, // No date filter
                1, Integer.MAX_VALUE, // Get all
                allowedRoleIds // Filter for Staff and Cashier roles
        );

        List<Employee> filterableEmployees = new ArrayList<>();
        for (SalesEmployeeStatisticDto dto : filterableEmployeeDTOs) {
            Employee emp = new Employee();
            emp.setId(dto.getEmployeeID()); // Using getEmployeeId()
            emp.setFullname(dto.getFullName());
            filterableEmployees.add(emp);
        }
        request.setAttribute("filterableEmployees", filterableEmployees); // List of filterable employees

        // Validate selected employee if a filter is applied
        if (targetEmployeeId != null) {
            boolean isValidEmployee = false;
            for (Employee emp : filterableEmployees) {
                if (emp.getId() == targetEmployeeId) {
                    isValidEmployee = true;
                    break;
                }
            }
            if (!isValidEmployee) {
                request.setAttribute("errorMessage", "Nhân viên được chọn không tồn tại hoặc không thuộc cửa hàng của bạn/không phải nhân viên bán hàng.");
                targetEmployeeId = null; // Reset to null if invalid
            }
        }

        int currentPage = 1;
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        String pageStr = request.getParameter("page");
        String recordsPerPageStr = request.getParameter("recordsPerPage");
        try {
            if (pageStr != null && !pageStr.isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
            }
            if (recordsPerPageStr != null && !recordsPerPageStr.isEmpty()) {
                recordsPerPage = Integer.parseInt(recordsPerPageStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Tham số phân trang không hợp lệ.");
        }

        // Calculate total records for pagination based on filters
        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId,
                cashierShopId, // Filter by Cashier's shopId
                startDate,
                endDate,
                allowedRoleIds // Filter for Staff and Cashier roles
        );
        int noOfPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (currentPage > noOfPages && noOfPages > 0) {
            currentPage = noOfPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        // Get actual sales statistics
        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId,
                cashierShopId, // Filter by Cashier's shopId
                startDate,
                endDate,
                currentPage,
                recordsPerPage,
                allowedRoleIds // Filter for Staff and Cashier roles
        );

        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "Cashier");
        request.setAttribute("statisticTitle", "Thống kê Doanh số (Thu ngân)");

        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("selectedEmployeeId", selectedEmployeeIdParam); // Retain selection on dropdown

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
    }

    private void handleStaffRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        Employee loggedInStaff = (Employee) session.getAttribute("loggedInUser"); // Get Staff from loggedInUser

        if (loggedInStaff == null || loggedInStaff.getId() == 0) {
            request.setAttribute("errorMessage", "Thông tin nhân viên không khả dụng. Vui lòng đăng nhập lại.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

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
            e.printStackTrace();
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        Integer targetEmployeeId = loggedInStaff.getId();
        Integer shopId = loggedInStaff.getShopId();

        List<Integer> allowedRoleIds = new ArrayList<>();
        allowedRoleIds.add(2); // RoleID 2 is Staff

        int currentPage = 1;
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        String pageStr = request.getParameter("page");
        String recordsPerPageStr = request.getParameter("recordsPerPage");
        try {
            if (pageStr != null && !pageStr.isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
            }
            if (recordsPerPageStr != null && !recordsPerPageStr.isEmpty()) {
                recordsPerPage = Integer.parseInt(recordsPerPageStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Tham số phân trang không hợp lệ.");
        }

        int totalRecords = eDAO.getTotalSalesStatisticsCount(
                targetEmployeeId,
                shopId,
                startDate,
                endDate,
                allowedRoleIds
        );
        int noOfPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (currentPage > noOfPages && noOfPages > 0) {
            currentPage = noOfPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        List<SalesEmployeeStatisticDto> salesStatistics = eDAO.getSalesStatistics(
                targetEmployeeId,
                shopId,
                startDate,
                endDate,
                currentPage,
                recordsPerPage,
                allowedRoleIds
        );

        request.setAttribute("salesStatistics", salesStatistics);
        request.setAttribute("userRole", "Staff");
        request.setAttribute("statisticTitle", "Thống kê Doanh số Cá nhân");

        request.setAttribute("startDate", startDateParam);
        request.setAttribute("endDate", endDateParam);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);

        // For Staff, the filterableEmployees list only contains themselves.
        List<Employee> filterableEmployees = new ArrayList<>();
        filterableEmployees.add(loggedInStaff);
        request.setAttribute("filterableEmployees", filterableEmployees);
        request.setAttribute("selectedEmployeeId", String.valueOf(loggedInStaff.getId()));

        request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean isEmployeeInList(int employeeId, List<Employee> employees) {
        if (employees == null) {
            return false;
        }
        for (Employee emp : employees) {
            if (emp.getId() == employeeId) {
                return true;
            }
        }
        return false;
    }

//    private void exportSalesStatisticsToExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        HttpSession session = request.getSession();
//
//        String userRoleName = null;
//        Integer loggedInEmployeeId = null;
//        Integer loggedInShopId = null;
//        String shopOwnerDatabaseName = null;
//        Object loggedInUserObject = session.getAttribute("loggedInUser"); // Get the unified logged-in user object
//
//        ShopOwner loggedInShopOwner = (ShopOwner) session.getAttribute("shopOwner"); // Still get for specific attributes
//        if (loggedInShopOwner != null) {
//            userRoleName = "ShopOwner";
//            shopOwnerDatabaseName = loggedInShopOwner.getDatabaseName();
//        } else if (loggedInUserObject instanceof Employee) { // Check if it's an Employee
//            Employee loggedInEmployee = (Employee) loggedInUserObject; // Cast to Employee
//            loggedInEmployeeId = loggedInEmployee.getId();
//            loggedInShopId = loggedInEmployee.getShopId();
//            if (loggedInEmployee.getRole() != null) {
//                userRoleName = loggedInEmployee.getRole().getName();
//            } else {
//                userRoleName = "Unknown";
//            }
//        }
//
//        if (userRoleName == null || "Unknown".equalsIgnoreCase(userRoleName)) {
//            response.getWriter().println("Bạn chưa đăng nhập hoặc không có quyền.");
//            return;
//        }
//
//        try {
//            String startDateStr = request.getParameter("startDate");
//            String endDateStr = request.getParameter("endDate");
//            String shopIdParam = request.getParameter("shopId");
//            String selectedEmployeeIdParam = request.getParameter("employeeId");
//
//            Date startDate = null;
//            Date endDate = null;
//            Integer filterShopId = null;
//            Integer targetEmployeeId = null;
//
//            try {
//                if (startDateStr != null && !startDateStr.isEmpty()) {
//                    startDate = Date.valueOf(startDateStr);
//                }
//                if (endDateStr != null && !endDateStr.isEmpty()) {
//                    endDate = Date.valueOf(endDateStr);
//                }
//                // Only parse if not "all"
//                if (shopIdParam != null && !shopIdParam.isEmpty() && !"all".equals(shopIdParam)) {
//                    filterShopId = Integer.parseInt(shopIdParam);
//                }
//                if (selectedEmployeeIdParam != null && !selectedEmployeeIdParam.isEmpty() && !"all".equals(selectedEmployeeIdParam)) {
//                    targetEmployeeId = Integer.parseInt(selectedEmployeeIdParam);
//                }
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                // Not setting error message to request because we are writing directly to response
//            }
//
//            // Define allowed roles for export based on user's role
//            List<Integer> rolesForExport = new ArrayList<>();
//
//            if ("Admin".equalsIgnoreCase(userRoleName)) {
//                rolesForExport.add(1); // Admin
//                rolesForExport.add(2); // Staff
//                rolesForExport.add(3); // Shop Owner
//                rolesForExport.add(4); // Cashier
//                // Admin can export for any shop or employee, so filterShopId and targetEmployeeId
//                // come directly from parameters.
//            } else if ("ShopOwner".equalsIgnoreCase(userRoleName) && loggedInShopOwner != null) {
//                Shop shopOfOwner = sDAO.getShopByName(loggedInShopOwner.getShopName(), shopOwnerDatabaseName);
//                if (shopOfOwner != null) {
//                    loggedInShopId = shopOfOwner.getShopID();
//                    // Shop Owner can only export for their shop
//                    filterShopId = loggedInShopId; // Override filterShopId to owner's shop
//                    if (shopIdParam != null && !shopIdParam.isEmpty() && !shopIdParam.equals("all") && !Integer.parseInt(shopIdParam).equals(loggedInShopId)) {
//                        // User tried to export for a different shop, log or error
//                        response.getWriter().println("Bạn không có quyền xuất thống kê của cửa hàng khác.");
//                        return;
//                    }
//                    rolesForExport.add(2); // Staff
//                    rolesForExport.add(4); // Cashier
//                    // rolesForExport.add(3); // ShopOwner (if they track their own sales)
//
//                    // If a specific employee is selected, ensure they are within allowed roles for this shop
//                    if (targetEmployeeId != null) {
//                        List<SalesEmployeeStatisticDto> validEmployees = eDAO.getSalesStatistics(null, filterShopId, null, null, 1, Integer.MAX_VALUE, rolesForExport);
//                        boolean isValidEmployee = false;
//                        for (SalesEmployeeStatisticDto dto : validEmployees) {
//                            if (dto.getEmployeeID().equals(targetEmployeeId)) {
//                                isValidEmployee = true;
//                                break;
//                            }
//                        }
//                        if (!isValidEmployee) {
//                            response.getWriter().println("Nhân viên được chọn không hợp lệ cho quyền của bạn.");
//                            return;
//                        }
//                    }
//                } else {
//                    response.getWriter().println("Không tìm thấy thông tin cửa hàng cho ShopOwner này.");
//                    return;
//                }
//            } else if ("Cashier".equalsIgnoreCase(userRoleName) && loggedInEmployeeId != null && loggedInShopId != null) {
//                // Cashier can only export for their shop
//                filterShopId = loggedInShopId;
//                if (shopIdParam != null && !shopIdParam.isEmpty() && !shopIdParam.equals("all") && !Integer.parseInt(shopIdParam).equals(loggedInShopId)) {
//                    response.getWriter().println("Bạn không có quyền xuất thống kê của cửa hàng khác.");
//                    return;
//                }
//                rolesForExport.add(2); // Staff
//                rolesForExport.add(4); // Cashier (themselves)
//
//                // If a specific employee is selected, ensure they are Staff or the Cashier themselves
//                if (targetEmployeeId != null && !(targetEmployeeId.equals(loggedInEmployeeId) || eDAO.isEmployeeInRole(targetEmployeeId, 2))) {
//                    response.getWriter().println("Nhân viên được chọn không hợp lệ cho quyền của bạn.");
//                    return;
//                }
//            } else if ("Staff".equalsIgnoreCase(userRoleName) && loggedInEmployeeId != null && loggedInShopId != null) {
//                // Staff can only export for themselves
//                targetEmployeeId = loggedInEmployeeId; // Force employeeId to be self
//                filterShopId = loggedInShopId; // Force shopId to be self's shop
//                rolesForExport.add(2); // Only Staff role
//                if (selectedEmployeeIdParam != null && !selectedEmployeeIdParam.isEmpty() && !selectedEmployeeIdParam.equals("all") && !Integer.parseInt(selectedEmployeeIdParam).equals(loggedInEmployeeId)) {
//                    response.getWriter().println("Bạn chỉ có thể xuất thống kê của chính mình.");
//                    return;
//                }
//                if (shopIdParam != null && !shopIdParam.isEmpty() && !shopIdParam.equals("all") && !Integer.parseInt(shopIdParam).equals(loggedInShopId)) {
//                    response.getWriter().println("Bạn chỉ có thể xuất thống kê của cửa hàng mình.");
//                    return;
//                }
//            } else {
//                response.getWriter().println("Bạn không có quyền xuất thống kê.");
//                return;
//            }
//
//            List<SalesEmployeeStatisticDto> statisticsToExport = eDAO.getSalesStatistics(
//                    targetEmployeeId,
//                    filterShopId,
//                    startDate,
//                    endDate,
//                    1, 
//                    Integer.MAX_VALUE, 
//                    rolesForExport
//            );
//
//           
//            response.setContentType("application/vnd.ms-excel");
//            response.setHeader("Content-Disposition", "attachment; filename=SalesStatistics.xlsx");
//
//            response.getWriter().println("Chức năng xuất Excel với dữ liệu đã lọc đang được triển khai. Dữ liệu sẽ xuất: " + statisticsToExport.size() + " bản ghi.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            
//            response.getWriter().println("Lỗi khi xuất Excel: " + e.getMessage());
//        }
//    }
}
