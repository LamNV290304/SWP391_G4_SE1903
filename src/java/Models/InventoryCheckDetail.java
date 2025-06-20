/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Thai Anh
 */
public class InventoryCheckDetail {
    private int inventoryCheckDetailID;
    private int inventoryCheckID;
    private int productID;
    private int quantitySystem;
    private int quantityActual;
    private String note;
    public InventoryCheckDetail() {
    }

    public InventoryCheckDetail(int inventoryCheckID, int productID, int quantitySystem, int quantityActual, String note) {
        this.inventoryCheckID = inventoryCheckID;
        this.productID = productID;
        this.quantitySystem = quantitySystem;
        this.quantityActual = quantityActual;
        this.note = note;
    }

    public int getInventoryCheckDetailID() {
        return inventoryCheckDetailID;
    }

    public void setInventoryCheckDetailID(int inventoryCheckDetailID) {
        this.inventoryCheckDetailID = inventoryCheckDetailID;
    }

    public InventoryCheckDetail(int productID, int quantitySystem, int quantityActual, String note) {
        this.productID = productID;
        this.quantitySystem = quantitySystem;
        this.quantityActual = quantityActual;
        this.note = note;
    }

    public int getInventoryCheckID() {
        return inventoryCheckID;
    }

    public void setInventoryCheckID(int inventoryCheckID) {
        this.inventoryCheckID = inventoryCheckID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantitySystem() {
        return quantitySystem;
    }

    public void setQuantitySystem(int quantitySystem) {
        this.quantitySystem = quantitySystem;
    }

    public int getQuantityActual() {
        return quantityActual;
    }

    public void setQuantityActual(int quantityActual) {
        this.quantityActual = quantityActual;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
