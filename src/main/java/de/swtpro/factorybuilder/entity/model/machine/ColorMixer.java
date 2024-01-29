package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ColorMixer")
public class ColorMixer extends AbstractMachine {
    String name = "Farbmischer";
    String modelGltf = "static/models/mock/machines/farbmischer.gltf";

    public ColorMixer(Factory factory, Position rootPosition) {
     super(factory, rootPosition);
    }

    public ColorMixer() {

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
