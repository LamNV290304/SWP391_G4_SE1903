/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.InventoryDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.TransferReceiptDAO;
import Dal.TransferReceiptDetailDAO;
import Models.Inventory;
import Models.Product;
import Models.Shop;
import Models.TransferReceipt;
import Models.TransferReceiptDetail;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "TransferReceipt", urlPatterns = {"/TransferReceipt"})
public class TransferReceiptController extends HttpServlet {
    
    DBContext connection = new DBContext("SWP6  ");
    TransferReceiptDAO dao = new TransferReceiptDAO(connection.getConnection());
    ProductDAO productDAO = new ProductDAO(connection.getConnection());
    InventoryDAO inventoryDAO = new InventoryDAO(connection.getConnection());
    TransferReceiptDetailDAO transferReceiptDetailDAO = new TransferReceiptDetailDAO(connection.getConnection());
    ShopDAO shopDAO = new ShopDAO();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String SQLStatusZero = "SELECT * FROM TransferReceipt WHERE Status = 0";
    private static final String SQLStatusNotZero = "SELECT * FROM TransferReceipt WHERE Status != 0";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        
        Vector<Product> vectorProduct = null;//= productDAO.getProduct("SELECT *  FROM Product");
        List<Inventory> ListInventory = inventoryDAO.getAllInventories();
        List<Shop> ListShop = shopDAO.getAllShops("SWP6");
        HttpSession session = request.getSession(true);
        Vector<TransferReceipt> list = dao.getAllTransferReceipt("SELECT * FROM TransferReceipt");
        Vector<TransferReceiptDetail> listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail");
        List<TransferReceiptDetail> ListAddToCartTransfer = (List<TransferReceiptDetail>) session.getAttribute("ListAddToCartTransfer");
        if (ListAddToCartTransfer == null) {
            ListAddToCartTransfer = new ArrayList<>();
        }
        String service = request.getParameter("service");

        if (service == null) {
            service = "listProcessTransferReceipt";
        }
        if (service.equals("deleteTransferReceipt")) {
            String TransferReceiptID = request.getParameter("TransferReceiptID");
            for (TransferReceiptDetail transferReceiptDetail : listDetail) {
                if (transferReceiptDetail.getTransferReceiptID().equals(TransferReceiptID)) {
                    int n = transferReceiptDetailDAO.deleteTransferReceiptDetail(transferReceiptDetail.getTransferReceiptDetailID());
                }
            }
            int n = dao.deleteTransferReceipt(TransferReceiptID);
            response.sendRedirect("TransferReceipt");
        }

//        if (service.equals("updateTransferReceipt")) {
//            String submit = request.getParameter("submit");
//            if (submit == null) {
//                String TransferReceiptID = request.getParameter("TransferReceiptID");
//                TransferReceipt p = dao.searchTransferReceipt(TransferReceiptID);
//                ListInventory = inventoryDAO.getAllInventoriesInProduct(p.getProductID());
//                request.setAttribute("vectorP", vectorProduct);
//                request.setAttribute("vectorI", ListInventory);
//                request.setAttribute("p", p);
//                request.getRequestDispatcher("TransferReceiptJSP/UpdateTransfer.jsp").forward(request, response);
//            } else {
//                String TransferReceiptID = request.getParameter("TransferReceiptID"),
//                        ProductID = request.getParameter("ProductID"),
//                        FromInventoryID = request.getParameter("FromInventoryID"),
//                        ToInventoryID = request.getParameter("ToInventoryID");
//                int Quantity = Integer.parseInt(request.getParameter("Quantity"));
//                Date TransferDate = Date.valueOf(request.getParameter("TransferDate"));
//                String Note = request.getParameter("Note");
//                int Status = 0;
//                TransferReceipt p = new TransferReceipt(TransferReceiptID, FromInventoryID, ToInventoryID, TransferDate, Note, Status);
//
//                dao.updateTransferReceipt(p);
//                response.sendRedirect("TransferReceipt");
//            }
//        }
//
        if (service.equals("updateStatus")) {
            String TransferReceiptID = request.getParameter("TransferReceiptID");
            String setStatus = request.getParameter("setStatus");

            listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail WHERE TransferReceiptID = '" + TransferReceiptID + "'");
            Map<String, Integer> product = new HashMap<>();
            for (TransferReceiptDetail transferReceiptDetail : listDetail) {
                product.put(transferReceiptDetail.getProductID(), transferReceiptDetail.getQuantity());
            }

            int status = 0;
            if ("accept".equalsIgnoreCase(setStatus)) {
                status = 1;
                //setQuantity sau khi chuyen

                TransferReceipt p = dao.searchTransferReceipt(TransferReceiptID);
                List<Inventory> fromI = inventoryDAO.getAllInventoriesInStore(p.getFromShopID());
                List<Inventory> toI = inventoryDAO.getAllInventoriesInStore(p.getToShopID());

                //giam so luong o from Shop
                for (Map.Entry<String, Integer> entry : product.entrySet()) {
                    String key = entry.getKey();
                    int val = entry.getValue();
                    for (Inventory inventory : fromI) {
                        if (inventory.getProduct().getProductID().equals(key)) {
                            log(inventory.getProduct().getProductID());
                            inventoryDAO.updateInventoryQuantity(inventory.getInventoryID(), inventory.getQuantity() - val);
                        }
                    }
                }
                //tang so luong o to shop
                for (Map.Entry<String, Integer> entry : product.entrySet()) {
                    String key = entry.getKey();
                    int val = entry.getValue();
                    for (Inventory inventory : toI) {
                        if (inventory.getProduct().getProductID().equals(key)) {
                            inventoryDAO.updateInventoryQuantity(inventory.getInventoryID(), inventory.getQuantity() + val);
                        }
                    }
                }
                
            } else if ("reject".equalsIgnoreCase(setStatus)) {
                status = 2;
            }

            dao.updateTransferReceiptStatus(TransferReceiptID, status);

            // Sau khi cập nhật, quay về danh sách
            response.sendRedirect("TransferReceipt");
        }

        if (service.equals("addTransferReceipt")) {
            String submit = request.getParameter("submit");

            if (submit == null) {

                String action = request.getParameter("action");
                String FromShopID = request.getParameter("FromShopID");
                String ToShopID = request.getParameter("ToShopID");
                String Note = request.getParameter("Note");
                if (FromShopID != null) {
                    ListInventory = inventoryDAO.getAllInventoriesInStore(FromShopID);
                }

                String addProduct = request.getParameter("addProduct");

                if (addProduct != null) {
                    String ProductID = request.getParameter("productID");
                    boolean check = false;
                    int T = -1;
                    for (int i = 0; i < ListAddToCartTransfer.size(); i++) {
                        if (ListAddToCartTransfer.get(i).getProductID().equalsIgnoreCase(ProductID)) {
                            check = true;
                            T = i;
                        }
                    }
                    if (!check) {
                        int Quantity = 1;
                        int maxID = 0;
                        for (TransferReceipt tr : list) {
                            String id = tr.getTransferReceiptID().replace("T00", "");
                            int number = Integer.parseInt(id);
                            if (number > maxID) {
                                maxID = number;
                            }
                        }
                        String TransferReceiptID = "T00" + (maxID + 1);

                        int TransferReceiptDetailID = -1;
                        if (ListAddToCartTransfer.size() == 0) {
                            TransferReceiptDetailID = listDetail.size() + 1;
                        } else {
                            maxID = 0;
                            for (TransferReceiptDetail transferReceiptDetail : ListAddToCartTransfer) {
                                int number = transferReceiptDetail.getTransferReceiptDetailID();
                                if (number > maxID) {
                                    maxID = number;
                                }

                            }
                            TransferReceiptDetailID = maxID + 1;
                        }
                        TransferReceiptDetail TD = new TransferReceiptDetail(TransferReceiptDetailID, TransferReceiptID, ProductID, Quantity);

                        ListAddToCartTransfer.add(TD);
                    } else {
                        int newQuantity = ListAddToCartTransfer.get(T).getQuantity() + 1;
                        ListAddToCartTransfer.get(T).setQuantity(newQuantity);
                    }

                }

                String updateQuantity = request.getParameter("updateQuantity");
                if (updateQuantity != null) {
                    int TransferReceiptDetailID = Integer.parseInt(request.getParameter("TransferReceiptDetailID"));
                    int Quantity = Integer.parseInt(request.getParameter("Quantity"));
                    for (TransferReceiptDetail transferReceiptDetail : ListAddToCartTransfer) {
                        if (transferReceiptDetail.getTransferReceiptDetailID() == TransferReceiptDetailID) {
                            transferReceiptDetail.setQuantity(Quantity);
                        }
                    }
                }
                String remove = request.getParameter("remove");
                if (remove != null) {
                    int TransferReceiptDetailID = Integer.parseInt(request.getParameter("TransferReceiptDetailID"));
                    for (int i = 0; i < ListAddToCartTransfer.size(); i++) {
                        if (ListAddToCartTransfer.get(i).getTransferReceiptDetailID() == TransferReceiptDetailID) {
                            ListAddToCartTransfer.remove(i);
                        }
                    }
                }
                request.setAttribute("toShopSelect", ToShopID);
                request.setAttribute("Note", Note);
                request.setAttribute("select", FromShopID);
                session.setAttribute("ListAddToCartTransfer", ListAddToCartTransfer);
                request.setAttribute("listShop", ListShop);
                request.setAttribute("vectorP", vectorProduct);
                request.setAttribute("vectorI", ListInventory);
                request.getRequestDispatcher("TransferReceiptJSP/AddTransferReceipt.jsp").forward(request, response);
            } else {
                String FromShopID = request.getParameter("FromShopID");
                String Note = request.getParameter("Note");
                String ToShopID = request.getParameter("ToShopID");
                String TransferReceiptID = "";
                if (!ListAddToCartTransfer.isEmpty()) {
                    TransferReceiptID = ListAddToCartTransfer.get(1).getTransferReceiptID();
                }
                
                java.util.Date TransferDate = new java.util.Date();
                int Status = 0;
                TransferReceipt T = new TransferReceipt(TransferReceiptID, FromShopID, ToShopID, TransferDate, Note, Status);
                
                dao.insertTransferReceipt(T);
                for (TransferReceiptDetail transferReceiptDetail : ListAddToCartTransfer) {
                    log(transferReceiptDetail.getProductID());
                    transferReceiptDetailDAO.insertTransferReceiptDetail(transferReceiptDetail);
                }

                response.sendRedirect("TransferReceipt");
            }
        }

        if (service.equals("Detail")) {
            String TransferReceiptID = request.getParameter("TransferReceiptID");
            log(TransferReceiptID);
            listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail WHERE TransferReceiptID = '" + TransferReceiptID + "'");
            for (TransferReceiptDetail transferReceiptDetail : listDetail) {
                log(transferReceiptDetail.getTransferReceiptID());
            }
            request.setAttribute("listDetail", listDetail);
            request.getRequestDispatcher("TransferReceiptJSP/ListTransferReceiptDetail.jsp").forward(request, response);

        }

        if (service.equals("listCompleteTransferReceipt")) {
            String submit = request.getParameter("submit");
            //Call Models

            if (submit == null) {
                list = dao.getAllTransferReceipt(SQLStatusNotZero);
            } else {
                String name = request.getParameter("search");
                list = dao.getAllTransferReceipt("SELECT * \n"
                        + "FROM TransferReceipt t \n"
                        + "JOIN Product p ON t.ProductID = p.ProductID\n"
                        + "JOIN Inventory fromI ON t.FromInventoryID = fromI.InventoryID\n"
                        + "Join Inventory toI ON t.ToInventoryID = toI.InventoryID\n"
                        + "join Shop fromS ON fromI.ShopID = fromS.ShopID\n"
                        + "Join Shop toS ON toI.ShopID = toS.ShopID\n"
                        + "WHERE t.Status = 0\n"
                        + "AND(t.TransferReceiptID LIKE CONCAT('%','" + name + "', '%') \n"
                        + "OR p.ProductName LIKE CONCAT('%','" + name + "', '%') \n"
                        + "OR toS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                        + "OR fromS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                        + ")"
                );

            }
            //Set data for view
            request.setAttribute("vectorS", ListShop);
            request.setAttribute("data", list);
            request.setAttribute("pageTitle", "TransferReceipt Manager");
            request.setAttribute("tableTitle", "List of TransferReceipt");
            //Select view
            request.getRequestDispatcher("TransferReceiptJSP/ListCompleteTransferReceipt.jsp").forward(request, response);

        }

        if (service.equals("listProcessTransferReceipt")) {
            String submit = request.getParameter("submit");
            //Call Models

            if (submit == null) {
                list = dao.getAllTransferReceipt(SQLStatusZero);
            } else {
                String name = request.getParameter("search");
                list = dao.getAllTransferReceipt("SELECT * \n"
                        + "FROM TransferReceipt t \n"
                        + "JOIN Product p ON t.ProductID = p.ProductID\n"
                        + "JOIN Inventory fromI ON t.FromInventoryID = fromI.InventoryID\n"
                        + "Join Inventory toI ON t.ToInventoryID = toI.InventoryID\n"
                        + "join Shop fromS ON fromI.ShopID = fromS.ShopID\n"
                        + "Join Shop toS ON toI.ShopID = toS.ShopID\n"
                        + "WHERE t.Status = 0\n"
                        + "AND(t.TransferReceiptID LIKE CONCAT('%','" + name + "', '%') \n"
                        + "OR p.ProductName LIKE CONCAT('%','" + name + "', '%') \n"
                        + "OR toS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                        + "OR fromS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                        + ")"
                );

            }
            //Set data for view
            request.setAttribute("vectorS", ListShop);
            request.setAttribute("data", list);
            request.setAttribute("pageTitle", "TransferReceipt Manager");
            request.setAttribute("tableTitle", "List of TransferReceipt");
            //Select view
            request.getRequestDispatcher("TransferReceiptJSP/ListTransferReceipt.jsp").forward(request, response);

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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TransferReceiptController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TransferReceiptController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
