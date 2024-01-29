package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Kiln")
public class Kiln extends AbstractMachine {
    String name = "Brennerofen";
    String modelGltf = "static/models/mock/machines/brennerofen.gltf";

    public Kiln(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public Kiln() {

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
