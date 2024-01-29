package de.swtpro.factorybuilder.entity.model.transportation;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CurvedPipe")
public class CurvedPipe extends AbstractTransportation {
    String name = "gebogenes Rohr";
    String modelGltf = "static/models/mock/transportation/pipe_curved.gltf";

    public CurvedPipe(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public CurvedPipe() {

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
