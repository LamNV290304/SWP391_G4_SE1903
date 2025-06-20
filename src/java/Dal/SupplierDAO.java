/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Models.Supplier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
/**
 *
 * @author Thai Anh
 */
public class SupplierDAO {
      private  Connection connection;

    public SupplierDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy tất cả nhà cung cấp
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Supplier s = new Supplier(
                        rs.getString("SupplierName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getBoolean("Status"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getString("CreatedBy")
                );
                s.setSupplierID(rs.getInt("SupplierID"));
                list.add(s);
            }
        } catch (SQLException e) {
            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Lấy nhà cung cấp theo ID
    public Supplier getSupplierByID(String id) {
        String sql = "SELECT * FROM Supplier WHERE SupplierID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Supplier(
                            rs.getString("SupplierName"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getString("Address"),
                            rs.getBoolean("Status"),
                            rs.getTimestamp("CreatedDate"),
                            rs.getString("CreatedBy")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Thêm nhà cung cấp mới
    public boolean insertSupplier(Supplier s) {
        String sql = "INSERT INTO Supplier (SupplierName, Phone, Email, Address, Status, CreatedDate, CreatedBy) " +
                     "VALUES (?,  ?, ?, ?, ?, GETDATE(), ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getAddress());
            ps.setBoolean(5, s.isStatus());
            ps.setString(6, s.getCreatedBy());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Cập nhật nhà cung cấp
    public boolean updateSupplier(Supplier s,int id) {
        String sql = "UPDATE Supplier SET SupplierName = ?, Phone = ?, Email = ?, Address = ?, Status = ?, CreatedBy = ? WHERE SupplierID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getAddress());
            ps.setBoolean(5, s.isStatus());
            ps.setString(6, s.getCreatedBy());
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Xoá nhà cung cấp
    public boolean deleteSupplier(int id) {
        String sql = "DELETE FROM Supplier WHERE SupplierID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(SupplierDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
    public static void main(String[] args) {
        Context.DBContext db = new Context.DBContext("SWP7"); // hoặc dùng constructor mặc định nếu bạn đã sửa
        Connection connection = db.getConnection();
        SupplierDAO dao = new SupplierDAO(connection);

        // 1. Insert supplier
        Supplier newSupplier = new Supplier(
                "Nhà cung cấp ABC",
                "0123456789",
                "abc@example.com",
                "123 Đường A, Hà Nội",
                true,
                new Timestamp(System.currentTimeMillis()),
                "admin"
        );
        //dao.insertSupplier(newSupplier);
        System.out.println("✔ Đã thêm nhà cung cấp mới.");
 dao.deleteSupplier(5);
        // 2. Get all suppliers
        List<Supplier> allSuppliers = dao.getAllSuppliers();
        System.out.println("📋 Danh sách nhà cung cấp:");
        for (Supplier s : allSuppliers) {
            System.out.println(s);
        }

        // 3. Get supplier by ID
        Supplier found = dao.getSupplierByID("5");
        if (found != null) {
            System.out.println("🔍 Tìm thấy nhà cung cấp: " + found);
        } else {
            System.out.println("⚠ Không tìm thấy nhà cung cấp với ID S001");
        }

        // 4. Update supplier
        if (found != null) {
            found.setSupplierName("Nhà cung cấp ABC - Updated");
            found.setPhone("0987654321");
           // dao.updateSupplier(found);
            System.out.println("✔ Đã cập nhật thông tin nhà cung cấp.");
        }

        // 5. Delete supplier
       
        System.out.println("🗑 Đã xóa nhà cung cấp có ID 0.");
    }
}
