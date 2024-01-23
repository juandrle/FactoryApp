package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.transaction.Transactional;
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
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+i, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                    break;
                case "erzreiniger", "planiermaschine":
                    for(int i = 0; i < 3;i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+i, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+1, rootPos.getZ()+1), placedModel.getFactoryID()).orElseThrow());
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
                    // deprecated
                    for(int i = 0; i < 3;i++){
                        for(int j = 0; j < 3;j++){
                            for(int k = 0; k < 2;k++)
                                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+i,rootPos.getY()+j, rootPos.getZ()+k), placedModel.getFactoryID()).orElseThrow());
                        }
                    }
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()+3), placedModel.getFactoryID()).orElseThrow());
                    break;
                case "montagemaschine_klein":
                    // deprecated
                    for(int i = 0; i < 3;i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+i, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow());
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
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+dif, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+dif, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+dif, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    break;
                case "farbsprueher", "vulkanisierer":
                    // input
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"North"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"South"));
    
                    // output
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    break;
                case "montagemaschine_mittel", "montagemaschine_gross":
                    // input
                    placedInputs.add(createInput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY(), rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"South"));
    
                    // output
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(placedModel.getRootPos(), placedModel.getFactoryID()).orElseThrow(),"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"North"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(),rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+1,rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+2, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"East"));
                   break;
                case "montagemaschine_klein":
                    // deprecated
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
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedOutputs.add(createOutput(f,"South"));
                    break;
                case "warenausgabe":
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX()+2,rootPos.getY()+1, rootPos.getZ()), placedModel.getFactoryID()).orElseThrow();
                    placedInputs.add(createInput(f,"South"));
                   break;
            }
            
            for(Field placedField: placedModel.getPlacedFields()){
                LOGGER.info("Trying to place model at position: " + placedField.getPosition().toString());
            }

            return true;
        } catch (NoSuchElementException e) {
            // TODO: handle exception
            // return false if field not found (out of bounds)
            LOGGER.error("Field not found. Out of bounds.");
            return false;
        }

    }
    @Transactional
    public PlacedModel createPlacedModel(Model model, Position rootPosition, long factoryID) {
        if (model != null && rootPosition != null) {
            Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
            PlacedModel placedModel = new PlacedModel(factory, rootPosition, model);
    
            // fill placedModel lists (placedFields, input and output)
            if (fillPlacedModelLists(placedModel)) {
                if (checkForPlacement(placedModel)) {
                    for(Field f:placedModel.getPlacedFields()){
                        fieldService.setPlacedModelOnField(placedModel, f);
                    }
            
                    return placedModelRepository.save(placedModel);
                }
            }
        }
        LOGGER.error("Placing model NOT successfull");
        return null;
    }
    public List<PlacedModel> findAllByFactoryId(Factory factory) {
        return placedModelRepository.findByFactory(factory);
    }
    
    private boolean checkField(Field f, PlacedModel thisModel, String ori) {
        boolean condition = false;
        String counterOri = "";
        int extraX = 0, extraY = 0;
        Input input = thisModel.getInputByPosition(f.getPosition());
        Output output = thisModel.getOutputByPosition(f.getPosition());
        int height = 0, width = 0;

        switch (ori) {
            case "North":
                //height = -factory.getHeight()/2 +1;
                if (f.getPosition().getY() > height) {
                    condition = true;
                    extraY = -1;
                }
                counterOri = "South";
                break;
            case "South":
                //height = factory.getHeight();
                if (f.getPosition().getY() < height/2- 1) {
                    condition = true;
                    extraY = 1;
                }
                counterOri = "North";
                break;
            case "East":
                //width = factory.getWidth()/2 - 1;
                if (f.getPosition().getX() < width) {
                    condition = true;
                    extraX = 1;
                }
                counterOri = "West";
                break;
            case "West":
                //width = factory.getWidth()/2 + 1;
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
    private boolean checkForPlacement(PlacedModel thisModel) {
        // check if fields are free
        for (Field f: thisModel.getPlacedFields()) {
            if (f.getPlacedModel() != null && thisModel.getId() != f.getPlacedModel().getId())
                return false;
            // check north
            if (!checkField(f, thisModel, "North"))
                return false;
            // check north
            if (!checkField(f, thisModel, "South"))
                return false;
            // check north
            if (!checkField(f, thisModel, "East"))
                return false;
            // check north
            if (!checkField(f, thisModel, "West"))
                return false;
        }
        // if everything is ok return true;
        return true;
    }
    @Transactional
    public boolean rotateModel(long thisModelID, String newOrientation) {
        PlacedModel thisModel = getPlacedModelById(thisModelID).orElse(null);
        if(thisModel == null) return false;

        long factoryID = thisModel.getFactoryID();
        Factory factory = factoryService.getFactoryById(factoryID).orElse(null);
        if(factory == null) return false;

        List<Field> newPosList = new ArrayList<>();
        List<Field> oldPosList = new ArrayList<>(thisModel.getPlacedFields());
        Position backupRootPos = createNewPosition(thisModel.getRootPos().getX(),thisModel.getRootPos().getY(),thisModel.getRootPos().getZ());
        final String backupOrientation = thisModel.getOrientation();
        String oldOrientation = thisModel.getOrientation();
        String tmpOrientation = thisModel.getOrientation();

        while(!tmpOrientation.equals(newOrientation)){
            tmpOrientation = rotateOrientation(tmpOrientation);
            //LOGGER.info("OLD ORIENTATION: " + oldOrientation + ", NEW ORIENTATION: " + tmpOrientation);
            thisModel.setOrientation(tmpOrientation);

            try {// adjusting every field where model is placed on

                // remove root position from placedFields list
                thisModel.getPlacedFields().remove(fieldService.getFieldByPosition(thisModel.getRootPos(), thisModel.getFactoryID()).orElseThrow());

                //LOGGER.info("old rootpos: " + thisModel.getRootPos().getX() +"/"+thisModel.getRootPos().getY() +"/"+thisModel.getRootPos().getZ());

                // adjust only the root position
                thisModel.setRootPos(adjustPosition(oldOrientation,tmpOrientation,thisModel.getRootPos(), true,null));

                //LOGGER.info("new rootpos: " + thisModel.getRootPos().getX() +"/"+thisModel.getRootPos().getY() +"/"+thisModel.getRootPos().getZ());

                for (Field f : thisModel.getPlacedFields()){
                    // new position, so we don't mess up the field position
                    Position position = createNewPosition(f.getPosition().getX(),f.getPosition().getY(),f.getPosition().getZ());
                    Position pos = adjustPosition(oldOrientation, tmpOrientation, position, false, backupRootPos);
                    var fld = fieldService.getFieldByPosition(pos, factoryID);
                    if(fld.isEmpty()){
                        LOGGER.error("fehler fld");
                    }
                    newPosList.add(fld.orElseThrow());

                }


                // add root position back to placedFields list
                newPosList.add(fieldService.getFieldByPosition(thisModel.getRootPos(), thisModel.getFactoryID()).orElseThrow());

                //clear old list and fill with the new data
                thisModel.getPlacedFields().clear();
                thisModel.getPlacedFields().addAll(newPosList);
                newPosList.clear();


                for (Input i : thisModel.getInputs()) {
                    // new position, so we don't mess up the field position
                    Position outPosition = createNewPosition(i.getPosition().getX(),i.getPosition().getY(),i.getPosition().getZ());
                    i.setPosition(adjustPosition(oldOrientation,tmpOrientation, outPosition, false, backupRootPos));
                    i.setOrientation(rotateOrientation(i.getOrientation()));
                }


                for (Output o : thisModel.getOutputs()) {
                    // new position, so we don't mess up the field position
                    Position outPosition = createNewPosition(o.getPosition().getX(),o.getPosition().getY(),o.getPosition().getZ());
                    o.setPosition(adjustPosition(oldOrientation,tmpOrientation, outPosition, false, backupRootPos));
                    o.setOrientation(rotateOrientation(o.getOrientation()));
                }


                oldOrientation = tmpOrientation;
                backupRootPos = createNewPosition(thisModel.getRootPos().getX(), thisModel.getRootPos().getY(), thisModel.getRootPos().getZ());
            }
            catch (Exception e) {
                LOGGER.error("Ein Fehler im System: " + e,e);
                return false;
            }


        }

        // START check
        /*
        if (checkForPlacement(thisModel, factory)) {
            for (Field f : backupModel.getPlacedFields()) {
                fieldService.deletePlacedModelOnField(f);
            }
            for (Field f : thisModel.getPlacedFields()) {
                fieldService.setPlacedModelOnField(thisModel, f);
            }
            return true;
        }

        return false;
        */
        // END check


        //fallback to old position and orientation
        if(!checkForPlacement(thisModel)){
            rotateModel(thisModelID,backupOrientation);
            return false;
        }


        try {
            //set field that placedModel used to be on to null
            for(Field f: oldPosList)
                fieldService.deletePlacedModelOnField(f);


            //place placedModel on new fields
            for(Field f: thisModel.getPlacedFields())
                fieldService.setPlacedModelOnField(thisModel,f);
        } catch (Exception e) {
            LOGGER.error("Ein Fehler im System: " + e,e);
            return false;
        }

        return true;
    }

    private Position adjustPosition(String oldOri, String newOri, Position tmpPos, boolean isRootPos, Position oldRootPos) {
        int absX = 0, absY = 0;
        if(!isRootPos) {
            absX = Math.abs(tmpPos.getX() - oldRootPos.getX());
            absY = Math.abs(tmpPos.getY() - oldRootPos.getY());
        }
        switch(oldOri){
            case "North":
                if(newOri.equals("East")) {
                    /* tmpPos.setX(tmpPos.getX() + absY - 1 -absX);
                    tmpPos.setY(tmpPos.getY() + absX + absY); */
                    tmpPos.setX(tmpPos.getX() + absY - absX);
                    tmpPos.setY(tmpPos.getY() - absX - 1 - absY);
                }
                break;
            case "East":
                if(newOri.equals("South")) {
                    /* tmpPos.setX(tmpPos.getX() - absY + absX);
                    tmpPos.setY(tmpPos.getY() + absX - 1 - absY); */
                    tmpPos.setX(tmpPos.getX() - absY - 1 - absX);
                    tmpPos.setY(tmpPos.getY() - absX + absY);
                }
                break;
            case "South":
                if(newOri.equals("West")) {
                    /* tmpPos.setX(tmpPos.getX() - absY + 1 +absX);
                    tmpPos.setY(tmpPos.getY() - absX + absY); */
                    tmpPos.setX(tmpPos.getX() - absY + absX);
                    tmpPos.setY(tmpPos.getY() + absX + 1 + absY);
                }
                break;
            case "West":
                if(newOri.equals("North")) {
                    /* tmpPos.setX(tmpPos.getX() + absY -absX);
                    tmpPos.setY(tmpPos.getY() - absX + 1 +absY); */
                    tmpPos.setX(tmpPos.getX() + absY + 1 + absX);
                    tmpPos.setY(tmpPos.getY() + absX - absY);
                }
                break;
            }

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
    @Transactional
    public boolean removeModelFromFactory(long placedModelID) {
        PlacedModel placedModel = getPlacedModelById(placedModelID).orElse(null);
        if (placedModel == null) return false;

        for (Field f: placedModel.getPlacedFields()) {
            fieldService.deletePlacedModelOnField(f);
        }

        placedModelRepository.deleteById(placedModelID);

        return true;
    }
    @Transactional
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
            if (checkForPlacement(placedModel)) {
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
