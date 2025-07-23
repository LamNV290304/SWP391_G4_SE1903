/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.PaymentDto;
import Dal.PaymentDAO;
import Dal.ServicePackageDAO;
import Models.Payment;
import Models.ShopOwner;
import Utils.StringUtils;
import Utils.Validator;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

/**
 *
 * @author Admin
 */
public class AdminPaymentHistory extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
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
            out.println("<title>Servlet AdminPaymentHistory</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminPaymentHistory at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req
     * @param res
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ShopOwner currentUser = (ShopOwner) req.getSession().getAttribute("shopOwner");
        if (currentUser == null || currentUser.getId() != 1) {
            res.sendRedirect(req.getContextPath() + "/SaleSphere");
            return;
        }

        int page = StringUtils.parseInt(req.getParameter("page"), 1);
        int offset = (page - 1) * PAGE_SIZE;
        String sort = req.getParameter("sort");
        String packageIdRaw = req.getParameter("packageId");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");
        String search = Validator.normalizeInput(req.getParameter("search"));
        Integer packageId = StringUtils.parseNullableInt(packageIdRaw);

        try (Connection conn = DBContext.getCentralConnection()) {
            PaymentDAO paymentDAO = new PaymentDAO(conn);
            ServicePackageDAO servicePackageDAO = new ServicePackageDAO(conn);

            List<PaymentDto> payments = paymentDAO.getAllPaymentsForAdmin(offset, PAGE_SIZE, sort, packageId, fromDate, toDate, search);
            int total = paymentDAO.countAllPaymentsForAdmin(packageId, fromDate, toDate, search);
            int totalPages = (int) Math.ceil(total * 1.0 / PAGE_SIZE);

            BigDecimal totalAmount = paymentDAO.getTotalPaid(fromDate, toDate);

            String exportType = req.getParameter("export");

            if ("excel".equals(exportType)) {
                System.out.println("hehehe");
                exportToExcel(req, res, payments, totalAmount); // Gọi hàm riêng
                return; // Kết thúc luồng xuất excel
            }

            req.setAttribute("totalAmount", totalAmount);
            req.setAttribute("payments", payments);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("sort", sort);
            req.setAttribute("fromDate", fromDate);
            req.setAttribute("toDate", toDate);
            req.setAttribute("selectedPackageId", packageId);
            req.setAttribute("packageList", servicePackageDAO.getAll());
            req.setAttribute("search", search);

            req.getRequestDispatcher("ShopOwner/adminPaymentHistory.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void exportToExcel(HttpServletRequest request, HttpServletResponse response, List<PaymentDto> payments, BigDecimal totalAmount) throws IOException {
        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Lịch sử thanh toán");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] columns = {"Gói dịch vụ", "Ngày thanh toán", "Số tiền", "Trạng thái", "Chủ shop", "Tên shop"};
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        int rowIndex = 1;
        for (PaymentDto p : payments) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(p.getPackageName());
            row.createCell(1).setCellValue(sdf.format(p.getPaymentDate()));
            row.createCell(2).setCellValue(p.getAmount());
            row.createCell(3).setCellValue(p.getStatus());
            row.createCell(4).setCellValue(p.getShopOwnerName());
            row.createCell(5).setCellValue(p.getShopName());
        }

        org.apache.poi.ss.usermodel.Row totalRow = sheet.createRow(rowIndex++);
        org.apache.poi.ss.usermodel.Cell labelCell = totalRow.createCell(0);
        labelCell.setCellValue("TỔNG SỐ TIỀN KHÁCH ĐÃ TRẢ:");

        org.apache.poi.ss.usermodel.CellStyle boldStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        labelCell.setCellStyle(boldStyle);

        org.apache.poi.ss.usermodel.Cell totalCell = totalRow.createCell(2);
        totalCell.setCellValue(totalAmount.doubleValue());

        org.apache.poi.ss.usermodel.CellStyle currencyStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.DataFormat format = workbook.createDataFormat();
        currencyStyle.setDataFormat(format.getFormat("#,##0₫"));
        totalCell.setCellStyle(currencyStyle);

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=LichSuThanhToan.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
