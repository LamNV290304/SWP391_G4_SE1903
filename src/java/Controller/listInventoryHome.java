/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import Dal.InventoryDAO;
import Models.Inventory;
import Utils.AccessControlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Thai Anh
 */
public class listInventoryHome extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

         request.setCharacterEncoding("UTF-8");
response.setContentType("text/html; charset=UTF-8");
String databaseName = (String) request.getSession().getAttribute("databaseName");
           Context.DBContext db = new Context.DBContext(databaseName); // hoặc dùng constructor mặc định nếu bạn đã sửa
        Connection connection = db.getConnection();
        if (databaseName == null) {
            response.sendRedirect("SaleSphere");
        }

        if (!AccessControlUtil.hasPermission(request, "listInventoryHome")) {
            response.sendRedirect("loginEmployee.jsp");
            return;
        }
        InventoryDAO dao = new InventoryDAO(connection);
         
        List<Inventory> inventoryList = dao.getAllInventories();
       
        /*BigDecimal cost = BigDecimal.ZERO;
         
        for(Inventory ivt : inventoryList){
          //cost = cost.add(ivt.getProduct().getImportPrice().multiply(BigDecimal.valueOf(ivt.getQuantity())));
         try (PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ThemPhieuNhap</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ThemPhieuNhap at 4 "+ivt.getProduct().toString()+ "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
        }
      */
       // request.setAttribute("totalCost", cost);
       // Kiểm tra nếu là yêu cầu export Excel
    String export = request.getParameter("export");
    if ("excel".equalsIgnoreCase(export)) {
        exportToExcel(response, inventoryList);
        return;
    }
        request.setAttribute("inventoryList", inventoryList);
        request.getRequestDispatcher("listInventory.jsp").forward(request, response);
    } 
private void exportToExcel(HttpServletResponse response, List<Inventory> inventoryList) throws IOException {
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=InventoryList.xlsx");

    try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Inventory");

        // Header
        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("STT");
        header.createCell(1).setCellValue("Tên sản phẩm");
        header.createCell(2).setCellValue("Số lượng");
        header.createCell(3).setCellValue("Giá nhập");

        // Data
        int rowIndex = 1;
        for (Inventory item : inventoryList) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rowIndex - 1);
            row.createCell(1).setCellValue(item.getProduct().getProductName());
            row.createCell(2).setCellValue(item.getQuantity());
            if (item.getProduct().getImportPrice() != null) {
    row.createCell(3).setCellValue(item.getProduct().getImportPrice().doubleValue());
} else {
    row.createCell(3).setCellValue("N/A"); // hoặc ghi 0.0 nếu bạn muốn
}

        }

        // Ghi ra output stream
        workbook.write(response.getOutputStream());
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
        processRequest(request, response);
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
        processRequest(request, response);
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
