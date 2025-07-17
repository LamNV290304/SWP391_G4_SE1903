/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author Thai Anh
 */
public class ExportReceipt {
    private int exportReceiptID;
    private int employeeID;
    private int shopID;
    private Date receiptDate;
    private BigDecimal totalAmount;
    private String note;
    private Boolean status;
    private int typeID;

    public ExportReceipt() {
    }

    public ExportReceipt(int exportReceiptID, int employeeID, int shopID, Date receiptDate, BigDecimal totalAmount, String note, Boolean status, int typeID) {
        this.exportReceiptID = exportReceiptID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.receiptDate = receiptDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
        this.typeID = typeID;
    }

    public ExportReceipt(int employeeID, int shopID, Date receiptDate, BigDecimal totalAmount, String note, Boolean status, int typeID) {
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.receiptDate = receiptDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
        this.typeID = typeID;
    }

    public int getExportReceiptID() {
        return exportReceiptID;
    }

    public void setExportReceiptID(int exportReceiptID) {
        this.exportReceiptID = exportReceiptID;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    
}
