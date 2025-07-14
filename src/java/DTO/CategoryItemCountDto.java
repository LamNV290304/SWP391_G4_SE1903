/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author duckh
 */
public class CategoryItemCountDto {

    private int categoryId;
    private String categoryName;
    private int itemCount;

    public CategoryItemCountDto() {
    }

    public CategoryItemCountDto(int categoryId, String categoryName, int itemCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.itemCount = itemCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    
}
