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
    private List<Field> fields;

    // Weitere Attribute für die FieldGroup, falls benötigt

    // Getter und Setter für id und fields

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void addField(Field field){
        fields.add(field);
    }

    public void deleteField(Field field){
        fields.remove(field);
    }
}

