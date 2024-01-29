package de.swtpro.factorybuilder.service.model.machine;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.entity.model.machine.ColorSprayer;
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
import java.util.NoSuchElementException;

@Service
public class ColorSprayerService implements PlacedModelServiceTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelService.class);
    FactoryService factoryService;
    FieldService fieldService;
    ManipulateAbstractModelService manipulateAbstractModelService;

    @Autowired
    public ColorSprayerService(FactoryService factoryService, FieldService fieldService, ManipulateAbstractModelService manipulateAbstractModelService) {
        this.factoryService = factoryService;
        this.fieldService = fieldService;
        this.manipulateAbstractModelService = manipulateAbstractModelService;
    }

    public ColorSprayer createPlacedModel(Position rootPosition, long factoryID) {
        try {
            Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
            ColorSprayer colorSprayer = new ColorSprayer(factory, rootPosition);
            if (!fillPlacedModelLists(colorSprayer)) return null;
            if (manipulateAbstractModelService.checkForPlacement(colorSprayer)) {
                for (Field f : colorSprayer.getPlacedFields())
                    fieldService.setPlacedModelOnField(colorSprayer, f);

                setIconAndModelGltfAndTypeAndName(colorSprayer);

                return colorSprayer;
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
                for (int j = 0; j < 2; j++)
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ()), factoryID).orElseThrow());

            // input
            placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "North"));
            placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "West"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "South"));

            // output
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "East"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "North"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "South"));
            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
            placedOutputs.add(createOutput(f, "East"));

            abstractModel.setPlacedFields(placedFields);
            abstractModel.setInputs(placedInputs);
            abstractModel.setOutputs(placedOutputs);
            return true;
        } catch (NoSuchElementException e) {
            LOGGER.error("Error occurred while filling placed model lists: " + e.getMessage());
            return false;
        }
    }
}
