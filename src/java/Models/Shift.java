/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 *
 * @author ADMIN
 */
public class Shift {

    private int shiftID;
    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private int NumberOfEmployees;

    public Shift(int shiftID, String shiftName, LocalTime startTime, LocalTime endTime, String description, int NumberOfEmployees) {
        this.shiftID = shiftID;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.NumberOfEmployees = NumberOfEmployees;
    }

    public Shift(String shiftName, LocalTime startTime, LocalTime endTime, String description,int NumberOfEmployees) {

        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.NumberOfEmployees = NumberOfEmployees;
    }

    

    public Shift() {
    }


    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getNumberOfEmployees() {
        return NumberOfEmployees;
    }

    public void setNumberOfEmployees(int NumberOfEmployees) {
        this.NumberOfEmployees = NumberOfEmployees;
    }
}
