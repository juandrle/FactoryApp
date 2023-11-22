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

    private int width;
    
    private int height;

    // where does the cube start
    private Position rootPos;
    
    // where is the input located
    private Position inPos;
    
    // where is the output located
    private Position outPos;
    
    // where are we on the grid
    private Position fieldPos;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Position getRootPos() {
        return rootPos;
    }

    public void setRootPos(Position rootPos) {
        this.rootPos = rootPos;
    }

    public Position getInPos() {
        return inPos;
    }

    public void setInPos(Position inPos) {
        this.inPos = inPos;
    }

    public Position getOutPos() {
        return outPos;
    }

    public void setOutPos(Position outPos) {
        this.outPos = outPos;
    }

    public Position getFieldPos() {
        return fieldPos;
    }

    public void setFieldPos(Position fieldPos) {
        this.fieldPos = fieldPos;
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
