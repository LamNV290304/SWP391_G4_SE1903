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
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
        String sql = "INSERT INTO ImportReceipt (ImportReceiptID, Code, SupplierID, EmployeeID, ShopID, ReceiptDate, TotalAmount, Note, Status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ir.getImportReceiptID());
            ps.setString(2, ir.getCode());
            ps.setString(3, ir.getSupplierID());
            ps.setString(4, ir.getEmployeeID());
            ps.setString(5, ir.getShopID());
            ps.setTimestamp(6, new Timestamp(ir.getReceiptDate().getTime()));
            ps.setFloat(7, (float) ir.getTotalAmount());
            ps.setString(8, ir.getNote());
            ps.setBoolean(9, ir.isStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Cập nhật phiếu nhập
    public boolean updateImportReceipt(ImportReceipt ir) {
        String sql = "UPDATE ImportReceipt SET Code = ?, SupplierID = ?, EmployeeID = ?, ShopID = ?, ReceiptDate = ?, TotalAmount = ?, Note = ?, Status = ? " +
                     "WHERE ImportReceiptID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ir.getCode());
            ps.setString(2, ir.getSupplierID());
            ps.setString(3, ir.getEmployeeID());
            ps.setString(4, ir.getShopID());
            ps.setTimestamp(5, new Timestamp(ir.getReceiptDate().getTime()));
            ps.setFloat(6, (float) ir.getTotalAmount());
            ps.setString(7, ir.getNote());
            ps.setBoolean(8, ir.isStatus());
            ps.setInt(9, ir.getImportReceiptID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // Xóa phiếu nhập
    public boolean deleteImportReceipt(int id) {
        String sql = "DELETE FROM ImportReceiptDetail WHERE ImportReceiptID = ?;\n" +
"DELETE FROM ImportReceipt WHERE ImportReceiptID = ?;";
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
        return new ImportReceipt(
                rs.getInt("ImportReceiptID"),
                rs.getString("Code"),
                rs.getString("SupplierID"),
                rs.getString("EmployeeID"),
                rs.getString("ShopID"),
                rs.getTimestamp("ReceiptDate"),
                rs.getFloat("TotalAmount"),
                rs.getString("Note"),
                rs.getBoolean("Status")
        );
    }
    public static void main(String[] args) throws SQLException {
    try (Connection conn = new DBContext("SWP7").getConnection()) {
        ImportReceiptDAO dao = new ImportReceiptDAO(conn);

        
        ImportReceipt newReceipt = new ImportReceipt(
                1234, "IMP1234", "SP001", "E001", "S001",
                new Timestamp(System.currentTimeMillis()), 2500000f, "Test phiếu nhập", true
        );//dao.insertImportReceipt(newReceipt);
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
     dao.deleteImportReceipt(123);
      List<ImportReceipt> list = dao.getAllImportReceipts();
     for(ImportReceipt im : list){
        System.out.println("id:="+im.getImportReceiptID());
      }
  //  } catch (SQLException e) {
   //     Logger.getLogger(ImportReceiptDAO.class.getName()).log(Level.SEVERE, null, e);
    }
}

}
