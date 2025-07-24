/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Context.DBContext;
import Models.PaymentMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class PaymentMethodDAO {
   private final Connection connection;

    public PaymentMethodDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy tất cả phương thức thanh toán
    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethod> list = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethod ORDER BY MethodName";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToPaymentMethod(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaymentMethodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    // Lấy phương thức thanh toán theo ID
    public PaymentMethod getByID(int id) {
        String sql = "SELECT * FROM PaymentMethod WHERE PaymentMethodID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaymentMethod(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaymentMethodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    // Thêm phương thức thanh toán
    public boolean insert(PaymentMethod method) {
        String sql = "INSERT INTO PaymentMethod (MethodName, Status) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, method.getMethodName());
            ps.setBoolean(2, method.isStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentMethodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    // Cập nhật phương thức thanh toán
    public boolean update(PaymentMethod method) {
        String sql = "UPDATE PaymentMethod SET MethodName = ?, Status = ? WHERE PaymentMethodID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, method.getMethodName());
            ps.setBoolean(2, method.isStatus());
            ps.setInt(3, method.getPaymentMethodID());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentMethodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    // Xoá (xóa cứng)
    public boolean delete(int id) {
        String sql = "DELETE FROM PaymentMethod WHERE PaymentMethodID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentMethodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    // Xoá mềm (status = 0)
    public boolean softDelete(int id) {
        String sql = "UPDATE PaymentMethod SET Status = 0 WHERE PaymentMethodID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentMethodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    // Ánh xạ ResultSet => PaymentMethod
    private PaymentMethod mapResultSetToPaymentMethod(ResultSet rs) throws SQLException {
        PaymentMethod method = new PaymentMethod();
        method.setPaymentMethodID(rs.getInt("PaymentMethodID"));
        method.setMethodName(rs.getString("MethodName"));
        method.setStatus(rs.getBoolean("Status"));
        return method;
    }

    // Test trong main
    public static void main(String[] args) {
        try (Connection conn = new DBContext("ShopDB_Go1").getConnection()) {
            PaymentMethodDAO dao = new PaymentMethodDAO(conn);

            // ✅ Thêm mới
            PaymentMethod m = new PaymentMethod();
            m.setMethodName("Momo");
            m.setStatus(true);
            boolean inserted = dao.insert(m);
            System.out.println(inserted ? "✅ Thêm thành công" : "❌ Thêm thất bại");

            // ✅ Lấy tất cả
            List<PaymentMethod> list = dao.getAllPaymentMethods();
            list.forEach(pm -> System.out.println("📌 " + pm.getPaymentMethodID() + " - " + pm.getMethodName()));

            // ✅ Lấy theo ID
            if (!list.isEmpty()) {
                int lastID = list.get(list.size() - 1).getPaymentMethodID();
                PaymentMethod found = dao.getByID(lastID);
                System.out.println("🔍 Tìm thấy: " + found.getMethodName());

                // ✅ Cập nhật
                found.setMethodName("Momo cập nhật");
                boolean updated = dao.update(found);
                System.out.println(updated ? "🔧 Cập nhật thành công" : "❌ Không cập nhật được");

                // ✅ Xoá mềm
                boolean softDeleted = dao.softDelete(lastID);
                System.out.println(softDeleted ? "🗑️ Đã xoá mềm" : "❌ Xoá mềm thất bại");

                // ✅ Xoá cứng (comment nếu không dùng)
                // boolean deleted = dao.delete(lastID);
                // System.out.println(deleted ? "🗑️ Đã xoá cứng" : "❌ Không xoá được");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
