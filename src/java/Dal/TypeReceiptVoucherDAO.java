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

    public List<TypeReceiptVoucher> getAllTypes() throws SQLException {
        List<TypeReceiptVoucher> list = new ArrayList<>();
        String sql = "SELECT * FROM TypeReceiptVoucher";
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
}
