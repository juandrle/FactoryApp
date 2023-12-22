package de.swtpro.factorybuilder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Model implements Serializable {
    @Id
    @GeneratedValue
    long id;
    String modelGltf;
    String icon;

    String type;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
