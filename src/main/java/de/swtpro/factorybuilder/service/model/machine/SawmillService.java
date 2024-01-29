package de.swtpro.factorybuilder.service.model.machine;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.entity.model.machine.Sawmill;
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
public class SawmillService implements PlacedModelServiceTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelService.class);
    FactoryService factoryService;
    FieldService fieldService;
    ManipulateAbstractModelService manipulateAbstractModelService;

    @Autowired
    public SawmillService(FactoryService factoryService, FieldService fieldService, ManipulateAbstractModelService manipulateAbstractModelService) {
        this.factoryService = factoryService;
        this.fieldService = fieldService;
        this.manipulateAbstractModelService = manipulateAbstractModelService;
    }


    public Sawmill createPlacedModel(Position rootPosition, long factoryID) {
        try {
            Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
            Sawmill sawMill = new Sawmill(factory, rootPosition);
            if (!fillPlacedModelLists(sawMill)) return null;
            if (manipulateAbstractModelService.checkForPlacement(sawMill)) {
                for (Field f : sawMill.getPlacedFields())
                    fieldService.setPlacedModelOnField(sawMill, f);

                setIconAndModelGltfAndTypeAndName(sawMill);

                return sawMill;
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

            for (int i = 0; i < 2; i++)
                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + i, rootPos.getZ()), factoryID).orElseThrow());

            int dif = 1;
            // input
            placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "North"));
            placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
            placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));

            // output
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + dif, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "North"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + dif, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "East"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + dif, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "South"));

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
