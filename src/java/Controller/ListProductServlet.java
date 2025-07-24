/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Context.DBContext;
import Dal.ProductDAO;
import Dal.UnitDAO;
import Models.Category;
import Models.Employee;
import Models.Product;
import Models.Unit;
import Models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/**
 *
 * @author Thai Anh
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 1024 * 1024 * 5,   // 5MB
    maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class ListProductServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ListProductServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListProductServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
          
        // Nhận tham số lọc từ request
        String search = request.getParameter("search");
        String categoryID = request.getParameter("categoryID");
        String statusParam = request.getParameter("status");
        String minImportPriceStr = request.getParameter("minImportPrice");
        String maxImportPriceStr = request.getParameter("maxImportPrice");
        String minSellingPriceStr = request.getParameter("minSellingPrice");
        String maxSellingPriceStr = request.getParameter("maxSellingPrice");

        Boolean status = null;
        if (statusParam != null && !statusParam.isEmpty()) {
            status = Boolean.parseBoolean(statusParam);
        }

        BigDecimal minImportPrice = parseBigDecimal(minImportPriceStr);
        BigDecimal maxImportPrice = parseBigDecimal(maxImportPriceStr);
        BigDecimal minSellingPrice = parseBigDecimal(minSellingPriceStr);
        BigDecimal maxSellingPrice = parseBigDecimal(maxSellingPriceStr);

        // Kết nối DB và gọi DAO
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        
        DBContext db = new DBContext(databaseName);
        
        ProductDAO productDAO = new ProductDAO(db.getConnection());
        UnitDAO unitDAO = new UnitDAO(db.getConnection());
        if (databaseName == null) {
    throw new ServletException("❌ Không tìm thấy tên database trong session!");
}
        List<Product> productList = productDAO.getAllProductsFiltered(
            search, categoryID, status,
            minImportPrice, maxImportPrice,
            minSellingPrice, maxSellingPrice
        );

        List<Category> categoryList = productDAO.getAllCategories();
        List<Unit> unitList = unitDAO.getAllActiveUnits();
        // Gửi dữ liệu về JSP
        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("unitList", unitList);

        // Forward
        request.getRequestDispatcher("listProduct.jsp").forward(request, response);
    } 
 private BigDecimal parseBigDecimal(String val) {
        try {
            if (val != null && !val.trim().isEmpty()) {
                return new BigDecimal(val.trim());
            }
        } catch (NumberFormatException e) {
            // Có thể log lỗi nếu cần
        }
        return null;
    }
    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
                Employee emp =(Employee) request.getSession().getAttribute("Employee");
        String action = request.getParameter("action");
        if (action == null || action.equals("null")) {
    System.out.println("⚠️ Không nhận được action hoặc action = null string!");
    response.sendRedirect("ListProductServlet");
    return;
}
Date utilDate = new Date();
LocalDateTime localDateTime = utilDate.toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime();
        if ("update".equals(action)) {
    int productID = Integer.parseInt(request.getParameter("productID"));
    String productName = request.getParameter("productName");
    String categoryID = request.getParameter("categoryID");
    BigDecimal importPrice = new BigDecimal(request.getParameter("importPrice"));
    BigDecimal sellingPrice = new BigDecimal(request.getParameter("sellingPrice"));
    String description = request.getParameter("description");
    boolean status = Boolean.parseBoolean(request.getParameter("status"));
    String unitID = request.getParameter("unitID");

    // Khởi tạo DAO
    String databaseName = (String) request.getSession().getAttribute("databaseName");
    DBContext dBContext = new DBContext(databaseName);
    ProductDAO productDAO = new ProductDAO(dBContext.getConnection());

    // Lấy sản phẩm cũ để giữ lại ảnh nếu không chọn ảnh mới
    Product oldProduct = productDAO.getProductById(productID);

    // Xử lý ảnh mới
    String imageFileName = oldProduct.getImageUrl(); // default là ảnh cũ
    try {
        jakarta.servlet.http.Part imagePart = request.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0) {
            String submittedFileName = imagePart.getSubmittedFileName();
            imageFileName = java.nio.file.Paths.get(submittedFileName).getFileName().toString();

            String uploadPath = getServletContext().getRealPath("/") + "images";
            java.io.File uploadDir = new java.io.File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            imagePart.write(uploadPath + java.io.File.separator + imageFileName);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Gán lại product để update
    Product product = new Product();
    product.setProductID(productID);
    product.setProductName(productName);
    product.setCategoryID(categoryID);
    product.setImportPrice(importPrice);
    product.setSellingPrice(sellingPrice);
    product.setDescription(description);
    product.setStatus(status);
    product.setUnitID(unitID);
    product.setImageUrl(imageFileName);
    product.setCreatedBy(emp.getUsername());
    product.setCreatedDate(localDateTime);
    productDAO.updateProduct(product);
    response.sendRedirect("ListProductServlet");
} else if ("delete".equals(action)) {
              int productID = Integer.parseInt(request.getParameter("productID"));
              String databaseName = (String) request.getSession().getAttribute("databaseName");
              DBContext dBContext = new DBContext(databaseName);
            ProductDAO productDAO = new ProductDAO(dBContext.getConnection());
            productDAO.deleteProduct(productID);

        // Sau khi update hoặc delete, redirect lại về list
        response.sendRedirect("ListProductServlet");
    }else if ("add".equals(action)) {
    String productName = request.getParameter("productName");
    String categoryID = request.getParameter("categoryID");
    BigDecimal importPrice = new BigDecimal(request.getParameter("importPrice"));
    BigDecimal sellingPrice = new BigDecimal(request.getParameter("sellingPrice"));
    String description = request.getParameter("description");
    boolean status = Boolean.parseBoolean(request.getParameter("status"));
    String unitID = request.getParameter("unitID");

    // Xử lý ảnh nếu có upload (ở mức cơ bản)
    String imageFileName = null;
    try {
        jakarta.servlet.http.Part imagePart = request.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0) {
            String submittedFileName = imagePart.getSubmittedFileName();
            imageFileName = java.nio.file.Paths.get(submittedFileName).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/") + "images";
            java.io.File uploadDir = new java.io.File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            imagePart.write(uploadPath + java.io.File.separator + imageFileName);
        }
    } catch (Exception e) {
        e.printStackTrace(); // hoặc log lỗi nếu cần
    }

    // Tạo đối tượng Product mới
    Product product = new Product();
    product.setProductName(productName);
    product.setCategoryID(categoryID);
    product.setImportPrice(importPrice);
    product.setSellingPrice(sellingPrice);
    product.setDescription(description);
    product.setStatus(status);
    product.setUnitID(unitID);
    product.setCreatedBy(emp.getUsername());
    product.setCreatedDate(localDateTime);
    if (imageFileName != null) {
        product.setImageUrl(imageFileName);
    }

    // Thêm vào database
    String databaseName = (String) request.getSession().getAttribute("databaseName");
            System.out.println(databaseName);
    DBContext dBContext = new DBContext(databaseName);
    ProductDAO productDAO = new ProductDAO(dBContext.getConnection());
    productDAO.createProduct(product);

    response.sendRedirect("ListProductServlet");
}else if ("import".equals(action)) {
    // Lấy file Excel từ form
    jakarta.servlet.http.Part filePart = request.getPart("excelFile");
    
    if (filePart != null && filePart.getSize() > 0) {
        try (InputStream inputStream = filePart.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // lấy sheet đầu tiên

            String databaseName = (String) request.getSession().getAttribute("databaseName");
            System.out.println(databaseName);
            DBContext db = new DBContext(databaseName);
            ProductDAO productDAO = new ProductDAO(db.getConnection());

            for (Row row : sheet) {
    if (row.getRowNum() == 0) continue; // bỏ dòng tiêu đề

    String name = row.getCell(0).getStringCellValue();
    String categoryID = getCellStringValue(row.getCell(1));
categoryID = categoryID.replaceAll("\\.0$", ""); // loại bỏ .0 ở cuối nếu có

    String importPriceStr = getCellStringValue(row.getCell(2));
String sellingPriceStr = getCellStringValue(row.getCell(3));

BigDecimal importPrice = importPriceStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(importPriceStr);
BigDecimal sellingPrice = sellingPriceStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(sellingPriceStr);
String description = row.getCell(4).getStringCellValue();

    String unitID = getCellStringValue(row.getCell(5));
if (unitID.contains(".")) {
    unitID = unitID.substring(0, unitID.indexOf("."));
}


    Product product = new Product();
    product.setProductName(name);
    product.setCategoryID(categoryID);
    product.setImportPrice(importPrice);
    product.setSellingPrice(sellingPrice);
    product.setDescription(description);
    product.setUnitID(unitID);
    product.setStatus(true);
    product.setCreatedBy(emp.getUsername());
    product.setCreatedDate(localDateTime);

    productDAO.createProduct(product);
}


            workbook.close();
        } catch (Exception e) {
            
            e.printStackTrace();
            request.setAttribute("importError", "Lỗi khi đọc file Excel!");
        }
    }
    response.sendRedirect("ListProductServlet");
}

    }
   private String getCellStringValue(Cell cell) {
    if (cell == null) return "";

    switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue().trim();

        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString(); // hoặc format lại nếu cần
            } else {
                double val = cell.getNumericCellValue();
                if (val == (int) val) {
                    return String.valueOf((int) val); // loại bỏ .0 nếu là số nguyên
                } else {
                    return String.valueOf(val);
                }
            }

        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());

        case FORMULA:
            try {
                return cell.getStringCellValue();
            } catch (IllegalStateException e) {
                return String.valueOf(cell.getNumericCellValue());
            }

        case BLANK:
        case _NONE:
        case ERROR:
        default:
            return "";
    }
}


    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
