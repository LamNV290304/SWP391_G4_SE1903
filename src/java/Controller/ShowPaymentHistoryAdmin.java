/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.PaymentDto;
import Dal.PaymentDAO;
import Dal.ServicePackageDAO;
import Dal.ShopOwnerDAO;
import Models.Payment;
import Models.ServicePackage;
import Models.ShopOwner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ShowPaymentHistoryAdmin extends HttpServlet {

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

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            
            ShopOwner shopOwnerLogged = (ShopOwner) request.getSession().getAttribute("shopOwner");

            if (shopOwnerLogged == null || shopOwnerLogged.getId() != 1) {
                response.sendRedirect(request.getContextPath() + "/SaleSphere");
                return;
            }
            
            String shopOwnerIdParam = request.getParameter("shopOwnerId");

            int shopOwnerId = Integer.parseInt(shopOwnerIdParam);

            String sort = Optional.ofNullable(request.getParameter("sort")).orElse("paymentDate:desc");
            Map<String, String> sortMap = new HashMap<>();
            for (String pair : sort.split(",")) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    sortMap.put(parts[0], parts[1]);
                }
            }
            String packageIdParam = request.getParameter("packageId");
            Integer selectedPackageId = null;
            if (packageIdParam != null && !packageIdParam.isEmpty()) {
                try {
                    selectedPackageId = Integer.parseInt(packageIdParam);
                } catch (NumberFormatException ignored) {
                }
            }

            String fromDateStr = request.getParameter("fromDate");
            String toDateStr = request.getParameter("toDate");

            int page = 1;
            int limit = 5;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException ignored) {
            }
            int offset = (page - 1) * limit;

            Connection conn = DBContext.getCentralConnection();
            PaymentDAO paymentDAO = new PaymentDAO(conn);
            ShopOwnerDAO shopOwnerDAO = new ShopOwnerDAO(conn);
            ServicePackageDAO packageDAO = new ServicePackageDAO(conn);

            ShopOwner shopOwner = shopOwnerDAO.getShopOwnerById(shopOwnerId);
            List<Payment> payments = paymentDAO.getPaymentsByShopOwner(shopOwnerId, offset, limit, sort, selectedPackageId, fromDateStr, toDateStr);
            int totalRecords = paymentDAO.countPaymentsByShopOwner(shopOwnerId, selectedPackageId, fromDateStr, toDateStr);
            int totalPages = (int) Math.ceil((double) totalRecords / limit);
            List<ServicePackage> packageList = packageDAO.getAll();
            double totalAmount = paymentDAO.sumSuccessfulPayments(shopOwnerId);

            String exportType = request.getParameter("export");

            if ("excel".equals(exportType)) {
                System.out.println("hehehe");
                exportToExcel(request, response, payments, totalAmount); // Gọi hàm riêng
                return; // Kết thúc luồng xuất excel
            }

            request.setAttribute("payments", payments);
            request.setAttribute("shop", shopOwner);
            request.setAttribute("sort", sort);
            request.setAttribute("sortMap", sortMap);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("packageList", packageList);
            request.setAttribute("selectedPackageId", selectedPackageId);
            request.setAttribute("fromDate", fromDateStr);
            request.setAttribute("toDate", toDateStr);
            request.setAttribute("totalAmount", totalAmount);

            request.getRequestDispatcher("ShopOwner/paymentHistory.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ShowPaymentHistoryAdmin.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải lịch sử thanh toán (Admin).");
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

    private void exportToExcel(HttpServletRequest request, HttpServletResponse response, List<Payment> payments, double totalAmount) throws IOException {
        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Lịch sử thanh toán");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] columns = {"Gói dịch vụ", "Ngày thanh toán", "Số tiền", "Trạng thái", "Chủ shop", "Tên shop"};
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        int rowIndex = 1;
        for (Payment p : payments) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(p.getPackageName());
            row.createCell(1).setCellValue(sdf.format(p.getPaymentDate()));
            row.createCell(2).setCellValue(p.getAmount());
            row.createCell(3).setCellValue(p.getStatus());
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
        totalCell.setCellValue(totalAmount);

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
