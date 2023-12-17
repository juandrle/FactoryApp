package de.swtpro.factorybuilder.entity;

import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Field implements Serializable {
    @Id
    private long id;
    private long factoryID;
    private long placedModelID;

    @Column(unique = true)
    private Position pos;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id", referencedColumnName = "id")
    private PlacedModel placedModel;

    public Field() {
    }


    //Get and Set

    public void setFactoryID(long factoryID) { this.factoryID = factoryID; }

    public long getFactoryID() { return factoryID; }

    public Position getPosition() { return pos;}

    public long getPlacedModelID() { return placedModelID; }

    public void setPosition(Position pos){ this.pos = pos; }

    public void setPlacedModelID(Long id) { this.placedModelID = id; }

    public long getId() {
        return id;
    }
}
