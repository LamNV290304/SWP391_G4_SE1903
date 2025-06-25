package Dal;

import Models.Payment;
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
        sql.append("SELECT p.PaymentDate, p.ExpireAt, p.Amount, p.Status, sp.Name AS PackageName ")
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

        // xử lý sort
        sql.append("ORDER BY ");
        String[] sortFields = sort.split(",");
        for (int i = 0; i < sortFields.length; i++) {
            String[] pair = sortFields[i].split(":");
            if (pair.length == 2) {
                String field = switch (pair[0]) {
                    case "expireAt" -> "p.ExpireAt";
                    case "packageName" -> "sp.Name";
                    default -> "p.PaymentDate";
                };
                String direction = "asc".equalsIgnoreCase(pair[1]) ? "ASC" : "DESC";
                sql.append(field).append(" ").append(direction);
                if (i < sortFields.length - 1) {
                    sql.append(", ");
                }
            }
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

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
}
