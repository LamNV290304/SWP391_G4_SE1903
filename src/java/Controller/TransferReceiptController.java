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


    DBContext connection = new DBContext("SWP7  ");

    TransferReceiptDAO dao = new TransferReceiptDAO(connection.getConnection());
    ProductDAO productDAO = new ProductDAO(connection.getConnection());
    InventoryDAO inventoryDAO = new InventoryDAO(connection.getConnection());
    TransferReceiptDetailDAO transferReceiptDetailDAO = new TransferReceiptDetailDAO(connection.getConnection());
    ShopDAO shopDAO = new ShopDAO();


    //List
    Vector<Product> vectorProduct = productDAO.getProduct("SELECT *  FROM Product");
    List<Inventory> ListInventory = inventoryDAO.getAllInventories();
    List<Shop> ListShop = shopDAO.getAllShops("SWP7");
    Vector<TransferReceipt> list = dao.getAllTransferReceipt("SELECT * FROM TransferReceipt");
    Vector<TransferReceiptDetail> listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail");

    private static final String SQLStatusZero = "SELECT * FROM TransferReceipt WHERE Status = 0";
    private static final String SQLStatusNotZero = "SELECT * FROM TransferReceipt WHERE Status != 0";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String service = request.getParameter("service");

        if (service == null) {
            listProcessTransferReceipt(request, response);
        } else {
            switch (service) {
                case "listProcessTransferReceipt":
                    listProcessTransferReceipt(request, response);
                    break;
                case "deleteTransferReceipt":
                    deleteTransferReceipt(request, response);
                    break;
                case "updateStatus":
                    updateStatus(request, response);
                    break;
                case "addTransferReceipt":
                    addTransferReceipt(request, response);
                    break;
                case "Detail":
                    Detail(request, response);
                    break;
                case "listCompleteTransferReceipt":
                    listCompleteTransferReceipt(request, response);
                    break;
            }
        }

    }

    private void listProcessTransferReceipt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String submit = request.getParameter("submit");
        int page = 1;
        int select = 1;
        Vector<TransferReceipt> selectList = new Vector<>();
        //Call Models

        if (submit == null) {
            list = dao.getAllTransferReceipt(SQLStatusZero);
            //check có bn page
            if (list.size() % 5 == 0) {
                page = list.size() / 5;
            } else {
                page = list.size() / 5 + 1;
            }
            //Select Page
            if (request.getParameter("selectPage") != null) {
                select = Integer.parseInt(request.getParameter("selectPage"));
            }
            int start = (select - 1) * 5;
            int end = Math.min(select * 5, list.size());

            for (int i = start; i < end; i++) {
                selectList.add(list.get(i));
            }
        } else {
            String name = request.getParameter("search");
            list = dao.getAllTransferReceipt("SELECT * FROM TransferReceipt t \n"
                    + "join Shop fromS ON t.FromShopID = fromS.ShopID\n"
                    + "Join Shop toS ON t.ToShopID = toS.ShopID\n"
                    + "WHERE t.Status = 0\n"
                    + "AND(t.TransferReceiptID LIKE CONCAT('%','" + name + "', '%')\n"
                    + "OR toS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                    + "OR fromS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                    + ")"
            );
            //check có bn page
            if (list.size() % 5 == 0) {
                page = list.size() / 5;
            } else {
                page = list.size() / 5 + 1;
            }
            //Select Page
            if (request.getParameter("selectPage") != null) {
                select = Integer.parseInt(request.getParameter("selectPage"));
            }
            int start = (select - 1) * 5;
            int end = Math.min(select * 5, list.size());

            for (int i = start; i < end; i++) {
                selectList.add(list.get(i));
            }
            request.setAttribute("currentSearch", name);
        }

        //XOA ListAddToCartTransfer khi back lai page list proccess
        HttpSession session = request.getSession(true);
        List<TransferReceiptDetail> ListAddToCartTransfer = (List<TransferReceiptDetail>) session.getAttribute("ListAddToCartTransfer");
        if (ListAddToCartTransfer != null) {
            ListAddToCartTransfer.clear();
        }
        session.setAttribute("ListAddToCartTransfer", ListAddToCartTransfer);

        //Set data for view
        request.setAttribute("currentPage", select);
        request.setAttribute("page", page);
        request.setAttribute("vectorS", ListShop);
        request.setAttribute("data", selectList);
        request.setAttribute("pageTitle", "TransferReceipt Manager");
        request.setAttribute("tableTitle", "List of TransferReceipt");
        //Select view
        request.getRequestDispatcher("TransferReceiptJSP/ListTransferReceipt.jsp").forward(request, response);

    }

    private void deleteTransferReceipt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int TransferReceiptID = Integer.parseInt(request.getParameter("TransferReceiptID"));
        for (TransferReceiptDetail transferReceiptDetail : listDetail) {
            if (transferReceiptDetail.getTransferReceiptID() == TransferReceiptID) {
                int n = transferReceiptDetailDAO.deleteTransferReceiptDetail(transferReceiptDetail.getTransferReceiptDetailID());
            }
        }
        int n = dao.deleteTransferReceipt(TransferReceiptID);
        response.sendRedirect("TransferReceipt");
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int TransferReceiptID = Integer.parseInt(request.getParameter("TransferReceiptID")) ;
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

    private void addTransferReceipt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        List<TransferReceiptDetail> ListAddToCartTransfer = (List<TransferReceiptDetail>) session.getAttribute("ListAddToCartTransfer");

        if (ListAddToCartTransfer == null) {
            ListAddToCartTransfer = new ArrayList<>();
        }
        String submit = request.getParameter("submit");

        if (submit == null) {

            String action = request.getParameter("action");
            String FromShopID = request.getParameter("FromShopID");
            String ToShopID = request.getParameter("ToShopID");
            String Note = request.getParameter("Note");
            if (FromShopID != null) {
                ListInventory = inventoryDAO.getAllInventoriesInStore(FromShopID);
            }

            //search Product
            String searchProduct = request.getParameter("searchProduct");
            String search = request.getParameter("search");
            if (search != null) {
//                ListInventory = inventoryDAO.getInventoryByShopAndProduct(searchProduct, FromShopID);

            } else {
//                vectorProduct = productDAO.getProduct("SELECT *  FROM Product");
            }
            request.setAttribute("searchProduct", searchProduct);

            String addProduct = request.getParameter("addProduct");
            int stt = 0;
            //addProduct
            if (addProduct != null) {
                String ProductID = request.getParameter("productID");
                boolean check = false;
                int T = -1;
                //check ton tai trong session ListAddToCartTransfer
                for (int i = 0; i < ListAddToCartTransfer.size(); i++) {
                    if (ListAddToCartTransfer.get(i).getProductID().equalsIgnoreCase(ProductID)) {
                        check = true;
                        T = i;
                    }
                }
                //them moi or tang quantity neu ton tai
                if (!check) {
                    int Quantity = 1;

                    TransferReceiptDetail TD = new TransferReceiptDetail(stt++, ProductID, Quantity);
                    log("Detail : " + TD.getTransferReceiptID());
                    ListAddToCartTransfer.add(TD);
                } else {
                    int newQuantity = ListAddToCartTransfer.get(T).getQuantity() + 1;
                    ListAddToCartTransfer.get(T).setQuantity(newQuantity);
                }

            }
            //update Quantity
            String updateQuantity = request.getParameter("updateQuantity");
            if (updateQuantity != null) {
                String ProductID = request.getParameter("ProductID");
                int Quantity = Integer.parseInt(request.getParameter("Quantity"));
                for (TransferReceiptDetail transferReceiptDetail : ListAddToCartTransfer) {
                    if (transferReceiptDetail.getProductID().equalsIgnoreCase(ProductID)) {
                        transferReceiptDetail.setQuantity(Quantity);
                    }
                }
            }

            //xoa trong ListAddToCartTransfer
            String remove = request.getParameter("remove");
            if (remove != null) {
                String ProductID = request.getParameter("ProductID");
                for (int i = 0; i < ListAddToCartTransfer.size(); i++) {
                    if (ListAddToCartTransfer.get(i).getProductID().equalsIgnoreCase(ProductID)) {
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
        } //submit để add TransferReceipt
        else {
            String FromShopID = request.getParameter("FromShopID");
            String Note = request.getParameter("Note");
            String ToShopID = request.getParameter("ToShopID");

            java.util.Date TransferDate = new java.util.Date();
            int Status = 0;
            TransferReceipt T = new TransferReceipt(FromShopID, ToShopID, TransferDate, Note, Status);

            dao.insertTransferReceipt(T);
            list = dao.getAllTransferReceipt(SQLStatusZero);
            int maxID = 0;
            for (TransferReceipt transferReceipt : list) {
                if (transferReceipt.getTransferReceiptID() > maxID) {
                    maxID = transferReceipt.getTransferReceiptID();
                }
            }
            for (TransferReceiptDetail transferReceiptDetail : ListAddToCartTransfer) {
                
                log(transferReceiptDetail.getProductID());
                
                transferReceiptDetail.setTransferReceiptID(maxID);
                transferReceiptDetailDAO.insertTransferReceiptDetail(transferReceiptDetail);
            }

            response.sendRedirect("TransferReceipt");
        }
    }

    private void Detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String TransferReceiptID = request.getParameter("TransferReceiptID");
        log(TransferReceiptID);
        listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail WHERE TransferReceiptID = '" + TransferReceiptID + "'");
        for (TransferReceiptDetail transferReceiptDetail : listDetail) {
            log(transferReceiptDetail.getProductID());
        }
        for (Product transferReceiptDetail : vectorProduct) {
            log(transferReceiptDetail.getProductID());
        }
        request.setAttribute("vectorP", vectorProduct);
        request.setAttribute("listDetail", listDetail);
        request.getRequestDispatcher("TransferReceiptJSP/ListTransferReceiptDetail.jsp").forward(request, response);

    }

    private void listCompleteTransferReceipt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String submit = request.getParameter("submit");
        int page = 1;
        int select = 1;
        Vector<TransferReceipt> selectList = new Vector<>();
        //Call Models

        if (submit == null) {
            list = dao.getAllTransferReceipt(SQLStatusNotZero);
            //check có bn page
            if (list.size() % 5 == 0) {
                page = list.size() / 5;
            } else {
                page = list.size() / 5 + 1;
            }
            //Select Page
            if (request.getParameter("selectPage") != null) {
                select = Integer.parseInt(request.getParameter("selectPage"));
            }
            int start = (select - 1) * 5;
            int end = Math.min(select * 5, list.size());

            for (int i = start; i < end; i++) {
                selectList.add(list.get(i));
            }

        } else {
            String name = request.getParameter("search");
            list = dao.getAllTransferReceipt("SELECT * FROM TransferReceipt t \n"
                    + "join Shop fromS ON t.FromShopID = fromS.ShopID\n"
                    + "Join Shop toS ON t.ToShopID = toS.ShopID\n"
                    + "WHERE t.Status != 0\n"
                    + "AND(t.TransferReceiptID LIKE CONCAT('%','" + name + "', '%')\n"
                    + "OR toS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                    + "OR fromS.ShopName LIKE CONCAT('%','" + name + "', '%')\n"
                    + ")"
            );
            //check có bn page
            if (list.size() % 5 == 0) {
                page = list.size() / 5;
            } else {
                page = list.size() / 5 + 1;
            }
            //Select Page
            if (request.getParameter("selectPage") != null) {
                select = Integer.parseInt(request.getParameter("selectPage"));
            }
            int start = (select - 1) * 5;
            int end = Math.min(select * 5, list.size());

            for (int i = start; i < end; i++) {
                selectList.add(list.get(i));
            }
            request.setAttribute("currentSearch", name);
        }
        //Set data for view
        request.setAttribute("currentPage", select);
        request.setAttribute("page", page);
        request.setAttribute("vectorS", ListShop);
        request.setAttribute("data", selectList);
        request.setAttribute("pageTitle", "TransferReceipt Manager");
        request.setAttribute("tableTitle", "List of TransferReceipt");
        //Select view
        request.getRequestDispatcher("TransferReceiptJSP/ListCompleteTransferReceipt.jsp").forward(request, response);

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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TransferReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
