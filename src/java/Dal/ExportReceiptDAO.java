/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Context.DBContext;
import Models.ExportReceipt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Thai Anh
 */
public class ExportReceiptDAO {
      private Connection connection;

    public ExportReceiptDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy tất cả phiếu xuất
    public List<ExportReceipt> getAll() {
        List<ExportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM ExportReceipt";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ExportReceipt er = new ExportReceipt();
                er.setExportReceiptID(rs.getInt("ExportReceiptID"));
                er.setEmployeeID(rs.getString("EmployeeID"));
                er.setShopID(rs.getString("ShopID"));
                er.setReceiptDate(rs.getTimestamp("ReceiptDate"));
                er.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                er.setNote(rs.getString("Note"));
                er.setStatus(rs.getBoolean("Status"));
                er.setTypeID(rs.getString("TypeID"));
                list.add(er);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Lấy theo ID
    public ExportReceipt getByID(int id) {
        String sql = "SELECT * FROM ExportReceipt WHERE ExportReceiptID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExportReceipt er = new ExportReceipt();
                    er.setExportReceiptID(rs.getInt("ExportReceiptID"));
                    er.setEmployeeID(rs.getString("EmployeeID"));
                    er.setShopID(rs.getString("ShopID"));
                    er.setReceiptDate(rs.getTimestamp("ReceiptDate"));
                    er.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                    er.setNote(rs.getString("Note"));
                    er.setStatus(rs.getBoolean("Status"));
                    er.setTypeID(rs.getString("TypeID"));
                    return er;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Thêm mới
    public boolean insert(ExportReceipt er) {
        String sql = "INSERT INTO ExportReceipt (ExportReceiptID, EmployeeID, ShopID, ReceiptDate, TotalAmount, Note, Status, TypeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, er.getExportReceiptID());
            ps.setString(2, er.getEmployeeID());
            ps.setString(3, er.getShopID());
            ps.setTimestamp(4, er.getReceiptDate() != null ? new Timestamp(er.getReceiptDate().getTime()) : null);
            ps.setBigDecimal(5, er.getTotalAmount());
            ps.setString(6, er.getNote());
            ps.setBoolean(7, er.getStatus() != null ? er.getStatus() : true);
            ps.setString(8, er.getTypeID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Cập nhật
    public boolean update(ExportReceipt er) {
        String sql = "UPDATE ExportReceipt SET EmployeeID = ?, ShopID = ?, ReceiptDate = ?, TotalAmount = ?, Note = ?, Status = ?, TypeID = ? WHERE ExportReceiptID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, er.getEmployeeID());
            ps.setString(2, er.getShopID());
            ps.setTimestamp(3, er.getReceiptDate() != null ? new Timestamp(er.getReceiptDate().getTime()) : null);
            ps.setBigDecimal(4, er.getTotalAmount());
            ps.setString(5, er.getNote());
            ps.setBoolean(6, er.getStatus() != null ? er.getStatus() : true);
            ps.setString(7, er.getTypeID());
            ps.setInt(8, er.getExportReceiptID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Xoá
    public boolean delete(int id) {
        String sql = "DELETE FROM ExportReceipt WHERE ExportReceiptID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Test nhanh
    public static void main(String[] args) {
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            ExportReceiptDAO dao = new ExportReceiptDAO(conn);
            List<ExportReceipt> list = dao.getAll();
            if (list.isEmpty()) {
                System.out.println("❌ Không có phiếu xuất nào.");
            } else {
                for (ExportReceipt er : list) {
                    System.out.println("🧾 ID: " + er.getExportReceiptID());
                    System.out.println("📦 Code: " + er.getEmployeeID());
                    System.out.println("👤 Nhân viên: " + er.getEmployeeID());
                    System.out.println("💰 Tổng tiền: " + er.getTotalAmount());
                    System.out.println("------------------------");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
