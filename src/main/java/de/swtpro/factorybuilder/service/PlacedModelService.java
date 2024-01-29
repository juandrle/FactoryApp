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

    PlacedModelService(PlacedModelRepository placedModelRepository, FieldService fieldService, FactoryService factoryService) {
        this.placedModelRepository = placedModelRepository;
        this.fieldService = fieldService;
        this.factoryService = factoryService;
    }

    public Optional<PlacedModel> getPlacedModelById(long id) {
        return placedModelRepository.findById(id);
    }

    private Position createNewPosition(int x, int y, int z) {
        return new Position(x, y, z);
    }

    private Input createInput(Field f, String orientation) {
        Input input = new Input();
        input.setOrientation(orientation);
        input.setPosition(f.getPosition());
        return input;
    }

    private Output createOutput(Field f, String orientation) {
        Output output = new Output();
        output.setOrientation(orientation);
        output.setPosition(f.getPosition());
        return output;
    }

    private boolean fillPlacedModelLists(PlacedModel placedModel) {
        Position rootPos = placedModel.getRootPos();
        long factoryID = placedModel.getFactoryID();
        Factory factory = factoryService.getFactoryById(factoryID).orElse(null);
        if (factory == null) return false;

        List<Field> placedFields = new ArrayList<>();
        List<Input> placedInputs = new ArrayList<>();
        List<Output> placedOutputs = new ArrayList<>();
        Field f;

        try {
            // add fields to placedModel list
            switch (placedModel.getModel().getName()) {
                // machines
                case "brennerofen", "schmelzofen":
                    for (int i = 0; i < 2; i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY(), rootPos.getZ() + i), factoryID).orElseThrow());
                    break;
                case "elektronikmaschine", "saegemuehle":
                    for (int i = 0; i < 2; i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + i, rootPos.getZ()), factoryID).orElseThrow());
                    break;
                case "erzreiniger", "planiermaschine":
                    for (int i = 0; i < 3; i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + i, rootPos.getZ()), factoryID).orElseThrow());
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 1, rootPos.getZ() + 1), factoryID).orElseThrow());
                    break;
                case "farbmischer", "schleifmaschine":
                    placedFields.add(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow());
                    break;
                case "farbsprueher", "vulkanisierer":
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++)
                            placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ()), factoryID).orElseThrow());
                    }
                    break;
                case "montagemaschine_gross":
                    // deprecated
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            for (int k = 0; k < 2; k++)
                                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ() + k), factoryID).orElseThrow());
                        }
                    }
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ() + 3), factoryID).orElseThrow());
                    break;
                case "montagemaschine_klein":
                    // deprecated
                    for (int i = 0; i < 3; i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + i, rootPos.getZ()), factoryID).orElseThrow());
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() - 1, rootPos.getZ()), factoryID).orElseThrow());
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() - 1, rootPos.getZ()), factoryID).orElseThrow());
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY(), rootPos.getZ() + 1), factoryID).orElseThrow());
                    break;
                case "montagemaschine":
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++)
                            placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ()), factoryID).orElseThrow());
                    }
                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ() + 1), factoryID).orElseThrow());
                    break;
                
                // start-/endpoint
                case "warenausgabe", "rohstoffannahme":
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            for (int k = 0; k < 3; k++)
                                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ() + k), factoryID).orElseThrow());
                        }
                    }
                    break;
                
                // obstacles
                case "saeule", "schild":
                    for (int i = 0; i < 2; i++)
                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY(), rootPos.getZ() + i), factoryID).orElseThrow());
                    break;
                
                // transportation
                case "roehre", "kurve", "weiche(3)", "weiche(4)":
                    placedFields.add(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow());
                    break;
            }

            // add in- and output position and orientation to lists
            switch (placedModel.getModel().getName()) {
                // machines
                case "brennerofen", "schmelzofen", "schleifmaschine", "farbmischer":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "North"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
                    break;
                case "elektronikmaschine", "erzreiniger", "planiermaschine", "saegemuehle":
                    int dif = 2;
                    if (placedModel.getModel().getName().equals("elektronikmaschine") || placedModel.getModel().getName().equals("saegemuehle"))
                        dif = 1;

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
                    break;
                case "farbsprueher", "vulkanisierer":
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
                    break;
                case "montagemaschine", "montagemaschine_gross":
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
                    break;
                case "montagemaschine_klein":
                    // deprecated
                    // input
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
                    placedInputs.add(createInput(f, "West"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() - 1, rootPos.getZ()), factoryID).orElseThrow();
                    placedInputs.add(createInput(f, "North"));
                    // output
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
                    placedOutputs.add(createOutput(f, "South"));
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
                    placedOutputs.add(createOutput(f, "East"));
                    break;

                // start-/endpoint
                case "rohstoffannahme":
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
                    placedOutputs.add(createOutput(f, "South"));
                    break;
                case "warenausgabe":
                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
                    placedInputs.add(createInput(f, "South"));
                    break;

                // transportation
                case "roehre":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
                    break;
                case "kurve":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
                    break;
                /* case "switch_3_2_in_ws":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
                    break;
                case "switch_3_2_in_es":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
                    break; */
                case "weiche(3)":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
                    break;
                case "weiche(4)":
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "North"));
                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
                    break;
            }

            placedModel.getPlacedFields().addAll(placedFields);
            placedModel.getInputs().addAll(placedInputs);
            placedModel.getOutputs().addAll(placedOutputs);

            for (Field placedField : placedModel.getPlacedFields()) {
                LOGGER.info("Placing model at position: " + placedField.getPosition().toString());
            }

            return true;
        } catch (NoSuchElementException e) {
            LOGGER.error("Field not found. Out of bounds.");
            return false;
        }

    }

    @Transactional
    public PlacedModel createPlacedModel(Model model, Position rootPosition, long factoryID) {
        if (model != null && rootPosition != null) {
            try {
                Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
                PlacedModel placedModel = new PlacedModel(factory, rootPosition, model);

                // fill placedModel lists (placedFields, input and output)
                if (!fillPlacedModelLists(placedModel)) return null;
                if (checkForPlacement(placedModel)) {
                    for (Field f : placedModel.getPlacedFields()) {
                        fieldService.setPlacedModelOnField(placedModel, f);
                    }

                    return placedModelRepository.save(placedModel);
                }
            } catch (Exception e) {
                LOGGER.error("Factory with Id: " + factoryID + " doesn't exist", e);
            }
        }
        LOGGER.error("Placing model NOT successfull");
        return null;
    }

    public List<PlacedModel> findAllByFactoryId(Factory factory) {
        return placedModelRepository.findByFactory(factory);
    }

    private boolean checkField(Field f, PlacedModel thisModel, String ori) {
        String counterOri = "";
        int extraX = 0, extraY = 0;
        List<Input> inputList = thisModel.getInputByPosition(f.getPosition());
        List<Output> outputList = thisModel.getOutputByPosition(f.getPosition());

        switch (ori) {
            case "North":
                extraX = -1;
                counterOri = "South";
                break;
            case "South":
                extraX = 1;
                counterOri = "North";
                break;
            case "East":
                extraY = 1;
                counterOri = "West";
                break;
            case "West":
                extraY = -1;
                counterOri = "East";
                break;
        }
        Position tmpPosition = new Position(f.getPosition().getX() + extraX, f.getPosition().getY() + extraY,
                f.getPosition().getZ());

        Field tmpField = fieldService.getFieldByPosition(tmpPosition, thisModel.getFactoryID()).orElse(null);
        // edge case: if field doesn't exist (out of bounds)
        if (tmpField == null) {
            if (!inputList.isEmpty()) {
                for (Input i : inputList) {
                    if (i.getOrientation().equals(ori)) return false;
                }
            }
            if (!outputList.isEmpty()) {
                for (Output o : outputList) {
                    if (o.getOrientation().equals(ori)) return false;
                }
            }
            return true;
        }

        PlacedModel tmpPlacedModel = tmpField.getPlacedModel();
        if (tmpPlacedModel != null) {
            // move case: if placedModel IDs match (no need to check)
            if (thisModel.getId() == tmpPlacedModel.getId())
                return true;

            // zeigt mein input auf ein feld das kein output ist
            if (!inputList.isEmpty()) {
                for (Input i : inputList) {
                    if (i.getOrientation().equals(ori)) {
                        for (Output o : tmpPlacedModel.getOutputByPosition(tmpPosition)) {
                            if (o.getOrientation().equals(counterOri))
                                return true;
                        }
                    }
                }
                if (outputList.isEmpty()) return false;
            }
            // zeigt mein output auf ein feld das kein input ist
            if (!outputList.isEmpty()) {
                for (Output o : outputList) {
                    if (o.getOrientation().equals(ori)) {
                        for (Input i : tmpPlacedModel.getInputByPosition(tmpPosition))
                            if (i.getOrientation().equals(counterOri))
                                return true;
                    }
                }
                return false;
            }
            // zeigt mein feld auf ein feld das einen in/output in richtung meines feldes
            // hat
            else {
                List<Output> placedModelOutputs = tmpPlacedModel.getOutputByPosition(tmpPosition);
                List<Input> placedModelInputs = tmpPlacedModel.getInputByPosition(tmpPosition);
                if (!placedModelOutputs.isEmpty()) {
                    for (Output o : placedModelOutputs) {
                        if (o.getOrientation().equals(counterOri) && thisModel.getId() != tmpPlacedModel.getId())
                            return false;
                    }
                }
                if (!placedModelInputs.isEmpty()) {
                    for (Input i : placedModelInputs) {
                        if (i.getOrientation().equals(counterOri) && thisModel.getId() != tmpPlacedModel.getId())
                            return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkForPlacement(PlacedModel thisModel) {
        // check if fields are free
        for (Field f : thisModel.getPlacedFields()) {
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
        if (thisModel == null) return false;

        long factoryID = thisModel.getFactoryID();
        Factory factory = factoryService.getFactoryById(factoryID).orElse(null);
        if (factory == null) return false;

        List<Field> newPosList = new ArrayList<>();
        List<Field> oldPosList = new ArrayList<>(thisModel.getPlacedFields());
        Position backupRootPos = createNewPosition(thisModel.getRootPos().getX(), thisModel.getRootPos().getY(), thisModel.getRootPos().getZ());
        final String backupOrientation = thisModel.getOrientation();
        String oldOrientation = thisModel.getOrientation();
        String tmpOrientation = thisModel.getOrientation();

        while (!tmpOrientation.equals(newOrientation)) {
            tmpOrientation = rotateOrientation(tmpOrientation);
            thisModel.setOrientation(tmpOrientation);

            try {// adjusting every field where model is placed on

                // remove root position from placedFields list
                thisModel.getPlacedFields().remove(fieldService.getFieldByPosition(thisModel.getRootPos(), factoryID).orElseThrow());

                // adjust only the root position
                thisModel.setRootPos(adjustPosition(oldOrientation, tmpOrientation, thisModel.getRootPos(), true, null));

                for (Field f : thisModel.getPlacedFields()) {
                    // new position, so we don't mess up the field position
                    Position position = createNewPosition(f.getPosition().getX(), f.getPosition().getY(), f.getPosition().getZ());
                    Position pos = adjustPosition(oldOrientation, tmpOrientation, position, false, backupRootPos);
                    var fld = fieldService.getFieldByPosition(pos, factoryID);
                    if (fld.isEmpty()) {
                        LOGGER.error("Field (fld) not found");
                    }
                    newPosList.add(fld.orElseThrow());

                }

                // add root position back to placedFields list
                newPosList.add(fieldService.getFieldByPosition(thisModel.getRootPos(), factoryID).orElseThrow());

                //clear old list and fill with the new data
                thisModel.getPlacedFields().clear();
                thisModel.getPlacedFields().addAll(newPosList);
                newPosList.clear();


                for (Input i : thisModel.getInputs()) {
                    // new position, so we don't mess up the field position
                    Position outPosition = createNewPosition(i.getPosition().getX(), i.getPosition().getY(), i.getPosition().getZ());
                    i.setPosition(adjustPosition(oldOrientation, tmpOrientation, outPosition, false, backupRootPos));
                    i.setOrientation(rotateOrientation(i.getOrientation()));
                }


                for (Output o : thisModel.getOutputs()) {
                    // new position, so we don't mess up the field position
                    Position outPosition = createNewPosition(o.getPosition().getX(), o.getPosition().getY(), o.getPosition().getZ());
                    o.setPosition(adjustPosition(oldOrientation, tmpOrientation, outPosition, false, backupRootPos));
                    o.setOrientation(rotateOrientation(o.getOrientation()));
                }


                oldOrientation = tmpOrientation;
                backupRootPos = createNewPosition(thisModel.getRootPos().getX(), thisModel.getRootPos().getY(), thisModel.getRootPos().getZ());
            } catch (Exception e) {
                LOGGER.error("An error occurred: " + e);
                return false;
            }

        }
        //validate placement or else fallback to old position and orientation
        if (!checkForPlacement(thisModel)) {
            rotateModel(thisModelID, backupOrientation);
            return false;
        }

        try {
            //set field that placedModel used to be on to null
            for (Field f : oldPosList) {
                if (f.getPlacedModel() != null && f.getPlacedModel().getId() == thisModel.getId())
                    fieldService.deletePlacedModelOnField(f);
            }

            //place placedModel on new fields
            for (Field f : thisModel.getPlacedFields())
                fieldService.setPlacedModelOnField(thisModel, f);
        } catch (Exception e) {
            LOGGER.error("Rotating model NOT successfull: " + e);
            return false;
        }

        return true;
    }

    private Position adjustPosition(String oldOri, String newOri, Position tmpPos, boolean isRootPos, Position oldRootPos) {
        int absX = 0, absY = 0;
        if (!isRootPos) {
            absX = Math.abs(tmpPos.getX() - oldRootPos.getX());
            absY = Math.abs(tmpPos.getY() - oldRootPos.getY());
        }
        switch (oldOri) {
            case "North":
                if (newOri.equals("East")) {
                    /* tmpPos.setX(tmpPos.getX() + absY - 1 -absX);
                    tmpPos.setY(tmpPos.getY() + absX + absY); */
                    tmpPos.setX(tmpPos.getX() + absY - absX);
                    tmpPos.setY(tmpPos.getY() - absX - 1 - absY);
                }
                break;
            case "East":
                if (newOri.equals("South")) {
                    /* tmpPos.setX(tmpPos.getX() - absY + absX);
                    tmpPos.setY(tmpPos.getY() + absX - 1 - absY); */
                    tmpPos.setX(tmpPos.getX() - absY - 1 - absX);
                    tmpPos.setY(tmpPos.getY() - absX + absY);
                }
                break;
            case "South":
                if (newOri.equals("West")) {
                    /* tmpPos.setX(tmpPos.getX() - absY + 1 +absX);
                    tmpPos.setY(tmpPos.getY() - absX + absY); */
                    tmpPos.setX(tmpPos.getX() - absY + absX);
                    tmpPos.setY(tmpPos.getY() + absX + 1 + absY);
                }
                break;
            case "West":
                if (newOri.equals("North")) {
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
        try {
            PlacedModel placedModel = getPlacedModelById(placedModelID).orElseThrow();

            for (Field f : placedModel.getPlacedFields()) {
                fieldService.deletePlacedModelOnField(f);
            }
            placedModel.getPlacedFields().clear();
            placedModelRepository.deleteById(placedModelID);
        } catch (NoSuchElementException e) {
            LOGGER.error("Placed model with ID: " + placedModelID + " not found");
            return false;
        } catch (Exception e) {
            LOGGER.error("Removing model NOT successfull: " + e);
            return false;
        }

        return true;
    }

    private Position adjustMove(Position position, Position oldRootPosition, Position newRootPosition) {
        position.setX(position.getX() - (oldRootPosition.getX() - newRootPosition.getX()));
        position.setY(position.getY() - (oldRootPosition.getY() - newRootPosition.getY()));
        return position;
        //return createNewPosition(x,y,position.getZ());
    }

    @Transactional
    public boolean moveModel(long modelID, Position newRootPosition) {
        //logger shows wrong position from frontend or from MoveRequestDTO?
        LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> NEW ROOT POSITION TO MOVE TO: " + newRootPosition.toString());
        //hardcoded to validate algorithm (and it does work!)
        //newRootPosition.setX(10);
        //newRootPosition.setY(12);
        //newRootPosition.setZ(0);

        PlacedModel placedModel = getPlacedModelById(modelID).orElse(null);
        if (placedModel == null) return false;
        Factory factory = factoryService.getFactoryById(placedModel.getFactoryID()).orElse(null);
        if (factory == null) return false;

        //LOGGER.info("NEW ROOT POS from DTO: " + newRootPosition.toString());

        Position rootPos = placedModel.getRootPos();

        // fallback rootPosition and placedFields
        Position fallbackRootPosition = createNewPosition(rootPos.getX(), rootPos.getY(), rootPos.getZ());
        List<Field> fallbackPlacedList = new ArrayList<>(placedModel.getPlacedFields());
        List<Output> fallbackOutputs = new ArrayList<>(placedModel.getOutputs());
        List<Input> fallbackInputs = new ArrayList<>(placedModel.getInputs());

        List<Field> newPlacedList = new ArrayList<>();


        try {
            for (Field f : placedModel.getPlacedFields()) {
                Position tmpPosition = createNewPosition(f.getPosition().getX(), f.getPosition().getY(), f.getPosition().getZ());
                //LOGGER.info("TEMP POS: " + tmpPosition.toString());
                Position pos = adjustMove(tmpPosition, fallbackRootPosition, newRootPosition);
                //LOGGER.info("ADJUSTED POS: " + pos.toString());
                Field tmpField = fieldService.getFieldByPosition(pos, factory.getFactoryID()).orElseThrow();
                newPlacedList.add(tmpField);
            }

            for (Input i : placedModel.getInputs()) {
                i.setPosition(adjustMove(i.getPosition(), rootPos, newRootPosition));
            }
            for (Output o : placedModel.getOutputs()) {
                o.setPosition(adjustMove(o.getPosition(), rootPos, newRootPosition));
            }
            placedModel.getPlacedFields().clear();
            placedModel.getPlacedFields().addAll(newPlacedList);
            placedModel.setRootPos(newRootPosition);

            if (checkForPlacement(placedModel)) {
                // set fields that placedModel used to be on to null
                for (Field f : fallbackPlacedList) {
                    fieldService.deletePlacedModelOnField(f);
                }
                // place placedModel on fields based on new placedFields list
                for (Field f : placedModel.getPlacedFields()) {
                    fieldService.setPlacedModelOnField(placedModel, f);
                }
                return true;
            } else {
                // reset lists and root position
                placedModel.getPlacedFields().clear();
                placedModel.getInputs().clear();
                placedModel.getOutputs().clear();

                placedModel.getInputs().addAll(fallbackInputs);
                placedModel.getOutputs().addAll(fallbackOutputs);
                placedModel.getPlacedFields().addAll(fallbackPlacedList);

                placedModel.setRootPos(fallbackRootPosition);

                LOGGER.error("Moving model NOT successfull");

                return false;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Field not found: " + e);
            return false;
        } catch (Exception e) {
            LOGGER.error("Moving model NOT successfull: " + e);
            return false;
        }
    }
}
