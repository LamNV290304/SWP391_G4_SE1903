/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author ADMIN
 */
public class TransferReceiptDetail {
    private int TransferReceiptDetailID;
    private String TransferReceiptID;
    private String ProductID;
    private int Quantity;

    public TransferReceiptDetail(int TransferReceiptDetailID, String TransferReceiptID, String ProductID, int Quantity) {
        this.TransferReceiptDetailID = TransferReceiptDetailID;
        this.TransferReceiptID = TransferReceiptID;
        this.ProductID = ProductID;
        this.Quantity = Quantity;
    }

    public int getTransferReceiptDetailID() {
        return TransferReceiptDetailID;
    }

    public void setTransferReceiptDetailID(int TransferReceiptDetailID) {
        this.TransferReceiptDetailID = TransferReceiptDetailID;
    }

    public String getTransferReceiptID() {
        return TransferReceiptID;
    }

    public void setTransferReceiptID(String TransferReceiptID) {
        this.TransferReceiptID = TransferReceiptID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
}
