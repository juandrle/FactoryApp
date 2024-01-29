package de.swtpro.factorybuilder.entity.model.other;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RawMaterialIntake")
public class RawMaterialIntake extends AbstractOther {
    String name = "Rohstoffannahme";
    String modelGltf = "static/models/mock/other/rohstoffannahme.gltf";

    public RawMaterialIntake(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public RawMaterialIntake() {

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
