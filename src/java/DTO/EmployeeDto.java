/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class EmployeeDto {

    private int id;
    private String fullName;
    private String email;
    private String username;
    private String phone;
    private boolean status;
    private Date createdDate;
    private String shopName;
    private String role;

    public EmployeeDto() {
    }

    public EmployeeDto(int id, String fullName, String email, String username, String phone, boolean status, Date createdDate, String shopName, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.status = status;
        this.createdDate = createdDate;
        this.shopName = shopName;
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "EmployeeDto {"
                + "id=" + id
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + ", username='" + username + '\''
                + ", phone='" + phone + '\''
                + ", status=" + status 
                + ", createdDate=" + createdDate
                + ", shopName='" + shopName + '\''
                + ", role='" + role + '\''
                + '}';
    }
}
