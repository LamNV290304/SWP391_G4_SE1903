    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package Dal;

    import Models.InvoiceDetail;
    import java.sql.Connection;
    import java.util.ArrayList;
    import java.util.List;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    /**
     *
     * @author duckh
     */
    public class InvoiceDetailDAO {

        private Connection connection;

        public InvoiceDetailDAO(Connection connection) {
            this.connection = connection;
        }

        public List<InvoiceDetail> getAllInvoiceDetail(String sql) {
            List<InvoiceDetail> list = new ArrayList<>();
            PreparedStatement ptm;
            try {
                ptm = connection.prepareStatement(sql);
                ResultSet rs = ptm.executeQuery();
                while (rs.next()) {
                    list.add(new InvoiceDetail(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getDouble(5),
                            rs.getDouble(6)));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return list;
        }

    }
