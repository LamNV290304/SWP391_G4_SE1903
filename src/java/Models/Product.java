package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    private String productID;
    private String productName;
    private String categoryID;
    private String unitID;
    private BigDecimal importPrice;
    private BigDecimal sellingPrice;
    private String description;
    private boolean status;
    private String imageUrl;
    private LocalDateTime createdDate;
    private String createdBy;

    
    private String categoryName;
    private String unitDescription;

    
    public Product() {
    }

    public Product(String productID, String productName, String categoryID, String unitID,
            BigDecimal importPrice, BigDecimal sellingPrice, String description,
            boolean status, String imageUrl, String createdBy) {
        this.productID = productID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.unitID = unitID;
        this.importPrice = importPrice;
        this.sellingPrice = sellingPrice;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
        this.createdDate = LocalDateTime.now();
    }

    
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice = importPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }
}
