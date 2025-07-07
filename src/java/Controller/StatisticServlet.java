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
        response.setContentType("text/html;charset=UTF-8");

        List<SalesEmployeeStatisticDto> salesStatistics;
        String statisticTitle; // Khai báo nhưng không gán giá trị mặc định ở đây, sẽ được gán trong khối try
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        Date startDate = null;
        Date endDate = null;
        try {
            // Kiểm tra xem EmployeeDAO đã được khởi tạo thành công và kết nối có mở không
            if (eDAO == null) {
                // Log lỗi và ném ngoại lệ nếu kết nối database không sẵn sàng

            }

            if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
                startDate = Date.valueOf(startDateStr);
                endDate = Date.valueOf(endDateStr);
                salesStatistics = eDAO.getSalesStatisticsForSalesEmployeesByDateRange(startDate, endDate);
                statisticTitle = "Thống kê Doanh số từ " + startDateStr + " đến " + endDateStr;
            } else {
                // Lấy thống kê tổng cộng nếu không có khoảng thời gian
                salesStatistics = eDAO.getSalesStatisticsForSalesEmployees();
                statisticTitle = "Thống kê Doanh số của Nhân viên Bán hàng (Tổng cộng)";
            }

            request.setAttribute("statisticTitle", statisticTitle);
            request.setAttribute("salesStatistics", salesStatistics);

            // Chuyển tiếp yêu cầu đến trang JSP để hiển thị
            request.getRequestDispatcher("sale_statistics.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // Bắt lỗi khi chuyển đổi chuỗi ngày tháng không hợp lệ thành đối tượng Date

            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng nhập đúng định dạng NĂM-THÁNG-NGÀY (ví dụ: 2024-01-31).");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception ex) {
            // Bắt lỗi SQL từ DAO, ví dụ: lỗi truy vấn database
            request.setAttribute("errorMessage", "Đã xảy ra lỗi cơ sở dữ liệu khi lấy dữ liệu thống kê: " + ex.getMessage());
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
