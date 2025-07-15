/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class Salary {
    private int salaryID;
    private String salaryName;
    private String salaryPeriod;
    private Date workPeriodStart;
    private Date workPeriodEnd;
    private BigDecimal totalSalary;
    private String status;
    private Date CreatedDate;
    private String CreatedBy;

    public Salary(String salaryName, String salaryPeriod, Date workPeriodStart, Date workPeriodEnd, BigDecimal totalSalary, String status, Date CreatedDate, String CreatedBy) {
        this.salaryName = salaryName;
        this.salaryPeriod = salaryPeriod;
        this.workPeriodStart = workPeriodStart;
        this.workPeriodEnd = workPeriodEnd;
        this.totalSalary = totalSalary;
        this.status = status;
        this.CreatedDate = CreatedDate;
        this.CreatedBy = CreatedBy;
    }

    public Salary(int salaryID, String salaryName, String salaryPeriod, Date workPeriodStart, Date workPeriodEnd, BigDecimal totalSalary, String status, Date CreatedDate, String CreatedBy) {
        this.salaryID = salaryID;
        this.salaryName = salaryName;
        this.salaryPeriod = salaryPeriod;
        this.workPeriodStart = workPeriodStart;
        this.workPeriodEnd = workPeriodEnd;
        this.totalSalary = totalSalary;
        this.status = status;
        this.CreatedDate = CreatedDate;
        this.CreatedBy = CreatedBy;
    }

    // Constructors
    public Salary() {}

    

    // Getters and Setters
    public int getSalaryID() {
        return salaryID;
    }

    public void setSalaryID(int salaryID) {
        this.salaryID = salaryID;
    }

    public String getSalaryName() {
        return salaryName;
    }

    public void setSalaryName(String salaryName) {
        this.salaryName = salaryName;
    }

    public String getSalaryPeriod() {
        return salaryPeriod;
    }

    public void setSalaryPeriod(String salaryPeriod) {
        this.salaryPeriod = salaryPeriod;
    }

    public Date getWorkPeriodStart() {
        return workPeriodStart;
    }

    public void setWorkPeriodStart(Date workPeriodStart) {
        this.workPeriodStart = workPeriodStart;
    }

    public Date getWorkPeriodEnd() {
        return workPeriodEnd;
    }

    public void setWorkPeriodEnd(Date workPeriodEnd) {
        this.workPeriodEnd = workPeriodEnd;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "salaryID=" + salaryID +
                ", salaryName='" + salaryName + '\'' +
                ", salaryPeriod='" + salaryPeriod + '\'' +
                ", workPeriodStart=" + workPeriodStart +
                ", workPeriodEnd=" + workPeriodEnd +
                ", totalSalary=" + totalSalary +
                ", status='" + status + '\'' +
                '}';
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }
}
