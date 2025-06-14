package Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {
    protected Connection connection;

    private static final String SERVER_NAME = "localhost";
    private static final String PORT = "1433";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123abc@A";

    /**
     * Constructor kết nối đến database cụ thể và lưu vào thuộc tính connection.
     * Dùng trong DAO để kế thừa và sử dụng connection.
     */
    public DBContext(String databaseName) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT + ";databaseName=" + databaseName + ";encrypt=false";
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            connection = null;
        }
    }

    /**
     * Constructor mặc định kết nối tới database trung tâm.
     */
    public DBContext() {
        this("CentralDB");
    }
    
    /**
     * Getter cho connection nếu các class con cần truy cập.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Hàm static để lấy connection nhanh tới database bất kỳ.
     */
    public static Connection getConnection(String databaseName) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT + ";databaseName=" + databaseName + ";encrypt=false";
        return DriverManager.getConnection(url, USERNAME, PASSWORD);
    }

    /**
     * Hàm static để lấy connection đến CentralDB.
     */
    public static Connection getCentralConnection() throws ClassNotFoundException, SQLException {
        return getConnection("CentralDB");
    }

    /**
     * Hàm static để lấy connection đến master DB (dùng để tạo database).
     */
    public static Connection getMasterConnection() throws ClassNotFoundException, SQLException {
        return getConnection("master");
    }
}
