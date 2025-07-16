/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class ShopOwnerRevenuaDto {
    private int shopId;
    private String shopName;
    private BigDecimal totalRevenue;

    public ShopOwnerRevenuaDto() {
    }

    public ShopOwnerRevenuaDto(int shopId, String shopName, BigDecimal totalRevenue) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.totalRevenue = totalRevenue;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
