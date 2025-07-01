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
public class ReceiptVoucher {
   private int receiptVoucherID;
    private int shopID;
    private int employeeID;
    private Integer customerID; // optional
    private Date receiptDate;
    private BigDecimal amount;
    private String note;
    private boolean status;
    private Date createdDate;
    private int typeID;

    public ReceiptVoucher() {
    }

    public ReceiptVoucher(int receiptVoucherID, int shopID, int employeeID, Integer customerID, Date receiptDate, BigDecimal amount, String note, boolean status, Date createdDate, int typeID) {
        this.receiptVoucherID = receiptVoucherID;
        this.shopID = shopID;
        this.employeeID = employeeID;
        this.customerID = customerID;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.typeID = typeID;
    }

    public ReceiptVoucher(int receiptVoucherID, int shopID, int employeeID, Date receiptDate, BigDecimal amount, String note, boolean status, Date createdDate, int typeID) {
        this.receiptVoucherID = receiptVoucherID;
        this.shopID = shopID;
        this.employeeID = employeeID;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.typeID = typeID;
    }

    public int getReceiptVoucherID() {
        return receiptVoucherID;
    }

    public void setReceiptVoucherID(int receiptVoucherID) {
        this.receiptVoucherID = receiptVoucherID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    @Override
    public String toString() {
        return "ReceiptVoucher{" + "receiptVoucherID=" + receiptVoucherID + ", shopID=" + shopID + ", employeeID=" + employeeID + ", customerID=" + customerID + ", receiptDate=" + receiptDate + ", amount=" + amount + ", note=" + note + ", status=" + status + ", createdDate=" + createdDate + ", typeID=" + typeID + '}';
    }
    
}
