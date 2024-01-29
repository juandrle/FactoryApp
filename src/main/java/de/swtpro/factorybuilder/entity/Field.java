package de.swtpro.factorybuilder.entity;

import de.swtpro.factorybuilder.entity.model.AbstractModel;
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
    @Column(unique = true, updatable = false, nullable = false)
    private Position pos;

    @Nullable
    @ManyToOne(cascade = ALL)
    private AbstractModel abstractModel;


    public Field(Position pos) {
        this.pos = pos;
    }

    public Field() {

    }

    //Get and Set

    public void setFactoryID(long factoryID) { this.factoryID = factoryID; }

    public long getFactoryID() { return factoryID; }

    public Position getPosition() { return pos;}

    //public void setPosition(Position pos){ this.pos = pos; }

    public long getId() {
        return id;
    }

    public AbstractModel getPlacedModel() {
        return abstractModel;
    }

    public void setPlacedModel(AbstractModel abstractModel) {
        this.abstractModel = abstractModel;
    }
}
