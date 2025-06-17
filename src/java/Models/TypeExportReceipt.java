package Models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thai Anh
 */
public class TypeExportReceipt {
     private int typeID;
    private String typeName;

    public TypeExportReceipt() {
    }

    public TypeExportReceipt(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "TypeExportReceipt{" + "typeID=" + typeID + ", typeName=" + typeName + '}';
    }
    
    public static void main(String[] args) {
        TypeExportReceipt type = new TypeExportReceipt("haha");
        System.out.println(type.toString());
    }
}
