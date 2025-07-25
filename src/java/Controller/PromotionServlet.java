/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Models.Promotion;
import Context.DBContext;
import Dal.PromotionDAO;
import Models.Category;
import java.sql.Date;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author duckh
 */
public class PromotionServlet extends HttpServlet {

  
    private PromotionDAO promotionDAO;

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
        promotionDAO = new PromotionDAO(connection.getConnection());
        return true;
    }

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
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deletePromotion(request, response);
                    break;
                default:
                    listPromotions(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertPromotion(request, response);
                    break;
                case "update":
                    updatePromotion(request, response);
                    break;
                default:
                    listPromotions(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listPromotions(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int pageIndex = 1;
        int pageSize = 3;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                pageIndex = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }

        String searchKeyword = request.getParameter("searchKeyword");
        if (searchKeyword == null) {
            searchKeyword = "";
        }

        String monthParam = request.getParameter("month");
        Integer month = null;
        if (monthParam != null && !monthParam.isEmpty()) {
            try {
                month = Integer.parseInt(monthParam);
            } catch (NumberFormatException e) {
                month = null;
            }
        }

        List<Promotion> promotions = promotionDAO.getFilteredPromotionsByPage(month, searchKeyword, pageIndex, pageSize);
        int totalPromotions = promotionDAO.countFilteredPromotions(month, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalPromotions / pageSize);

        request.setAttribute("promotions", promotions);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("selectedMonth", month);

        request.getRequestDispatcher("promotion_list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Category> categoryList = promotionDAO.getAllCategories();
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("promotion_form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Promotion promo = promotionDAO.getPromotionById(id);
        List<Category> categoryList = promotionDAO.getAllCategories();

        request.setAttribute("promotion", promo);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/promotion_form.jsp").forward(request, response);
    }

    private void insertPromotion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("promotionName");
        Date startDate = Date.valueOf(request.getParameter("startDate"));
        Date endDate = Date.valueOf(request.getParameter("endDate"));
        boolean status = Boolean.parseBoolean(request.getParameter("status"));
        double discountRate = Double.parseDouble(request.getParameter("discountRate"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        Promotion promo = new Promotion(0, name, startDate, endDate, status);
        promo.setDiscountRate(discountRate);
        promo.setCategoryId(categoryId);

        promotionDAO.createPromotion(promo);
        response.sendRedirect("PromotionServlet?action=list");
    }

    private void updatePromotion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("promotionName");
        Date startDate = Date.valueOf(request.getParameter("startDate"));
        Date endDate = Date.valueOf(request.getParameter("endDate"));
        boolean status = "1".equals(request.getParameter("status"));

        double discountRate = Double.parseDouble(request.getParameter("discountRate"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        Promotion promo = new Promotion(id, name, startDate, endDate, status);

        promo.setDiscountRate(discountRate);
        promo.setCategoryId(categoryId);

        promotionDAO.updatePromotion(promo);

        response.sendRedirect("PromotionServlet?action=list");
    }

    private void deletePromotion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        promotionDAO.deletePromotion(id);
        response.sendRedirect("PromotionServlet?action=list");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
