/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Thai Anh
 */
public class PaymentMethod {
     private int paymentMethodID;
    private String methodName;
    private boolean status;

    // Constructors
    public PaymentMethod() {}

    public PaymentMethod(int paymentMethodID, String methodName, boolean status) {
        this.paymentMethodID = paymentMethodID;
        this.methodName = methodName;
        this.status = status;
    }

    // Getters and Setters
    public int getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(int paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" + "paymentMethodID=" + paymentMethodID + ", methodName=" + methodName + ", status=" + status + '}';
    }
    
}
