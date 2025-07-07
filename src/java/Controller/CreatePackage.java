/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.ServicePackageDAO;
import Models.ServicePackage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
public class CreatePackage extends HttpServlet {

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
            out.println("<title>Servlet CreatePackage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreatePackage at " + request.getContextPath() + "</h1>");
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
            String name = request.getParameter("name");
            int duration = Integer.parseInt(request.getParameter("durationInDays"));
            double price = Double.parseDouble(request.getParameter("price"));
            String description = request.getParameter("description");

            ServicePackage newPkg = new ServicePackage();
            newPkg.setName(name);
            newPkg.setDurationInDays(duration);
            newPkg.setPrice(price);
            newPkg.setDescription(description);

            ServicePackageDAO dao = new ServicePackageDAO(DBContext.getCentralConnection());
            boolean inserted = dao.createPackage(newPkg);

            HttpSession session = request.getSession();

            if (inserted) {
                session.setAttribute("flash_success", "Cập nhật thành công!");
                response.sendRedirect("ShowServicePackage");
            } else {
                session.setAttribute("flash_fail", "Cập nhật thất bại!");
                response.sendRedirect("ShowServicePackage");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ShowServicePackage?error=Có lỗi khi thêm gói!");
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

}
