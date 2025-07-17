/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import Models.TypePaymentVoucher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Thai Anh
 */
public class TypePaymentVoucherDAO {
      private Connection conn;

    public TypePaymentVoucherDAO(Connection conn) {
        this.conn = conn;
    }

    public List<TypePaymentVoucher> getAllTypes(int status) throws SQLException {
        List<TypePaymentVoucher> list = new ArrayList<>();
        String sql;
        if(status==1){
         sql = "SELECT * FROM TypePaymentVoucher where Status =1";}
        else if(status==0){
        sql = "SELECT * FROM TypePaymentVoucher where Status =0";}
        else{
            sql = "SELECT * FROM TypePaymentVoucher";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TypePaymentVoucher(
                        rs.getInt("TypeID"),
                        rs.getString("TypeName")
                ));
            }
        }
        return list;
    }

    public void insertType(String typeName) throws SQLException {
        String sql = "INSERT INTO TypePaymentVoucher(TypeName) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeName);
            ps.executeUpdate();
        }
    }
     public void updateType(int typeId, String newName) throws SQLException {
        String sql = "UPDATE TypePaymentVoucher SET TypeName = ? WHERE TypeID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, typeId);
            ps.executeUpdate();
        }
    }

    public void deleteType(int typeId) throws SQLException {
        // Xóa mềm
        String sql = "UPDATE TypePaymentVoucher SET Status = 0 WHERE TypeID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            ps.executeUpdate();
        }
    }
}
