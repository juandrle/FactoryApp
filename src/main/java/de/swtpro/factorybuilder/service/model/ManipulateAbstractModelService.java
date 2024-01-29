package de.swtpro.factorybuilder.service.model;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Field;
import de.swtpro.factorybuilder.entity.Input;
import de.swtpro.factorybuilder.entity.Output;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.service.FieldService;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ManipulateAbstractModelService implements PlacedModelServiceTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelService.class);
    FieldService fieldService;
    AbstractModelService abstractModelService;

    ManipulateAbstractModelService(FieldService fieldService, AbstractModelService abstractModelService) {
        this.fieldService = fieldService;
        this.abstractModelService = abstractModelService;
    }

    private boolean checkField(Field f, AbstractModel thisModel, String ori) {
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

        Field tmpField = fieldService.getFieldByPosition(tmpPosition, thisModel.getFactory().getFactoryID()).orElse(null);
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

        AbstractModel tmpAbstractModel = tmpField.getPlacedModel();
        if (tmpAbstractModel != null) {
            // move case: if placedModel IDs match (no need to check)
            if (thisModel.getId() == tmpAbstractModel.getId())
                return true;

            // zeigt mein input auf ein feld das kein output ist
            if (!inputList.isEmpty()) {
                for (Input i : inputList) {
                    if (i.getOrientation().equals(ori)) {
                        for (Output o : tmpAbstractModel.getOutputByPosition(tmpPosition)) {
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
                        for (Input i : tmpAbstractModel.getInputByPosition(tmpPosition))
                            if (i.getOrientation().equals(counterOri))
                                return true;
                    }
                }
                return false;
            }
            // zeigt mein feld auf ein feld das einen in/output in richtung meines feldes
            // hat
            else {
                List<Output> placedModelOutputs = tmpAbstractModel.getOutputByPosition(tmpPosition);
                List<Input> placedModelInputs = tmpAbstractModel.getInputByPosition(tmpPosition);
                if (!placedModelOutputs.isEmpty()) {
                    for (Output o : placedModelOutputs) {
                        if (o.getOrientation().equals(counterOri) && thisModel.getId() != tmpAbstractModel.getId())
                            return false;
                    }
                }
                if (!placedModelInputs.isEmpty()) {
                    for (Input i : placedModelInputs) {
                        if (i.getOrientation().equals(counterOri) && thisModel.getId() != tmpAbstractModel.getId())
                            return false;
                    }
                }
            }
        }
        return true;
    }

    @Transactional
    public boolean rotateModel(long thisModelID, String newOrientation) {
        AbstractModel thisModel = abstractModelService.getPlacedModelById(thisModelID).orElse(null);
        if (thisModel == null) return false;
        Factory factory = thisModel.getFactory();
        long factoryID = factory.getFactoryID();

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

    public boolean checkForPlacement(AbstractModel thisModel) {
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

    private Position adjustMove(Position position, Position oldRootPosition, Position newRootPosition) {
        position.setX(position.getX() - (oldRootPosition.getX() - newRootPosition.getX()));
        position.setY(position.getY() - (oldRootPosition.getY() - newRootPosition.getY()));
        return position;
        //return createNewPosition(x,y,position.getZ());
    }

    @Transactional
    public boolean removeModelFromFactory(long placedModelID) {
        try {
            AbstractModel abstractModel = abstractModelService.getPlacedModelById(placedModelID).orElseThrow();

            for (Field f : abstractModel.getPlacedFields()) {
                fieldService.deletePlacedModelOnField(f);
            }
            abstractModel.getPlacedFields().clear();
            abstractModelService.deleteById(placedModelID);
        } catch (NoSuchElementException e) {
            LOGGER.error("Placed model with ID: " + placedModelID + " not found");
            return false;
        } catch (Exception e) {
            LOGGER.error("Removing model NOT successfull: " + e);
            return false;
        }

        return true;
    }

    @Transactional
    public boolean moveModel(long modelID, Position newRootPosition) {
        //logger shows wrong position from frontend or from MoveRequestDTO?
        LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> NEW ROOT POSITION TO MOVE TO: " + newRootPosition.toString());
        //hardcoded to validate algorithm (and it does work!)
        //newRootPosition.setX(10);
        //newRootPosition.setY(12);
        //newRootPosition.setZ(0);

        AbstractModel abstractModel = abstractModelService.getPlacedModelById(modelID).orElse(null);
        if (abstractModel == null) return false;
        Factory factory = abstractModel.getFactory();
        if (factory == null) return false;

        //LOGGER.info("NEW ROOT POS from DTO: " + newRootPosition.toString());

        Position rootPos = abstractModel.getRootPos();

        // fallback rootPosition and placedFields
        Position fallbackRootPosition = createNewPosition(rootPos.getX(), rootPos.getY(), rootPos.getZ());
        List<Field> fallbackPlacedList = new ArrayList<>(abstractModel.getPlacedFields());
        List<Output> fallbackOutputs = new ArrayList<>(abstractModel.getOutputs());
        List<Input> fallbackInputs = new ArrayList<>(abstractModel.getInputs());

        List<Field> newPlacedList = new ArrayList<>();


        try {
            for (Field f : abstractModel.getPlacedFields()) {
                Position tmpPosition = createNewPosition(f.getPosition().getX(), f.getPosition().getY(), f.getPosition().getZ());
                //LOGGER.info("TEMP POS: " + tmpPosition.toString());
                Position pos = adjustMove(tmpPosition, fallbackRootPosition, newRootPosition);
                //LOGGER.info("ADJUSTED POS: " + pos.toString());
                Field tmpField = fieldService.getFieldByPosition(pos, factory.getFactoryID()).orElseThrow();
                newPlacedList.add(tmpField);
            }

            for (Input i : abstractModel.getInputs()) {
                i.setPosition(adjustMove(i.getPosition(), rootPos, newRootPosition));
            }
            for (Output o : abstractModel.getOutputs()) {
                o.setPosition(adjustMove(o.getPosition(), rootPos, newRootPosition));
            }
            abstractModel.getPlacedFields().clear();
            abstractModel.getPlacedFields().addAll(newPlacedList);
            abstractModel.setRootPos(newRootPosition);

            if (checkForPlacement(abstractModel)) {
                // set fields that placedModel used to be on to null
                for (Field f : fallbackPlacedList) {
                    fieldService.deletePlacedModelOnField(f);
                }
                // place placedModel on fields based on new placedFields list
                for (Field f : abstractModel.getPlacedFields()) {
                    fieldService.setPlacedModelOnField(abstractModel, f);
                }
                return true;
            } else {
                // reset lists and root position
                abstractModel.getPlacedFields().clear();
                abstractModel.getInputs().clear();
                abstractModel.getOutputs().clear();

                abstractModel.getInputs().addAll(fallbackInputs);
                abstractModel.getOutputs().addAll(fallbackOutputs);
                abstractModel.getPlacedFields().addAll(fallbackPlacedList);

                abstractModel.setRootPos(fallbackRootPosition);

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
