/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DTO.SalesEmployeeStatisticDto;
import Context.DBContext;
import Dal.EmployeeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author duckh
 */
public class StatisticServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    EmployeeDAO eDAO = new EmployeeDAO(connection.getConnection());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<SalesEmployeeStatisticDto> salesStatistics;
        String statisticTitle = "Thống kê Doanh số của Nhân viên Bán hàng";
        String statisticType = request.getParameter("type");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        Date startDate = null;
        Date endDate = null;
        try {
            if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
                startDate = Date.valueOf(startDateStr);
                endDate = Date.valueOf(endDateStr);
                salesStatistics = eDAO.getSalesStatisticsForSalesEmployeesByDateRange(startDate, endDate);
                statisticTitle = "Thống kê Doanh số từ " + startDateStr + " đến " + endDateStr;
            } else {
                salesStatistics = eDAO.getSalesStatisticsForSalesEmployees();
                statisticTitle="Thống kê Doanh số của nhân viên Bán hàng";
            }
             request.setAttribute("statisticTitle", statisticTitle);
            request.setAttribute("salesStatistics", salesStatistics);

            request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);

        } catch (Exception ex) {

            request.setAttribute("errorMessage", "Lỗi khi lấy dữ liệu thống kê: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
