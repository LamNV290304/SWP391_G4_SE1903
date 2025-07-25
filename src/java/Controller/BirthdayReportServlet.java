/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.CustomerDAO;
import Dal.PromotionDAO;
import Models.Customer;
import Models.Promotion;
import Utils.MailSender;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author duckh
 */
public class BirthdayReportServlet extends HttpServlet {


    private CustomerDAO cDAO;
    private PromotionDAO pDAO;

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
        cDAO = new CustomerDAO(connection.getConnection());
        pDAO = new PromotionDAO(connection.getConnection());

        return true;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String start = request.getParameter("startDate");
            String end = request.getParameter("endDate");
            String month = request.getParameter("month");
            String searchKeyword = request.getParameter("searchKeyword");
            if (searchKeyword == null) {
                searchKeyword = "";
            }
            Date fromDate, toDate;
            Integer monthFilter = null;
            LocalDate today = LocalDate.now();

            fromDate = (start != null) ? Date.valueOf(start) : Date.valueOf(today.minusMonths(1));
            toDate = (end != null) ? Date.valueOf(end) : Date.valueOf(today);

            if (month != null && !month.isEmpty()) {
                monthFilter = Integer.parseInt(month);
            } else {
                monthFilter = today.getMonthValue();
            }

            // ✅ SỬA Ở ĐÂY
            List<Customer> customers = cDAO.getCustomersByMonthAndKeyword(monthFilter, searchKeyword);

            request.setAttribute("selectedMonth", monthFilter);
            request.setAttribute("customers", customers);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);

            request.getRequestDispatcher("birthday_report.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(BirthdayReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String[] selectedIds = request.getParameterValues("selectedIds");
            String monthParam = request.getParameter("month");
            String searchKeyword = request.getParameter("searchKeyword");
            if (searchKeyword == null) {
                searchKeyword = "";
            }

            Integer selectedMonth = null;
            try {
                selectedMonth = (monthParam != null && !monthParam.isEmpty())
                        ? Integer.parseInt(monthParam)
                        : LocalDate.now().getMonthValue();
            } catch (NumberFormatException e) {
                selectedMonth = LocalDate.now().getMonthValue();
            }

            // Nếu không chọn khách nào
            if (selectedIds == null || selectedIds.length == 0) {
                request.setAttribute("error", "❌ Vui lòng chọn ít nhất một khách hàng để gửi email.");
                // Load lại danh sách và giữ nguyên filter
                List<Customer> customers = cDAO.getCustomersByMonthAndKeyword(selectedMonth, searchKeyword);
                request.setAttribute("customers", customers);
                request.setAttribute("selectedMonth", selectedMonth);
                request.setAttribute("searchKeyword", searchKeyword);
                request.getRequestDispatcher("birthday_report.jsp").forward(request, response);
                return;
            }

            // Lấy chương trình khuyến mãi đang hoạt động
            Promotion activePromotion = pDAO.getActivePromotion();
            if (activePromotion == null) {
                request.setAttribute("error", "❌ Không có chương trình khuyến mãi nào đang diễn ra.");
                List<Customer> customers = cDAO.getCustomersByMonthAndKeyword(selectedMonth, searchKeyword);
                request.setAttribute("customers", customers);
                request.setAttribute("selectedMonth", selectedMonth);
                request.setAttribute("searchKeyword", searchKeyword);
                request.getRequestDispatcher("birthday_report.jsp").forward(request, response);
                return;
            }

            // Tiến hành gửi email
            int successCount = 0;
            int failCount = 0;
            StringBuilder failLog = new StringBuilder();
            MailSender mailSender = new MailSender();

            for (String idStr : selectedIds) {
                try {
                    int customerId = Integer.parseInt(idStr);
                    Customer customer = cDAO.getCustomerById(customerId);

                    if (customer == null || customer.getBirthday() == null) {
                        failCount++;
                        failLog.append("Không tìm thấy hoặc thiếu ngày sinh: ID ").append(idStr).append("<br/>");
                        continue;
                    }

                    // Kiểm tra sinh nhật có nằm trong thời gian khuyến mãi không
                    LocalDate birthday = customer.getBirthday().toLocalDate();
                    LocalDate birthdayThisYear = birthday.withYear(LocalDate.now().getYear());
                    if (birthdayThisYear.isBefore(activePromotion.getStartDate().toLocalDate())
                            || birthdayThisYear.isAfter(activePromotion.getEndDate().toLocalDate())) {
                        failCount++;
                        failLog.append("Sinh nhật không trong thời gian khuyến mãi: ").append(customer.getCustomerName()).append("<br/>");
                        continue;
                    }

                    // Soạn nội dung email
                    String subject = "🎂 Happy Birthday " + customer.getCustomerName() + " 🎉";
                    String content = "Chúc mừng sinh nhật bạn! 🎉\n\n"
                            + "Chúng tôi gửi tặng bạn chương trình khuyến mãi đặc biệt:\n"
                            + "- " + activePromotion.getPromotionName() + "\n"
                            + "- Giảm " + activePromotion.getDiscountRate() + "%\n"
                            + "- Áp dụng cho danh mục: " + activePromotion.getCategoryName() + "\n"
                            + "- Thời gian: từ " + activePromotion.getStartDate() + " đến " + activePromotion.getEndDate() + "\n\n"
                            + "Chúc bạn một ngày tuyệt vời!\n\n"
                            + "Trân trọng,\nĐội ngũ CSKH";

                    // Gửi
                    mailSender.sendInvoiceMail(customer.getEmail(), subject, content);
                    successCount++;

                } catch (Exception e) {
                    failCount++;
                    failLog.append("Lỗi gửi email ID ").append(idStr).append(": ").append(e.getMessage()).append("<br/>");
                }
            }

            // Thông báo kết quả
            if (successCount > 0) {
                request.setAttribute("successMessage", "✅ Đã gửi thành công " + successCount + " email.");
            }
            if (failCount > 0) {
                request.setAttribute("error", "❌ Gửi thất bại " + failCount + " khách hàng.<br/>" + failLog.toString());
            }

            // Load lại danh sách đã lọc
            List<Customer> customers = cDAO.getCustomersByMonthAndKeyword(selectedMonth, searchKeyword);
            request.setAttribute("customers", customers);
            request.setAttribute("selectedMonth", selectedMonth);
            request.setAttribute("searchKeyword", searchKeyword);

            request.getRequestDispatcher("birthday_report.jsp").forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(BirthdayReportServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "❌ Lỗi hệ thống: " + ex.getMessage());
            request.getRequestDispatcher("birthday_report.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
