/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.CategoryItemCountDto;
import DTO.ShopTotalValueDto;
import Dal.ItemCategoryDAO;
import Dal.ShopDAO;
import Dal.ShopItemDAO;
import Models.Shop;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author duckh
 */
public class ReportItemServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    ShopItemDAO shopItemDAO = new ShopItemDAO(connection.getConnection());
    ItemCategoryDAO itemDao = new ItemCategoryDAO(connection.getConnection());
    ShopDAO sDAO = new ShopDAO(connection.getConnection());

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
        String shopIdParam = request.getParameter("shopId"); 
        Integer shopIdFilter = null; 
        String shopNameFilter = null;

        BigDecimal calculatedTotalValue; 
        if (shopIdParam != null && !shopIdParam.isEmpty()) {
            try {
                shopIdFilter = Integer.parseInt(shopIdParam);
            
                calculatedTotalValue = shopItemDAO.getTotalValueByShopId(shopIdFilter);
           
                Shop selectedShop = sDAO.getShopById(shopIdFilter); 
                if (selectedShop != null) {
                    shopNameFilter = selectedShop.getShopName();
                }
            } catch (NumberFormatException e) {
                getServletContext().log("Lỗi định dạng ID cửa hàng: " + shopIdParam, e);
                request.getSession().setAttribute("errorMessage", "ID cửa hàng không hợp lệ.");
                request.getRequestDispatcher("shopItemReports.jsp").forward(request, response);
                return;
            }
        } else {
        
            calculatedTotalValue = shopItemDAO.getTotalValueAllShops();
       
            shopNameFilter = null;
        }
        request.setAttribute("selectedShopTotalValue", calculatedTotalValue);


        if (categoryNameFilter != null && categoryNameFilter.trim().isEmpty()) {
            categoryNameFilter = null;
        }

        try {
            List<CategoryItemCountDto> categoryCounts = itemDao.getCategoryItemCounts(categoryNameFilter, shopNameFilter);
            request.setAttribute("categoryCounts", categoryCounts);

            List<Shop> allShops = sDAO.getAllShops();
            request.setAttribute("allShops", allShops);

            List<Models.ItemCategory> allCategories = itemDao.getAllCategories();
            request.setAttribute("allCategories", allCategories);

            List<ShopTotalValueDto> shopTotalValues = shopItemDAO.getShopTotalValues(categoryNameFilter, shopNameFilter);
            request.setAttribute("shopTotalValues", shopTotalValues);

            request.setAttribute("selectedCategoryName", categoryNameFilter);
            request.setAttribute("selectedShopId", shopIdFilter);

        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi khi tải báo cáo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
        }

        request.getRequestDispatcher("shopItemReports.jsp").forward(request, response);
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
