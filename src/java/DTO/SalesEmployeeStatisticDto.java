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
public class SalesEmployeeStatisticDto {

    private int employeeID;
    private String fullName;
    private BigDecimal totalRevenue;
    private int totalOrders;
    private BigDecimal averageRevenuePerOrder;

    public SalesEmployeeStatisticDto() {
    }

    public SalesEmployeeStatisticDto(int employeeID, String fullName, BigDecimal totalRevenue, int totalOrders, BigDecimal averageRevenuePerOrder) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.averageRevenuePerOrder = averageRevenuePerOrder;
    }

    public BigDecimal getAverageRevenuePerOrder() {
        return averageRevenuePerOrder;
    }

    public void setAverageRevenuePerOrder(BigDecimal averageRevenuePerOrder) {
        this.averageRevenuePerOrder = averageRevenuePerOrder;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    @Override
    public String toString() {
        return "SalesEmployeeStatisticDto{" + "employeeID=" + employeeID + ", fullName=" + fullName + ", totalRevenue=" + totalRevenue + ", totalOrders=" + totalOrders + '}';
    }

}
