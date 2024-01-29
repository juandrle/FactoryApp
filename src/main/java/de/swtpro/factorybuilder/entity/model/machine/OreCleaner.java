package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OreCleaner")
public class OreCleaner extends AbstractMachine {
    String name = "Erzreiniger";
    String modelGltf = "static/models/mock/machines/erzreiniger.gltf";

    public OreCleaner(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public OreCleaner() {

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
