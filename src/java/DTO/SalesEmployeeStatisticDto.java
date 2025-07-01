/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author duckh
 */
public class SalesEmployeeStatisticDto {
     private int employeeID;
    private String fullName;
    private double totalRevenue;
    private int totalOrders;

    public SalesEmployeeStatisticDto() {
    }

    public SalesEmployeeStatisticDto(int employeeID, String fullName, double totalRevenue, int totalOrders) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
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

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
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
