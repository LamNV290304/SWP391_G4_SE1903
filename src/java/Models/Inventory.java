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
public class Inventory {
     private int inventoryID;
    private Product product;
    private Shop shop;
    private int quantity;
    private Date lastUpdated;

    public Inventory() {
    }

    public Inventory(Product product, Shop shop, int quantity, Date lastUpdated) {
        this.product = product;
        this.shop = shop;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    // Getter & Setter cho inventoryID
    public int  getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    // ✅ Getter & Setter cho Product
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // ✅ Getter & Setter cho Shop
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    // Getter & Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter & Setter cho lastUpdated
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Inventory{" + "inventoryID=" + inventoryID + ", product=" + product + ", shop=" + shop + ", quantity=" + quantity + ", lastUpdated=" + lastUpdated + '}';
    }
    
}
