/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import Context.DBContext;
import Dal.*;
import Models.PaymentVoucher;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
/**
 *
 * @author Thai Anh
 */
@MultipartConfig
public class PaymentVoucherServlet extends HttpServlet {

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
        try {
            String databaseName = (String) request.getSession().getAttribute("databaseName");
            response.setContentType("text/html;charset=UTF-8");
            Connection conn = new DBContext(databaseName).getConnection();
            TypeReceiptVoucherDAO thuDAO = new TypeReceiptVoucherDAO(conn);   // DAO cho phiếu thu
            TypePaymentVoucherDAO chiDAO = new TypePaymentVoucherDAO(conn);   // DAO cho phiếu chi
            PaymentVoucherDAO dao = new PaymentVoucherDAO(conn);
            request.setAttribute("typeReceiptList", thuDAO.getAllTypes(1));
            request.setAttribute("typePaymentList", chiDAO.getAllTypes(1));
            BigDecimal totalCost = BigDecimal.ZERO;
            List<PaymentVoucher> list = dao.getAllPaymentVouchers();
for (PaymentVoucher pv : list) {
    totalCost = totalCost.add(pv.getAmount());
}
request.setAttribute("totalCost", totalCost);
            request.getRequestDispatcher("listPaymentVoucher.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(PaymentVoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        try (Connection conn = new DBContext(databaseName).getConnection()) {
            PaymentVoucherDAO dao = new PaymentVoucherDAO(conn);
            ShopDAO shopDAO = new ShopDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            SupplierDAO supplierDAO = new SupplierDAO(conn); // nếu có
            TypePaymentVoucherDAO typeDAO = new TypePaymentVoucherDAO(conn);
            PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO(conn);

            // Parse các tham số lọc từ request
            Integer shopID = parseInteger(request.getParameter("shopID"));
            Integer employeeID = parseInteger(request.getParameter("employeeID"));
            Integer typeID = parseInteger(request.getParameter("typeID"));
            Integer paymentMethodID = parseInteger(request.getParameter("paymentMethodID"));
            BigDecimal minAmount = parseBigDecimal(request.getParameter("minAmount"));
            BigDecimal maxAmount = parseBigDecimal(request.getParameter("maxAmount"));
            java.util.Date fromDate = parseDate(request.getParameter("fromDate"));
            java.util.Date toDate = parseDate(request.getParameter("toDate"));

            List<PaymentVoucher> vouchers = dao.filterPaymentVouchers(
                    shopID, employeeID, typeID,
                    minAmount, maxAmount,
                    fromDate, toDate,
                    paymentMethodID
            );

            request.setAttribute("paymentVouchers", vouchers);
            request.setAttribute("shops", shopDAO.getAllShops());
            request.setAttribute("employees", employeeDAO.getAllEmployee());
            request.setAttribute("suppliers", supplierDAO.getAllSuppliers()); // nếu có
            request.setAttribute("types", typeDAO.getAllTypes(1));
            request.setAttribute("paymentMethods", paymentMethodDAO.getAllPaymentMethods());

            // Gửi lại các filter parameter về JSP
            request.setAttribute("paramShopID", shopID);
            request.setAttribute("paramEmployeeID", employeeID);
            request.setAttribute("paramTypeID", typeID);
            request.setAttribute("paramPaymentMethodID", paymentMethodID);
            request.setAttribute("paramMinAmount", request.getParameter("minAmount"));
            request.setAttribute("paramMaxAmount", request.getParameter("maxAmount"));
            request.setAttribute("paramFromDate", request.getParameter("fromDate"));
            request.setAttribute("paramToDate", request.getParameter("toDate"));

            request.getRequestDispatcher("listPaymentVoucher.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer parseInteger(String param) {
        try {
            return (param != null && !param.isEmpty()) ? Integer.parseInt(param) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String param) {
        try {
            return (param != null && !param.isEmpty()) ? new BigDecimal(param) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private java.util.Date parseDate(String param) {
        try {
            return (param != null && !param.isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd").parse(param) : null;
        } catch (Exception e) {
            return null;
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
         String action = request.getParameter("action");
String databaseName = (String) request.getSession().getAttribute("databaseName");
    try (Connection conn = new DBContext(databaseName).getConnection()) {
        PaymentVoucherDAO dao = new PaymentVoucherDAO(conn);

        if ("update".equals(action)) {
            // Xử lý cập nhật phiếu chi
            int id = Integer.parseInt(request.getParameter("paymentVoucherID"));
            int shopID = Integer.parseInt(request.getParameter("shopID"));
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));
            String supplierStr = request.getParameter("supplierID");
            Integer supplierID = (supplierStr == null || supplierStr.isEmpty()) ? null : Integer.parseInt(supplierStr);
            java.util.Date paymentDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("paymentDate"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            String note = request.getParameter("note");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            int typeID = Integer.parseInt(request.getParameter("typeID"));
            int paymentMethodID = Integer.parseInt(request.getParameter("paymentMethodID"));

            PaymentVoucher pv = new PaymentVoucher();
            pv.setPaymentVoucherID(id);
            PaymentVoucher old = dao.getPaymentVoucherByID(id); 
            pv.setShopID(shopID);
            pv.setEmployeeID(employeeID);
            pv.setSupplierID(supplierID);
            pv.setPaymentDate(paymentDate);
            pv.setAmount(amount);
            pv.setNote(note);
            pv.setStatus(status);
            pv.setTypeID(typeID);
            pv.setPaymentMethodID(paymentMethodID);
            pv.setCreatedDate(old.getCreatedDate());
            dao.updatePaymentVoucher(pv);
        } else if ("delete".equals(action)) {
            // Xử lý xoá phiếu chi
            int id = Integer.parseInt(request.getParameter("paymentVoucherID"));
            dao.deletePaymentVoucher(id);
        } else if ("importExcel".equals(action)) {
    System.out.println("Bắt đầu xử lý importExcel...");
Part filePart = request.getPart("excelFile");
System.out.println("Đã lấy được filePart: " + (filePart != null));
    InputStream fileContent = filePart.getInputStream();

    // Sử dụng Apache POI để đọc Excel
    Workbook workbook = WorkbookFactory.create(fileContent);
    Sheet sheet = workbook.getSheetAt(0);

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue;

        try {
            PaymentVoucher pv = new PaymentVoucher();
            pv.setShopID((int) row.getCell(0).getNumericCellValue());
            pv.setEmployeeID((int) row.getCell(1).getNumericCellValue());

            Cell supplierCell = row.getCell(2);
            if (supplierCell != null && supplierCell.getCellType() != CellType.BLANK) {
                pv.setSupplierID((int) supplierCell.getNumericCellValue());
            } else {
                pv.setSupplierID(null);
            }

            java.util.Date paymentDate = row.getCell(3).getDateCellValue();
            pv.setPaymentDate(paymentDate);

            pv.setAmount(BigDecimal.valueOf(row.getCell(4).getNumericCellValue()));
            pv.setNote(row.getCell(5).getStringCellValue());
            pv.setStatus("Đã chi".equalsIgnoreCase(row.getCell(6).getStringCellValue()));
            pv.setTypeID((int) row.getCell(7).getNumericCellValue());
            pv.setPaymentMethodID((int) row.getCell(8).getNumericCellValue());
            pv.setCreatedDate(new java.util.Date());

            dao.insertPaymentVoucher(pv); // cần tạo phương thức insert
        } catch (Exception ex) {
           ex.printStackTrace();
    Logger.getLogger(PaymentVoucherServlet.class.getName()).log(Level.SEVERE, "Lỗi import Excel", ex);
    request.setAttribute("error", "Lỗi khi import file Excel!");
            // Optionally log row + error
        }
    }

    workbook.close();
}

    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("error", "Lỗi xử lý phiếu chi");
    }

    response.sendRedirect("PaymentVoucherServlet");
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

}
