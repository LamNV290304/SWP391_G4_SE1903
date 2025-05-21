/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author duckh
 */
public class InvoiceDetail {

    private int invoiceDetailID;
    private String invoiceID;
    private int quantity;
    private Double discount;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int invoiceDetailID, String invoiceID, int quantity, Double discount) {
        this.invoiceDetailID = invoiceDetailID;
        this.invoiceID = invoiceID;
        this.quantity = quantity;
        this.discount = discount;
    }

    public int getInvoiceDetailID() {
        return invoiceDetailID;
    }

    public void setInvoiceDetailID(int invoiceDetailID) {
        this.invoiceDetailID = invoiceDetailID;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    
    
}
