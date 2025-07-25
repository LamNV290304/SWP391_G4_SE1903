/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.PaymentVoucher;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class PaymentVoucherDAO {

    private final Connection connection;

    public PaymentVoucherDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy tất cả phiếu chi
    public List<PaymentVoucher> getAllPaymentVouchers() {
        List<PaymentVoucher> list = new ArrayList<>();
        String sql = "SELECT * FROM PaymentVoucher ORDER BY PaymentDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToPaymentVoucher(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return list;
    }

    // Thêm phiếu chi
    public boolean insertPaymentVoucher(PaymentVoucher pv) {
        String sql = "INSERT INTO PaymentVoucher (ShopID, EmployeeID, SupplierID, PaymentDate, Amount, Note, Status, CreatedDate, TypeID, PaymentMethodID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pv.getShopID());
            ps.setInt(2, pv.getEmployeeID());

            if (pv.getSupplierID() != null) {
                ps.setInt(3, pv.getSupplierID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setTimestamp(4, new Timestamp(pv.getPaymentDate().getTime()));
            ps.setBigDecimal(5, pv.getAmount());
            ps.setString(6, pv.getNote());
            ps.setBoolean(7, pv.isStatus());
            ps.setTimestamp(8, new Timestamp(pv.getCreatedDate().getTime()));
            ps.setInt(9, pv.getTypeID());
            ps.setInt(10, pv.getPaymentMethodID());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return false;
    }
// Tính tổng số tiền đã chi trong khoảng ngày
public double getTotalExpense(java.sql.Date fromDate, java.sql.Date toDate) {
    String sql = "SELECT SUM(Amount) AS Total FROM PaymentVoucher WHERE CreatedDate BETWEEN ? AND ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setDate(1, fromDate);
        ps.setDate(2, toDate);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("Total");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return 0;
}

// Đếm số lượng phiếu chi trong khoảng ngày
public int countPaymentVouchers(java.sql.Date fromDate, java.sql.Date toDate) {
    String sql = "SELECT COUNT(*) AS Count FROM PaymentVoucher WHERE CreatedDate BETWEEN ? AND ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setDate(1, fromDate);
        ps.setDate(2, toDate);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("Count");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return 0;
}

    // Lấy phiếu chi theo ID
    public PaymentVoucher getPaymentVoucherByID(int id) {
        String sql = "SELECT * FROM PaymentVoucher WHERE PaymentVoucherID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaymentVoucher(rs);
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    // Xóa phiếu chi
    public boolean deletePaymentVoucher(int id) {
        String sql = "DELETE FROM PaymentVoucher WHERE PaymentVoucherID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return false;
    }

    // Cập nhật phiếu chi
    public boolean updatePaymentVoucher(PaymentVoucher pv) {
        String sql = "UPDATE PaymentVoucher SET ShopID = ?, EmployeeID = ?, SupplierID = ?, "
                + "PaymentDate = ?, Amount = ?, Note = ?, Status = ?, CreatedDate = ?, TypeID = ?, PaymentMethodID = ? "
                + "WHERE PaymentVoucherID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pv.getShopID());
            ps.setInt(2, pv.getEmployeeID());

            if (pv.getSupplierID() != null) {
                ps.setInt(3, pv.getSupplierID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setTimestamp(4, new Timestamp(pv.getPaymentDate().getTime()));
            ps.setBigDecimal(5, pv.getAmount());
            ps.setString(6, pv.getNote());
            ps.setBoolean(7, pv.isStatus());
            ps.setTimestamp(8, new Timestamp(pv.getCreatedDate().getTime()));
            ps.setInt(9, pv.getTypeID());
            ps.setInt(10, pv.getPaymentMethodID());
            ps.setInt(11, pv.getPaymentVoucherID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return false;
    }

    // Hàm hỗ trợ ánh xạ ResultSet -> PaymentVoucher
    private PaymentVoucher mapResultSetToPaymentVoucher(ResultSet rs) throws SQLException {
        PaymentVoucher pv = new PaymentVoucher();

        pv.setPaymentVoucherID(rs.getInt("PaymentVoucherID"));
        pv.setShopID(rs.getInt("ShopID"));
        pv.setEmployeeID(rs.getInt("EmployeeID"));

        int supplierId = rs.getInt("SupplierID");
        if (!rs.wasNull()) {
            pv.setSupplierID(supplierId);
        }

        pv.setPaymentDate(rs.getTimestamp("PaymentDate"));
        pv.setAmount(rs.getBigDecimal("Amount"));
        pv.setNote(rs.getString("Note"));
        pv.setStatus(rs.getBoolean("Status"));
        pv.setCreatedDate(rs.getTimestamp("CreatedDate"));
        pv.setTypeID(rs.getInt("TypeID"));
        pv.setPaymentMethodID(rs.getInt("PaymentMethodID"));

        return pv;
    }
public List<PaymentVoucher> filterPaymentVouchers(Integer shopID, Integer employeeID, Integer typeID,
                                                      BigDecimal minAmount, BigDecimal maxAmount,
                                                      java.util.Date fromDate, java.util.Date toDate, Integer paymentMethodID) {
        List<PaymentVoucher> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM PaymentVoucher WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (shopID != null) {
            sql.append(" AND ShopID = ?");
            params.add(shopID);
        }
        if (employeeID != null) {
            sql.append(" AND EmployeeID = ?");
            params.add(employeeID);
        }
        if (typeID != null) {
            sql.append(" AND TypeID = ?");
            params.add(typeID);
        }
        if (minAmount != null) {
            sql.append(" AND Amount >= ?");
            params.add(minAmount);
        }
        if (maxAmount != null) {
            sql.append(" AND Amount <= ?");
            params.add(maxAmount);
        }
        if (fromDate != null) {
            sql.append(" AND PaymentDate >= ?");
            params.add(new Timestamp(fromDate.getTime()));
        }
        if (toDate != null) {
            sql.append(" AND PaymentDate <= ?");
            params.add(new Timestamp(toDate.getTime()));
        }
        if (paymentMethodID != null) {
            sql.append(" AND PaymentMethodID = ?");
            params.add(paymentMethodID);
        }

        sql.append(" ORDER BY PaymentDate DESC");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPaymentVoucher(rs));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(PaymentVoucherDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return list;
    }
    public static void main(String[] args) {
        try (Connection conn = new DBContext("Test").getConnection()) {
            PaymentVoucherDAO dao = new PaymentVoucherDAO(conn);

            // ✅ 1. Thêm phiếu chi mới
            PaymentVoucher newVoucher = new PaymentVoucher();
            newVoucher.setShopID(1);
            newVoucher.setEmployeeID(2);
            newVoucher.setSupplierID(null); // nếu không có nhà cung cấp
            newVoucher.setPaymentDate(new java.util.Date());
            newVoucher.setAmount(new BigDecimal("1500000.00"));
            newVoucher.setNote("Thanh toán tiền hàng tháng 6");
            newVoucher.setStatus(true);
            newVoucher.setCreatedDate(new java.util.Date());
            newVoucher.setTypeID(1); // ví dụ 1 là "Thanh toán NCC"
            newVoucher.setPaymentMethodID(2); // ví dụ: chuyển khoản

            boolean inserted = dao.insertPaymentVoucher(newVoucher);
            System.out.println(inserted ? "✅ Đã thêm phiếu chi." : "❌ Lỗi khi thêm.");

            // ✅ 2. Lấy toàn bộ danh sách phiếu chi
            List<PaymentVoucher> vouchers = dao.getAllPaymentVouchers();
            for (PaymentVoucher v : vouchers) {
                System.out.println("📄 Phiếu chi: " + v);
            }

            // ✅ 3. Lấy theo ID cuối cùng
            if (!vouchers.isEmpty()) {
                int lastID = vouchers.get(0).getPaymentVoucherID();
                PaymentVoucher found = dao.getPaymentVoucherByID(lastID);
                System.out.println("🔍 Tìm theo ID: " + found);

                // ✅ 4. Cập nhật ghi chú
                found.setNote("Cập nhật nội dung test");
                boolean updated = dao.updatePaymentVoucher(found);
                System.out.println(updated ? "✅ Đã cập nhật." : "❌ Không cập nhật được.");

                // ✅ 5. Xoá thử (có thể comment nếu không muốn xoá)
                // boolean deleted = dao.deletePaymentVoucher(lastID);
                // System.out.println(deleted ? "🗑️ Đã xoá phiếu chi." : "❌ Không xoá được.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
