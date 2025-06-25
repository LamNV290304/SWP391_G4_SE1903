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
public class Noti {

    private String Title, Message, Link;
    private int ReceiverEmployeeID;
    private Date CreatedDate;
    private int IsRead;

    public Noti() {
        
    }

    public Noti(String Title, String Message, String Link, int ReceiverEmployeeID, Date CreatedDate, int IsRead) {
        this.Title = Title;
        this.Message = Message;
        this.Link = Link;
        this.ReceiverEmployeeID = ReceiverEmployeeID;
        this.IsRead = IsRead;
        this.CreatedDate = CreatedDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }

    public int getReceiverEmployeeID() {
        return ReceiverEmployeeID;
    }

    public void setReceiverEmployeeID(int ReceiverEmployeeID) {
        this.ReceiverEmployeeID = ReceiverEmployeeID;
    }

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int IsRead) {
        this.IsRead = IsRead;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

}
