package Dal;

import Context.DBContext;
import DTO.SoldProductDetailDto;
import Models.Inventory;
import Models.InvoiceDetail;
import Models.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duckh
 */
public class InvoiceDetailDAO {

    private Connection connection;

    public InvoiceDetailDAO(Connection connection) {
        this.connection = connection;
    }

    public List<InvoiceDetail> getAllInvoiceDetail() {
        String sql = "SELECT [InvoiceDetailID]\n"
                + "      ,[InvoiceID]\n"
                + "      ,[ProductID]\n"
                + "      ,[UnitPrice]\n"
                + "      ,[Quantity]\n"
                + "      ,[Discount]\n"
                + "      ,[TotalPrice]\n"
                + "  FROM [dbo].[InvoiceDetail]";
        List<InvoiceDetail> list = new ArrayList<>();
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(new InvoiceDetail(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getDouble(6)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean addInvoiceDetailAndUpdateInventory(InvoiceDetail detail, int shopID) {
        InventoryDAO iDAO = new InventoryDAO(connection);

        Inventory inventory = iDAO.getInventoryByShopAndProduct(detail.getProductID(), shopID);
        if (inventory == null) {
            System.out.println("Không tìm thấy tồn kho với ProductID = " + detail.getProductID() + " và ShopID = " + shopID);
            return false;
        }
        int currentQuantity = inventory.getQuantity();
        if (currentQuantity < detail.getQuantity()) {
            System.out.println("Tồn kho không đủ! Hiện tại có " + currentQuantity);
            return false;
        }
        InvoiceDetail existingDetail = getInvoiceDetailByInvoiceIDAndProductID(detail.getInvoiceID(), detail.getProductID());
        if (existingDetail != null) {

            detail.setInvoiceDetailID(existingDetail.getInvoiceDetailID());
            detail.setShopID(shopID);

            detail.setQuantity(existingDetail.getQuantity() + detail.getQuantity());
            detail.calculateTotalPrice();

            return updateInvoiceDetail(detail, iDAO);
        }
        String sql = "INSERT INTO InvoiceDetail (InvoiceID, ProductID, UnitPrice, Quantity, Discount, TotalPrice)"
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, detail.getInvoiceID());
            ptm.setInt(2, detail.getProductID());
            ptm.setBigDecimal(3, detail.getUnitPrice());
            ptm.setInt(4, detail.getQuantity());
            ptm.setDouble(5, detail.getDiscount());
            ptm.setDouble(6, detail.getTotalPrice());

            ptm.executeUpdate();

            int updatedInventory = currentQuantity - detail.getQuantity();
            boolean updateInven = iDAO.updateInventoryQuantity(inventory.getInventoryID(), updatedInventory);
            return updateInven;

        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public InvoiceDetail getInvoiceDetailByInvoiceIDAndProductID(int invoiceID, int productID) {
        String sql = "SELECT [InvoiceDetailID]\n"
                + "      ,[InvoiceID]\n"
                + "      ,[ProductID]\n"
                + "      ,[UnitPrice]\n"
                + "      ,[Quantity]\n"
                + "      ,[Discount]\n"
                + "      ,[TotalPrice]\n"
                + "  FROM [dbo].[InvoiceDetail]\n"
                + "	WHERE InvoiceID = ? AND ProductID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setInvoiceDetailID(rs.getInt("InvoiceDetailID"));
                detail.setInvoiceID(rs.getInt("InvoiceID"));
                detail.setProductID(rs.getInt("ProductID"));
                detail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setDiscount(rs.getDouble("Discount"));

                return detail;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InvoiceDetail getInvoiceDetailByInvoiceDetailID(int invoiceDetailID) {
        String sql = "SELECT iv.InvoiceDetailID\n"
                + "      ,iv.InvoiceID\n"
                + "      ,iv.ProductID\n"
                + "      ,iv.UnitPrice\n"
                + "	  ,i.ShopID\n"
                + "      ,iv.Quantity\n"
                + "      ,iv.Discount\n"
                + "      ,TotalPrice\n"
                + "  FROM [dbo].[InvoiceDetail] iv  join Invoice i on iv.InvoiceID =  i.InvoiceID\n"
                + "  Where [InvoiceDetailID] = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceDetailID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                InvoiceDetail i = new InvoiceDetail(
                        rs.getInt("InvoiceDetailID"),
                        rs.getInt("InvoiceID"),
                        rs.getInt("ProductID"),
                        rs.getBigDecimal("UnitPrice"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Discount")
                );
                i.setShopID(rs.getInt("ShopID"));
                return i;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<InvoiceDetail> getDetailByInvoiceID(int invoiceID) {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT [InvoiceDetailID]\n"
                + "      ,[InvoiceID]\n"
                + "      ,[ProductID]\n"
                + "      ,[UnitPrice]\n"
                + "      ,[Quantity]\n"
                + "      ,[Discount]\n"
                + "      ,[TotalPrice]\n"
                + "  FROM [dbo].[InvoiceDetail]\n"
                + "  WHERE InvoiceID = ?";

        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                InvoiceDetail i = new InvoiceDetail(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getInt(5), rs.getDouble(6));
                list.add(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean updateInvoiceDetail(InvoiceDetail newDetail, InventoryDAO inventoryDAO) {
        try {
            InvoiceDetail oldDetail = getInvoiceDetailByInvoiceDetailID(newDetail.getInvoiceDetailID());
            if (oldDetail == null) {
                throw new Exception("Không tìm thấy InvoiceDetail để sửa");
            }
            if (newDetail.getQuantity() == 0) {
                return deleteByDetailID(newDetail.getInvoiceDetailID());
            }
            boolean productChanged = oldDetail.getProductID() != (newDetail.getProductID());
            if (productChanged) {

                Inventory oldInventory = inventoryDAO.getInventoryByShopAndProduct(oldDetail.getProductID(), oldDetail.getShopID());

                if (oldInventory == null) {
                    throw new Exception("Không tìm thấy tồn kho cũ");
                }
                int restoredQty = oldInventory.getQuantity() + oldDetail.getQuantity();
                if (!inventoryDAO.updateInventoryQuantity(oldInventory.getInventoryID(), restoredQty)) {
                    throw new Exception("Cập nhật tồn kho cũ thất bại");
                }

                Inventory newInventory = inventoryDAO.getInventoryByShopAndProduct(newDetail.getProductID(), newDetail.getShopID());
                if (newInventory == null) {
                    throw new Exception("Không tìm thấy tồn kho mới");
                }
                if (newInventory.getQuantity() < newDetail.getQuantity()) {
                    throw new Exception("Không đủ tồn kho cho sản phẩm mới");
                }

                int newQty = newInventory.getQuantity() - newDetail.getQuantity();
                if (!inventoryDAO.updateInventoryQuantity(newInventory.getInventoryID(), newQty)) {
                    throw new Exception("Cập nhật tồn kho mới thất bại");
                }

            } else {

                Inventory inventory = inventoryDAO.getInventoryByShopAndProduct(newDetail.getProductID(), newDetail.getShopID());
                if (inventory == null) {
                    throw new Exception("Không tìm thấy tồn kho");
                }
                int diff = newDetail.getQuantity() - oldDetail.getQuantity();
                if (diff > 0 && inventory.getQuantity() < diff) {
                    throw new Exception("Không đủ tồn kho để tăng số lượng");
                }
                int updatedQty = inventory.getQuantity() - diff;
                if (!inventoryDAO.updateInventoryQuantity(inventory.getInventoryID(), updatedQty)) {
                    throw new Exception("Cập nhật tồn kho thất bại");
                }
            }

            newDetail.calculateTotalPrice();
            String sql = "UPDATE InvoiceDetail SET InvoiceID=?, ProductID=?, UnitPrice=?, Quantity=?, Discount=?, TotalPrice=? WHERE InvoiceDetailID=?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, newDetail.getInvoiceID());
                ps.setInt(2, newDetail.getProductID());
                ps.setBigDecimal(3, newDetail.getUnitPrice());
                ps.setInt(4, newDetail.getQuantity());
                ps.setDouble(5, newDetail.getDiscount());
                ps.setDouble(6, newDetail.getTotalPrice());
                ps.setInt(7, newDetail.getInvoiceDetailID());

                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật chi tiết hóa đơn:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByInvoiceID(int invoiceID) {
        String sql = "DELETE FROM [dbo].[InvoiceDetail] WHERE InvoiceID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, invoiceID);
            return ptm.executeUpdate() >= 0;
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteByDetailID(int invoiceDetailID) {
        String sql = "DELETE FROM [dbo].[InvoiceDetail]\n"
                + "      WHERE InvoiceDetailID=?";
        InvoiceDetail detail = getInvoiceDetailByInvoiceDetailID(invoiceDetailID);
        InventoryDAO iDAO = new InventoryDAO(connection);
        if (detail != null) {
            Inventory inventory = iDAO.getInventoryByShopAndProduct(detail.getProductID(), detail.getShopID());
            if (inventory == null) {
                return false;
            }
            int restoredQuantity = inventory.getQuantity() + detail.getQuantity();
            boolean updateInventory = iDAO.updateInventoryQuantity(inventory.getInventoryID(), restoredQuantity);
            if (!updateInventory) {
                return false;
            }
        }
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, invoiceDetailID);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public BigDecimal getTotalSaleRevenueByShop(int shopId, Date startDate, Date endDate) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        String sql = "SELECT SUM(id.Quantity * id.UnitPrice) AS TotalSaleRevenue "
                + "FROM InvoiceDetail id "
                + "JOIN Invoice i ON id.InvoiceID = i.ID "
                + "JOIN Employee e ON i.EmployeeID = e.ID "
                + "WHERE e.Role = 'Sale' AND e.ShopID = ? "
                + "AND i.InvoiceDate BETWEEN ? AND ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ps.setDate(2, new java.sql.Date(startDate.getTime()));
            ps.setDate(3, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getBigDecimal("TotalSaleRevenue");
                    if (total == null) {
                        total = BigDecimal.ZERO;
                    }
                }
            }
        }
        return total;
    }

    public int getTotalSoldProductsByShop(int shopID) {
        String sql = "SELECT SUM(id.Quantity) "
                + "FROM InvoiceDetail id "
                + "JOIN Invoice i ON id.InvoiceID = i.InvoiceID "
                + "WHERE i.ShopID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BigDecimal getTotalRevenueByEmployee(int employeeId) throws SQLException {
        String sql = "SELECT SUM(ID.UnitPrice * ID.Quantity) AS TotalRevenue "
                + "FROM InvoiceDetail ID "
                + "JOIN Invoice I ON ID.InvoiceID = I.InvoiceID "
                + "WHERE I.EmployeeID = ? AND I.Status = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("TotalRevenue") != null ? rs.getBigDecimal("TotalRevenue") : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalAmountSoldForAllProductsByEmployee(
            Integer employeeId, Integer shopId, Date startDate, Date endDate) throws SQLException {

        BigDecimal totalAmount = BigDecimal.ZERO;
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT SUM(ID.Quantity * ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS TotalAmount ");
        sql.append("FROM InvoiceDetail ID ");
        sql.append("JOIN Invoice I ON ID.InvoiceID = I.InvoiceID ");
        sql.append("WHERE I.SaleEmployeeID = ? ");
        params.add(employeeId);

        sql.append(" AND I.Status = 1 "); // Chỉ lấy hóa đơn đã hoàn thành

        if (shopId != null) {
            sql.append(" AND I.ShopID = ? ");
            params.add(shopId);
        }

        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(new java.sql.Date(startDate.getTime()));
            params.add(new java.sql.Date(endDate.getTime()));
        }

        System.out.println("SQL Total Amount Query (getTotalAmountSoldForAllProductsByEmployee): " + sql.toString());
        System.out.println("Total Amount Parameters: " + params.toString());

        try (PreparedStatement ps = this.connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Lấy giá trị, nếu null thì gán là BigDecimal.ZERO
                    BigDecimal result = rs.getBigDecimal("TotalAmount");
                    totalAmount = (result != null) ? result : BigDecimal.ZERO;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy tổng doanh thu sản phẩm đã bán: " + ex.getMessage(), ex);
            throw ex;
        }
        return totalAmount;
    }

    public int getTotalQuantitySoldForAllProductsByEmployee(
            Integer employeeId, Integer shopId, Date startDate, Date endDate) throws SQLException {

        int totalQuantity = 0;
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT SUM(ID.Quantity) AS TotalQuantitySold ");
        sql.append("FROM InvoiceDetail ID ");
        sql.append("JOIN Invoice I ON ID.InvoiceID = I.InvoiceID ");
        sql.append("WHERE I.SaleEmployeeID = ? ");
        params.add(employeeId);

        sql.append(" AND I.Status = 1 "); // Chỉ lấy hóa đơn đã hoàn thành

        if (shopId != null) {
            sql.append(" AND I.ShopID = ? ");
            params.add(shopId);
        }

        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(new java.sql.Date(startDate.getTime()));
            params.add(new java.sql.Date(endDate.getTime()));
        }

        System.out.println("SQL Total Quantity Query (getTotalQuantitySoldForAllProductsByEmployee): " + sql.toString());
        System.out.println("Total Quantity Parameters: " + params.toString());

        try (PreparedStatement ps = this.connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalQuantity = rs.getInt("TotalQuantitySold");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy tổng số lượng sản phẩm đã bán: " + ex.getMessage(), ex);
            throw ex;
        }
        return totalQuantity;
    }

    public List<SoldProductDetailDto> getSoldProductDetailsByEmployee(
            Integer employeeId, Integer shopId, Date startDate, Date endDate,
            int currentPage, int recordsPerPage) throws SQLException {

        List<SoldProductDetailDto> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT P.ProductID, P.ProductName, SUM(ID.Quantity) AS QuantitySold, ");
        // Nên lấy giá trung bình có trọng số hoặc giá theo đơn hàng cụ thể nếu cùng sản phẩm có nhiều giá
        // Hiện tại AVG(ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) là giá trị trung bình đã chiết khấu của đơn giá
        // Nếu bạn muốn hiển thị 'giá của 1 sản phẩm đó' như giá cố định từ master data, bạn sẽ cần JOIN bảng Products
        // và chọn Product.Price. Nhưng nếu bạn muốn giá theo giao dịch thực tế, AVG này hợp lý hơn.
        sql.append("AVG(ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS UnitPrice, ");
        sql.append("SUM(ID.Quantity * ID.UnitPrice * (1 - ISNULL(ID.Discount, 0))) AS Amount "); // Tính Amount tại đây
        sql.append("FROM Product P ");
        sql.append("JOIN InvoiceDetail ID ON P.ProductID = ID.ProductID ");
        sql.append("JOIN Invoice I ON ID.InvoiceID = I.InvoiceID ");
        sql.append("WHERE I.SaleEmployeeID = ? ");
        params.add(employeeId); // employeeId filter

        sql.append(" AND I.Status = 1 "); // Chỉ lấy hóa đơn đã hoàn thành

        if (shopId != null) { // Thêm lọc theo ShopID nếu cần (đảm bảo Sale chỉ xem trong shop của họ)
            sql.append(" AND I.ShopID = ? ");
            params.add(shopId);
        }

        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(new java.sql.Date(startDate.getTime()));
            params.add(new java.sql.Date(endDate.getTime()));
        }

        sql.append(" GROUP BY P.ProductID, P.ProductName ");
        // Nếu bạn muốn giá đơn vị chính xác cho mỗi sản phẩm nếu nó thay đổi theo thời gian, bạn có thể GROUP BY thêm ID.UnitPrice
        // Nhưng AVG(UnitPrice) có lẽ là đủ cho mục đích thống kê
        sql.append(" ORDER BY P.ProductName ");

        int offset = (currentPage - 1) * recordsPerPage;
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(recordsPerPage);

        System.out.println("SQL Product Detail Query (getSoldProductDetailsByEmployee): " + sql.toString());
        System.out.println("Product Detail Parameters: " + params.toString());

        try (PreparedStatement ps = this.connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tạo SoldProductDetailDto với Amount
                    list.add(new SoldProductDetailDto(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getInt("QuantitySold"),
                            rs.getBigDecimal("UnitPrice"),
                            rs.getBigDecimal("Amount") // Lấy cột Amount đã tính
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy chi tiết sản phẩm đã bán: " + ex.getMessage(), ex);
            throw ex;
        }
        return list;
    }

// Thêm hàm lấy tổng số bản ghi cho chi tiết sản phẩm đã bán
    public int getTotalSoldProductDetailsCountByEmployee(
            Integer employeeId, Integer shopId, Date startDate, Date endDate) throws SQLException {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT COUNT(DISTINCT P.ProductID) "); // Đếm số lượng sản phẩm duy nhất
        sql.append("FROM Product P ");
        sql.append("JOIN InvoiceDetail ID ON P.ProductID = ID.ProductID ");
        sql.append("JOIN Invoice I ON ID.InvoiceID = I.InvoiceID ");
        sql.append("WHERE I.SaleEmployeeID = ? ");
        params.add(employeeId);

        sql.append(" AND I.Status = 1 ");

        if (shopId != null) {
            sql.append(" AND I.ShopID = ? ");
            params.add(shopId);
        }

        if (startDate != null && endDate != null) {
            sql.append(" AND I.InvoiceDate BETWEEN ? AND ? ");
            params.add(new java.sql.Date(startDate.getTime()));
            params.add(new java.sql.Date(endDate.getTime()));
        }

        System.out.println("SQL Product Detail Count Query: " + sql.toString());
        System.out.println("Product Detail Count Parameters: " + params.toString());

        try (PreparedStatement ps = this.connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi đếm tổng số chi tiết sản phẩm đã bán: " + ex.getMessage(), ex);
            throw ex;
        }
        return 0;
    }

    public BigDecimal getTotalProductRevenueByInvoice(int invoiceId) throws SQLException {
        BigDecimal totalProductRevenue = BigDecimal.ZERO;
        String sql = "SELECT SUM(Quantity * UnitPrice) "
                + "FROM InvoiceDetail "
                + "WHERE InvoiceID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalProductRevenue = rs.getBigDecimal(1);
                }
            }
        }
        return totalProductRevenue != null ? totalProductRevenue : BigDecimal.ZERO;
    }

    public BigDecimal getTotalProductRevenueByShop(int shopId, Date fromDate, Date toDate) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        String sql = "SELECT SUM(ID.UnitPrice * ID.Quantity) AS TotalRevenue "
                + "FROM InvoiceDetail ID "
                + "JOIN Invoice I ON ID.InvoiceID = I.InvoiceID "
                + "WHERE I.ShopID = ? AND I.Status = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, shopId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalRevenue = rs.getBigDecimal("TotalRevenue");
                    if (totalRevenue == null) {
                        totalRevenue = BigDecimal.ZERO;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalRevenue;
    }

    public List<InvoiceDetail> getDetailsWithProductInfoByInvoiceID(int invoiceID) {
        List<InvoiceDetail> list = new ArrayList<>();

        String sql = "SELECT id.[InvoiceDetailID], id.[InvoiceID], id.[ProductID], id.[UnitPrice], "
                + "id.[Quantity], id.[Discount], id.[TotalPrice], "
                + "p.[ProductName]"
                + "FROM [dbo].[InvoiceDetail] id "
                + "JOIN [dbo].[Product] p ON id.ProductID = p.ProductID "
                + "WHERE id.InvoiceID = ?";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, invoiceID);
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {

                    Product product = new Product(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getString("CategoryID"),
                            rs.getString("UnitID"),
                            rs.getBigDecimal("SellingPrice"),
                            rs.getString("Description"),
                            rs.getBoolean("Status"),
                            rs.getTimestamp("CreatedDate").toLocalDateTime(),
                            rs.getString("CreatedBy")
                    );

                    InvoiceDetail detail = new InvoiceDetail(
                            rs.getInt("InvoiceDetailID"),
                            rs.getInt("InvoiceID"),
                            rs.getInt("ProductID"),
                            rs.getBigDecimal("UnitPrice"),
                            rs.getInt("Quantity"),
                            rs.getDouble("Discount"),
                            rs.getDouble("TotalPrice"),
                            product
                    );
                    list.add(detail);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDetailDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy chi tiết hóa đơn kèm sản phẩm", ex);
        }
        return list;
    }

    public static void main(String[] args) {

        Connection dbConnection = null;
        try {
            // Kết nối CSDL thông qua DBContext
            DBContext dbContext = new DBContext("ShopDB_SWPP");
            dbConnection = dbContext.getConnection();

            System.out.println("Kết nối cơ sở dữ liệu thành công!");

            // Tạo DAO và truyền connection
            InvoiceDetailDAO dao = new InvoiceDetailDAO(dbConnection);

            // Dữ liệu đầu vào
            int employeeId = 4; // ID nhân viên Sale cần test
            int shopId = 1;     // ID cửa hàng

            // Thiết lập khoảng thời gian: từ 2025-06-22 đến 2025-07-22
            Timestamp start = Timestamp.valueOf("2025-06-22 00:00:00");
            Timestamp end = Timestamp.valueOf("2025-07-22 23:59:59");

            // Gọi hàm test
            int totalQuantity = dao.getTotalQuantitySoldForAllProductsByEmployee(employeeId, shopId, start, end);

            // In kết quả
            System.out.println("➡️ Tổng số lượng sản phẩm đã bán bởi nhân viên ID " + employeeId + ": " + totalQuantity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
