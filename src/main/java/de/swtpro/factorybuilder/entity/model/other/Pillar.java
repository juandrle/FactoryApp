package de.swtpro.factorybuilder.entity.model.other;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Pillar")
public class Pillar extends AbstractOther {
    String name = "Säule";
    String modelGltf = "static/models/mock/other/saeule.gltf";

    public Pillar(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public Pillar() {

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
