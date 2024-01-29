package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Sawmill")
public class Sawmill extends AbstractMachine {
    String name = "Sägemühle";
    String modelGltf = "static/models/mock/machines/saegemuehle.gltf";

    public Sawmill(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public Sawmill() {

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
