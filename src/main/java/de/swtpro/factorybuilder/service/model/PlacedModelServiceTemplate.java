
package de.swtpro.factorybuilder.service.model;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.utility.Position;

public interface PlacedModelServiceTemplate {
    default Position createNewPosition(int x, int y, int z){
        return new Position(x, y, z);
    }
    default Input createInput(Field f, String orientation) {
        Input input = new Input();
        input.setOrientation(orientation);
        input.setPosition(f.getPosition());
        return input;
    }
    default Output createOutput(Field f, String orientation) {
        Output output = new Output();
        output.setOrientation(orientation);
        output.setPosition(f.getPosition());
        return output;
    }
    default void setIconAndModelGltfAndTypeAndName(AbstractModel abstractModel) {
        abstractModel.setIcon(abstractModel.getIcon());
        abstractModel.setModelGltf(abstractModel.getModelGltf());
        abstractModel.setName(abstractModel.getName());
        abstractModel.setType(abstractModel.getType());
    }

}
