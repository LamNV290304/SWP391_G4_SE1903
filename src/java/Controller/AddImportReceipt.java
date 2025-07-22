package Controller;

import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Dal.ImportReceiptDAO;
import Dal.InventoryDAO;
import Models.ImportReceipt;
import Models.Inventory;
import Models.Product;
import Models.Shop;
import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.ImportReceiptDetailDAO;
import Dal.NotiDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.SupplierDAO;
import Dal.TypeImportReceiptDAO;
import Models.Employee;
import Models.ImportReceiptDetail;
import Models.Noti;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import static java.time.LocalDate.now;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class AddImportReceipt extends HttpServlet {

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
        try {
            String databaseName = (String) request.getSession().getAttribute("databaseName");
            response.setContentType("text/html;charset=UTF-8");
            Connection conn = new DBContext(databaseName).getConnection();
            EmployeeDAO empDao = new EmployeeDAO(conn);
            TypeImportReceiptDAO typeImp = new TypeImportReceiptDAO(conn);
            ShopDAO shopDao = new ShopDAO(conn);
            SupplierDAO supDAO = new SupplierDAO(conn);
            ProductDAO ProDAO = new ProductDAO(conn);

            request.setAttribute("listEmp", empDao.getAllEmployee());
            request.setAttribute("listSup", supDAO.getAllSuppliers());
            request.setAttribute("listShop", shopDao.getAllShops());

            request.setAttribute("listType", typeImp.getAllTypeImportReceipts(1));

            request.setAttribute("listProduct", ProDAO.getAllProducts());

            request.setAttribute("listEmp", empDao.getAllEmployee());
            request.setAttribute("listSup", supDAO.getAllSuppliers());
            request.setAttribute("listShop", shopDao.getAllShops());
            request.setAttribute("listType", typeImp.getAllTypeImportReceipts(1));
            request.setAttribute("listProduct", ProDAO.getAllProducts());
            request.getRequestDispatcher("AddImportReceipt.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AddImportReceipt.class.getName()).log(Level.SEVERE, null, ex);
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

        // Nếu không phải edit/delete thì hiển thị form thêm mới (mặc định)
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
        String code = request.getParameter("code");
        String supplierID = request.getParameter("SupplierID");
        String employeeID = request.getParameter("EmployeeID");
        String shopID_raw = request.getParameter("shopID");
        String importDateStr = request.getParameter("Date");

        if (code == null || supplierID == null || employeeID == null || shopID_raw == null || importDateStr == null) {
            request.setAttribute("erroll", "Type,Supplier,Employee,Shop,ImportDate must be not null");
            request.getRequestDispatcher("ErrolReceipt.jsp").forward(request, response);
        }
        Integer shopID = Integer.parseInt(shopID_raw);
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

        String note = request.getParameter("note");

        double value = Double.parseDouble(request.getParameter("Total"));
        String databaseName = (String) request.getSession().getAttribute("databaseName");
        try (Connection conn = new DBContext(databaseName).getConnection()) {

            ImportReceiptDAO receiptDAO = new ImportReceiptDAO(conn);
            InventoryDAO inventoryDAO = new InventoryDAO(conn);

            ImportReceiptDetailDAO receiptDetailDAO = new ImportReceiptDetailDAO(conn);

            ProductDAO productDAO = new ProductDAO(conn);
            ShopDAO shopDAO = new ShopDAO(conn);

            // Tạo đối tượng phiếu nhập
            ImportReceipt receipt = new ImportReceipt();
            receipt.setTypeID(Integer.parseInt(code)); // Tên sản phẩm không cần ở đây
            receipt.setSupplierID(Integer.parseInt(supplierID));

            receipt.setEmployeeID(Integer.parseInt(employeeID));// Tên cửa hàng không cần ở đây

            receipt.setShopID(shopID);

            receipt.setReceiptDate(importDate);
            receipt.setNote(note);
            receipt.setTotalAmount(value);

            receipt.setStatus(true);

            // Thêm phiếu nhập
            receiptDAO.insertImportReceipt(receipt);

            List<ImportReceiptDetail> listImportDetail = new ArrayList<>();
            String[] productIDs = request.getParameterValues("productID[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] prices = request.getParameterValues("price[]");
            String[] notes = request.getParameterValues("note[]");

            int size = productIDs.length;
            for (int i = 0; i < size; i++) {

                int impReId = receiptDAO.getLatestImportReceiptByID().getImportReceiptID();
                int quantity = Integer.parseInt(quantities[i]);

                double price = Double.parseDouble(prices[i]);

                ImportReceiptDetail importDetail = new ImportReceiptDetail(
                        impReId, productIDs[i], quantity, price, notes[i]);

                listImportDetail.add(importDetail);

                receiptDetailDAO.insertDetail(importDetail);

            }

            for (ImportReceiptDetail importDetail : listImportDetail) {

                // Kiểm tra và cập nhật tồn kho
                Inventory inv = inventoryDAO.getInventoryByShopAndProduct(Integer.parseInt(importDetail.getProductID()), Integer.parseInt(shopID_raw));

                if (inv != null) {

                    int newQty = inv.getQuantity() + importDetail.getQuantity();

                    inventoryDAO.updateInventoryQuantity(inv.getInventoryID(), newQty);
                    productDAO.getProductById(Integer.parseInt(importDetail.getProductID())).setImportPrice(BigDecimal.valueOf(importDetail.getPrice()));
                } else {

                    // Tạo mới hàng tồn kho nếu chưa có
                    Inventory newInv = new Inventory();

                    //newInv.setInventoryID("INV" + System.currentTimeMillis()); // ID tạm thời
                    newInv.setProduct(productDAO.getProductById(Integer.parseInt(importDetail.getProductID())));

                    newInv.setShop(shopDAO.getShopById(shopID));

                    newInv.setQuantity(importDetail.getQuantity());
                    newInv.setLastUpdated(Timestamp.from(Instant.now()));
                    inventoryDAO.insertInventory(newInv);
                    productDAO.getProductById(Integer.parseInt(importDetail.getProductID())).setImportPrice(BigDecimal.valueOf(importDetail.getPrice()));
                }
            }

            response.sendRedirect("ImportReceiptServlet?message=add_success");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi thêm phiếu nhập: " + e.getMessage());
            request.getRequestDispatcher("ImportReceiptServlet").forward(request, response);
            return;
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
