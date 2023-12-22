package de.swtpro.factorybuilder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.File;
import java.io.Serializable;

@Entity
public class Model implements Serializable {
    @Id
    @GeneratedValue
    long id;

    String modelGltf;

    String icon;

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
}
