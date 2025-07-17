/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Models.TypeReceiptVoucher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Thai Anh
 */
public class TypeReceiptVoucherDAO {
       private Connection conn;

    public TypeReceiptVoucherDAO(Connection conn) {
        this.conn = conn;
    }

    public List<TypeReceiptVoucher> getAllTypes( int status) throws SQLException {
        List<TypeReceiptVoucher> list = new ArrayList<>();
        String sql;
        if(status==1){
         sql = "SELECT * FROM TypeReceiptVoucher where Status =1";}
        else if(status==0){
        sql = "SELECT * FROM TypeReceiptVoucher where Status =0";}
        else{
            sql = "SELECT * FROM TypeReceiptVoucher";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TypeReceiptVoucher(
                        rs.getInt("TypeID"),
                        rs.getString("TypeName")
                ));
            }
        }
        return list;
    }

    public void insertType(String typeName) throws SQLException {
        String sql = "INSERT INTO TypeReceiptVoucher(TypeName) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeName);
            ps.executeUpdate();
        }
    }
      public void updateType(int typeId, String newName) throws SQLException {
        String sql = "UPDATE TypeReceiptVoucher SET TypeName = ? WHERE TypeID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, typeId);
            ps.executeUpdate();
        }
    }

    public void deleteType(int typeId) throws SQLException {
        // Xóa mềm: cập nhật trạng thái về 0
        String sql = "UPDATE TypeReceiptVoucher SET Status = 0 WHERE TypeID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            ps.executeUpdate();
        }
    }
}
