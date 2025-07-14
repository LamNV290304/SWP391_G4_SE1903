/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import DTO.PaymentDto;
import Dal.PaymentDAO;
import Dal.ServicePackageDAO;
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
            res.sendRedirect("login.jsp");
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

            BigDecimal totalAmount = paymentDAO.getTotalPaid(fromDate, toDate); // Viết hàm này trong DAO
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

}
