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
public class Employee extends User {

    private int roleId;
    private Role role;
    private int shopId;
    private Shop shop;

    public Employee(int roleId, Role role, int shopId, Shop shop, int Id, String Username, String Password, String Fullname, String Phone, String Email, boolean Status, Date CreateDate) {
        super(Id, Username, Password, Fullname, Phone, Email, Status, CreateDate);
        this.roleId = roleId;
        this.role = role;
        this.shopId = shopId;
        this.shop = shop;
    }

    public Employee(int roleId, int shopId, int Id, String Username, String Password, String Fullname, String Phone, String Email, boolean Status, Date CreateDate) {
        super(Id, Username, Password, Fullname, Phone, Email, Status, CreateDate);
        this.roleId = roleId;
        this.role = null;
        this.shopId = shopId;
        this.shop = null;
    }

    public Employee(int Id, String Username, String Password, String Fullname, String Email, String Phone, int roleId, int shopId, boolean Status, Date CreateDate) {
        super(Id, Username, Password, Fullname, Phone, Email, Status, CreateDate);
        this.roleId = roleId;
        this.role = null;
        this.shopId = shopId;
        this.shop = null;
    }

    public Employee() {
        super();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

}
