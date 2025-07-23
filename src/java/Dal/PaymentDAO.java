package Dal;

import Context.DBContext;
import DTO.PaymentDto;
import Models.Payment;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    private final Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Payment> getPaymentsByShopOwner(
            int shopOwnerId, int offset, int limit, String sort,
            Integer packageId, String fromDate, String toDate) {

        List<Payment> payments = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.Id, p.PaymentDate, p.ExpireAt, p.Amount, p.Status, sp.Name AS PackageName ")
                .append("FROM Payments p JOIN ServicePackages sp ON p.PackageId = sp.Id ")
                .append("WHERE p.ShopOwnerId = ? ");

        if (packageId != null) {
            sql.append("AND p.PackageId = ? ");
        }
        if (fromDate != null && !fromDate.isBlank()) {
            sql.append("AND p.PaymentDate >= ? ");
        }
        if (toDate != null && !toDate.isBlank()) {
            sql.append("AND p.PaymentDate <= ? ");
        }

        // Xử lý sort
        if (sort == null || sort.isBlank()) {
            sql.append("ORDER BY p.Id DESC ");
        } else {
            String direction = "asc".equalsIgnoreCase(sort) ? "ASC" : "DESC";
            sql.append("ORDER BY p.PaymentDate ").append(direction).append(" ");
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, shopOwnerId);
            if (packageId != null) {
                ps.setInt(index++, packageId);
            }
            if (fromDate != null && !fromDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(fromDate));
            }
            if (toDate != null && !toDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(toDate));
            }
            ps.setInt(index++, offset);
            ps.setInt(index, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment p = new Payment();
                    p.setPackageName(rs.getString("PackageName"));
                    p.setPaymentDate(rs.getDate("PaymentDate"));
                    p.setExpireAt(rs.getDate("ExpireAt"));
                    p.setAmount(rs.getDouble("Amount"));
                    p.setStatus(rs.getString("Status"));
                    payments.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    public int countPaymentsByShopOwner(int shopOwnerId, Integer packageId, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Payments WHERE ShopOwnerId = ?");
        if (packageId != null) {
            sql.append(" AND PackageId = ?");
        }
        if (fromDate != null && !fromDate.isBlank()) {
            sql.append(" AND PaymentDate >= ?");
        }
        if (toDate != null && !toDate.isBlank()) {
            sql.append(" AND PaymentDate <= ?");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, shopOwnerId);
            if (packageId != null) {
                ps.setInt(index++, packageId);
            }
            if (fromDate != null && !fromDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(fromDate));
            }
            if (toDate != null && !toDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(toDate));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean insert(Payment payment) {
        String sql = "INSERT INTO Payments (ShopOwnerId, PackageId, PaymentDate, ExpireAt, Amount, Status, TxnRef) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, payment.getShopOwnerId());
            ps.setInt(2, payment.getPackageId());
            ps.setDate(3, payment.getPaymentDate());
            ps.setDate(4, payment.getExpireAt());
            ps.setDouble(5, payment.getAmount());
            ps.setString(6, payment.getStatus());
            ps.setString(7, payment.getTxnRef());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatusByTxnRef(String txnRef, String status) {
        String sql = "UPDATE Payments SET Status = ? WHERE TxnRef = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, txnRef);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Payment getByTxnRef(String txnRef) {
        String sql = "SELECT * FROM Payments WHERE TxnRef = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, txnRef);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setId(rs.getInt("Id"));
                    payment.setShopOwnerId(rs.getInt("ShopOwnerId"));
                    payment.setPackageId(rs.getInt("PackageId"));
                    payment.setPaymentDate(rs.getDate("PaymentDate"));
                    payment.setExpireAt(rs.getDate("ExpireAt"));
                    payment.setAmount(rs.getDouble("Amount"));
                    payment.setStatus(rs.getString("Status"));
                    payment.setTxnRef(rs.getString("TxnRef"));
                    return payment;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PaymentDto> getAllPaymentsForAdmin(int offset, int limit, String sort,
            Integer packageId, String fromDate, String toDate, String search) {
        List<PaymentDto> payments = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.PaymentDate, p.ExpireAt, p.Amount, p.Status, p.PackageId, ")
                .append("sp.Name AS PackageName, so.FullName AS ShopOwnerName, so.ShopName ")
                .append("FROM Payments p ")
                .append("JOIN ServicePackages sp ON p.PackageId = sp.Id ")
                .append("JOIN ShopOwners so ON p.ShopOwnerId = so.Id ")
                .append("WHERE 1=1 ");

        if (packageId != null) {
            sql.append("AND p.PackageId = ? ");
        }
        if (fromDate != null && !fromDate.isBlank()) {
            sql.append("AND p.PaymentDate >= ? ");
        }
        if (toDate != null && !toDate.isBlank()) {
            sql.append("AND p.PaymentDate <= ? ");
        }
        if (search != null && !search.isBlank()) {
            sql.append("AND (so.FullName LIKE ? OR so.ShopName LIKE ?) ");
        }

        if (sort == null || sort.isBlank()) {
            sql.append("ORDER BY p.PaymentDate DESC ");
        } else {
            sql.append("ORDER BY ");
            String[] sortFields = sort.split(",");
            for (int i = 0; i < sortFields.length; i++) {
                String[] pair = sortFields[i].split(":");
                if (pair.length == 2) {
                    String field = switch (pair[0]) {
                        case "expireAt" ->
                            "p.ExpireAt";
                        case "packageName" ->
                            "sp.Name";
                        default ->
                            "p.PaymentDate";
                    };
                    String direction = "asc".equalsIgnoreCase(pair[1]) ? "ASC" : "DESC";
                    sql.append(field).append(" ").append(direction);
                    if (i < sortFields.length - 1) {
                        sql.append(", ");
                    }
                }
            }
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (packageId != null) {
                ps.setInt(index++, packageId);
            }
            if (fromDate != null && !fromDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(fromDate));
            }
            if (toDate != null && !toDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(toDate));
            }
            if (search != null && !search.isBlank()) {
                String keyword = "%" + search.trim() + "%";
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }
            ps.setInt(index++, offset);
            ps.setInt(index, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentDto dto = new PaymentDto();
                    dto.setPackageId(rs.getInt("PackageId"));
                    dto.setPackageName(rs.getString("PackageName"));
                    dto.setShopOwnerName(rs.getString("ShopOwnerName"));
                    dto.setShopName(rs.getString("ShopName"));
                    dto.setPaymentDate(rs.getDate("PaymentDate"));
                    dto.setExpireAt(rs.getDate("ExpireAt"));
                    dto.setAmount(rs.getDouble("Amount"));
                    dto.setStatus(rs.getString("Status"));
                    payments.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public int countAllPaymentsForAdmin(Integer packageId, String fromDate, String toDate, String search) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Payments p JOIN ShopOwners so ON p.ShopOwnerId = so.Id WHERE 1=1");

        if (packageId != null) {
            sql.append(" AND p.PackageId = ?");
        }
        if (fromDate != null && !fromDate.isBlank()) {
            sql.append(" AND p.PaymentDate >= ?");
        }
        if (toDate != null && !toDate.isBlank()) {
            sql.append(" AND p.PaymentDate <= ?");
        }
        if (search != null && !search.isBlank()) {
            sql.append(" AND (so.FullName LIKE ? OR so.ShopName LIKE ?)");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (packageId != null) {
                ps.setInt(index++, packageId);
            }
            if (fromDate != null && !fromDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(fromDate));
            }
            if (toDate != null && !toDate.isBlank()) {
                ps.setDate(index++, Date.valueOf(toDate));
            }
            if (search != null && !search.isBlank()) {
                String keyword = "%" + search.trim() + "%";
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BigDecimal getTotalPaid(String fromDate, String toDate) throws SQLException {
        String sql = "SELECT SUM(Amount) AS Total FROM Payments WHERE 1 = 1";

        if (fromDate != null && !fromDate.isEmpty()) {
            sql += " AND PaymentDate >= ?";
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql += " AND PaymentDate <= ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int index = 1;
            if (fromDate != null && !fromDate.isEmpty()) {
                ps.setDate(index++, Date.valueOf(fromDate));
            }
            if (toDate != null && !toDate.isEmpty()) {
                ps.setDate(index++, Date.valueOf(toDate));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("Total") != null ? rs.getBigDecimal("Total") : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public double sumSuccessfulPayments(int shopOwnerId) {
        String sql = "SELECT SUM(Amount) FROM Payments WHERE ShopOwnerId = ? AND Status = 'Thành công'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shopOwnerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        try (Connection connection = DBContext.getConnection("CentralDB")) {
            System.out.println("✅ Kết nối thành công!");

            // Tạo DAO và gọi hàm
            PaymentDAO dao = new PaymentDAO(connection);

            int shopOwnerId = 8003;
            int offset = 0;
            int limit = 10;
            String sort = "desc";
            Integer packageId = null; // hoặc 3 nếu muốn lọc
            String fromDate = "2025-7-10";
            String toDate = "2025-7-17";

            List<Payment> list = dao.getPaymentsByShopOwner(shopOwnerId, offset, limit, sort, packageId, fromDate, toDate);

            System.out.println("📄 Danh sách giao dịch:");
            for (Payment p : list) {
                System.out.println("Gói: " + p.getPackageName()
                        + " | Ngày thanh toán: " + p.getPaymentDate()
                        + " | Hết hạn: " + p.getExpireAt()
                        + " | Số tiền: " + p.getAmount()
                        + " | Trạng thái: " + p.getStatus());
            }

        } catch (SQLException e) {
            System.err.println("❌ Kết nối thất bại: " + e.getMessage());
        }
    }
}
