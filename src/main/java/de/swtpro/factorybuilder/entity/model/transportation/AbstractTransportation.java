package de.swtpro.factorybuilder.entity.model.transportation;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Transportation")
public abstract class AbstractTransportation extends AbstractModel {
    String icon = "";
    ModelType type = ModelType.TRANSPORT;

    public AbstractTransportation(Factory factory, Position rootPos) {
        super(factory, rootPos);
    }

    public AbstractTransportation() {

    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public ModelType getType() {
        return type;
    }
}
