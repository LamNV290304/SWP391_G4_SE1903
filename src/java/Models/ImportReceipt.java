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
public class ImportReceipt {
    private int importReceiptID;
    private int supplierID;
    private int employeeID;
    private int shopID;
    private Date receiptDate;
    private double totalAmount;
    private String note;
    private boolean status;
private int typeID;
    public ImportReceipt() {
    }

    public ImportReceipt(int supplierID, int employeeID, int shopID, Date receiptDate, double totalAmount, String note, boolean status, int typeID) {
        this.supplierID = supplierID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.receiptDate = receiptDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
        this.typeID = typeID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    

    public int getImportReceiptID() {
        return importReceiptID;
    }

    public void setImportReceiptID(int importReceiptID) {
        this.importReceiptID = importReceiptID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
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

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "ImportReceipt{" + "importReceiptID=" + importReceiptID + ", supplierID=" + supplierID + ", employeeID=" + employeeID + ", shopID=" + shopID + ", receiptDate=" + receiptDate + ", totalAmount=" + totalAmount + ", note=" + note + ", status=" + status + '}';
    }
    
}
