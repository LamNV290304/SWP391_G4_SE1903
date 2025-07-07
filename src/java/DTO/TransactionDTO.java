/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Admin
 */
public class TransactionDTO {
    public LocalDate date;
    public String packageName;
    public BigDecimal amount;
    public String status;

    public TransactionDTO(LocalDate date, String packageName, BigDecimal amount, String status) {
        this.date = date;
        this.packageName = packageName;
        this.amount = amount;
        this.status = status;
    }

    public TransactionDTO() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
