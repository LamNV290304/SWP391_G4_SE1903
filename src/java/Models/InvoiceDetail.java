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
    private String produtctID;
    private int quantity;
    private Double discount;
    private Double finalPrice;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int invoiceDetailID, String invoiceID, String produtctID, int quantity, Double discount, Double finalPrice) {
        this.invoiceDetailID = invoiceDetailID;
        this.invoiceID = invoiceID;
        this.produtctID = produtctID;
        this.quantity = quantity;
        this.discount = discount;
        this.finalPrice = finalPrice;
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

    public String getProdutctID() {
        return produtctID;
    }

    public void setProdutctID(String produtctID) {
        this.produtctID = produtctID;
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

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    
}
