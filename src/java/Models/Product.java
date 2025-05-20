/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class Product {
    private String ProductID;
    private String ProductName;
    private String CategoryID;
    private String UnitID;
    private double Price;
    private String Description;
    private int Status;
    private Date CreatedDate;
    private String CreatedBy;

    public Product(String ProductID, String ProductName, String CategoryID, String UnitID, double Price, String Description, int Status, Date CreatedDate, String CreatedBy) {
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.CategoryID = CategoryID;
        this.UnitID = UnitID;
        this.Price = Price;
        this.Description = Description;
        this.Status = Status;
        this.CreatedDate = CreatedDate;
        this.CreatedBy = CreatedBy;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String UnitID) {
        this.UnitID = UnitID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }
}
