package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Vulcanizer")
public class Vulcanizer extends AbstractMachine {
    String name = "Vulkanisierer";
    String modelGltf = "static/models/mock/machines/vulkanisierer.gltf";

    public Vulcanizer(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public Vulcanizer() {

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
