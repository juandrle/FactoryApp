package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.utility.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlacedModelService {

    //testwise
    private static final Logger LOGGER = LoggerFactory.getLogger(PlacedModelService.class);
    PlacedModelRepository placedModelRepository;
    FactoryService factoryService;
    FieldService fieldService;
    PlacedModelService(PlacedModelRepository placedModelRepository, FieldService fieldService, FactoryService factoryService){
        this.placedModelRepository = placedModelRepository;
        this.fieldService = fieldService;
        this.factoryService = factoryService;
    }
    public Optional<PlacedModel> getPlacedModelById(long id) {
        return placedModelRepository.findById(id);
    }
    private Position createNewPosition(int x, int y, int z){
        return new Position(x,y,z);
    }
    private void fillPlacedModelLists(PlacedModel placedModel){
        //TODO röhren hinzufügen
        //TODO in und output
        Position rootPos = placedModel.getRootPos();
        List<Field> placedFields = placedModel.getPlacedFields();
        switch(placedModel.getModel().getName()){
            //Maschine
            case "brennerofen", "schmelzofen":
                for(int i = 0; i < 2;i++)
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY(), rootPos.getZ()+i), placedModel.getFactoryID()).orElseThrow());
                break;
            case "elektronikmaschine", "saegemuehle":
                for(int i = 0; i < 2;i++)
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                break;
            case "erzreiniger", "planiermaschine":
                for(int i = 0; i < 3;i++)
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()+1), placedModel.getFactoryID()).orElseThrow());
                break;
            case "farbmischer", "schleifmaschine":
                placedFields.add(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow());
                break;
            case "farbsprueher", "vulkanisierer":
                for(int i = 0; i < 2;i++){
                    for(int j = 0; j < 2;j++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY()+j, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                }
                break;
            case "montagemaschine_gross":
                for(int i = 0; i < 3;i++){
                    for(int j = 0; j < 3;j++){
                        for(int k = 0; k < 2;k++)
                            placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY()+j, rootPos.getZ()+k), placedModel.getFactoryID()).orElseThrow());
                    }
                }
                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()+3), placedModel.getFactoryID()).orElseThrow());
                break;
            case "montagemaschine_klein":
                for(int i = 0; i < 3;i++)
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()-1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()-1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()+1), placedModel.getFactoryID()).orElseThrow());
                break;
            case "montagemaschine_mittel":
                for(int i = 0; i < 3;i++){
                    for(int j = 0; j < 3;j++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY()+j, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                }
                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()+1), placedModel.getFactoryID()).orElseThrow());
                break;

            //start-/endpunkt
            case "rohstoffannahme":
                for(int i = 0; i < 3;i++){
                    for(int j = 0; j < 3;j++){
                        for(int k = 0; k < 3;k++)
                            placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY()+j, rootPos.getZ()+k), placedModel.getFactoryID()).orElseThrow());
                    }
                }
                break;
            case "warenausgabe":
                for(int i = 0; i < 3;i++){
                    for(int j = 0; j < 3;j++){
                        for(int k = 0; k < 3;k++)
                            placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY()+j, rootPos.getZ()+k), placedModel.getFactoryID()).orElseThrow());
                    }
                }
                break;

            //hindernisse
            case "saeule", "schild":
                for(int i = 0; i < 2;i++)
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY(), rootPos.getZ()+i), placedModel.getFactoryID()).orElseThrow());
                break;

        }
    }
    public PlacedModel createPlacedModel(Model model, Position rootPosition, long factoryID) {
        Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
        PlacedModel placedModel = new PlacedModel(factory, rootPosition, model);

        // TODO placedModel befüllen
        fillPlacedModelLists(placedModel);

        // TODO checkForPlacement
        // return checkForPlacement(m);

        // TODO placedModel in fieldRepository speichern
        for(Field f:placedModel.getPlacedFields()){
            fieldService.setPlacedModelOnField(placedModel, f);
        }
        return placedModelRepository.save(placedModel);
    }
    public List<PlacedModel> findAllByFactoryId(Factory factory) {
        return placedModelRepository.findByFactory(factory);
    }
    private void removeModelFromField(Field field) {
        field.setPlacedModel(null);
        // Todo: switch repository entry
    }
    private boolean checkField(Field f, PlacedModel thisModel, String ori, Factory factory) {
        boolean condition = false;
        String counterOri = "";
        int extraX = 0, extraY = 0;
        Input input = thisModel.getInputByPosition(f.getPosition());
        Output output = thisModel.getOutputByPosition(f.getPosition());

        switch (ori) {
            case "North":
                if (f.getPosition().getY() > 0) {
                    condition = true;
                    extraY = -1;
                }
                counterOri = "South";
                break;
            case "South":
                int height = factory.getHeight();
                if (f.getPosition().getY() < height - 1) {
                    condition = true;
                    extraY = 1;
                }
                counterOri = "North";
                break;
            case "East":
                int width = factory.getWidth();
                if (f.getPosition().getX() < width - 1) {
                    condition = true;
                    extraX = 1;
                }
                counterOri = "West";
                break;
            case "West":
                if (f.getPosition().getX() > 0) {
                    condition = true;
                    extraX = -1;
                }
                counterOri = "East";
                break;
        }
        if (condition) {
            Position tmpPosition = new Position(f.getPosition().getX() + extraX, f.getPosition().getY() + extraY,
                    f.getPosition().getZ());
            // Wegen switch case kein Nullcheck nötig
            // TODO: fieldservice rausbekommen?
            Field tmpField = fieldService.getFieldByPosition(tmpPosition, thisModel.getFactoryID()).orElse(null);
            assert tmpField != null;
            // TODO: Null Check
            PlacedModel tmpPlacedModel = tmpField.getPlacedModel();

            if (thisModel.getId() == tmpPlacedModel.getId())
                return true;

            // zeigt mein input auf ein feld das kein output ist
            if (input != null && input.getOrientation().equals(ori)) {
                if (!tmpPlacedModel.getOutputByPosition(tmpPosition).getOrientation().equals(counterOri))
                    return false;

            }
            // zeigt mein output auf ein feld das kein input ist
            else if (output != null && output.getOrientation().equals(ori)) {
                if (!tmpPlacedModel.getInputByPosition(tmpPosition).getOrientation().equals(counterOri))
                    return false;

            }
            // zeigt mein feld auf ein feld das einen in/output in richtugn meines feldes
            // hat
            else {
                if (tmpPlacedModel.getOutputByPosition(tmpPosition).getOrientation().equals(counterOri))
                    return false;
                if (tmpPlacedModel.getInputByPosition(tmpPosition).getOrientation().equals(counterOri))
                    return false;

            }
        }
        return true;
    }
    private boolean checkForPlacement(PlacedModel thisModel, Factory factory) {
        // check if fields are free
        for (Field f: thisModel.getPlacedFields()) {
            if (f.getPlacedModel() != null && thisModel.getId() != f.getPlacedModel().getId())
                return false;
        }

        for (Field f: thisModel.getPlacedFields()) {
            // check north
            if (!checkField(f, thisModel, "North", factory))
                return false;
            // check north
            if (!checkField(f, thisModel, "South", factory))
                return false;
            // check north
            if (!checkField(f, thisModel, "East", factory))
                return false;
            // check north
            if (!checkField(f, thisModel, "West", factory))
                return false;
        }
        // if everything is ok return true;
        return true;
    }

    public boolean rotateModel(long thisModelID, Position newPosition, Factory factory) {
        // TODO: nullCheck
        PlacedModel thisModel = getPlacedModelById(thisModelID).orElse(null);
        List<Field> newPosList = new ArrayList<>();
        PlacedModel backupModel = thisModel;
        // check field if height or width still fits
        for (Field f : thisModel.getPlacedFields())
            newPosList.add(fieldService.getFieldByPosition(adjustPosition(thisModel, newPosition, f.getPosition()),
                    thisModel.getFactoryID()).orElse(null));

        for (Output o : thisModel.getOutputs()) {
            newPosList.add(fieldService.getFieldByPosition(adjustPosition(thisModel, newPosition, o.getPosition()),
                    thisModel.getFactoryID()).orElse(null));
            o.setOrientation(rotateOrientation(o.getOrientation()));
        }
        for (Input i : thisModel.getInputs()) {
            newPosList.add(fieldService.getFieldByPosition(adjustPosition(thisModel, newPosition, i.getPosition()),
                    thisModel.getFactoryID()).orElse(null));
            i.setOrientation(rotateOrientation(i.getOrientation()));
        }
        thisModel.setOrientation(rotateOrientation(thisModel.getOrientation()));

        if (checkForPlacement(thisModel, factory)) {
            for (Field f : backupModel.getPlacedFields()) {
                removeModelFromField(f);
            }
            for (Field f : thisModel.getPlacedFields()) {
                fieldService.setPlacedModelOnField(thisModel, f);
            }
            return true;
        }

        //TODO pruefe ob die gedregte version an die stelle passt ansonsten passe dies an mit vince klären
        return false;
    }
    private Position adjustPosition(PlacedModel thisModel, Position newPosition, Position tmpPos) {
        int tmpValue = tmpPos.getX() - thisModel.getRootPos().getX();
        tmpPos.setX(tmpPos.getY() - thisModel.getRootPos().getY() + newPosition.getX());
        tmpPos.setY(tmpValue + newPosition.getY());
        tmpPos.setZ(tmpPos.getZ() - thisModel.getRootPos().getZ() + newPosition.getZ());
        return tmpPos;
    }

    private String rotateOrientation(String orientation) {
        return switch (orientation) {
            case "North" -> "East";
            case "East" -> "South";
            case "South" -> "West";
            case "West" -> "North";
            default -> "";
        };
    }
    public boolean removeModelFromFactory(long placedModelID) {
        // TODO: placedModelID to String (UUID from frontend) NULL HANDLING
        PlacedModel placedModel = getPlacedModelById(placedModelID).orElse(null);
        // Factory factory = getFactoryByID(placedModel.getFactoryID());
        List<Field> fieldsOfPlacedModel = placedModel.getPlacedFields();
        // TODO: delete placedmodel from grid, placedModel and factory repos
        placedModelRepository.deleteById(placedModelID);
        // what does a valid delete look like ?
        // valid delete from repository? -> return true
        for (Field f : fieldsOfPlacedModel) {
            removeModelFromField(f);
        }
        // non valid delete from repository -> reinitialize fields and return false
        // hard coded for skeleton round-trip
        return true;
    }
    public boolean moveModel(long modelID, Position newRootPosition) {
        PlacedModel placedModel = getPlacedModelById(modelID).orElse(null);
        if (placedModel == null) return false;
        Factory factory = factoryService.getFactoryById(placedModel.getFactoryID()).orElseThrow();
        
        // fallback placedFields list
        List<Field> fallbackPlacedList = new ArrayList<>();
        for (Field f: placedModel.getPlacedFields()) {
            fallbackPlacedList.add(f);
        }
        /* for (Field f: fallbackPlacedList) {
            LOGGER.info("FALLBACK POSITION x: " + f.getPosition().getX() + "/ y: " + f.getPosition().getY() + "/ z: " + f.getPosition().getZ());
        } */
        // fallback rootPosition
        Position fallbackRootPosition = placedModel.getRootPos();

        // empty all placedModel lists
        placedModel.getPlacedFields().clear();
        placedModel.getInputs().clear();
        placedModel.getOutputs().clear();

        // change root position and fill placedModel lists starting from new root position
        placedModel.setRootPos(newRootPosition);
        fillPlacedModelLists(placedModel);
        /* for (Field f: placedModel.getPlacedFields()) {
            LOGGER.info("NEW POSITION x: " + f.getPosition().getX() + "/ y: " + f.getPosition().getY() + "/ z: " + f.getPosition().getZ());
        } */

        if (checkForPlacement(placedModel, factory)) {
            // set fields that placedModel used to be on to null
            for (Field f: fallbackPlacedList) {
                fieldService.deletePlacedModelOnField(f);
            }
            // place placedModel on fields based on new placedFields list
            for (Field f: placedModel.getPlacedFields()){
                fieldService.setPlacedModelOnField(placedModel, f);
            }
            return true;
        } else {
            placedModel.getPlacedFields().clear();
            placedModel.getInputs().clear();
            placedModel.getOutputs().clear();

            placedModel.setRootPos(fallbackRootPosition);
            fillPlacedModelLists(placedModel);
            return false;
        }
    }
}
