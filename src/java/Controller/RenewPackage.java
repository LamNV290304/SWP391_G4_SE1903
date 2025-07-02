/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.Config;
import Context.DBContext;
import Dal.PaymentDAO;
import Dal.ServicePackageDAO;
import Dal.ShopSubscriptionDAO;
import Models.Payment;
import Models.ServicePackage;
import Models.ShopOwner;
import Models.ShopSubscription;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Admin
 */
public class RenewPackage extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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

        try {
            int subscriptionId = Integer.parseInt(request.getParameter("subscriptionId"));
            ShopOwner shopOwner = (ShopOwner) request.getSession().getAttribute("shopOwner");
            if (shopOwner == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Connection conn = DBContext.getCentralConnection();
            ShopSubscriptionDAO subDAO = new ShopSubscriptionDAO(conn);
            PaymentDAO paymentDAO = new PaymentDAO(conn);
            ServicePackageDAO packageDAO = new ServicePackageDAO(conn);

            // Lấy thông tin subscription hiện tại
            ShopSubscription subscription = subDAO.getById(subscriptionId);
            if (subscription == null || subscription.getShopOwnerId()!= shopOwner.getId()) {
                response.sendRedirect("ShopOwner/home.jsp?error=" + URLEncoder.encode("Không tìm thấy gói hợp lệ.", StandardCharsets.UTF_8));
                return;
            }

            ServicePackage servicePackage = packageDAO.getById(subscription.getPackageId());
            if (servicePackage == null) {
                response.sendRedirect("ShopOwner/home.jsp?error=" + URLEncoder.encode("Không tìm thấy gói dịch vụ.", StandardCharsets.UTF_8));
                return;
            }

            // Tính ngày hết hạn mới
            Date now = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(subscription.getEndDate().after(now) ? subscription.getEndDate() : now);
            cal.add(Calendar.DATE, servicePackage.getDurationInDays());
            Date newExpireDate = new Date(cal.getTimeInMillis());

            String txnRef = Config.getRandomNumber(8);
            String orderInfo = "Gia hạn gói " + servicePackage.getName() + ";" + subscription.getPackageId() + ";" + shopOwner.getId();
            double amount = servicePackage.getPrice();

            // Ghi nhận Payment chờ
            Payment payment = new Payment();
            payment.setShopOwnerId(shopOwner.getId());
            payment.setPackageId(servicePackage.getId());
            payment.setPaymentDate(now);
            payment.setExpireAt(newExpireDate);
            payment.setAmount(amount);
            payment.setStatus("Thất bại"); // Chỉ chuyển thành "Thành công" sau khi callback VNPay
            payment.setTxnRef(txnRef);
            paymentDAO.insert(payment);

            // Tạo URL thanh toán
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf((long) (amount * 100)));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_OrderInfo", orderInfo);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", Config.getIpAddress(request));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
            cld.add(Calendar.MINUTE, 15);
            vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

            // Build hash + URL
            String paymentUrl = generateVnpayUrl(vnp_Params);
            response.sendRedirect(paymentUrl);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("ShopOwner/home.jsp?error=" + URLEncoder.encode("Lỗi khi xử lý gia hạn.", StandardCharsets.UTF_8));
        }
    }

    public static String generateVnpayUrl(Map<String, String> vnp_Params) throws IOException {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encoded = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII);
                hashData.append(fieldName).append("=").append(encoded);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append("=").append(encoded);
                if (i < fieldNames.size() - 1) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }
        String secureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);
        return Config.vnp_PayUrl + "?" + query;
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
