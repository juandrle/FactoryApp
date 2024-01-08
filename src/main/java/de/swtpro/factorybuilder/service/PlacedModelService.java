package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.utility.Position;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class PlacedModelService {
    PlacedModelRepository placedModelRepository;

    FactoryRepository factoryRepository;
    FieldService fieldService;
    PlacedModelService(PlacedModelRepository placedModelRepository, FieldService fieldService, FactoryRepository factoryRepository){
        this.placedModelRepository = placedModelRepository;
        this.fieldService = fieldService;
        this.factoryRepository = factoryRepository;
    }
    public Optional<PlacedModel> getPlacedModelById(long id) {
        return placedModelRepository.findById(id);
    }
    private PlacedModel placeModelIntoField(PlacedModel placedModel, Field field) {
        field.setPlacedModel(placedModel);
        return placedModelRepository.save(placedModel);
    }
    public PlacedModel createPlacedModel(Model model, Position rootPosition, long factoryID) {
        // TODO get in and outputs from frontend
        // return checkForPlacement(m);


        Factory factory = factoryRepository.findById(factoryID).orElseThrow();

        PlacedModel placedModel = new PlacedModel(factory, rootPosition, model);
        // TODO placed model befüllen

        return placedModelRepository.save(placedModel);
    }
    public List<PlacedModel> findAllByFactoryId(Factory factory) {
        return placedModelRepository.findByFactory(factory);
    }
    private void putModelOnField(PlacedModel placedModel, Position rootPosition) {
        // TODO: create model from input of frontend
//        if (checkForPlacement(new PlacedModel())) {
//            // todo place it on the field and call placeModelIntoField(to refresh the
//            // database
//            // also answer frontend
//        }
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
        for (Field f : thisModel.getPlacedFields()) {
            if (f.getPlacedModel() != null)
                return false;
        }

        for (Field f : thisModel.getPlacedFields()) {
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
                placeModelIntoField(thisModel, f);
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
        for (Field f: placedModel.getPlacedFields()) {
            f.setPlacedModel(null);
        }
        // TODO: change position to new Position
        // placeMachineToField(machine,newPos);
        // Todo: which informations are needed for this operatino?
        // Todo: switch fields and machine repos
        return true;
    }
}
