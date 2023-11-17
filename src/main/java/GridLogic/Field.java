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
    private int x;
    private int y;
    private int z;
    private String machineID;

    //private int fieldSize;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id", referencedColumnName = "id")
    private Machine machine;


    //Get and Set
    public int getX() { return x;}
    public int getY() { return y;}
    public int getZ() { return z; }
    public String getMachineID() { return machineID; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setZ(int Z) { this.z = z; }
    public void setMachine(String machineID){ this.machineID = machineID; }

}
