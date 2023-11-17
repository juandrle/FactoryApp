package GridLogic;

import Machines.Machine;
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

    //private int fieldSize;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id", referencedColumnName = "id")
    private Machine machine;


    //Get and Set
    public Position getPosition() { return pos;}

    public long getMachineID() { return machineID; }

    public void setZ(Position pos) { this.pos = pos; }

}
