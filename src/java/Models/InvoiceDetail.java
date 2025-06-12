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
    private String productID;
    private Double unitPrice;
    private int quantity;
    private Double discount;
    private Double totalPrice;

    private String shopID;

    public InvoiceDetail() {
    }

    public InvoiceDetail(String invoiceID, String productID, Double unitPrice, int quantity, Double discount) {
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = (discount != null) ? discount : 0.0;
        calculateTotalPrice();
    }

    public InvoiceDetail(int invoiceDetailID, String invoiceID, String productID, Double unitPrice, int quantity, Double discount) {
        this.invoiceDetailID = invoiceDetailID;
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = (discount != null) ? discount : 0.0;
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        if (unitPrice != null) {
            double discountValue = (discount != null) ? discount : 0.0;
            double discountRate = 1 - discountValue / 100.0;
            this.totalPrice = unitPrice * quantity * discountRate;
        } else {
            this.totalPrice = 0.0;
        }
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

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
        calculateTotalPrice();
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }
}
