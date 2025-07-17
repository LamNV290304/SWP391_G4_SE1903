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
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ExportReceipt er = new ExportReceipt();
                er.setExportReceiptID(rs.getInt("ExportReceiptID"));
                er.setEmployeeID(rs.getInt("EmployeeID"));
                er.setShopID(rs.getInt("ShopID"));
                er.setReceiptDate(rs.getTimestamp("ReceiptDate"));
                er.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                er.setNote(rs.getString("Note"));
                er.setStatus(rs.getBoolean("Status"));
                er.setTypeID(rs.getInt("TypeID"));
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
                    er.setEmployeeID(rs.getInt("EmployeeID"));
                    er.setShopID(rs.getInt("ShopID"));
                    er.setReceiptDate(rs.getTimestamp("ReceiptDate"));
                    er.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                    er.setNote(rs.getString("Note"));
                    er.setStatus(rs.getBoolean("Status"));
                    er.setTypeID(rs.getInt("TypeID"));
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
        String sql = "INSERT INTO ExportReceipt (EmployeeID, ShopID, ReceiptDate, TotalAmount, Note, Status, TypeID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, er.getEmployeeID());
            ps.setInt(2, er.getShopID());
            ps.setTimestamp(3, er.getReceiptDate() != null ? new Timestamp(er.getReceiptDate().getTime()) : null);
            ps.setBigDecimal(4, er.getTotalAmount());
            ps.setString(5, er.getNote());
            ps.setBoolean(6, er.getStatus() != null ? er.getStatus() : true);
            ps.setInt(7, er.getTypeID());
            
                return ps.executeUpdate() > 0;
            } catch (SQLException ex) {
                Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }

    public Integer insertAndReturnID(ExportReceipt er) {
        String sql = "INSERT INTO ExportReceipt (EmployeeID, ShopID, ReceiptDate, TotalAmount, Note, Status, TypeID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, er.getEmployeeID());
            ps.setInt(2, er.getShopID());
            ps.setTimestamp(3, er.getReceiptDate() != null ? new Timestamp(er.getReceiptDate().getTime()) : null);
            ps.setBigDecimal(4, er.getTotalAmount());
            ps.setString(5, er.getNote());
            ps.setBoolean(6, er.getStatus() != null ? er.getStatus() : true);
            ps.setInt(7, er.getTypeID());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Trả về ExportReceiptID
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ExportReceipt getNewest() {
        String sql = "SELECT TOP 1 * FROM ExportReceipt ORDER BY ExportReceiptID DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                ExportReceipt er = new ExportReceipt();
                er.setExportReceiptID(rs.getInt("ExportReceiptID"));
                er.setEmployeeID(rs.getInt("EmployeeID"));
                er.setShopID(rs.getInt("ShopID"));
                er.setReceiptDate(rs.getTimestamp("ReceiptDate"));
                er.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                er.setNote(rs.getString("Note"));
                er.setStatus(rs.getBoolean("Status"));
                er.setTypeID(rs.getInt("TypeID"));
                return er;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public boolean update(ExportReceipt er) {
        String sql = "UPDATE ExportReceipt SET EmployeeID = ?, ShopID = ?, ReceiptDate = ?, TotalAmount = ?, Note = ?, Status = ?, TypeID = ? WHERE ExportReceiptID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, er.getEmployeeID());
            ps.setInt(2, er.getShopID());
            ps.setTimestamp(3, er.getReceiptDate() != null ? new Timestamp(er.getReceiptDate().getTime()) : null);
            ps.setBigDecimal(4, er.getTotalAmount());
            ps.setString(5, er.getNote());
            ps.setBoolean(6, er.getStatus() != null ? er.getStatus() : true);
            ps.setInt(7, er.getTypeID());
            ps.setInt(8, er.getExportReceiptID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Xoá
    public boolean delete(int id) {
        String sql = "DELETE FROM ExportReceiptDetail WHERE ExportReceiptID = ?\n"
                + "DELETE FROM ExportReceipt WHERE ExportReceiptID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.executeUpdate(); 
        }catch (SQLException ex) {
                Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
    }
    

    public Integer getExportReceiptIDByInfo(ExportReceipt er) {
        String sql = "SELECT ExportReceiptID FROM ExportReceipt "
                + "WHERE EmployeeID = ? AND ShopID = ? AND TypeExportReceiptID = ? "
                + "AND ReceiptDate = ? AND TotalAmount = ? AND Note = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, er.getEmployeeID());
            ps.setInt(2, er.getShopID());
            ps.setInt(3, er.getTypeID());

            // Chuyển Date sang Timestamp để phù hợp kiểu DATETIME trong SQL Server
            ps.setTimestamp(4, new Timestamp(er.getReceiptDate().getTime()));
            ps.setBigDecimal(5, er.getTotalAmount());
            ps.setString(6, er.getNote());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ExportReceiptID");
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy ExportReceiptID theo thông tin", e);
        }

        return null; // Không tìm thấy
    }
 public List<ExportReceipt> filter(Integer employeeId, Integer shopId, Integer typeId, java.util.Date fromDate, java.util.Date toDate) {
    List<ExportReceipt> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM ExportReceipt WHERE 1=1");

    if (employeeId != null) sql.append(" AND EmployeeID = ?");
    if (shopId != null) sql.append(" AND ShopID = ?");
    if (typeId != null) sql.append(" AND TypeID = ?");
    if (fromDate != null) sql.append(" AND ReceiptDate >= ?");
    if (toDate != null) sql.append(" AND ReceiptDate <= ?");

    try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
        int index = 1;
        if (employeeId != null) ps.setInt(index++, employeeId);
        if (shopId != null) ps.setInt(index++, shopId);
        if (typeId != null) ps.setInt(index++, typeId);

        // Sử dụng java.sql.Timestamp để tương thích với DATETIME trong SQL Server
        if (fromDate != null) ps.setTimestamp(index++, new java.sql.Timestamp(fromDate.getTime()));
        if (toDate != null) ps.setTimestamp(index++, new java.sql.Timestamp(toDate.getTime()));

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ExportReceipt er = extractExportReceipt(rs);
                list.add(er);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, "Lỗi filter ExportReceipt", ex);
    }
    return list;
}

private ExportReceipt extractExportReceipt(ResultSet rs) throws SQLException {
    ExportReceipt er = new ExportReceipt();
    er.setExportReceiptID(rs.getInt("ExportReceiptID"));
    er.setEmployeeID(rs.getInt("EmployeeID"));
    er.setShopID(rs.getInt("ShopID"));
    er.setReceiptDate(rs.getTimestamp("ReceiptDate"));
    er.setTotalAmount(rs.getBigDecimal("TotalAmount"));
    er.setNote(rs.getString("Note"));
    er.setStatus(rs.getBoolean("Status"));
    er.setTypeID(rs.getInt("TypeID"));
    return er;
}


    public static void main(String[] args) {
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            ExportReceiptDAO dao = new ExportReceiptDAO(conn);
            List<ExportReceipt> list = dao.getAll();
System
            .out.println(dao.getNewest());


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
            dao.delete(7);
           
        } catch (SQLException e) {
            Logger.getLogger(ExportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
