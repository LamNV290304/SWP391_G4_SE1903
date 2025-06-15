/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;
import java.sql.Timestamp;
/**
 *
 * @author duckh
 */
public class Customer {
    private int customerID;
    private String customerName;
    private String phone;
    private String email;
    private String address;
    private boolean status;
    private Timestamp createdDate;
    private String createdBy;

    public Customer() {
    }

    public Customer(int customerID, String customerName, String phone, String email, String address, boolean status, Timestamp createdDate, String createdBy) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public Customer(String customerName, String phone, String email, String address, boolean status, Timestamp createdDate, String createdBy) {
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    
}
