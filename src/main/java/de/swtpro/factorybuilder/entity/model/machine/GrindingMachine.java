package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GrindingMachine")
public class GrindingMachine extends AbstractMachine {
    String name = "Schleifmaschine";
    String modelGltf = "static/models/mock/machines/schleifmaschine.gltf";

    public GrindingMachine(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public GrindingMachine() {

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
