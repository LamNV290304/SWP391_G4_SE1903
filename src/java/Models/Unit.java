package Models;

public class Unit {

    private int unitID;
    private String description;
    private int status;
    
    public Unit() {
    }

    public Unit(Integer unitID, String description) {
        this.unitID = unitID;
        this.description = description;
    }

    public Unit(String description) {
        this.description = description;
    }
    
    
    public Integer getUnitID() {
        return unitID;
    }

    public void setUnitID(Integer unitID) {
        this.unitID = unitID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}

