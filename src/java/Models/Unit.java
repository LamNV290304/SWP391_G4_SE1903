package Models;

public class Unit {

    private String unitID;
    private String description;

    // Constructors
    public Unit() {
    }

    public Unit(String unitID, String description) {
        this.unitID = unitID;
        this.description = description;
    }

    // Getters and Setters
    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
