package de.swtpro.factorybuilder.entity;

import java.io.Serializable;
import java.util.List;

import de.swtpro.factorybuilder.utility.Position;

import jakarta.persistence.*;

import static jakarta.persistence.CascadeType.ALL;

@Entity
public class PlacedModel implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private long version;
    @ManyToOne
    @JoinColumn(name = "factoryId", referencedColumnName = "id")
    private Factory factory;

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

    @ManyToOne
    @JoinColumn(name = "modelId", referencedColumnName = "id")
    private Model model;

    public PlacedModel(Factory factory, Position rootPos, Model model){
        this.factory = factory;
        this.rootPos = rootPos;
        this.model = model;
        this.orientation = "North";
        // TODO: what's the initial capacity?
        this.capacity = 4;
        this.placedFields = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public PlacedModel() {

    }


    public long getId() {
        return id;
    }

    public long getFactoryID() {
        return factory != null ? factory.getFactoryID() : null;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
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
        return model != null ? model.getId() : null;    }

    public void setModel(Model model) {
        this.model = model;
    }
    public Model getModel() { return this.model; }
}
