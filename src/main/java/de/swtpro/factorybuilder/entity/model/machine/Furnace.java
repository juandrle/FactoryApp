package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Furnace")
public class Furnace extends AbstractMachine {
    String name = "Schmelzofen";
    String modelGltf = "static/models/mock/machines/schmelzofen.gltf";

    public Furnace(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public Furnace() {

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
