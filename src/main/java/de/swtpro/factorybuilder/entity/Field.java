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

    private long modelID;
    private Position pos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id", referencedColumnName = "id")
    private PlacedModel machine;


    //Get and Set
    public Position getPosition() { return pos;}

    public long getModelID() { return modelID; }

    public void setPosition(Position pos){ this.pos = pos; }

    public void setModelID(Long id) { this.modelID = id; }

}
