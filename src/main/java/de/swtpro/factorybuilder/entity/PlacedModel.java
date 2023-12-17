package de.swtpro.factorybuilder.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.swtpro.factorybuilder.utility.Position;

import jakarta.persistence.*;

import static jakarta.persistence.CascadeType.ALL;

@Entity
public class PlacedModel implements Serializable{
    
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    private long factoryID;

    @Embedded
    private Position rootPos;
    
    // where is the input located
    @ElementCollection
    private List<Input> inputs;
    
    // where is the output located
    @ElementCollection
    private List<Output> outputs;
    
    // where are we on the grid

    @OneToMany(cascade = ALL, mappedBy = "placedModel")
    private List<Field> placedFields;

    private String orientation;

    // how many items are needed / can be stored in a machine
    private int capacity;

    private long modelId;

    public void PlacedModel() { }//TODO: plaziermethode machen

    public long getId() {
        return id;
    }

    public long getFactoryID() { return factoryID; }

    public void setFactoryID(long factoryID){ this.factoryID = factoryID;}

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

    public Input getInputByPosition(Position pos){
        for(Input i: getInputs()){
            if(i.getPosition() == pos)return i;
        }
        return null;
    }

    public Output getOutputByPosition(Position pos){
        for(Output o: getOutputs()){
            if(o.getPosition() == pos)return o;
        }
        return null;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public List<Field> getPlacedFields(){ return placedFields;}

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

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

}
