/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.PaymentDAO;
import Dal.ServicePackageDAO;
import Dal.ShopOwnerDAO;
import Models.Payment;
import Models.ServicePackage;
import Models.ShopOwner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
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
            String shopOwnerIdParam = request.getParameter("shopOwnerId");
            if (shopOwnerIdParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu shopOwnerId");
                return;
            }

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

}
