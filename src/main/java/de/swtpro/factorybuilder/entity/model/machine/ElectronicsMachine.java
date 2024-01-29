package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ElectronicsMachine")
public class ElectronicsMachine extends AbstractMachine {

    String name = "Elektronikmaschine";
    String modelGltf = "static/models/mock/machines/elektronikmaschine.gltf";

    public ElectronicsMachine(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public ElectronicsMachine() {

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
