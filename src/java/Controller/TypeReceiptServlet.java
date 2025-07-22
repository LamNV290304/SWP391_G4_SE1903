/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.SupplierDAO;
import Dal.TypeExportReceiptDAO;
import Dal.TypeImportReceiptDAO;
import Models.Supplier;
import Models.TypeExportReceipt;
import Models.TypeImportReceipt;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Thai Anh
 */
public class TypeReceiptServlet extends HttpServlet {

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
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        Connection conn = new DBContext(databaseName).getConnection();
        TypeExportReceiptDAO daoEx = new TypeExportReceiptDAO(conn);
        TypeImportReceiptDAO daoIm = new TypeImportReceiptDAO(conn);
        SupplierDAO supDAO = new SupplierDAO(conn);
        List<TypeExportReceipt> typeExportList = daoEx.getAllTypeExportReceipts(1);
        List<TypeImportReceipt> typeImports = daoIm.getAllTypeImportReceipts(1);
        request.setAttribute("typeExportList", typeExportList);
        request.setAttribute("typeImportList", typeImports);
        request.setAttribute("supList", supDAO.getAllSuppliers());
        request.getRequestDispatcher("listTypeReceipt.jsp").forward(request, response);
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
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        Connection conn = new DBContext(databaseName).getConnection();
        TypeExportReceiptDAO daoEx = new TypeExportReceiptDAO(conn);
        TypeImportReceiptDAO daoIm = new TypeImportReceiptDAO(conn);
        SupplierDAO supDAO = new SupplierDAO(conn);

        switch (action) {
            case "addExportType" -> {
                TypeExportReceipt newEx = new TypeExportReceipt(request.getParameter("nameTypeEx"));
                daoEx.insert(newEx);
                response.sendRedirect("TypeReceiptServlet");
                return;
            }

            case "editExport" -> {
                String name = request.getParameter("nameTypeEx");
                TypeExportReceipt type = daoEx.getByID(id);
                type.setTypeName(name);
                daoEx.update(type);
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
            case "deleteExport" -> {
                daoEx.delete(id);
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
            case "addImportType" -> {
                TypeImportReceipt newIm = new TypeImportReceipt(request.getParameter("nameTypeIm"));
                daoIm.insert(newIm);
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
            case "editImport" -> {
                String name = request.getParameter("nameTypeIm");
                TypeImportReceipt type = daoIm.getByID(id);
                type.setTypeName(name);
                daoIm.update(type);
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
            case "deleteImport" -> {
                daoIm.delete(Integer.parseInt(id));
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
            case "addSupplier" -> {
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String address = request.getParameter("address");
                String importDateStr = request.getParameter("Date");
                Date importDate = null;

                if (importDateStr != null && !importDateStr.isEmpty()) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        importDate = formatter.parse(importDateStr);
                        Date now = new Date(); // lấy thời gian hiện tại

                        if (importDate.after(now)) {
                            request.setAttribute("erroll", "Date Invalid");
                            request.getRequestDispatcher("ErrolReceipt.jsp").forward(request, response);
                        }
                        // Nếu cần kiểm tra:
                    } catch (Exception e) {
                        e.printStackTrace(); // hoặc xử lý lỗi
                    }
                }
                String createdBy = request.getParameter("createdBy");
                Supplier supplier = new Supplier(name, phone, email, address, true, importDate, createdBy);
                supDAO.insertSupplier(supplier);
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
            case "editSupplier" -> {
                String name = request.getParameter("supplierName");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String address = request.getParameter("address");
                boolean status = Boolean.parseBoolean(request.getParameter("status"));
                supDAO.updateSupplier(new Supplier(name, phone, email, address, status, new Date(), "admin"), Integer.parseInt(id));
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
            case "deleteSupplier" -> {
                supDAO.deleteSupplier(Integer.parseInt(id));
                response.sendRedirect("TypeReceiptServlet");
                return;
            }
        }
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InventoryCheckServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InventoryCheckServlet at " + (id) + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
