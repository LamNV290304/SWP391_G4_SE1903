/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Models.TypeImportReceipt;
import Context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class TypeImportReceiptDAO {
     private Connection connection;

    public TypeImportReceiptDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy toàn bộ loại phiếu nhập
    public List<TypeImportReceipt> getAllTypeImportReceipts() {
        List<TypeImportReceipt> list = new ArrayList<>();
        String sql = "SELECT TypeID, TypeName FROM TypeImportReceipt";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TypeImportReceipt type = new TypeImportReceipt();
                type.setTypeID(rs.getInt("TypeID"));
                type.setTypeName(rs.getString("TypeName"));
                list.add(type);
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Lấy loại phiếu nhập theo ID
    public TypeImportReceipt getByID(String id) {
        String sql = "SELECT TypeID, TypeName FROM TypeImportReceipt WHERE TypeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TypeImportReceipt type = new TypeImportReceipt();
                    type.setTypeID(rs.getInt("TypeID"));
                    type.setTypeName(rs.getString("TypeName"));
                    return type;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Thêm loại phiếu nhập
    public boolean insert(TypeImportReceipt type) {
        String sql = "INSERT INTO TypeImportReceipt(TypeName) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.getTypeName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Cập nhật loại phiếu nhập
    public boolean update(TypeImportReceipt type) {
        String sql = "UPDATE TypeImportReceipt SET TypeName = ? WHERE TypeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.getTypeName());
            ps.setInt(2, type.getTypeID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Xóa loại phiếu nhập
    public boolean delete(int id) {
        String sql = "DELETE FROM TypeImportReceipt WHERE TypeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
    public static void main(String[] args) {
          try (Connection conn = new DBContext("SWP7").getConnection()) {
        TypeImportReceiptDAO dao = new TypeImportReceiptDAO(conn);
        
TypeImportReceipt type1 = new TypeImportReceipt("Nhap cho Tanh");
//if(dao.insert(type1)){System.out.println("insert thanh cong");}
dao.delete(5);
List<TypeImportReceipt> types = dao.getAllTypeImportReceipts();
        if (types.isEmpty()) {
            System.out.println("❌ Không có loại phiếu nhập nào.");
        } else {
            System.out.println("📋 Danh sách loại phiếu nhập:");
            for (TypeImportReceipt type : types) {
                System.out.println("🆔 ID: " + type.getTypeID());
                System.out.println("📄 Tên: " + type.getTypeName());
                System.out.println("-----------------------------");
            }
        }
        
    } catch (SQLException e) {
        Logger.getLogger(TypeImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
    }
    }
}
