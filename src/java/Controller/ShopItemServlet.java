/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.ItemCategoryDAO;
import Dal.ShopDAO;
import Dal.ShopItemDAO;
import Dal.UnitDAO;
import Models.ItemCategory;
import Models.Shop;
import Models.ShopItem;
import Models.Unit;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duckh
 */
public class ShopItemServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    ItemCategoryDAO itemDAO = new ItemCategoryDAO(connection.getConnection());
    ShopItemDAO shopItemDAO = new ShopItemDAO(connection.getConnection());
    ShopDAO sDAO = new ShopDAO();
    UnitDAO uDAO = new UnitDAO(connection.getConnection());

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
                case "list":
                    listShopItems(request, response);
                    break;
                case "add":
                    showAddShopItemForm(request, response);
                    break;
                case "edit":
                    showEditShopItemForm(request, response);
                    break;
                case "searchByDate":
                    listShopItems(request, response);
                    break;
                case "search":
                    listShopItems(request, response);
                    break;
                default:
                    listShopItems(request, response);
                    break;
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list"); // Redirect để tránh lỗi khi refresh trang
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID đồ dùng không hợp lệ.");
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list"); // Redirect để tránh lỗi khi refresh trang
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    addShopItem(request, response);
                    break;
                case "update":
                    updateShopItem(request, response);
                    break;
                case "delete":
                    deleteShopItem(request, response);
                    break;
                default:
                    listShopItems(request, response);
                    break;
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list");
        } catch (NumberFormatException | DateTimeParseException e) {
            request.getSession().setAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ. Vui lòng kiểm tra các trường số và ngày tháng.");
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void listShopItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String action = request.getParameter("action");
        // Nếu action rỗng hoặc không có, đặt mặc định là "list"
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String searchQuery = request.getParameter("searchQuery"); // Tên tham số từ JSP của bạn

        int page = 1;
        int pageSize = 5; // Đặt mặc định kích thước trang

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Xử lý lỗi hoặc log nếu cần
            }
        }

        List<ShopItem> items;
        int totalItems;
        LocalDate startDate = null;
        LocalDate endDate = null;

        try {
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }

            // --- Logic tìm kiếm và phân trang ---
            if ("searchByDate".equals(action) && (startDate != null || endDate != null)) {
                items = shopItemDAO.getShopItemsByDateRange(startDate, endDate, page, pageSize);
                totalItems = shopItemDAO.countShopItemsByDateRange(startDate, endDate);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("action", "searchByDate"); // GIỮ ACTION HIỆN TẠI
            } else if ("search".equals(action) && searchQuery != null && !searchQuery.trim().isEmpty()) {
                items = shopItemDAO.searchShopItemsByKeyWithPagination(searchQuery, page, pageSize);
                totalItems = shopItemDAO.countShopItemsByName(searchQuery);
                request.setAttribute("searchQuery", searchQuery);
                request.setAttribute("action", "search"); // GIỮ ACTION HIỆN TẠI
            } else {
                // Mặc định hoặc khi không có tham số tìm kiếm cụ thể
                items = shopItemDAO.getShopItemsByPage(page, pageSize);
                totalItems = shopItemDAO.getTotalShopItemCount();
                request.setAttribute("action", "list"); // ACTION MẶC ĐỊNH
            }

            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            if (totalPages == 0) {
                totalPages = 1;
            }

            // Đặt các thuộc tính vào request để JSP có thể truy cập
            request.setAttribute("shopItems", items);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalItems", totalItems);
            request.setAttribute("totalPages", totalPages);
            // Các thuộc tính tìm kiếm đã được đặt ở trên trong từng nhánh if/else if

            request.getRequestDispatcher("shopItemList.jsp").forward(request, response);

        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi lấy danh sách đồ dùng: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list");
        } catch (DateTimeParseException e) {
            request.getSession().setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng sử dụng YYYY-MM-DD.");
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list");
        }
    }

    private void showAddShopItemForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try {
            // Đặt thuộc tính rỗng để JSP hiển thị form trống
            request.setAttribute("itemToEdit", new ShopItem());

            // Vẫn cần danh sách danh mục và cửa hàng cho dropdown
            List<ItemCategory> categories = itemDAO.getAllCategories();
            request.setAttribute("categories", categories);
            List<Shop> shops = sDAO.getAllShops("SWP1"); // Kiểm tra lại cách ShopDAO lấy connection
            request.setAttribute("shops", shops);
            List<Unit> units = uDAO.getAllUnits();
            request.setAttribute("units", units);
            request.getRequestDispatcher("shopItemAdd.jsp").forward(request, response);
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi hiển thị form thêm đồ dùng: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list"); // Redirect để tránh lỗi khi refresh
        }
    }

    private void showEditShopItemForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try {
            int itemId = Integer.parseInt(request.getParameter("id"));
            ShopItem itemToEdit = shopItemDAO.getItemById(itemId);
            request.setAttribute("itemToEdit", itemToEdit);

            List<ItemCategory> categories = itemDAO.getAllCategories();
            request.setAttribute("categories", categories);
            List<Shop> shops = sDAO.getAllShops("SWP1");
            request.setAttribute("shops", shops);
            List<Unit> units = uDAO.getAllUnits();
            request.setAttribute("units", units);

            request.getRequestDispatcher("shopItemEdit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID đồ dùng không hợp lệ khi chỉnh sửa.");
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list");
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi cơ sở dữ liệu khi hiển thị form chỉnh sửa đồ dùng: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ShopItemServlet?action=list");
        }
    }

    private void deleteShopItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int idToDelete = Integer.parseInt(request.getParameter("id"));
        boolean success = shopItemDAO.deleteItem(idToDelete);
        if (success) {
            request.getSession().setAttribute("successMessage", "Xóa đồ dùng thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Xóa đồ dùng thất bại.");
        }
        response.sendRedirect("ShopItemServlet?action=list");
    }

    private void addShopItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Lấy dữ liệu từ form
        String itemName = request.getParameter("itemName");
        String description = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        request.setAttribute("units", uDAO.getAllUnits());
        BigDecimal price = null;
        String priceStr = request.getParameter("price");
        if (priceStr != null && !priceStr.trim().isEmpty()) {
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorMessage", "Giá trị 'Giá' không hợp lệ. Vui lòng nhập số.");
                response.sendRedirect("ShopItemServlet?action=add");
                return;
            }
        }

        Integer shopId = null;
        String shopIdStr = request.getParameter("shopId");
        if (shopIdStr != null && !shopIdStr.trim().isEmpty()) {
            shopId = Integer.parseInt(shopIdStr);
        }

        String notes = request.getParameter("notes");

        ShopItem newItem = new ShopItem();
        newItem.setItemName(itemName);
        newItem.setDescription(description);
        newItem.setCategoryId(categoryId);
        newItem.setQuantity(quantity);
        newItem.setUnitId(unitId);
        newItem.setPrice(price);
        newItem.setItemDate(Timestamp.from(Instant.now()));
        newItem.setShopId(shopId);
        newItem.setNotes(notes);

        int newId = shopItemDAO.addItem(newItem);
        if (newId != -1) {
            request.getSession().setAttribute("successMessage", "Thêm đồ dùng thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Thêm đồ dùng thất bại.");
        }
        response.sendRedirect("ShopItemServlet?action=list");
    }

    private void updateShopItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        String itemName = request.getParameter("itemName");
        String description = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        BigDecimal price = null;
        String priceStr = request.getParameter("price");
        if (priceStr != null && !priceStr.isEmpty()) {
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorMessage", "Giá trị 'Giá' không hợp lệ. Vui lòng nhập số.");
                response.sendRedirect("ShopItemServlet?action=edit&id=" + itemId);
                return;
            }
        }

        Integer shopId = null;
        String shopIdStr = request.getParameter("shopId");
        if (shopIdStr != null && !shopIdStr.isEmpty()) {
            shopId = Integer.parseInt(shopIdStr);
        }

        String notes = request.getParameter("notes");

        ShopItem itemToUpdate = new ShopItem();
        itemToUpdate.setItemId(itemId);
        itemToUpdate.setItemName(itemName);
        itemToUpdate.setDescription(description);
        itemToUpdate.setCategoryId(categoryId);
        itemToUpdate.setQuantity(quantity);
        itemToUpdate.setPrice(price);
        itemToUpdate.setUnitId(unitId);
        itemToUpdate.setItemDate(Timestamp.from(Instant.now()));
        itemToUpdate.setShopId(shopId);
        itemToUpdate.setNotes(notes);

        request.setAttribute("units", uDAO.getAllUnits());

        boolean success = shopItemDAO.updateItem(itemToUpdate);
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật đồ dùng thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Cập nhật đồ dùng thất bại.");
        }
        response.sendRedirect("ShopItemServlet?action=list");
    }

}
