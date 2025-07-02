package Controller;

import Context.DBContext;
import DTO.ShopSubscriptionDto;
import Dal.PaymentDAO;
import Dal.ShopSubscriptionDAO;
import Models.Payment;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VNPayReturn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String responseCode = request.getParameter("vnp_ResponseCode");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String txnRef = request.getParameter("vnp_TxnRef");

        if (responseCode == null || !responseCode.equals("00")) {
            // ❌ Giao dịch thất bại
            response.sendRedirect("ShowServicePackage?error=" + URLEncoder.encode("Thanh toán thất bại hoặc bị huỷ", StandardCharsets.UTF_8));
            return;
        }

        try (Connection conn = DBContext.getCentralConnection()) {
            String[] parts = orderInfo.split(";");
            String actionType = parts[0].trim(); // Thanh toán gói... hoặc Gia hạn gói...
            int packageId = Integer.parseInt(parts[1]);
            int shopOwnerId = Integer.parseInt(parts[2]);

            PaymentDAO paymentDAO = new PaymentDAO(conn);
            ShopSubscriptionDAO subDAO = new ShopSubscriptionDAO(conn);

            // Cập nhật trạng thái giao dịch
            paymentDAO.updateStatusByTxnRef(txnRef, "Thành công");
            Payment payment = paymentDAO.getByTxnRef(txnRef);

            if (actionType.toLowerCase().contains("gia hạn")) {
                // Gia hạn: cập nhật thời gian hết hạn cho gói đang dùng
                ShopSubscriptionDto currentSub = subDAO.getActiveSubscriptionByShopId(shopOwnerId);
                if (currentSub != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currentSub.getEndDate().after(payment.getPaymentDate()) ? currentSub.getEndDate() : payment.getPaymentDate());
                    cal.add(Calendar.DATE, currentSub.getPackageDurationInDays());
                    subDAO.extendSubscription(currentSub.getId(), new java.sql.Date(cal.getTimeInMillis()));
                }
            } else {
                // Đăng ký mới: huỷ gói cũ và thêm mới
                subDAO.cancelPreviousSubscriptions(shopOwnerId);
                subDAO.insertNewSubscription(shopOwnerId, packageId, payment.getId());
            }

            response.sendRedirect("ShowServicePackage?success=" + URLEncoder.encode("Thanh toán thành công!", StandardCharsets.UTF_8));

        } catch (Exception ex) {
            try {
                Logger.getLogger(VNPayReturn.class.getName()).log(Level.SEVERE, null, ex);
                PaymentDAO paymentDAO = new PaymentDAO(DBContext.getCentralConnection());
                paymentDAO.updateStatusByTxnRef(txnRef, "Thất bại");
                response.sendRedirect("ShowServicePackage?error=" + URLEncoder.encode("Lỗi xử lý thanh toán.", StandardCharsets.UTF_8));
            } catch (ClassNotFoundException | SQLException ex1) {
                Logger.getLogger(VNPayReturn.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "VNPay return handler servlet";
    }
}
