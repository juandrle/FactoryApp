package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.DTO.PlacedModelDTO;
import de.swtpro.factorybuilder.controller.EntityRestAPIController;
import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.utility.Position;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FactoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryService.class);

    private final GridRepository gridRepository;

    private final PlacedModelRepository placedModelRepository;

    private final ModelRepository modelRepository;

    private final FactoryRepository factoryRepository;

    FactoryService(GridRepository gridRepository, PlacedModelRepository placedModelRepository,
                   ModelRepository modelRepository, FactoryRepository factoryRepository) {
        this.gridRepository = gridRepository;
        this.placedModelRepository = placedModelRepository;
        this.modelRepository = modelRepository;
        this.factoryRepository = factoryRepository;
    }

    // @Bean
    // private PasswordEncoder passwordEncoderService(){
    // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    // }

    public Factory getFactoryByID(long id) {
        return factoryRepository.findById(id).orElse(null);
    }

    public PlacedModel getPlacedModelById(long id) {
        return placedModelRepository.findById(id).orElse(null);
    }

    public Factory saveFactory(Factory factory) {
        // PasswordEncoder passwordEncoder = passwordEncoderService();
        // factory.setPassword(passwordEncoder.encode(factory.getPassword()));
        return factoryRepository.save(factory);
    }

    public Optional<Factory> getFactoryById(long id) {
        return factoryRepository.findById(id);
    }

    public void deleteFactoryById(long id) {
        factoryRepository.deleteById(id);
    }

    public void initializeField(long factoryID) {
        Factory factory;
        try {
            factory = getFactoryById(factoryID).orElseThrow();
        } catch (Exception e) {
            System.out.println("Factory with factoryID " + factoryID + " doesn't exist");
            return;
        }
        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < factory.getWidth(); i++) {
            for (int j = 0; j < factory.getDepth(); j++) {
                for (int k = 0; k < factory.getHeight(); k++) {
                    Field field = new Field();
                    field.setPosition(new Position(i - factory.getWidth() / 2, j - factory.getDepth() / 2, k));
                    field.setFactoryID(factoryID);
                    fields.add(field);
                }
            }
        }
        gridRepository.saveAll(fields);
    }

    // actually added real Method in FieldService
    public Field getFieldByPosition(Position pos) {
        for (Field f : gridRepository.findAll()) {
            Position fieldPos = f.getPosition();
            if (fieldPos.getX() == pos.getX() &&
                    fieldPos.getY() == pos.getY() &&
                    fieldPos.getZ() == pos.getZ())
                return f;
        }
        return null;
    }

    // done in FieldService
    public Field getFieldByPosition(Position pos, long factoryID) {
        // todo getField by position;
        return null;
    }

    public Field getFieldById(Long id) {
        return gridRepository.findById(id).orElse(null);
    }

    /**
     * public Model getModelById(Long id) {
     * return placedModelRepository.findById(id).orElse(null);
     * }
     **/
    public Long getPlacedModelIdFromField(@PathVariable Long id) {
        Field field = getFieldById(id);
        return field.getPlacedModel().getModelId();
    }

    private void placeModelIntoField(PlacedModel placedModel, Position position) {
        Field f = getFieldByPosition(position, placedModel.getFactoryID());
        f.setPlacedModel(placedModel);
        // Todo: save in repository
    }

    public boolean createPlacedModel(String modelID, Position rootPosition, long factoryID) {
        // TODO get in and outputs from frontend
        // TODO fix id from model
        Model m = modelRepository.findById(1L).orElse(null);
        LOGGER.info("Model: " + String.valueOf(m.getId()));
        // TODO: convert model to placed model and save
        // return checkForPlacement(m);
        return true;
    }

    private void putModelOnField(PlacedModel placedModel, Position rootPosition) {
        // TODO: create model from input of frontend
        if (checkForPlacement(new PlacedModel())) {
            // todo place it on the field and call placeModelIntoField(to refresh the
            // database
            // also answer frontend
        }
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
            Field tmpField = getFieldByPosition(tmpPosition, thisModel.getFactoryID());
            PlacedModel tmpPlacedModel = getPlacedModelById(tmpField.getPlacedModel().getId());

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

    private boolean checkForPlacement(PlacedModel thisModel) {
        Factory factory = getFactoryByID(thisModel.getFactoryID());
        int height = factory.getHeight(), width = factory.getWidth(), depth = factory.getDepth();
        Field tmpField;
        PlacedModel tmpPlacedModel;
        Position tmpPosition;

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

    public void rotateModel(PlacedModel thisModel, Position newPosition) {
        List<Field> newPosList = new ArrayList<>();
        PlacedModel backupModel = thisModel;
        // check field if height or width still fits
        for (Field f : thisModel.getPlacedFields())
            newPosList.add(getFieldByPosition(adjustPosition(thisModel, newPosition, f.getPosition()),
                    thisModel.getFactoryID()));

        for (Output o : thisModel.getOutputs()) {
            newPosList.add(getFieldByPosition(adjustPosition(thisModel, newPosition, o.getPosition()),
                    thisModel.getFactoryID()));
            o.setOrientation(rotateOrientation(o.getOrientation()));
        }
        for (Input i : thisModel.getInputs()) {
            newPosList.add(getFieldByPosition(adjustPosition(thisModel, newPosition, i.getPosition()),
                    thisModel.getFactoryID()));
            i.setOrientation(rotateOrientation(i.getOrientation()));
        }
        thisModel.setOrientation(rotateOrientation(thisModel.getOrientation()));

        if (checkForPlacement(thisModel)) {
            for (Field f : backupModel.getPlacedFields()) {
                removeModelFromField(f);
            }
            for (Field f : thisModel.getPlacedFields()) {
                placeModelIntoField(thisModel, f.getPosition());
            }
        }
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

    private void removeModelFromField(Field field) {
        field.setPlacedModel(null);
        // Todo: switch repository entry
    }

    public boolean removeModelFromFactory(long placedModelID) {
        // TODO: placedModelID to String (UUID from frontend)
        PlacedModel placedModel = getPlacedModelById(placedModelID);
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

    public boolean moveModel(long modelID) {
        PlacedModel placedModel = getPlacedModelById(modelID);
        // Todo: remove from field and remove from repository
        // placeMachineToField(machine,newPos);
        // Todo: which informations are needed for this operatino?
        // Todo: switch fields and machine repos
        return true;
    }

    public void saveField(Field field) {

    }

    public List<PlacedModelDTO> getEntitysFromFactory(int factoryId) {

        // Nimm alle aus modelRepository mit factoryId == factoryId
        List<PlacedModel> placedModels = placedModelRepository.findByFactoryID(factoryId);
        List<PlacedModelDTO> dtos = new ArrayList<>();

        for (PlacedModel placedModel : placedModels) {

            Model m = modelRepository.findById(placedModel.getModelId()).orElse(null);
            PlacedModelDTO dto = new PlacedModelDTO(
                    placedModel.getFactoryID(),
                    placedModel.getId(),
                    placedModel.getOrientation(),
                    placedModel.getRootPos().getX(),
                    placedModel.getRootPos().getY(),
                    placedModel.getRootPos().getZ(),
                    m.getModelFile() // Füge den Pfad hinzu, wie erforderlich
            );
            dtos.add(dto);
        }

        return dtos;
    }

}
