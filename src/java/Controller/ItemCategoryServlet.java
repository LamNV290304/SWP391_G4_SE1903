/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.ItemCategoryDAO;
import Models.ItemCategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author duckh
 */
public class ItemCategoryServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP1");
    ItemCategoryDAO itemDao = new ItemCategoryDAO(connection.getConnection());
   

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ItemCategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ItemCategoryServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
                    listCategories(request, response);
                    break;
                case "edit":
                    showEditCategoryForm(request, response);
                    break;
                default:
                    listCategories(request, response);
                    break;
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("shopItemList.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID đồ dùng không hợp lệ.");
            e.printStackTrace();
            request.getRequestDispatcher("shopItemList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Quan trọng để xử lý tiếng Việt
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Mặc định chuyển hướng về danh sách
        }

        try {
            switch (action) {
                case "add":
                    addCategory(request, response);
                    break;
                case "update":
                    updateCategory(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                default:
                    listCategories(request, response);
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

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<ItemCategory> categories = itemDao.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("categoryList.jsp").forward(request, response);
    }

    private void showEditCategoryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        ItemCategory categoryToEdit = itemDao.getCategoryById(categoryId);
        request.setAttribute("categoryToEdit", categoryToEdit);

        // Vẫn cần gửi danh sách tất cả danh mục để hiển thị bảng
        listCategories(request, response); // Chuyển tiếp đến listCategories, nó sẽ forward tới JSP
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int idToDelete = Integer.parseInt(request.getParameter("id"));
        boolean success = itemDao.deleteCategory(idToDelete);
        if (success) {
            request.getSession().setAttribute("successMessage", "Xóa danh mục thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Xóa danh mục thất bại. Có thể có đồ dùng đang sử dụng danh mục này.");
        }
        response.sendRedirect("ItemCategoryServlet?action=list");
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");

        ItemCategory newCategory = new ItemCategory();
        newCategory.setCategoryName(categoryName);
        newCategory.setDescription(description);

        int newId = itemDao.addCategory(newCategory);
        if (newId != -1) {
            request.getSession().setAttribute("successMessage", "Thêm danh mục thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Thêm danh mục thất bại.");
        }
        response.sendRedirect("ItemCategoryServlet?action=list");
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");

        ItemCategory categoryToUpdate = new ItemCategory();
        categoryToUpdate.setCategoryId(categoryId);
        categoryToUpdate.setCategoryName(categoryName);
        categoryToUpdate.setDescription(description);

        boolean success = itemDao.updateCategory(categoryToUpdate);
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật danh mục thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Cập nhật danh mục thất bại.");
        }
        response.sendRedirect("ItemCategoryServlet?action=list");
    }
}
