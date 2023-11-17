package de.swtpro.factorybuilder.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class Cube implements Serializable{
    
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    private int sideLengthA;
    
    private int sideLengthB;

    private int posX;

    private int posY;
    
    private int posZ;

    private String orientation;

    // where is the input coming from
    private String inOrientation;

    // where is the output going
    private String outOrientation;

    // how many items are needed / can be stored in a machine
    private int capacity;

    private String modelId;


    public Cube() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getSideLengthA() {
        return sideLengthA;
    }

    public void setSideLengthA(int sideLengthA) {
        this.sideLengthA = sideLengthA;
    }

    public int getSideLengthB() {
        return sideLengthB;
    }

    public void setSideLengthB(int sideLengthB) {
        this.sideLengthB = sideLengthB;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public void setPosZ(int posZ) {
        this.posZ = posZ;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getInOrientation() {
        return inOrientation;
    }

    public void setInOrientation(String inOrientation) {
        this.inOrientation = inOrientation;
    }

    public String getOutOrientation() {
        return outOrientation;
    }

    public void setOutOrientation(String outOrientation) {
        this.outOrientation = outOrientation;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

}
