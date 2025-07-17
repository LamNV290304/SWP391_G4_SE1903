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
public class PaymentVoucher {
    private int paymentVoucherID;
    private int shopID;
    private int employeeID;
    private Integer supplierID; // optional
    private Date paymentDate;
    private int paymentType;
    private BigDecimal amount;
    private String note;
    private boolean status;
    private Date createdDate;
    private int typeID;
    private int paymentMethodID;

    public PaymentVoucher() {
    }

    public PaymentVoucher(int paymentVoucherID, int shopID, int employeeID, Integer supplierID, Date paymentDate, int paymentType, BigDecimal amount, String note, boolean status, Date createdDate, int typeID, int paymentMethodID) {
        this.paymentVoucherID = paymentVoucherID;
        this.shopID = shopID;
        this.employeeID = employeeID;
        this.supplierID = supplierID;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.amount = amount;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.typeID = typeID;
        this.paymentMethodID = paymentMethodID;
    }

    public PaymentVoucher(int paymentVoucherID, int shopID, int employeeID, Date paymentDate, int paymentType, BigDecimal amount, String note, boolean status, Date createdDate, int typeID, int paymentMethodID) {
        this.paymentVoucherID = paymentVoucherID;
        this.shopID = shopID;
        this.employeeID = employeeID;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.amount = amount;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.typeID = typeID;
        this.paymentMethodID = paymentMethodID;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(int paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }
    

    public int getPaymentVoucherID() {
        return paymentVoucherID;
    }

    public void setPaymentVoucherID(int paymentVoucherID) {
        this.paymentVoucherID = paymentVoucherID;
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

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
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
        return "PaymentVoucher{" + "paymentVoucherID=" + paymentVoucherID + ", shopID=" + shopID + ", employeeID=" + employeeID + ", supplierID=" + supplierID + ", paymentDate=" + paymentDate + ", amount=" + amount + ", note=" + note + ", status=" + status + ", createdDate=" + createdDate + ", typeID=" + typeID + '}';
    }
    
}
