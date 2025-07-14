/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigDecimal;

/**
 *
 * @author duckh
 */
public class ShopTotalValueDto {

    private int shopId;
    private String shopName;
    private BigDecimal totalValue;

    public ShopTotalValueDto() {
    }

    public ShopTotalValueDto(int shopId, String shopName, BigDecimal totalValue) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.totalValue = totalValue;
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

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
    
}
