/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Thai Anh
 */
public class ImportReceiptDetail {
    private int importReceiptDetailID;
    private int importReceiptID;
    private String productID;
    private int quantity;
    private double price;
    private String note;

    public ImportReceiptDetail() {
    }

    public ImportReceiptDetail(int importReceiptID, String productID, int quantity, double price,String note) {
        this.importReceiptID = importReceiptID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.note= note;
    }

    public int getImportReceiptDetailID() {
        return importReceiptDetailID;
    }

    public void setImportReceiptDetailID(int importReceiptDetailID) {
        this.importReceiptDetailID = importReceiptDetailID;
    }

    public int getImportReceiptID() {
        return importReceiptID;
    }

    public void setImportReceiptID(int importReceiptID) {
        this.importReceiptID = importReceiptID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ImportReceiptDetail{" + "importReceiptDetailID=" + importReceiptDetailID + ", importReceiptID=" + importReceiptID + ", productID=" + productID + ", quantity=" + quantity + ", price=" + price + ", note=" + note + '}';
    }
    
}
