package de.swtpro.factorybuilder.entity.model.other;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GoodsIssue")
public class GoodsIssue extends AbstractOther {
    String name = "Warenausgabe";
    String modelGltf = "static/models/mock/other/warenausgabe.gltf";

    public GoodsIssue(Factory factory, Position rootPosition) {
        super(factory, rootPosition);
    }

    public GoodsIssue() {

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
