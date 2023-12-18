package de.swtpro.factorybuilder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.File;

@Entity
public class Model {
    @Id
    long id;

    @Lob
    String modelGltf;

    @Lob
    String icon;

    public long getId() {
        return id;
    }

    public String getModelFile() {
        return modelGltf;
    }
    public String getIcon () {
        return icon;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setModelFile(String modelGltf) {
        this.modelGltf = modelGltf;
    }

    public void setIcon ( String icon) {
        this.icon = icon;
    }
}
