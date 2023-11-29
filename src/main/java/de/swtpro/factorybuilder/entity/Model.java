package de.swtpro.factorybuilder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.File;

@Entity
public class Model {
    @Id
    long id;

    File modelFile;

    public long getId() {
        return id;
    }

    public File getModelFile() {
        return modelFile;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setModelFile(File modelFile) {
        this.modelFile = modelFile;
    }
}
