package de.swtpro.factorybuilder.entity.model.machine;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Machine")
public abstract class AbstractMachine extends AbstractModel {
    ModelType type = ModelType.MACHINE;
    String icon = "";

    public AbstractMachine(Factory factory, Position rootPos) {
        super(factory, rootPos);
    }

    public AbstractMachine() {

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
