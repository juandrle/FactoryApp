package de.swtpro.factorybuilder.service.model.other;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.entity.model.other.GoodsIssue;
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
public class GoodsIssueService implements PlacedModelServiceTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelService.class);
    FactoryService factoryService;
    FieldService fieldService;
    ManipulateAbstractModelService manipulateAbstractModelService;
    @Autowired
    public GoodsIssueService(FactoryService factoryService, FieldService fieldService, ManipulateAbstractModelService manipulateAbstractModelService) {
        this.factoryService = factoryService;
        this.fieldService = fieldService;
        this.manipulateAbstractModelService = manipulateAbstractModelService;
    }


    public GoodsIssue createPlacedModel(Position rootPosition, long factoryID) {
        try {
            Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
            GoodsIssue goodsIssue = new GoodsIssue(factory, rootPosition);
            if (!fillPlacedModelLists(goodsIssue)) return null;
            if (manipulateAbstractModelService.checkForPlacement(goodsIssue)) {
                for (Field f : goodsIssue.getPlacedFields())
                    fieldService.setPlacedModelOnField(goodsIssue, f);

                setIconAndModelGltfAndTypeAndName(goodsIssue);

                return goodsIssue;
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
            Field f;

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    for (int k = 0; k < 3; k++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ() + k), factoryID).orElseThrow());

            f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
            placedInputs.add(createInput(f, "South"));

            abstractModel.setPlacedFields(placedFields);
            abstractModel.setInputs(placedInputs);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error occurred while filling placed model lists: " + e.getMessage());
            return false;
        }
    }
}
