/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DTO.SalesEmployeeStatisticDto;
import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ShopDAO;
import Models.Shop;
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
    ShopDAO sDAO = new ShopDAO();
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
        Integer selectedShopId = null;
        Date startDate = null;
        Date endDate = null;
        try {
            // --- 1. Lấy danh sách các cửa hàng và đặt vào request ---
            List<Shop> shops = sDAO.getAllShops("SWP1");
            request.setAttribute("shops", shops);

            // --- 2. Đọc tham số shopId từ request ---
            String shopIdParam = request.getParameter("shopId");
            if (shopIdParam != null && !shopIdParam.isEmpty()) {
                try {
                    selectedShopId = Integer.parseInt(shopIdParam);
                    request.setAttribute("selectedShopId", selectedShopId); // Giữ lại lựa chọn trên dropdown
                } catch (NumberFormatException e) {
                   
                    request.setAttribute("errorMessage", "ID cửa hàng không hợp lệ.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return; // Dừng xử lý nếu shopId không hợp lệ
                }
            }

            // Lưu trữ ngày tháng về lại request để hiển thị trên input type="date"
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = Date.valueOf(startDateStr);
                request.setAttribute("startDate", startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = Date.valueOf(endDateStr);
                request.setAttribute("endDate", endDateStr);
            }

            // --- 4. Gọi DAO để lấy dữ liệu thống kê, truyền cả shopId ---
            if (startDate != null && endDate != null) {
                // Có khoảng ngày, gọi phương thức có tham số ngày và shopId
                salesStatistics = eDAO.getSalesStatisticsForSalesEmployeesByDateRange(startDate, endDate, selectedShopId);
                statisticTitle = "Thống kê Doanh số từ " + startDateStr + " đến " + endDateStr;
            } else {
                // Không có khoảng ngày, gọi phương thức chỉ có shopId (tổng cộng)
                salesStatistics = eDAO.getSalesStatisticsForSalesEmployees(selectedShopId);
                statisticTitle = "Thống kê Doanh số của Nhân viên Bán hàng (Tổng cộng)";
            }

            // --- 5. Thêm tên cửa hàng vào tiêu đề thống kê nếu có lọc ---
            if (selectedShopId != null) {
                Shop shopName = sDAO.getShopByID(selectedShopId,"SWP1");
                statisticTitle += " (Cửa hàng: " + shopName.getShopName() + ")";
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
