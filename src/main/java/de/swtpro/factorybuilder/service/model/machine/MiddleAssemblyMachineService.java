package de.swtpro.factorybuilder.service.model.machine;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.entity.model.machine.MiddleAssemblyMachine;
import de.swtpro.factorybuilder.service.FactoryService;
import de.swtpro.factorybuilder.service.FieldService;
import de.swtpro.factorybuilder.service.model.AbstractModelService;
import de.swtpro.factorybuilder.service.model.ManipulateAbstractModelService;
import de.swtpro.factorybuilder.service.model.PlacedModelServiceTemplate;
import de.swtpro.factorybuilder.utility.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MiddleAssemblyMachineService implements PlacedModelServiceTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelService.class);
    FactoryService factoryService;
    FieldService fieldService;
    ManipulateAbstractModelService manipulateAbstractModelService;
    @Autowired
    public MiddleAssemblyMachineService(FactoryService factoryService, FieldService fieldService, ManipulateAbstractModelService manipulateAbstractModelService) {
        this.factoryService = factoryService;
        this.fieldService = fieldService;
        this.manipulateAbstractModelService = manipulateAbstractModelService;
    }


    public MiddleAssemblyMachine createPlacedModel(Position rootPosition, long factoryID) {
        try {
            Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
            MiddleAssemblyMachine middleAssemblyMachine = new MiddleAssemblyMachine(factory, rootPosition);
            if (!fillPlacedModelLists(middleAssemblyMachine)) return null;
            if (manipulateAbstractModelService.checkForPlacement(middleAssemblyMachine)) {
                for (Field f : middleAssemblyMachine.getPlacedFields())
                    fieldService.setPlacedModelOnField(middleAssemblyMachine, f);

                setIconAndModelGltfAndTypeAndName(middleAssemblyMachine);

                return middleAssemblyMachine;
            }
        } catch (Exception e) {
            // TODO: REAL ERROR HANDLING
            throw new RuntimeException(e);
        }
        LOGGER.error("placing Model was not successful");
        return null;
    }
    private boolean fillPlacedModelLists(AbstractModel abstractModel) {
        try {
            Position rootPos = abstractModel.getRootPos();
            Factory factory = abstractModel.getFactory();
            long factoryID = factory.getFactoryID();
            List<Field> placedFields = new ArrayList<>();
            List<Input> placedInputs = new ArrayList<>();
            List<Output> placedOutputs = new ArrayList<>();
            Field f;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ()), factoryID).orElseThrow());
            }
            placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ() + 1), factoryID).orElseThrow());

            // input
            placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "West"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "West"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "South"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "South"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "South"));

            // output
            placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "North"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "North"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "North"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "East"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "East"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "East"));

            abstractModel.setPlacedFields(placedFields);
            abstractModel.setInputs(placedInputs);
            abstractModel.setOutputs(placedOutputs);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error occurred while filling placed model lists: " + e.getMessage());
            return false;
        }
    }
}
