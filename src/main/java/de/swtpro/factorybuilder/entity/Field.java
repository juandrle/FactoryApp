package de.swtpro.factorybuilder.entity;

import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "field")
public class Field implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    private long version;

    private long factoryID;

    private long placedModelID;

    private Position pos;

    @OneToOne(cascade = CascadeType.ALL)
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

}
