/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.RevenueDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 *
 * @author Admin
 */
public class RevenueDashboard extends HttpServlet {

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
            out.println("<title>Servlet RevenueDashboard</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RevenueDashboard at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fromMonth = request.getParameter("fromMonth");
            String toMonth = request.getParameter("toMonth");

            LocalDate now = LocalDate.now();
            YearMonth defaultFrom = YearMonth.from(now).minusMonths(5);
            YearMonth defaultTo = YearMonth.from(now);

            YearMonth fromYM = (fromMonth != null && !fromMonth.isEmpty()) ? YearMonth.parse(fromMonth) : defaultFrom;
            YearMonth toYM = (toMonth != null && !toMonth.isEmpty()) ? YearMonth.parse(toMonth) : defaultTo;

            LocalDate startDate = fromYM.atDay(1);
            LocalDate endDate = toYM.atEndOfMonth();

            RevenueDAO dao = new RevenueDAO(DBContext.getCentralConnection());

            request.setAttribute("fromMonth", fromYM);
            request.setAttribute("toMonth", toYM);
            request.setAttribute("totalRevenue", dao.getTotalRevenue(startDate, endDate));
            request.setAttribute("totalTransactions", dao.getSuccessfulTransactionCount(startDate, endDate));
            request.setAttribute("activeShops", dao.getActiveShopCount());
            request.setAttribute("growthRate", dao.calculateGrowth(startDate, endDate));

            request.setAttribute("monthlyRevenue", dao.getMonthlyRevenueData(startDate, endDate));
            request.setAttribute("monthLabels", dao.getMonthLabels(startDate, endDate));
            request.setAttribute("packageLabels", dao.getPackageLabels(startDate, endDate));
            request.setAttribute("packageRatios", dao.getPackageRatios(startDate, endDate));

            request.getRequestDispatcher("ShopOwner/revenueDashboard.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
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
