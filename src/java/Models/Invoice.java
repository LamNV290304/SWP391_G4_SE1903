/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.Timestamp;

/**
 *
 * @author duckh
 */
public class Invoice {

    private int invoiceID;
    private int customerID;
    private String customerName;
    private int employeeID;
    private int shopID;
    private Timestamp invoiceDate;
    private Double totalAmount;
    private String note;
    private boolean status;

    private String shopName;
    
    public Invoice() {
    }

    public Invoice(int invoiceID, int customerID, String customerName, int employeeID, int shopID, Timestamp invoiceDate, Double totalAmount, String note, boolean status, String shopName) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
        this.shopName = shopName;
    }

    public Invoice(int invoiceID, int customerID, String customerName, int employeeID, int shopID, Timestamp invoiceDate, Double totalAmount, String note, boolean status) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
    }

    public Invoice(int customerID, int employeeID, int shopID, Timestamp invoiceDate, Double totalAmount, String note, boolean status) {
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
    }

    public Invoice(int invoiceID, int customerID, int employeeID, int shopID, Timestamp invoiceDate, Double totalAmount, String note, boolean status) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
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

}
