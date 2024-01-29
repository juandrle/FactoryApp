package de.swtpro.factorybuilder.entity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;

import jakarta.persistence.*;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subclass")
public abstract class AbstractModel implements Serializable {

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
    @OneToMany(cascade = ALL, mappedBy = "abstractModel")
    private List<Field> placedFields;

    private String orientation;

    // how many items are needed / can be stored in a machine
    private int capacity;
    String name;
    String modelGltf;
    String icon;
    ModelType type;
    private String script;

    public AbstractModel(Factory factory, Position rootPos) {
        this.factory = factory;
        this.rootPos = rootPos;
        this.orientation = "North";
        // TODO: what's the initial capacity?
        this.capacity = 4;
        this.placedFields = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public AbstractModel() {

    }

    public long getId() {
        return id;
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

    public List<Input> getInputs() {
        return inputs;
    }

    public List<Input> getInputByPosition(Position pos) {
        List<Input> inputList = new ArrayList<>();
        for (Input i : getInputs()) {
            if (i.getPosition().equals(pos)) inputList.add(i);
        }
        return inputList;
    }

    public List<Output> getOutputByPosition(Position pos) {
        List<Output> outputList = new ArrayList<>();
        for (Output o : getOutputs()) {
            if (o.getPosition().equals(pos)) outputList.add(o);
        }
        return outputList;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public List<Field> getPlacedFields() {
        return placedFields;
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

    public Factory getFactory() {
        return factory;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    public void setPlacedFields(List<Field> placedFields) {
        this.placedFields = placedFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelGltf() {
        return modelGltf;
    }

    public String getIcon() {
        return icon;
    }

    public ModelType getType() {
        return type;
    }

    public void setModelGltf(String modelGltf) {
        this.modelGltf = modelGltf;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setType(ModelType type) {
        this.type = type;
    }

}

