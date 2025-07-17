/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.TypeExportReceipt;
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
public class TypeExportReceiptDAO {

    private Connection connection;

    public TypeExportReceiptDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy toàn bộ loại phiếu xuất
    public List<TypeExportReceipt> getAllTypeExportReceipts(int status) {
        List<TypeExportReceipt> list = new ArrayList<>();
        String sql;
        if (status == 1) {
            sql = "SELECT * FROM TypeExportReceipt where Status =1";
        } else if (status == 0) {
            sql = "SELECT * FROM TypeExportReceipt where Status =0";
        } else {
            sql = "SELECT * FROM TypeExportReceipt";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TypeExportReceipt type = new TypeExportReceipt();
                type.setTypeID(rs.getInt("TypeID"));
                type.setTypeName(rs.getString("TypeName"));
                type.setStatus(rs.getInt("Status"));
                list.add(type);
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Lấy loại phiếu xuất theo ID
    public TypeExportReceipt getByID(String id) {
        String sql = "SELECT * FROM TypeExportReceipt WHERE TypeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TypeExportReceipt type = new TypeExportReceipt();
                    type.setTypeID(rs.getInt("TypeID"));
                    type.setTypeName(rs.getString("TypeName"));
                    type.setStatus(rs.getInt("Status"));
                    return type;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Thêm loại phiếu xuất
    public boolean insert(TypeExportReceipt type) {
        String sql = "INSERT INTO TypeExportReceipt(TypeName) VALUES ( ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.getTypeName());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logger.getLogger(TypeExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Cập nhật loại phiếu xuất
    public boolean update(TypeExportReceipt type) {
        String sql = "UPDATE TypeExportReceipt SET TypeName = ? WHERE TypeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.getTypeName());
            ps.setInt(2, type.getTypeID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Xóa loại phiếu xuất
    public boolean delete(String id) {
        String sql = "UPDATE TypeExportReceipt SET Status = 0 WHERE TypeID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public static void main(String[] args) {
        try (Connection conn = new DBContext("Test").getConnection()) {
            TypeExportReceiptDAO dao = new TypeExportReceiptDAO(conn);
            List<TypeExportReceipt> types = dao.getAllTypeExportReceipts(1);
            dao.delete("2");
            if (types.isEmpty()) {
                System.out.println("❌ Không có loại phiếu xuất nào.");
            } else {
                System.out.println("📋 Danh sách loại phiếu xuất:");
                for (TypeExportReceipt type : types) {
                    System.out.println("🆔 ID: " + type.getTypeID());
                    System.out.println("📄 Tên: " + type.getTypeName());
                    System.out.println("Status: " + type.getStatus());
                    System.out.println("-----------------------------");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
