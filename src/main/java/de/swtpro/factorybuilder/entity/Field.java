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
    private Position pos;
    private long machineID;

    private int completeFieldSize;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id", referencedColumnName = "id")
    private Machine machine;


    //Get and Set
    public Position getPosition() { return pos;}

    public long getMachineID() { return machineID; }

    public void setPosition(Position pos){ this.pos = pos; }

    public void setMachineID(Long id) { this.machineID = id; }

    public int getCompleteFieldSize(){return completeFieldSize;}

}