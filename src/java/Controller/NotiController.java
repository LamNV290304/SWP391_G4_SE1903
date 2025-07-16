/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.NotiDAO;
import Models.Noti;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "NotiController", urlPatterns = {"/NotiController"})
public class NotiController extends HttpServlet {

    DBContext connection = new DBContext("Test");
    NotiDAO notiDAO = new NotiDAO(connection.getConnection());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if (service == null || service.isEmpty()) {
            // Mặc định hiển thị tất cả thông báo
            loadAllNotifications(request, response);
        } else {
            switch (service) {
                case "LoadAll":
                    loadAllNotifications(request, response);
                    break;
                case "LoadUnread":
                    loadUnreadNotifications(request, response);
                    break;
                case "SetIsRead":
                    SetIsRead(request, response);
                    break;
                case "SetAllRead":
                    SetAllAsRead(request, response);
                    break;
            }
        }
    }

    private void loadAllNotifications(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sql = "SELECT * FROM Noti ORDER BY CreatedDate DESC";
        Vector<Noti> vectorNoti = notiDAO.getAllNoti(sql);

        int unreadCount = 0;
        for (Noti n : vectorNoti) {
            if (n.getIsRead() == 0) {
                unreadCount++;
            }
        }

        Map<Integer, Integer> mapDate = notiDAO.MapListNotiDate();

        request.setAttribute("vectorNoti", vectorNoti);
        request.setAttribute("sizeNoti", unreadCount);
        request.setAttribute("mapNotiDate", mapDate);
        request.setAttribute("currentTab", "all");
        request.getRequestDispatcher("TransferReceiptJSP/Noti.jsp").forward(request, response);
    }

    private void loadUnreadNotifications(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sql = "SELECT * FROM Noti WHERE IsRead = 0 ORDER BY CreatedDate DESC";
        Vector<Noti> vectorNoti = notiDAO.getAllNoti(sql);

        // Cũng cần lấy tổng số thông báo chưa đọc để hiển thị badge
        String countSql = "SELECT * FROM Noti WHERE IsRead = 0";
        Vector<Noti> allUnreadNoti = notiDAO.getAllNoti(countSql);
        int unreadCount = allUnreadNoti.size();

        Map<Integer, Integer> mapDate = notiDAO.MapListNotiDate();

        request.setAttribute("vectorNoti", vectorNoti);
        request.setAttribute("sizeNoti", unreadCount);
        request.setAttribute("mapNotiDate", mapDate);
        request.setAttribute("currentTab", "unread");
        request.getRequestDispatcher("TransferReceiptJSP/Noti.jsp").forward(request, response);
    }

    private void SetIsRead(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String check = request.getParameter("NotiID");

        if (check != null) {
            int NotiID = Integer.parseInt(request.getParameter("NotiID"));
            notiDAO.updateNotiIsRead(NotiID, 1);
        }
        String link = request.getParameter("link");

        if (link != null && !link.trim().isEmpty() && !link.equals("#")) {
            response.sendRedirect(link);
        } else {
            response.sendRedirect("NotiController");
        }
    }

    private void SetAllAsRead(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String sql = "SELECT * FROM Noti WHERE IsRead = 0";
        Vector<Noti> vectorNoti = notiDAO.getAllNoti(sql);
        for (Noti noti : vectorNoti) {
            notiDAO.updateNotiIsRead(noti.getNotiID(), 1);
        }
        response.sendRedirect("NotiController");
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
