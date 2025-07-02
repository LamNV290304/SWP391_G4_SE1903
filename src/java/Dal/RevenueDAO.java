/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class RevenueDAO {
    private Connection conn;

    public RevenueDAO(Connection conn) {
        this.conn = conn;
    }

    public BigDecimal getTotalRevenue(LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT SUM(Amount) FROM Payments WHERE PaymentDate BETWEEN ? AND ? AND Status = 'Thành công'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    public int getSuccessfulTransactionCount(LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Payments WHERE PaymentDate BETWEEN ? AND ? AND Status = 'Thành công'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int getActiveShopCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM ShopSubscriptions WHERE IsActive = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public BigDecimal calculateGrowth(LocalDate start, LocalDate end) throws SQLException {
        BigDecimal startRevenue = getTotalRevenue(start, start.withDayOfMonth(start.lengthOfMonth()));
        BigDecimal endRevenue = getTotalRevenue(end.withDayOfMonth(1), end);

        if (startRevenue == null || startRevenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return endRevenue.subtract(startRevenue)
                .divide(startRevenue, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public List<BigDecimal> getMonthlyRevenueData(LocalDate start, LocalDate end) throws SQLException {
        List<BigDecimal> list = new ArrayList<>();
        LocalDate current = start.withDayOfMonth(1);
        while (!current.isAfter(end)) {
            LocalDate lastDay = current.withDayOfMonth(current.lengthOfMonth());
            String sql = "SELECT SUM(Amount) FROM Payments WHERE PaymentDate BETWEEN ? AND ? AND Status = 'Thành công'";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(current));
                ps.setDate(2, Date.valueOf(lastDay));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) list.add(rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO);
            }
            current = current.plusMonths(1);
        }
        return list;
    }

    public List<String> getMonthLabels(LocalDate start, LocalDate end) {
        List<String> labels = new ArrayList<>();
        LocalDate current = start.withDayOfMonth(1);
        while (!current.isAfter(end)) {
            labels.add("\"" + current.getMonthValue() + "/" + current.getYear() + "\"");
            current = current.plusMonths(1);
        }
        return labels;
    }

    public List<String> getPackageLabels(LocalDate start, LocalDate end) throws SQLException {
        List<String> labels = new ArrayList<>();
        String sql = "SELECT DISTINCT SP.Name FROM Payments P JOIN ServicePackages SP ON P.PackageId = SP.Id WHERE PaymentDate BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) labels.add("\"" + rs.getString(1) + "\"");
        }
        return labels;
    }

    public List<Integer> getPackageRatios(LocalDate start, LocalDate end) throws SQLException {
        List<Integer> counts = new ArrayList<>();
        List<String> labels = getPackageLabels(start, end);

        for (String label : labels) {
            String sql = "SELECT COUNT(*) FROM Payments P JOIN ServicePackages SP ON P.PackageId = SP.Id WHERE PaymentDate BETWEEN ? AND ? AND SP.Name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(start));
                ps.setDate(2, Date.valueOf(end));
                ps.setString(3, label.replace("\"", ""));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) counts.add(rs.getInt(1));
            }
        }
        return counts;
    }
}