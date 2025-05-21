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

    private String invoiceID;
    private String customerID;
    private String EmployeeID;
    private String ShopID;
    private Timestamp InvoiceDate;
    private Double totalAmount;
    private String note;
    private boolean status;

    public Invoice() {
    }

    public Invoice(String invoiceID, String customerID, String EmployeeID, String ShopID, Timestamp InvoiceDate, Double totalAmount, String note, boolean status) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.EmployeeID = EmployeeID;
        this.ShopID = ShopID;
        this.InvoiceDate = InvoiceDate;
        this.totalAmount = totalAmount;
        this.note = note;
        this.status = status;
    }


    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String ShopID) {
        this.ShopID = ShopID;
    }

    public Timestamp getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(Timestamp InvoiceDate) {
        this.InvoiceDate = InvoiceDate;
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
