package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PlaningMachine")
public class PlaningMachine extends AbstractMachine {
    String name = "Planiermaschine";
    String modelGltf = "static/models/mock/machines/planiermaschine.gltf";

    public PlaningMachine(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public PlaningMachine() {

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
