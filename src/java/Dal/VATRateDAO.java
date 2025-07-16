/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.VATRate;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author duckh
 */
public class VATRateDAO {

    private Connection connection;

    public VATRateDAO(Connection connection) {
        this.connection = connection;
    }

    public List<VATRate> getAllVATRates() {
        List<VATRate> vatRates = new ArrayList<>();
        String sql = "SELECT [VATRateID]\n"
                + "      ,[Rate]\n"
                + "  FROM [dbo].[VATRates]";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VATRate vatRate = new VATRate();
                vatRate.setVATRateID(rs.getInt("VATRateID"));
                vatRate.setRate(rs.getBigDecimal("Rate"));
                vatRates.add(vatRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vatRates;
    }

    public VATRate getVATRateByRate(BigDecimal rate) {
        VATRate vatRate = null;
        String sql = "SELECT [VATRateID], [Rate] FROM [dbo].[VATRates] WHERE [Rate] = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, rate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vatRate = new VATRate();
                    vatRate.setVATRateID(rs.getInt("VATRateID"));
                    vatRate.setRate(rs.getBigDecimal("Rate"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting VATRate by rate: " + e.getMessage());
            e.printStackTrace();
        }
        return vatRate;
    }

    public VATRate getVATRateById(int vatRateID) {
        String sql = "SELECT VATRateID, Rate FROM VATRates WHERE VATRateID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, vatRateID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VATRate vatRate = new VATRate();
                    vatRate.setVATRateID(rs.getInt("VATRateID"));
                    vatRate.setRate(rs.getBigDecimal("Rate"));
                    return vatRate;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Connection dbConnection = null;
        DBContext dbContext = new DBContext("SWP1");
        dbConnection = dbContext.getConnection();
        VATRateDAO vatRateDAO = new VATRateDAO(dbConnection);
        List<VATRate> l = vatRateDAO.getAllVATRates();
        for (VATRate vATRate : l) {
            System.out.println(vATRate.getRate());
        }
    }
}
