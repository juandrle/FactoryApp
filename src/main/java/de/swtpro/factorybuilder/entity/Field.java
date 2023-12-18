package de.swtpro.factorybuilder.entity;

import de.swtpro.factorybuilder.utility.Position;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.io.Serializable;

import static jakarta.persistence.CascadeType.ALL;

@Entity
public class Field implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private long factoryID;

    @Column(unique = true)
    private Position pos;

    @Nullable
    @ManyToOne(cascade = ALL)
    private PlacedModel placedModel;

    public Field() {
    }


    //Get and Set

    public void setFactoryID(long factoryID) { this.factoryID = factoryID; }

    public long getFactoryID() { return factoryID; }

    public Position getPosition() { return pos;}

    public void setPosition(Position pos){ this.pos = pos; }

    public long getId() {
        return id;
    }

    public PlacedModel getPlacedModel() {
        return placedModel;
    }

    public void setPlacedModel(PlacedModel placedModel) {
        this.placedModel = placedModel;
    }
}
