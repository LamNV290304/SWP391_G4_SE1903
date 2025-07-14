/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;

/**
 *
 * @author duckh
 */
public class VATRate {

    private int VATRateID;
    private BigDecimal Rate;

    public VATRate() {
    }

    public VATRate(int VATRateID, BigDecimal Rate) {
        this.VATRateID = VATRateID;
        this.Rate = Rate;
    }

    // Getters and Setters
    public int getVATRateID() {
        return VATRateID;
    }

    public void setVATRateID(int VATRateID) {
        this.VATRateID = VATRateID;
    }

    public BigDecimal getRate() {
        return Rate;
    }

    public void setRate(BigDecimal Rate) {
        this.Rate = Rate;
    }
}
