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

    public List<TypePaymentVoucher> getAllTypes() throws SQLException {
        List<TypePaymentVoucher> list = new ArrayList<>();
        String sql = "SELECT * FROM TypePaymentVoucher";
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
}
