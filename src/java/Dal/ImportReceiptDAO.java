/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Models.ImportReceipt;
import Context.DBContext;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Anh
 */
public class ImportReceiptDAO {

    private final Connection connection;

    public ImportReceiptDAO(Connection connection) {
        this.connection = connection;
    }

    // Lấy tất cả phiếu nhập
    public List<ImportReceipt> getAllImportReceipts() {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM ImportReceipt";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToImportReceipt(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Lấy phiếu nhập theo ID
    public ImportReceipt getImportReceiptByID(int id) {
        String sql = "SELECT * FROM ImportReceipt WHERE ImportReceiptID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToImportReceipt(rs);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Thêm mới phiếu nhập
    public boolean insertImportReceipt(ImportReceipt ir) {
        String sql = "INSERT INTO ImportReceipt (TypeID, SupplierID, EmployeeID, ShopID, ReceiptDate, TotalAmount, Note, Status) "
                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ir.getTypeID());
            ps.setInt(2, ir.getSupplierID());
            ps.setInt(3, ir.getEmployeeID());
            ps.setInt(4, ir.getShopID());
            ps.setTimestamp(5, new Timestamp(ir.getReceiptDate().getTime()));
            ps.setFloat(6, (float) ir.getTotalAmount());
            ps.setString(7, ir.getNote());
            ps.setBoolean(8, ir.isStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Cập nhật phiếu nhập
    public boolean updateImportReceipt(ImportReceipt ir) {
        String sql = "UPDATE ImportReceipt SET TypeID = ?, SupplierID = ?, EmployeeID = ?, ShopID = ?, ReceiptDate = ?, TotalAmount = ?, Note = ?, Status = ? "
                + "WHERE ImportReceiptID = ? "
                + "DELETE FROM ImportReceipt WHERE ImportReceiptID = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ir.getTypeID());
            ps.setInt(2, ir.getSupplierID());
            ps.setInt(3, ir.getEmployeeID());
            ps.setInt(4, ir.getShopID());
            ps.setTimestamp(5, new Timestamp(ir.getReceiptDate().getTime()));
            ps.setFloat(6, (float) ir.getTotalAmount());
            ps.setString(7, ir.getNote());
            ps.setBoolean(8, ir.isStatus());
            ps.setInt(9, ir.getImportReceiptID());
            ps.setInt(10, ir.getImportReceiptID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
// Lấy phiếu nhập mới nhất (theo ID lớn nhất)

    public ImportReceipt getLatestImportReceiptByID() {
        String sql = "SELECT TOP 1 * FROM ImportReceipt ORDER BY ImportReceiptID DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToImportReceipt(rs);
            }
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Xóa phiếu nhập
    public boolean deleteImportReceipt(int id) {
        String sql = "DELETE FROM ImportReceiptDetail WHERE ImportReceiptID = ?;\n"
                + "DELETE FROM ImportReceipt WHERE ImportReceiptID = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Hàm tiện ích để ánh xạ từ ResultSet sang đối tượng
    private ImportReceipt mapResultSetToImportReceipt(ResultSet rs) throws SQLException {
        ImportReceipt iR = new ImportReceipt(
                rs.getInt("SupplierID"),
                rs.getInt("EmployeeID"),
                rs.getInt("ShopID"),
                rs.getTimestamp("ReceiptDate"),
                rs.getFloat("TotalAmount"),
                rs.getString("Note"),
                rs.getBoolean("Status"),
                rs.getInt("TypeID")
        );
        iR.setImportReceiptID(rs.getInt("ImportReceiptID"));
        return iR;
    }
// Lọc phiếu nhập theo nhiều điều kiện

    public List<ImportReceipt> filterImportReceipts(String shopId, String employeeId, String supplierId, String fromDate, String toDate) {
        List<ImportReceipt> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ImportReceipt WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (shopId != null && !shopId.isEmpty()) {
            sql.append(" AND ShopID = ?");
            params.add(Integer.parseInt(shopId));
        }
        if (employeeId != null && !employeeId.isEmpty()) {
            sql.append(" AND EmployeeID = ?");
            params.add(Integer.parseInt(employeeId));
        }
        if (supplierId != null && !supplierId.isEmpty()) {
            sql.append(" AND SupplierID = ?");
            params.add(Integer.parseInt(supplierId));
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND CAST(ReceiptDate AS DATE) >= ?");
            params.add(java.sql.Date.valueOf(fromDate));
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND CAST(ReceiptDate AS DATE) <= ?");
            params.add(java.sql.Date.valueOf(toDate));
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToImportReceipt(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = new DBContext("SWP7").getConnection()) {
            ImportReceiptDAO dao = new ImportReceiptDAO(conn);

//        ImportReceipt newReceipt = new ImportReceipt(
//                 "4", "1", "1", "1",
//                new Timestamp(System.currentTimeMillis()), 2500000f, "Test phiếu nhập", true
//        );//dao.insertImportReceipt(newReceipt);
            System.out.println("Cập nhật thành Công");
            // Lấy tất cả
            //   dao.getAllImportReceipts().forEach(System.out::println);

            // Lấy theo ID
            // ImportReceipt r = dao.getImportReceiptByID(2001);
            // System.out.println("🔍 Tìm thấy: " + r);
            // Cập nhật
            //  if (r != null) {
            //      r.setNote("Đã sửa nội dung");
            //       r.setTotalAmount(2700000f);
            //        dao.updateImportReceipt(r);
            //     }
            // Xóa
            dao.deleteImportReceipt(9);
            List<ImportReceipt> list = dao.getAllImportReceipts();
            for (ImportReceipt im : list) {
                System.out.println("id:=" + im.getImportReceiptID());
            }
            //  } catch (SQLException e) {
            //     Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
