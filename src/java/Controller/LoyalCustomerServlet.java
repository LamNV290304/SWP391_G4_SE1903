/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.LoyalCustomerDto;
import Dal.CustomerStatisticDAO;
import Models.Employee;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.time.YearMonth;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duckh
 */
public class LoyalCustomerServlet extends HttpServlet {

    private CustomerStatisticDAO csDAO;

    private boolean initDAO(HttpServletRequest request, HttpServletResponse response) {
        String dbName = (String) request.getSession().getAttribute("databaseName");
        if (dbName == null) {
            try {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        DBContext db = new DBContext(dbName);
        csDAO = new CustomerStatisticDAO(db.getConnection());
        return true;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoyalCustomerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoyalCustomerServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!initDAO(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "view";
        }

        try {
            switch (action) {
                case "search":
                    handleSearch(request, response);
                    break;

                case "view":

                default:
                    handleView(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        Employee loggedInManager = (Employee) session.getAttribute("Employee");

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(1);

        int shopId = loggedInManager.getShopId();

        List<LoyalCustomerDto> topCustomers = csDAO.getTopCustomers(
                Date.valueOf(start), Date.valueOf(end), shopId, 5);

        request.setAttribute("topCustomers", topCustomers);
        request.setAttribute("startDate", start.toString());
        request.setAttribute("endDate", end.toString());

        request.getRequestDispatcher("loyal_customer_statistics.jsp").forward(request, response);
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        Employee loggedInManager = (Employee) session.getAttribute("Employee");
        int shopId = loggedInManager.getShopId();

        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String searchKeyword = request.getParameter("searchKeyword");
        String selectedMonthParam = request.getParameter("selectedMonth");

        if (searchKeyword == null) {
            searchKeyword = "";
        }

        // Xử lý phân trang
        String pageParam = request.getParameter("page");
        int page = 1;
        try {
            page = Integer.parseInt(pageParam);
            if (page < 1) {
                page = 1;
            }
        } catch (NumberFormatException e) {
            page = 1;
        }
        int pageSize = 5;
        int offset = (page - 1) * pageSize;


        Date startDate = (startDateParam != null && !startDateParam.isEmpty())
                ? Date.valueOf(startDateParam)
                : Date.valueOf(LocalDate.now().minusMonths(1));

        Date endDate = (endDateParam != null && !endDateParam.isEmpty())
                ? Date.valueOf(endDateParam)
                : Date.valueOf(LocalDate.now());

        if (selectedMonthParam != null && !selectedMonthParam.isEmpty()) {
            try {
                int selectedMonth = Integer.parseInt(selectedMonthParam);
                YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), selectedMonth);
                startDate = Date.valueOf(yearMonth.atDay(1));
                endDate = Date.valueOf(yearMonth.atEndOfMonth());
            } catch (NumberFormatException | DateTimeException e) {
                
            }
        }

  
        List<LoyalCustomerDto> customers = csDAO.searchLoyalCustomers(
                searchKeyword, startDate, endDate, shopId, offset, pageSize
        );

        int totalRecords = csDAO.countLoyalCustomers(
                searchKeyword, startDate, endDate, shopId
        );
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("topCustomers", customers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startDate", startDate.toString());
        request.setAttribute("endDate", endDate.toString());
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("selectedMonth", selectedMonthParam); 

        request.getRequestDispatcher("loyal_customer_statistics.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
