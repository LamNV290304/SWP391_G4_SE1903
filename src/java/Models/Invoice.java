/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;
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
    private String employeeName;
    private int shopID;
    private Timestamp invoiceDate;
    private BigDecimal totalAmount;
    private BigDecimal VatAmount;
    private int vatRateID;
    private String note;
    private boolean status;

    private String shopName;

    public Invoice() {
    }

    public Invoice(int invoiceID, int customerID, int employeeID, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, BigDecimal VatAmount, int vatRateID, String note, boolean status) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.VatAmount = VatAmount;
        this.vatRateID = vatRateID;
        this.note = note;
        this.status = status;
    }
    

    public Invoice(int invoiceID, int customerID, String customerName, int employeeID, String employeeName, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, String note, boolean status, String shopName) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
        this.shopName = shopName;
    }

    public Invoice(int invoiceID, int customerID, String customerName, int employeeID, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, String note, boolean status, String shopName) {
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

    public Invoice(int invoiceID, int customerID, String customerName, int employeeID, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, String note, boolean status) {
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

    public Invoice(int customerID, int employeeID, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, String note, boolean status) {
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
    }

    public Invoice(int invoiceID, int customerID, int employeeID, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, String note, boolean status) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
    }

    public Invoice(int invoiceID, int customerID, String customerName, int employeeID, String employeeName, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, BigDecimal VatAmount, int vatRateID, String note, boolean status, String shopName) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.VatAmount = VatAmount;
        this.vatRateID = vatRateID;
        this.note = note;
        this.status = status;
        this.shopName = shopName;
    }

    public Invoice(int customerID, int employeeID, int shopID, Timestamp invoiceDate, BigDecimal totalAmount, BigDecimal VatAmount, int vatRateID, String note, boolean status) {
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.shopID = shopID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.VatAmount = VatAmount;
        this.vatRateID = vatRateID;
        this.note = note;
        this.status = status;

    }

    public int getVatRateID() {
        return vatRateID;
    }

    public void setVatRateID(int vatRateID) {
        this.vatRateID = vatRateID;
    }

    public BigDecimal getVatAmount() {
        return VatAmount;
    }

    public void setVatAmount(BigDecimal VatAmount) {
        this.VatAmount = VatAmount;
    }

    public BigDecimal getTotalAmountWithVAT() {
        // Chuyển đổi totalAmount sang BigDecimal trước khi cộng để đảm bảo độ chính xác
        BigDecimal totalAmountDecimal = (totalAmount != null) ? new BigDecimal(totalAmount.toString()) : BigDecimal.ZERO;
        BigDecimal finalVatAmount = (VatAmount != null) ? VatAmount : BigDecimal.ZERO;

        return totalAmountDecimal.add(finalVatAmount);
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
