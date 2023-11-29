package de.swtpro.factorybuilder.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.swtpro.factorybuilder.utility.Position;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class PlacedModel implements Serializable{
    
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    private long factoryID;

    // where does the cube start
    private Position rootPos;
    
    // where is the input located
    private List<Input> inputs;
    
    // where is the output located
    private List<Output> outputs;
    
    // where are we on the grid
    private Position fieldPos;

    private String orientation;

    // how many items are needed / can be stored in a machine
    private int capacity;

    private long modelId;


    public PlacedModel() {
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


    public Position getRootPos() {
        return rootPos;
    }

    public void setRootPos(Position rootPos) {
        this.rootPos = rootPos;
    }

    public List<Input> getInputs() { return inputs;}

    public List<Output> getOutputs() {
        return outputs;
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
