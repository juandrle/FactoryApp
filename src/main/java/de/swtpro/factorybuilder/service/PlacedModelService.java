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
import java.util.NoSuchElementException;

@Service
public class PlacedModelService {

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
    private Input createInput(Field f, String orientation){
        Input input = new Input();
            input.setOrientation(orientation);
            input.setPosition(f.getPosition());
            return input;
    }
    private Output createOutput(Field f, String orientation){
        Output output = new Output();
        output.setOrientation(orientation);
        output.setPosition(f.getPosition());
        return output;
    }
    private boolean fillPlacedModelLists(PlacedModel placedModel){
        Position rootPos = placedModel.getRootPos();
        List<Field> placedFields = placedModel.getPlacedFields();
        List<Input> placedInputs = placedModel.getInputs();
        List<Output> placedOutputs = placedModel.getOutputs();
        Field f;

        try {
            // add fields to placedModel list
            switch(placedModel.getModel().getName()){
                // machine
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
    
                // start-/endpoint
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
    
                // obstacles
                case "saeule", "schild":
                    for(int i = 0; i < 2;i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY(), rootPos.getZ()+i), placedModel.getFactoryID()).orElseThrow());
                    break;
    
            }
            
            // add in- and output position and orientation to lists
            switch(placedModel.getModel().getName()){
                // machines
                case "brennerofen", "schmelzofen", "schleifmaschine", "farbmischer":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"North"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"West"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"South"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"East"));
                    break;
                case "elektronikmaschine", "erzreiniger", "planiermaschine", "saegemuehle":
                    int dif = 2;
                    if(placedModel.getModel().getName().equals("elektronikmaschine")||placedModel.getModel().getName().equals("saegemuehle")) dif = 1;
    
                    // input
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"North"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"West"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"South"));
    
                    // output
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+dif,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+dif,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+dif,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    break;
                case "farbsprueher", "vulkanisierer":
                    // input
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"South"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"North"));
    
                    // output
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    break;
                case "montagemaschine_mittel", "montagemaschine_gross":
                    // input
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"North"));
    
                    // output
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                   break;
                case "montagemaschine_klein":
                    // input
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()-1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"North"));
                    // output
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                  break;
    
                // start-/endpoint
                case "rohstoffannahme":
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    break;
                case "warenausgabe":
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"South"));
                   break;
            }
            
            return true;
        } catch (NoSuchElementException e) {
            // TODO: handle exception
            // return false if field not found (out of bounds)
            return false;
        }

    }
    public PlacedModel createPlacedModel(Model model, Position rootPosition, long factoryID) {
        if (model != null && rootPosition != null) {
            Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
            PlacedModel placedModel = new PlacedModel(factory, rootPosition, model);
    
            // fill placedModel lists (placedFields, input and output)
            if (fillPlacedModelLists(placedModel)) {
                if (checkForPlacement(placedModel, factory)) {
                    for(Field f:placedModel.getPlacedFields()){
                        fieldService.setPlacedModelOnField(placedModel, f);
                    }
            
                    return placedModelRepository.save(placedModel);
                }
            }
        }

        return null;
    }
    public List<PlacedModel> findAllByFactoryId(Factory factory) {
        return placedModelRepository.findByFactory(factory);
    }
    private void removeModelFromField(Field field) {
        field.setPlacedModel(null);
    }
    private boolean checkField(Field f, PlacedModel thisModel, String ori, Factory factory) {
        boolean condition = false;
        String counterOri = "";
        int extraX = 0, extraY = 0;
        Input input = thisModel.getInputByPosition(f.getPosition());
        Output output = thisModel.getOutputByPosition(f.getPosition());
        int height, width;

        switch (ori) {
            case "North":
                height = -factory.getHeight()/2 +1;
                if (f.getPosition().getY() > height) {
                    condition = true;
                    extraY = -1;
                }
                counterOri = "South";
                break;
            case "South":
                height = factory.getHeight();
                if (f.getPosition().getY() < height/2- 1) {
                    condition = true;
                    extraY = 1;
                }
                counterOri = "North";
                break;
            case "East":
                width = factory.getWidth()/2 - 1;
                if (f.getPosition().getX() < width) {
                    condition = true;
                    extraX = 1;
                }
                counterOri = "West";
                break;
            case "West":
                width = factory.getWidth()/2 + 1;
                if (f.getPosition().getX() > width) {
                    condition = true;
                    extraX = -1;
                }
                counterOri = "East";
                break;
        }
        if (condition) {
            Position tmpPosition = new Position(f.getPosition().getX() + extraX, f.getPosition().getY() + extraY,
                    f.getPosition().getZ());
            Field tmpField = fieldService.getFieldByPosition(tmpPosition, thisModel.getFactoryID()).orElse(null);
            assert tmpField != null;
            PlacedModel tmpPlacedModel = tmpField.getPlacedModel();
            if (tmpPlacedModel != null) {
                if (thisModel.getId() == tmpPlacedModel.getId())
                    return true;
    
                // zeigt mein input auf ein feld das kein output ist
                if (input != null && input.getOrientation().equals(ori)) {
                    return tmpPlacedModel.getOutputByPosition(tmpPosition).getOrientation().equals(counterOri);
    
                }
                // zeigt mein output auf ein feld das kein input ist
                else if (output != null && output.getOrientation().equals(ori)) {
                    return tmpPlacedModel.getInputByPosition(tmpPosition).getOrientation().equals(counterOri);
    
                }
                // zeigt mein feld auf ein feld das einen in/output in richtugn meines feldes
                // hat
                else {
                    Output placedModelOutput = tmpPlacedModel.getOutputByPosition(tmpPosition);
                    if (placedModelOutput != null) {
                        if (placedModelOutput.getOrientation().equals(counterOri))
                            return false;
                        return !tmpPlacedModel.getInputByPosition(tmpPosition).getOrientation().equals(counterOri);
                    }
                }
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
        PlacedModel thisModel = getPlacedModelById(thisModelID).orElseThrow();
        List<Field> newPosList = new ArrayList<>();
        PlacedModel backupModel = thisModel;
        // check field if height or width still fits
        assert thisModel != null;
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

        //TODO pruefe ob die gedrehte version an die stelle passt ansonsten passe dies an (mit vince klären)
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
        List<Field> fallbackPlacedList = new ArrayList<>(placedModel.getPlacedFields());
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
        if (fillPlacedModelLists(placedModel)) {
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
            }
        }
        
        // reset lists and root position
        placedModel.getPlacedFields().clear();
        placedModel.getInputs().clear();
        placedModel.getOutputs().clear();
        placedModel.setRootPos(fallbackRootPosition);
        fillPlacedModelLists(placedModel);
        return false;
    }
}
