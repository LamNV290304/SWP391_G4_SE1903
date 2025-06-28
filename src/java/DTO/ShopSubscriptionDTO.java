/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Admin
 */
public class ShopSubscriptionDTO {

    private int id;
    private int shopOwnerId;
    private int packageId;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private String note;
    private String shopName;
    private String packageName;
    private BigDecimal packagePrice;
    private String packageDescription;

    public ShopSubscriptionDTO(int id, int shopOwnerId, int packageId, Date startDate, Date endDate, boolean isActive, String note, String shopName, String packageName, BigDecimal packagePrice, String packageDescription) {
        this.id = id;
        this.shopOwnerId = shopOwnerId;
        this.packageId = packageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.note = note;
        this.shopName = shopName;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packageDescription = packageDescription;
    }



    public ShopSubscriptionDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(int shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(BigDecimal packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }
    
}
