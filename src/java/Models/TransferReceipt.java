/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class TransferReceipt {
    private String TransferReceiptID;
    private String ProductID;
    private String FromInventoryID;
    private String ToInventoryID;
    private int Quantity;
    private Date TransferDate;
    private String Note;

    public TransferReceipt() {
    }

    public TransferReceipt(String TransferReceiptID, String ProductID, String FromInventoryID, String ToInventoryID, int Quantity, Date TransferDate, String Note) {
        this.TransferReceiptID = TransferReceiptID;
        this.ProductID = ProductID;
        this.FromInventoryID = FromInventoryID;
        this.ToInventoryID = ToInventoryID;
        this.Quantity = Quantity;
        this.TransferDate = TransferDate;
        this.Note = Note;
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

    public String getFromInventoryID() {
        return FromInventoryID;
    }

    public void setFromInventoryID(String FromInventoryID) {
        this.FromInventoryID = FromInventoryID;
    }

    public String getToInventoryID() {
        return ToInventoryID;
    }

    public void setToInventoryID(String ToInventoryID) {
        this.ToInventoryID = ToInventoryID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public Date getTransferDate() {
        return TransferDate;
    }

    public void setTransferDate(Date TransferDate) {
        this.TransferDate = TransferDate;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }
}
