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
    private int TransferReceiptID;
    private String FromShopID;
    private String ToShopID;
    private Date TransferDate;
    private String Note;
    private int Status;

    public TransferReceipt( String FromShopID, String ToShopID, Date TransferDate, String Note, int Status) {

        this.FromShopID = FromShopID;
        this.ToShopID = ToShopID;

        this.TransferDate = TransferDate;
        this.Note = Note;
        this.Status = Status;
    }

    public TransferReceipt(int TransferReceiptID, String FromShopID, String ToShopID, Date TransferDate, String Note, int Status) {
        this.TransferReceiptID = TransferReceiptID;

        this.FromShopID = FromShopID;
        this.ToShopID = ToShopID;

        this.TransferDate = TransferDate;
        this.Note = Note;
        this.Status = Status;
    }

    public int getTransferReceiptID() {
        return TransferReceiptID;
    }

    public void setTransferReceiptID(int TransferReceiptID) {
        this.TransferReceiptID = TransferReceiptID;
    }


    public String getFromShopID() {
        return FromShopID;
    }

    public void setFromShopID(String FromShopID) {
        this.FromShopID = FromShopID;
    }

    public String getToShopID() {
        return ToShopID;
    }

    public void setToShopID(String ToShopID) {
        this.ToShopID = ToShopID;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }
}
