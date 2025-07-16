package Models;

import java.math.BigDecimal;

/**
 * Model class for SalarySetting table Represents salary configuration for
 * employees
 */
public class SalarySetting {

    private int salarySettingID;
    private int employeeID;
    private String salaryType;  // 'PerShift', 'PerHour', 'FixedMonthly'
    private BigDecimal amount;

    // Additional fields for JOIN queries
    private String fullName;    // Employee full name (from Employee table)
    private String username;    // Employee username (from Employee table)

    // Default constructor
    public SalarySetting() {
    }

    // Constructor without ID (for inserting new records)
    public SalarySetting(int employeeID, String salaryType, BigDecimal amount) {
        this.employeeID = employeeID;
        this.salaryType = salaryType;
        this.amount = amount;
    }

    // Full constructor
    public SalarySetting(int salarySettingID, int employeeID, String salaryType, BigDecimal amount) {
        this.salarySettingID = salarySettingID;
        this.employeeID = employeeID;
        this.salaryType = salaryType;
        this.amount = amount;
    }

    // Constructor with employee details (for JOIN queries)
    public SalarySetting(int salarySettingID, int employeeID, String salaryType,
            BigDecimal amount, String fullName, String username) {
        this.salarySettingID = salarySettingID;
        this.employeeID = employeeID;
        this.salaryType = salaryType;
        this.amount = amount;
        this.fullName = fullName;
        this.username = username;
    }

    // Getters and Setters
    public int getSalarySettingID() {
        return salarySettingID;
    }

    public void setSalarySettingID(int salarySettingID) {
        this.salarySettingID = salarySettingID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Utility methods
    public String getSalaryTypeDisplayName() {
        switch (salaryType) {
            case "PerShift":
                return "Theo ca làm việc";
            case "PerHour":
                return "Theo giờ";
            case "FixedMonthly":
                return "Lương cố định tháng";
            default:
                return salaryType;
        }
    }

    public String getAmountDisplayText() {
        String suffix = "";
        switch (salaryType) {
            case "PerShift":
                suffix = "/ca";
                break;
            case "PerHour":
                suffix = "/giờ";
                break;
            case "FixedMonthly":
                suffix = "/tháng";
                break;
        }
        return String.format("%,.0f₫%s", amount, suffix);
    }

    @Override
    public String toString() {
        return "SalarySetting{"
                + "salarySettingID=" + salarySettingID
                + ", employeeID=" + employeeID
                + ", salaryType='" + salaryType + '\''
                + ", amount=" + amount
                + ", fullName='" + fullName + '\''
                + ", username='" + username + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SalarySetting that = (SalarySetting) o;

        return salarySettingID == that.salarySettingID;
    }

    @Override
    public int hashCode() {
        return salarySettingID;
    }
}
