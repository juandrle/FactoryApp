package de.swtpro.factorybuilder.entity.model.transportation;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ThreeWaySwitch")
public class ThreeWaySwitch extends AbstractTransportation {
    String name = "";
    String modelGltf = "";

    public ThreeWaySwitch(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public ThreeWaySwitch() {

    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getModelGltf() {
        return modelGltf;
    }
}
