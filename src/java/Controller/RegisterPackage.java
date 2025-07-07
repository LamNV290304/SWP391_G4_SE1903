/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.PaymentDAO;
import Dal.ServicePackageDAO;
import Models.Payment;
import Models.ServicePackage;
import Models.ShopOwner;
import Context.Config;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 *
 * @author Admin
 */
public class RegisterPackage extends HttpServlet {

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
            out.println("<title>Servlet RegisterPackage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterPackage at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            ShopOwner shopOwner = (ShopOwner) request.getSession().getAttribute("shopOwner");
            if (shopOwner == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            PaymentDAO paymentDAO = new PaymentDAO(DBContext.getCentralConnection());
            ServicePackageDAO servicePackageDAO = new ServicePackageDAO(DBContext.getCentralConnection());
            ServicePackage servicePackage = servicePackageDAO.getById(packageId);

            if (servicePackage == null) {
                request.getRequestDispatcher("ShopOwner/home.jsp").forward(request, response);
                return;
            }

            String txnRef = Config.getRandomNumber(8);
            String orderInfo = "Thanh toán gói " + servicePackage.getName() + ";" + packageId + ";" + shopOwner.getId();
            double amount = servicePackage.getPrice();

            Date now = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DATE, servicePackage.getDurationInDays());
            Date expireAt = new Date(cal.getTimeInMillis());

            Payment payment = new Payment();
            payment.setShopOwnerId(shopOwner.getId());
            payment.setPackageId(packageId);
            payment.setPaymentDate(now);
            payment.setExpireAt(expireAt);
            payment.setAmount(amount);
            payment.setStatus("Thất bại");
            payment.setTxnRef(txnRef);

            paymentDAO.insert(payment);

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

            String paymentUrl = generateVnpayUrl(vnp_Params);
            response.sendRedirect(paymentUrl);

        } catch (ClassNotFoundException | SQLException ex ) {
            request.getRequestDispatcher("ShopOwner/home.jsp?error=" + URLEncoder.encode("Lỗi khi xử lý đăng kí.", StandardCharsets.UTF_8)).forward(request, response);
        }
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

    public static String generateVnpayUrl(Map<String, String> vnp_Params) throws IOException {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());
                hashData.append(fieldName).append('=').append(encodedValue);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                     .append('=')
                     .append(encodedValue);
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return Config.vnp_PayUrl + "?" + query.toString();
    }

}
