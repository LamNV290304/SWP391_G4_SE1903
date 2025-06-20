package Models;

public class Unit {

    private Integer unitID;
    private String description;

    
    public Unit() {
    }

    public Unit(Integer unitID, String description) {
        this.unitID = unitID;
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
}
