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
    private int TransferReceiptID;
    private int ProductID;
    private int Quantity;

    public TransferReceiptDetail(int TransferReceiptID, int ProductID, int Quantity) { 
        this.TransferReceiptID = TransferReceiptID;
        this.ProductID = ProductID;
        this.Quantity = Quantity;
    }

    public TransferReceiptDetail(int TransferReceiptDetailID, int TransferReceiptID, int ProductID, int Quantity) {
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

    public int getTransferReceiptID() {
        return TransferReceiptID;
    }

    public void setTransferReceiptID(int TransferReceiptID) {
        this.TransferReceiptID = TransferReceiptID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
}
