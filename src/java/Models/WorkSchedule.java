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
public class WorkSchedule {

    private int WorkScheduleID, EmployeeID, ShopID, ShiftID;
    private Date WorkDate, CreatedDate;
    private int Status;
    private String Note, CreatedBy;

    public WorkSchedule() {
    }
    
    public WorkSchedule( int EmployeeID, int ShopID, int ShiftID, Date WorkDate, int Status, String Note, String CreatedBy) {
        this.EmployeeID = EmployeeID;
        this.ShopID = ShopID;
        this.ShiftID = ShiftID;
        this.WorkDate = WorkDate;
        this.Status = Status;
        this.Note = Note;
        this.CreatedBy = CreatedBy;
    }
    
    public WorkSchedule(int WorkScheduleID, int EmployeeID, int ShopID, int ShiftID, Date WorkDate, int Status, String Note, Date CreatedDate, String CreatedBy) {
        this.WorkScheduleID = WorkScheduleID;
        this.EmployeeID = EmployeeID;
        this.ShopID = ShopID;
        this.ShiftID = ShiftID;
        this.WorkDate = WorkDate;
        this.CreatedDate = CreatedDate;
        this.Status = Status;
        this.Note = Note;
        this.CreatedBy = CreatedBy;
    }

    public int getWorkScheduleID() {
        return WorkScheduleID;
    }

    public void setWorkScheduleID(int WorkScheduleID) {
        this.WorkScheduleID = WorkScheduleID;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int ShopID) {
        this.ShopID = ShopID;
    }

    public int getShiftID() {
        return ShiftID;
    }

    public void setShiftID(int ShiftID) {
        this.ShiftID = ShiftID;
    }

    public Date getWorkDate() {
        return WorkDate;
    }

    public void setWorkDate(Date WorkDate) {
        this.WorkDate = WorkDate;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

}
