/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class ShopOwner extends User{
    private String DatabaseName;
    private String ShopCode;
    private String ShopName;
    private String taxNumber;

    public ShopOwner() {
        super();
    }

    public ShopOwner(String DatabaseName, String ShopCode, String ShopName, String taxNumber, int Id, String Username, String Password, String Fullname, String Phone, String Email, boolean Status, Date CreateDate) {
        super(Id, Username, Password, Fullname, Phone, Email, Status, CreateDate);
        this.DatabaseName = DatabaseName;
        this.ShopCode = ShopCode;
        this.ShopName = ShopName;
        this.taxNumber = taxNumber;
    }    

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
    

    public String getDatabaseName() {
        return DatabaseName;
    }

    public void setDatabaseName(String DatabaseName) {
        this.DatabaseName = DatabaseName;
    }

    public String getShopCode() {
        return ShopCode;
    }

    public void setShopCode(String ShopCode) {
        this.ShopCode = ShopCode;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

}
