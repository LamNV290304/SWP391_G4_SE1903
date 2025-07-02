/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Context.DBContext;
import Models.ServicePackage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ServicePackageDAO {

    private Connection connection;

    public ServicePackageDAO(Connection connection) {
        this.connection = connection;
    }

    public List<ServicePackage> getAll() {
        List<ServicePackage> packages = new ArrayList<>();
        String sql = "SELECT * FROM ServicePackages";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ServicePackage pkg = new ServicePackage();
                pkg.setId(rs.getInt("Id"));
                pkg.setName(rs.getString("Name"));
                pkg.setDurationInDays(rs.getInt("DurationInDays"));
                pkg.setPrice(rs.getDouble("Price"));
                pkg.setDescription(rs.getString("Description"));
                packages.add(pkg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return packages;
    }

    public ServicePackage getById(int id) {
        String sql = "SELECT * FROM ServicePackages WHERE Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ServicePackage(
                            rs.getInt("Id"),
                            rs.getString("Name"),
                            rs.getInt("DurationInDays"),
                            rs.getDouble("Price"),
                            rs.getString("Description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(ServicePackage pkg) {
        String sql = "INSERT INTO ServicePackages (Name, DurationInDays, Price, Description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pkg.getName());
            ps.setInt(2, pkg.getDurationInDays());
            ps.setDouble(3, pkg.getPrice());
            ps.setString(4, pkg.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ServicePackage pkg) {
        String sql = "UPDATE ServicePackages SET Name=?, DurationInDays=?, Price=?, Description=? WHERE Id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pkg.getName());
            ps.setInt(2, pkg.getDurationInDays());
            ps.setDouble(3, pkg.getPrice());
            ps.setString(4, pkg.getDescription());
            ps.setInt(5, pkg.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM ServicePackages WHERE Id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePackage(ServicePackage pkg) {
        String sql = "UPDATE ServicePackages SET Name = ?, DurationInDays = ?, Price = ?, Description = ? WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, pkg.getName());
            ps.setInt(2, pkg.getDurationInDays());
            ps.setDouble(3, pkg.getPrice());
            ps.setString(4, pkg.getDescription());
            ps.setInt(5, pkg.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createPackage(ServicePackage pkg) {
        String sql = "INSERT INTO ServicePackages (Name, DurationInDays, Price, Description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, pkg.getName());
            ps.setInt(2, pkg.getDurationInDays());
            ps.setDouble(3, pkg.getPrice());
            ps.setString(4, pkg.getDescription());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            ServicePackageDAO servicePackageDAO = new ServicePackageDAO(DBContext.getCentralConnection());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServicePackageDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServicePackageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
