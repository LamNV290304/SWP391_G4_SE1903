/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author duckh
 */
public class ShopItem {

    private int itemId;
    private String itemName;

    private int categoryId; 
    private int quantity;
    private int unitId;   
    private BigDecimal price;
    private Timestamp itemDate;
    private Integer shopId;
    private String notes;

    
    private ItemCategory category;
    private Unit unit;
    private Shop shop;

    public ShopItem() {
    }


    public ShopItem(int itemId, String itemName, int categoryId, int quantity, int unitId, BigDecimal price, Timestamp itemDate, Integer shopId, String notes) {
        this.itemId = itemId;
        this.itemName = itemName;
     
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.unitId = unitId;
        this.price = price;
        this.itemDate = itemDate;
        this.shopId = shopId;
        this.notes = notes;
      
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getItemDate() {
        return itemDate;
    }

    public void setItemDate(Timestamp itemDate) {
        this.itemDate = itemDate;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

   

    
    

   
}
