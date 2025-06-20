/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

/**
 *
 * @author Thai Anh
 */
public class InventoryCheck {
      private int inventoryCheckID;
    private int employeeID;
    private int shopID;
    private Date checkDate;
    private String note;

    public InventoryCheck() {
    }

    public InventoryCheck(int employeeID, int shopID, Date checkDate, String note) {
        
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.checkDate = checkDate;
        this.note = note;
    }

    public int getInventoryCheckID() {
        return inventoryCheckID;
    }

    public void setInventoryCheckID(int inventoryCheckID) {
        this.inventoryCheckID = inventoryCheckID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "InventoryCheck{" + "inventoryCheckID=" + inventoryCheckID + ", employeeID=" + employeeID + ", shopID=" + shopID + ", checkDate=" + checkDate + ", note=" + note + '}';
    }
    
}
