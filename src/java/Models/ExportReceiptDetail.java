/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Thai Anh
 */
public class ExportReceiptDetail {
      private int EportReceiptDetailID;
    private int importReceiptID;
    private String productID;
    private int quantity;
    private double price;
    private String note;

    public ExportReceiptDetail() {
    }

    public ExportReceiptDetail(int importReceiptID, String productID, int quantity, double price, String note) {
        this.importReceiptID = importReceiptID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.note = note;
    }

    public int getEportReceiptDetailID() {
        return EportReceiptDetailID;
    }

    public void setEportReceiptDetailID(int EportReceiptDetailID) {
        this.EportReceiptDetailID = EportReceiptDetailID;
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
        return "ExportReceiptDetail{" + "EportReceiptDetailID=" + EportReceiptDetailID + ", importReceiptID=" + importReceiptID + ", productID=" + productID + ", quantity=" + quantity + ", price=" + price + ", note=" + note + '}';
    }

   
}
