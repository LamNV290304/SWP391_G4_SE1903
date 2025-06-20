package Controller;

import Context.DBContext;
import Dal.ProductDAO;
import Models.Category;
import Models.Product;
import Models.Unit;
import Utils.CloudinaryUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/products")
@MultipartConfig

public class ProductServlet extends HttpServlet {

    DBContext connection = new DBContext("SWP_NEW");
    ProductDAO productDAO = new ProductDAO(connection.getConnection());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            listProducts(request, response);
        } else {
            switch (action) {
                case "list":
                    listProducts(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createProduct(request, response);
        } else if ("update".equals(action)) {
            updateProduct(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 10;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        String search = request.getParameter("search");
        String categoryFilter = request.getParameter("category");

        List<Product> products = productDAO.getAllProducts(page, pageSize, search, categoryFilter);
        int totalProducts = productDAO.getTotalProducts(search, categoryFilter);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Category> categories = productDAO.getAllCategories();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.setAttribute("categoryFilter", categoryFilter);

        request.getRequestDispatcher("product-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories = productDAO.getAllCategories();
        List<Unit> units = productDAO.getAllUnits();

        request.setAttribute("categories", categories);
        request.setAttribute("units", units);

        request.getRequestDispatcher("product-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productIdParam = request.getParameter("id");
        try {
            int productId = Integer.parseInt(productIdParam); 
            Product product = productDAO.getProductById(productId);

            if (product == null) {
                response.sendRedirect("products?error=Product not found");
                return;
            }

            List<Category> categories = productDAO.getAllCategories();
            List<Unit> units = productDAO.getAllUnits();

            request.setAttribute("product", product);
            request.setAttribute("categories", categories);
            request.setAttribute("units", units);

            request.getRequestDispatcher("product-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("products?error=Invalid product ID");
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            
            String productName = request.getParameter("productName");
            String categoryId = request.getParameter("categoryId");
            String unitId = request.getParameter("unitId");
            String importPriceStr = request.getParameter("importPrice");
            String sellingPriceStr = request.getParameter("sellingPrice");
            String description = request.getParameter("description");
            boolean status = "on".equals(request.getParameter("status"));
            String createdBy = "admin";

            StringBuilder errors = new StringBuilder();

            if (productName == null || productName.trim().isEmpty()) {
                errors.append("Product Name is required. ");
            }

            BigDecimal importPrice = null;
            BigDecimal sellingPrice = null;

            try {
                importPrice = new BigDecimal(importPriceStr);
                if (importPrice.compareTo(BigDecimal.ZERO) < 0) {
                    errors.append("Import Price must be positive. ");
                }
            } catch (NumberFormatException | NullPointerException e) {
                errors.append("Import Price is required and must be a valid number. ");
            }

            try {
                sellingPrice = new BigDecimal(sellingPriceStr);
                if (sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
                    errors.append("Selling Price must be positive. ");
                }
            } catch (NumberFormatException | NullPointerException e) {
                errors.append("Selling Price is required and must be a valid number. ");
            }

            if (importPrice != null && sellingPrice != null
                    && sellingPrice.compareTo(importPrice) < 0) {
                errors.append("Selling Price should not be less than Import Price. ");
            }

            if (errors.length() > 0) {
                request.setAttribute("error", errors.toString());
                showNewForm(request, response);
                return;
            }

            Part imagePart = request.getPart("image");
            String imageUrl = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                imageUrl = CloudinaryUtil.uploadImage(imagePart);
            }

            Product product = new Product();
            
            product.setProductName(productName);
            product.setCategoryID(categoryId);
            product.setUnitID(unitId);
            product.setImportPrice(importPrice);
            product.setSellingPrice(sellingPrice);
            product.setDescription(description);
            product.setStatus(status);
            product.setImageUrl(imageUrl);
            product.setCreatedBy(createdBy);

            if (productDAO.createProduct(product)) {
                response.sendRedirect("products?success=Product created successfully");
            } else {
                request.setAttribute("error", "Failed to create product");
                showNewForm(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            showNewForm(request, response);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String productIdParam = request.getParameter("productId");
            int productId = Integer.parseInt(productIdParam); 

            String productName = request.getParameter("productName");
            String categoryId = request.getParameter("categoryId");
            String unitId = request.getParameter("unitId");
            String importPriceStr = request.getParameter("importPrice");
            String sellingPriceStr = request.getParameter("sellingPrice");
            String description = request.getParameter("description");
            boolean status = "on".equals(request.getParameter("status"));

            Product existingProduct = productDAO.getProductById(productId);
            if (existingProduct == null) {
                response.sendRedirect("products?error=Product not found");
                return;
            }

            StringBuilder errors = new StringBuilder();

            if (productName == null || productName.trim().isEmpty()) {
                errors.append("Product Name is required. ");
            }

            BigDecimal importPrice = null;
            BigDecimal sellingPrice = null;

            try {
                importPrice = new BigDecimal(importPriceStr);
                if (importPrice.compareTo(BigDecimal.ZERO) < 0) {
                    errors.append("Import Price must be positive. ");
                }
            } catch (NumberFormatException | NullPointerException e) {
                errors.append("Import Price is required and must be a valid number. ");
            }

            try {
                sellingPrice = new BigDecimal(sellingPriceStr);
                if (sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
                    errors.append("Selling Price must be positive. ");
                }
            } catch (NumberFormatException | NullPointerException e) {
                errors.append("Selling Price is required and must be a valid number. ");
            }

            if (importPrice != null && sellingPrice != null
                    && sellingPrice.compareTo(importPrice) < 0) {
                errors.append("Selling Price should not be less than Import Price. ");
            }

            if (errors.length() > 0) {
                request.setAttribute("error", errors.toString());
                request.setAttribute("product", existingProduct);
                showEditForm(request, response);
                return;
            }

            Part imagePart = request.getPart("image");
            String imageUrl = existingProduct.getImageUrl();

            if (imagePart != null && imagePart.getSize() > 0) {
                if (existingProduct.getImageUrl() != null) {
                    CloudinaryUtil.deleteImage(existingProduct.getImageUrl());
                }

                imageUrl = CloudinaryUtil.uploadImage(imagePart);
            }

            Product product = new Product();
            product.setProductID(productId);
            product.setProductName(productName);
            product.setCategoryID(categoryId);
            product.setUnitID(unitId);
            product.setImportPrice(importPrice);
            product.setSellingPrice(sellingPrice);
            product.setDescription(description);
            product.setStatus(status);
            product.setImageUrl(imageUrl);

            if (productDAO.updateProduct(product)) {
                response.sendRedirect("products?success=Product updated successfully");
            } else {
                request.setAttribute("error", "Failed to update product");
                showEditForm(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("products?error=Invalid product ID");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productIdParam = request.getParameter("id");
        try {
            int productId = Integer.parseInt(productIdParam); 
            Product product = productDAO.getProductById(productId);

            if (product != null) {
                if (product.getImageUrl() != null) {
                    CloudinaryUtil.deleteImage(product.getImageUrl());
                }

                if (productDAO.deleteProduct(productId)) {
                    response.sendRedirect("products?success=Product deleted successfully");
                } else {
                    response.sendRedirect("products?error=Failed to delete product");
                }
            } else {
                response.sendRedirect("products?error=Product not found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("products?error=Invalid product ID");
        }
    }
}
