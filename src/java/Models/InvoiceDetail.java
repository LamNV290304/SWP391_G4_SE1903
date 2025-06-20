/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;

/**
 *
 * @author duckh
 */
public class InvoiceDetail {

    private int invoiceDetailID;
    private int invoiceID;
    private int productID;
    private BigDecimal unitPrice;
    private int quantity;
    private Double discount;
    private Double totalPrice;

    private int shopID;
    private Product product;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int invoiceDetailID, int invoiceID, int productID, BigDecimal unitPrice, int quantity, Double discount, Double totalPrice, Product product) {
        this.invoiceDetailID = invoiceDetailID;
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
        this.totalPrice = totalPrice; // Gán trực tiếp totalPrice từ DB
        this.product = product; // Gán đối tượng Product
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public InvoiceDetail(int invoiceID, int productID, BigDecimal unitPrice, int quantity, Double discount) {
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;

        this.discount = (discount != null) ? discount : 0.0;
        calculateTotalPrice();

    }

    public InvoiceDetail(int invoiceDetailID, int invoiceID, int productID, BigDecimal unitPrice, int quantity, Double discount) {
        this.invoiceDetailID = invoiceDetailID;
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = (discount != null) ? discount : 0;
        calculateTotalPrice();

    }

    public void calculateTotalPrice() {
        if (this.unitPrice != null) {
            double discountValue = (this.discount != null) ? this.discount : 0.0;
            double discountRate = 1 - discountValue / 100.0;

            // Chuyển BigDecimal unitPrice sang double để tính toán, đây là điểm cần cân nhắc
            this.totalPrice = this.unitPrice.doubleValue() * this.quantity * discountRate;
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

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

}
