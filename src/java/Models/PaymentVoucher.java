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
public class PaymentVoucher {
    private int paymentVoucherID;
    private int shopID;
    private int employeeID;
    private Integer supplierID;
    private Date paymentDate;
    private double amount;
    private String note;
    private boolean status;
    private Date createdDate;
    public PaymentVoucher() {
    }

    public PaymentVoucher(int shopID, int employeeID, Integer supplierID, Date paymentDate,
                          double amount, String note, boolean status, String createdBy, Date createdDate) {
        this.shopID = shopID;
        this.employeeID = employeeID;
        this.supplierID = supplierID;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
    }
    public PaymentVoucher(int shopID, int employeeID, Date paymentDate,
                          double amount, String note, boolean status, String createdBy, Date createdDate) {
        this.shopID = shopID;
        this.employeeID = employeeID;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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

    @Override
    public String toString() {
        return "PaymentVoucher{" + "paymentVoucherID=" + paymentVoucherID + ", shopID=" + shopID + ", employeeID=" + employeeID + ", supplierID=" + supplierID + ", paymentDate=" + paymentDate + ", amount=" + amount + ", note=" + note + ", status=" + status + ", createdDate=" + createdDate + '}';
    }
    
}
