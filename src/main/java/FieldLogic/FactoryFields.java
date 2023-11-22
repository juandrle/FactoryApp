package FieldLogic;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "FactoryFields")
public class FactoryFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "FactoryFields", cascade = CascadeType.ALL)
    private List<FieldOLD> fields;

    // Weitere Attribute für die FieldGroup, falls benötigt

    // Getter und Setter für id und fields

    public List<FieldOLD> getFields() {
        return fields;
    }

    public void setFields(List<FieldOLD> fields) {
        this.fields = fields;
    }

    public void addField(FieldOLD field){
        fields.add(field);
    }

    public void deleteField(FieldOLD field){
        fields.remove(field);
    }
}

