/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class ShopSubscriptionDTO {
    private int id;
    private int shopOwnerId;
    private int packageId;
    private int paymentId;
    private Date startDate, endDate;
    private boolean isActive;
    private String note;
    private String packageName;

    public ShopSubscriptionDTO(int id, int shopOwnerId, int packageId, int paymentId, Date startDate, Date endDate, boolean isActive, String note, String packageName) {
        this.id = id;
        this.shopOwnerId = shopOwnerId;
        this.packageId = packageId;
        this.paymentId = paymentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.note = note;
        this.packageName = packageName;
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

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
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
    
    
}
