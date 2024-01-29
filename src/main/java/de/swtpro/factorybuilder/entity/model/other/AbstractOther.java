package de.swtpro.factorybuilder.entity.model.other;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Other")
public abstract class AbstractOther extends AbstractModel {
    String icon = "";
    ModelType type = ModelType.OTHER;

    public AbstractOther(Factory factory, Position rootPos) {
        super(factory, rootPos);
    }
    public AbstractOther(){

    }

    @Override
    public ModelType getType() {
        return type;
    }

    @Override
    public String getIcon() {
        return icon;
    }
}
