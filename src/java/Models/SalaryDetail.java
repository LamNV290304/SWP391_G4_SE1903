/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;

/**
 *
 * @author ADMIN
 */
public class SalaryDetail {
    private int salaryDetailID;
    private int salaryID;
    private int employeeID;
    private BigDecimal basicSalary;

    // Constructors
    public SalaryDetail() {}

    public SalaryDetail(int salaryID, int employeeID, BigDecimal basicSalary) {
        this.salaryID = salaryID;
        this.employeeID = employeeID;
        this.basicSalary = basicSalary;
    }

    public SalaryDetail(int salaryDetailID, int salaryID, int employeeID, BigDecimal basicSalary) {
        this.salaryDetailID = salaryDetailID;
        this.salaryID = salaryID;
        this.employeeID = employeeID;
        this.basicSalary = basicSalary;
    }

    // Getters and Setters
    public int getSalaryDetailID() {
        return salaryDetailID;
    }

    public void setSalaryDetailID(int salaryDetailID) {
        this.salaryDetailID = salaryDetailID;
    }

    public int getSalaryID() {
        return salaryID;
    }

    public void setSalaryID(int salaryID) {
        this.salaryID = salaryID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }


    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    @Override
    public String toString() {
        return "SalaryDetail{" +
                "salaryDetailID=" + salaryDetailID +
                ", salaryID=" + salaryID +
                ", employeeID=" + employeeID +
                ", basicSalary=" + basicSalary +
                '}';
    }
}
