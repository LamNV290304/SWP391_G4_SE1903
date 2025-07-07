/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class ShopSubscription {

    private int id;
    private int packageId;
    private int shopOwnerId;
    private Date startDate;
    private Date endDate;
    private int packageDurationInDays;

    public ShopSubscription() {
    }

    public ShopSubscription(int id, int packageId, int shopOwnerId, Date startDate, Date endDate, int packageDurationInDays) {
        this.id = id;
        this.packageId = packageId;
        this.shopOwnerId = shopOwnerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.packageDurationInDays = packageDurationInDays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(int shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
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

    public int getPackageDurationInDays() {
        return packageDurationInDays;
    }

    public void setPackageDurationInDays(int packageDurationInDays) {
        this.packageDurationInDays = packageDurationInDays;
    }
    
    

}
