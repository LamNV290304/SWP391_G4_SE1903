
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.InventoryDAO;
import Dal.NotiDAO;
import Dal.ProductDAO;
import Dal.ShopDAO;
import Dal.TransferReceiptDAO;
import Dal.TransferReceiptDetailDAO;
import Models.Employee;
import Models.Inventory;
import Models.Noti;
import Models.Product;
import Models.Shop;
import Models.TransferReceipt;
import Models.TransferReceiptDetail;
import Utils.MailUtil;
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

    DBContext connection = new DBContext("SWP8");

    TransferReceiptDAO dao = new TransferReceiptDAO(connection.getConnection());
    ProductDAO productDAO = new ProductDAO(connection.getConnection());
    InventoryDAO inventoryDAO = new InventoryDAO(connection.getConnection());
    TransferReceiptDetailDAO transferReceiptDetailDAO = new TransferReceiptDetailDAO(connection.getConnection());
    ShopDAO shopDAO = new ShopDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO(connection.getConnection());
    NotiDAO notiDAO = new NotiDAO(connection.getConnection());
    //List
    Vector<Product> vectorProduct = productDAO.getProduct("SELECT *  FROM Product");
    List<Inventory> ListInventory = inventoryDAO.getAllInventories();
    List<Shop> ListShop = shopDAO.getAllShops("SWP8");
    Vector<TransferReceipt> list = dao.getAllTransferReceipt("SELECT * FROM TransferReceipt");
    Vector<TransferReceiptDetail> listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail");
    List<Employee> employees = employeeDAO.getAllEmployee();

    Vector<Noti> vectorNoti = notiDAO.getAllNoti("SELECT [NotiID]\n"
            + "      ,[Title]\n"
            + "      ,[Message]\n"
            + "      ,[Link]\n"
            + "      ,[ReceiverEmployeeID]\n"
            + "      ,[CreatedDate]\n"
            + "      ,[IsRead]\n"
            + "  FROM [dbo].[Noti] "
            + "Where IsRead = 0"
            + "ORDER BY [CreatedDate] DESC");

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
        log("size: " + vectorNoti.size());

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

        Vector<Noti> vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                + "Where IsRead = 0"
                + "ORDER BY [CreatedDate] DESC");
        request.setAttribute("sizeNoti", vectorNoti.size());
        vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                + "ORDER BY [CreatedDate] DESC");

        //Set data for view
        request.setAttribute("vectorNoti", vectorNoti);
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
        int TransferReceiptID = Integer.parseInt(request.getParameter("TransferReceiptID"));
        String setStatus = request.getParameter("setStatus");

        listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail WHERE TransferReceiptID = '" + TransferReceiptID + "'");
        Map<Integer, Integer> product = new HashMap<>();
        for (TransferReceiptDetail transferReceiptDetail : listDetail) {
            product.put(transferReceiptDetail.getProductID(), transferReceiptDetail.getQuantity());
        }
        TransferReceipt t = dao.searchTransferReceipt(TransferReceiptID);

        int ToShopID = t.getToShopID();

        if (t.getStatus() == 0) {
            int status = 0;
            if ("accept".equalsIgnoreCase(setStatus)) {
                status = 1;
                //setQuantity sau khi chuyen

                TransferReceipt p = dao.searchTransferReceipt(TransferReceiptID);
                List<Inventory> fromI = inventoryDAO.getAllInventoriesInStore(p.getFromShopID());
                List<Inventory> toI = inventoryDAO.getAllInventoriesInStore(p.getToShopID());

                //giam so luong o from Shop
                for (Map.Entry<Integer, Integer> entry : product.entrySet()) {
                    int key = entry.getKey();
                    int val = entry.getValue();
                    for (Inventory inventory : fromI) {
                        if (inventory.getProduct().getProductID().equals(key)) {
                            inventoryDAO.updateInventoryQuantity(inventory.getInventoryID(), inventory.getQuantity() - val);
                        }
                    }
                }
                //tang so luong o to shop
                for (Map.Entry<Integer, Integer> entry : product.entrySet()) {
                    int key = entry.getKey();
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

            //Insert Noti
            String Title = "Transfer Receipt";
            String Message = "Status của TransferReceiptID: " + t.getTransferReceiptID() + " đã " + setStatus;
            String Link = "TransferReceipt?service=listCompleteTransferReceipt";
            int ReceiverEmployeeID = 0;
            for (Employee e : employees) {
                if (e.getRoleId() == 2 && e.getShopId() == ToShopID) {
                    ReceiverEmployeeID = e.getId();
                }
            }
            int IsRead = 0;
            Noti n = new Noti(Title, Message, Link, ReceiverEmployeeID, IsRead);
            notiDAO.insertNoti(n);

        } else {
            //Insert Noti
            String Title = "Transfer Receipt";
            String Message = "Status của TransferReceiptID: " + t.getTransferReceiptID() + " đã được xử lý";
            String Link = "TransferReceipt?service=listCompleteTransferReceipt";
            int ReceiverEmployeeID = 0;
            for (Employee e : employees) {
                if (e.getRoleId() == 2 && e.getShopId() == ToShopID) {
                    ReceiverEmployeeID = e.getId();
                }
            }

            int IsRead = 0;
            Noti n = new Noti(Title, Message, Link, ReceiverEmployeeID, IsRead);
            notiDAO.insertNoti(n);
        }

        // Sau khi cập nhật, quay về danh sách
        response.sendRedirect("TransferReceipt?service=listCompleteTransferReceipt");
    }

    private void addTransferReceipt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        List<TransferReceiptDetail> ListAddToCartTransfer = (List<TransferReceiptDetail>) session.getAttribute("ListAddToCartTransfer");

        if (ListAddToCartTransfer == null) {
            ListAddToCartTransfer = new ArrayList<>();
        }
        String submit = request.getParameter("submit");
        log("day la nut submit: " + submit);
        if (submit == null) {
            log("new");
            String checkFromShopID = request.getParameter("FromShopID");
            int FromShopID = 0;
            int ToShopID = 0;

            String Note = request.getParameter("Note");
            if (checkFromShopID != null && !checkFromShopID.trim().isEmpty()) {
                FromShopID = Integer.parseInt(request.getParameter("FromShopID"));
                ListInventory = inventoryDAO.getAllInventoriesInStore(FromShopID);
                //xoa session neu select lai shop 
                if (ListAddToCartTransfer != null) {
                    ListAddToCartTransfer.clear();
                }
                session.setAttribute("ListAddToCartTransfer", ListAddToCartTransfer);
            }
            //Loc to shop
            List<Shop> ListToShop = new ArrayList<>();
            for (Shop s : ListShop) {
                if (s.getShopID() != FromShopID) {
                    ListToShop.add(s);
                }
            }

            if (request.getParameter("ToShopID") != null && !request.getParameter("ToShopID").trim().isEmpty()) {
                ToShopID = Integer.parseInt(request.getParameter("ToShopID"));
            }

            //search Product
            int searchProduct = 0;
            if (request.getParameter("searchProduct") != null && !request.getParameter("searchProduct").trim().isEmpty()) {
                searchProduct = Integer.parseInt(request.getParameter("searchProduct"));
                request.setAttribute("searchProduct", searchProduct);

            }
            if (searchProduct != 0) {
                ListInventory = inventoryDAO.getAllInventoriesInProductIDAndStoreID(searchProduct, FromShopID);

            }

            String addProduct = request.getParameter("addProduct");
            int stt = 0;
            //addProduct
            if (addProduct != null) {
                int ProductID = 0;
                if (request.getParameter("productID") != null && !request.getParameter("productID").trim().isEmpty()) {
                    ProductID = Integer.parseInt(request.getParameter("productID"));
                }

                boolean check = false;
                int T = -1;
                //check ton tai trong session ListAddToCartTransfer
                for (int i = 0; i < ListAddToCartTransfer.size(); i++) {
                    if (ListAddToCartTransfer.get(i).getProductID() == ProductID) {
                        check = true;
                        T = i;
                    }
                }
                //check quantity cua product
                int validQuantity = 0;
                for (Inventory i : ListInventory) {
                    if (i.getProduct().getProductID() == ProductID) {
                        validQuantity = i.getQuantity();
                    }
                }
                //them moi or tang quantity neu ton tai
                if (!check) {
                    int Quantity = 1;
                    stt = ListAddToCartTransfer.size() + 1;
                    TransferReceiptDetail TD = new TransferReceiptDetail(stt, ProductID, Quantity);
                    ListAddToCartTransfer.add(TD);
                } else {

                    int newQuantity = ListAddToCartTransfer.get(T).getQuantity() + 1;
                    if (newQuantity <= validQuantity) {
                        ListAddToCartTransfer.get(T).setQuantity(newQuantity);
                    } else {
                        request.setAttribute("messageQuantity", "Ko du so luong san pham");
                    }
                }

            }
            //update Quantity
            String updateQuantity = request.getParameter("updateQuantity");
            if (updateQuantity != null) {

                int ProductID = 0;
                if (request.getParameter("ProductID") != null && !request.getParameter("ProductID").trim().isEmpty()) {
                    ProductID = Integer.parseInt(request.getParameter("ProductID"));
                }
                int validQuantity = 0;
                for (Inventory i : ListInventory) {
                    if (i.getProduct().getProductID() == ProductID) {
                        validQuantity = i.getQuantity();
                    }
                }
                int Quantity = Integer.parseInt(request.getParameter("Quantity"));
                if (Quantity <= validQuantity) {
                    for (TransferReceiptDetail transferReceiptDetail : ListAddToCartTransfer) {
                        if (transferReceiptDetail.getProductID() == ProductID) {
                            transferReceiptDetail.setQuantity(Quantity);
                        }
                    }
                } else {
                    request.setAttribute("messageQuantity", "Ko du so luong san pham");
                }
            }

            //xoa trong ListAddToCartTransfer
            String remove = request.getParameter("remove");
            if (remove != null) {
                int ProductID = 0;
                if (request.getParameter("ProductID") != null && !request.getParameter("ProductID").trim().isEmpty()) {
                    ProductID = Integer.parseInt(request.getParameter("ProductID"));
                }
                for (int i = 0; i < ListAddToCartTransfer.size(); i++) {
                    if (ListAddToCartTransfer.get(i).getProductID() == ProductID) {
                        ListAddToCartTransfer.remove(i);
                    }
                }
            }
            //set view for Noti
            Vector<Noti> vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                    + "Where IsRead = 0"
                    + "ORDER BY [CreatedDate] DESC");
            request.setAttribute("sizeNoti", vectorNoti.size());
            vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                    + "ORDER BY [CreatedDate] DESC");

            request.setAttribute("listToShop", ListToShop);
            request.setAttribute("vectorNoti", vectorNoti);
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
            int FromShopID = Integer.parseInt(request.getParameter("FromShopID"));
            int ToShopID = Integer.parseInt(request.getParameter("ToShopID"));
            String Note = request.getParameter("Note");
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

                transferReceiptDetail.setTransferReceiptID(maxID);
                transferReceiptDetailDAO.insertTransferReceiptDetail(transferReceiptDetail);
            }
            //Lay Shop Name
            String FromShopName = "", ToShopName = "";
            for (Shop s : ListShop) {
                if (s.getShopID() == FromShopID) {
                    FromShopName = s.getShopName();
                } else if (s.getShopID() == ToShopID) {
                    ToShopName = s.getShopName();
                }
            }

            //Insert Noti
            String Title = "Transfer Receipt";
            String Message = "From: " + FromShopName + " To: " + ToShopName;
            String Link = "TransferReceipt?service=listProcessTransferReceipt";
            int ReceiverEmployeeID = 0;
            for (Employee e : employees) {
                if (e.getRoleId() == 2 && e.getShopId() == ToShopID) {
                    ReceiverEmployeeID = e.getId();
                    log("Test: " + ReceiverEmployeeID);

                }
            }
            int IsRead = 0;
            Noti n = new Noti(Title, Message, Link, ReceiverEmployeeID, IsRead);
            notiDAO.insertNoti(n);

            //Send Mail
            String email = "xuanhieu20012004@gmail.com";

            //Thiếu check Status, nếu status khác 0 thì trả về đã Accept
            String linkA = "http://localhost:9999/SWP391_G4_SE1903/TransferReceipt?service=updateStatus&setStatus=accept&TransferReceiptID=" + maxID;
            String linkR = "http://localhost:9999/SWP391_G4_SE1903/TransferReceipt?service=updateStatus&setStatus=reject&TransferReceiptID=" + maxID;

            String content = sendContent(FromShopName, ToShopName, Note, ListAddToCartTransfer, linkA, linkR);
            MailUtil.sendRequest(email, content);

            response.sendRedirect("TransferReceipt");
        }
    }

    private String sendContent(String FromShopID, String ToShopID, String Note, List<TransferReceiptDetail> list, String linkA, String linkR) {
        String content = "";
        content += "<h1>From: " + FromShopID + " To: " + ToShopID + " Note: " + Note + "</h1>"
                + "<h2 style=\"font-family: Arial, sans-serif; color: #333;\">Product Details</h2>\n"
                + "        <table border=\"1\" cellpadding=\"5\" cellspacing=\"0\" style=\"width: 60%; border-collapse: collapse; font-family: Arial, sans-serif;\">\n"
                + "            <thead>\n"
                + "                <tr>\n"
                + "                    <th style=\"background-color: #f2f2f2; text-align: left; padding: 8px;\">Product Name</th>\n"
                + "                    <th style=\"background-color: #f2f2f2; text-align: left; padding: 8px;\">Quantity</th>\n"
                + "                </tr>\n"
                + "            </thead>\n"
                + "            <tbody>\n";
        for (int i = 0; i < list.size(); i++) {
            int ProductID = list.get(i).getProductID();
            String ProductName = "";
            for (Product p : vectorProduct) {
                if (p.getProductID() == ProductID) {
                    ProductName = p.getProductName();
                    break;
                }
            }
            int Quantity = list.get(i).getQuantity();
            content
                    += "                <tr>\n"
                    + "                    <td style=\"padding: 8px;\">" + ProductName + "</td>\n"
                    + "                    <td style=\"padding: 8px;\">" + Quantity + "</td>\n"
                    + "                </tr>\n";

        }

        content += "            </tbody>\n"
                + "        </table>"
                + "<div style=\"margin-top: 20px;\">\n"
                + "            <a href=\"" + linkA + "\""
                + "               style=\"display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; margin-right: 10px;\">\n"
                + "                Accept\n"
                + "            </a>\n"
                + "\n"
                + "            <a href=\"" + linkR + "\""
                + "               style=\"display: inline-block; padding: 10px 20px; background-color: #f44336; color: white; text-decoration: none; border-radius: 5px;\">\n"
                + "                Reject\n"
                + "            </a>\n"
                + "        </div>";

        return content;
    }

    private void Detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String TransferReceiptID = request.getParameter("TransferReceiptID");
        log(TransferReceiptID);
        listDetail = transferReceiptDetailDAO.getAllTransferReceiptDetail("SELECT * FROM TransferReceiptDetail WHERE TransferReceiptID = '" + TransferReceiptID + "'");

        Vector<Noti> vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                + "Where IsRead = 0"
                + "ORDER BY [CreatedDate] DESC");
        request.setAttribute("sizeNoti", vectorNoti.size());
        vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                + "ORDER BY [CreatedDate] DESC");

        request.setAttribute("vectorNoti", vectorNoti);
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
        Vector<Noti> vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                + "Where IsRead = 0"
                + "ORDER BY [CreatedDate] DESC");
        request.setAttribute("sizeNoti", vectorNoti.size());
        vectorNoti = notiDAO.getAllNoti("SELECT * FROM [dbo].[Noti] "
                + "ORDER BY [CreatedDate] DESC");

        request.setAttribute("vectorNoti", vectorNoti);
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
