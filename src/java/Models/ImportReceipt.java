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
    private String code;
    private String supplierID;
    private String employeeID;
    private String shopID;
    private Date receiptDate;
    private double totalAmount;
    private String note;
    private boolean status;

    public ImportReceipt() {
    }

    public ImportReceipt(int importReceiptID, String code, String supplierID, String employeeID, String shopID, Date receiptDate, double totalAmount, String note, boolean status) {
        this.importReceiptID = importReceiptID;
        this.code = code;
        this.supplierID = supplierID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.receiptDate = receiptDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
    }

    public int getImportReceiptID() {
        return importReceiptID;
    }

    public void setImportReceiptID(int importReceiptID) {
        this.importReceiptID = importReceiptID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
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
        return "ImportReceipt{" + "importReceiptID=" + importReceiptID + ", code=" + code + ", supplierID=" + supplierID + ", employeeID=" + employeeID + ", shopID=" + shopID + ", receiptDate=" + receiptDate + ", totalAmount=" + totalAmount + ", note=" + note + ", status=" + status + '}';
    }
    
}
