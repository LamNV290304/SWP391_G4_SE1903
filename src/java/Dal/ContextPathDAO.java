/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Dictionary;

/**
 *
 * @author Admin
 */
public class ContextPathDAO {
    private Connection connection;
    
    public ContextPathDAO (Connection connection){
        this.connection = connection;
    }
    
    
    
}
