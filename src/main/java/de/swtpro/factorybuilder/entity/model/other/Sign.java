package de.swtpro.factorybuilder.entity.model.other;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Sign")
public class Sign extends AbstractOther {
    String name = "Schild";
    String modelGltf = "static/models/mock/other/schild.gltf";

    public Sign(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public Sign() {

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
