package de.swtpro.factorybuilder.entity;

import de.swtpro.factorybuilder.utility.ModelType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Model implements Serializable {
    @Id
    @GeneratedValue
    long id;

    String name;
    String modelGltf;
    String icon;
    ModelType type;

    public long getId() {
        return id;
    }

    public String getModelFile() {
        return modelGltf;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setModelFile(String modelGltf) {
        this.modelGltf = modelGltf;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setType(ModelType type) {
        this.type = type;
    }

    public ModelType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
