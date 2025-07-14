/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.CategoryItemCountDto;
import DTO.ShopTotalValueDto;
import Dal.ItemCategoryDAO;
import Dal.ShopItemDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author duckh
 */
public class ReportItemServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    private ShopItemDAO shopItemDAO = new ShopItemDAO(connection.getConnection());
    ItemCategoryDAO itemDao = new ItemCategoryDAO(connection.getConnection());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "show";
        }
        try {
            switch (action) {
                case "show":
                    showReports(request, response);
                    break;
                default:
                    showReports(request, response);
                    break;
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi cơ sở dữ liệu khi tải báo cáo: " + e.getMessage());
            getServletContext().log("Lỗi DB trong ReportServlet", e);
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/errorPage.jsp");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            getServletContext().log("Lỗi chung trong ReportServlet", e);
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    private void showReports(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String categoryNameFilter = request.getParameter("categoryName");
        String shopNameFilter = request.getParameter("shopName");

        // Đảm bảo rằng các chuỗi rỗng được chuyển thành null nếu bạn muốn
        // để DAO không lọc khi không có giá trị
        if (categoryNameFilter != null && categoryNameFilter.trim().isEmpty()) {
            categoryNameFilter = null;
        }
        if (shopNameFilter != null && shopNameFilter.trim().isEmpty()) {
            shopNameFilter = null;
        }

        try {
            // 2. Gọi các phương thức DAO với các tham số tìm kiếm
            List<CategoryItemCountDto> categoryCounts = itemDao.getCategoryItemCounts(categoryNameFilter, shopNameFilter);
            request.setAttribute("categoryCounts", categoryCounts);

            List<ShopTotalValueDto> shopTotalValues = shopItemDAO.getShopTotalValues(categoryNameFilter, shopNameFilter);
            request.setAttribute("shopTotalValues", shopTotalValues);

        } catch (SQLException e) {
            // Xử lý lỗi SQLException
            e.printStackTrace(); // Log lỗi cho mục đích debug
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi khi tải báo cáo: " + e.getMessage());
        } catch (Exception e) {
            // Xử lý các loại lỗi khác có thể xảy ra (ví dụ:ClassNotFoundException nếu có trong DAO)
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
        }

        // 3. Chuyển tiếp request đến JSP
        request.getRequestDispatcher("shopItemReports.jsp").forward(request, response); // Đảm bảo tên JSP là chính xác
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
