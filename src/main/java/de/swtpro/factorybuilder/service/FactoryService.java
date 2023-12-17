package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.utility.Position;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FactoryService {

    private final GridRepository gridRepository;

    private final ModelRepository modelRepository;

    private final FactoryRepository factoryRepository;

    FactoryService(GridRepository gridRepository, ModelRepository modelRepository, FactoryRepository factoryRepository){
        this.gridRepository = gridRepository;
        this.modelRepository = modelRepository;
        this.factoryRepository = factoryRepository;
    }

//    @Bean
//    private PasswordEncoder passwordEncoderService(){
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    public Factory getFactoryByID(long id){ return factoryRepository.findById(id).orElse(null);}

    public PlacedModel getPlacedModelById(long id){ return modelRepository.findById(id).orElse(null);}

    public Factory saveFactory(Factory factory) {
//        PasswordEncoder passwordEncoder = passwordEncoderService();
//        factory.setPassword(passwordEncoder.encode(factory.getPassword()));
        return factoryRepository.save(factory);
    }

    public Optional<Factory> getFactoryById(long id) {
        return factoryRepository.findById(id);
    }
    public void deleteFactoryById(long id) {
        factoryRepository.deleteById(id);
    }

    public void initializeField(long factoryID){
        Factory factory = getFactoryByID(factoryID);

        for(int i= 0; i < factory.getHeight(); i++){
            for(int j= 0; j < factory.getWidth(); j++){
                for(int k = 0; k < factory.getDepth(); k++){
                    Field field = new Field();
                    field.setPosition(new Position(i,j,k));
                    field.setPlacedModelID(null);
                    gridRepository.save(field);
                }
            }
        }
    }

    public Field getFieldByPosition(Position pos, long factoryID){
        for(Field f: gridRepository.findAll()){
            Position fieldPos = f.getPosition();
            if(fieldPos.getX() == pos.getX() &&
               fieldPos.getY() == pos.getY() &&
               fieldPos.getZ() == pos.getZ() &&
               f.getFactoryID() == factoryID)
                return f;
        }
        return null;
    }

    public Field getFieldById(Long id) {
        return gridRepository.findById(id).orElse(null);
    }
/**
    public Model getModelById(Long id) {
        return modelRepository.findById(id).orElse(null);
    }
 **/
    public Long getPlacedModelIdFromField(@PathVariable Long id) {
        Field field = getFieldById(id);
        return field.getPlacedModelID();
    }

    private void placeModelIntoField(PlacedModel placedModel, Position position){
        Field f = getFieldByPosition(position,placedModel.getFactoryID());
        f.setPlacedModelID(placedModel.getId());
        //Todo: save in repository
    }

    public void putModelOnField(PlacedModel placedModel, Position rootPosition){
        //TODO: create model from input of frontend
        if(checkForPlacement(new PlacedModel())){
            //todo place it on the field and call placeModelInoField(to refresh the database
            //also answer frontend
        }
    }

    private boolean checkField(Field f, PlacedModel thisModel, String ori, Factory factory){
        boolean condition = false;
        String counterOri = "";
        int extraX = 0, extraY = 0;
        Input input = thisModel.getInputByPosition(f.getPosition());
        Output output = thisModel.getOutputByPosition(f.getPosition());

        switch (ori){
            case "North":
                if(f.getPosition().getY()> 0){
                    condition = true;
                    extraY = -1;
                }
                counterOri = "South";
                break;
            case "South":
                int height = factory.getHeight();
                if(f.getPosition().getY()< height-1){
                    condition = true;
                    extraY = 1;
                }
                counterOri = "North";
                break;
            case "East":
                int width = factory.getWidth();
                if(f.getPosition().getX()< width-1){
                    condition = true;
                    extraX = 1;
                }
                counterOri = "West";
                break;
            case "West":
                if(f.getPosition().getX()> 0){
                    condition = true;
                    extraX = -1;
                }
                counterOri = "East";
                break;
        }
        if(condition){
            Position tmpPosition = new Position(f.getPosition().getX()+extraX,f.getPosition().getY()+extraY,f.getPosition().getZ());
            Field tmpField = getFieldByPosition(tmpPosition,thisModel.getFactoryID());
            PlacedModel tmpPlacedModel  = getPlacedModelById(tmpField.getPlacedModelID());

            if(thisModel.getId() == tmpPlacedModel.getId()) return true;

            //zeigt mein input auf ein feld das kein output ist
            if (input != null && input.getOrientation().equals(ori)){
                if(!tmpPlacedModel.getOutputByPosition(tmpPosition).getOrientation().equals(counterOri)) return false;

            }
            //zeigt mein output auf ein feld das kein input ist
            else if(output != null && output.getOrientation().equals(ori)){
                if(!tmpPlacedModel.getInputByPosition(tmpPosition).getOrientation().equals(counterOri)) return false;

            }
            //zeigt mein feld auf ein feld das einen in/output in richtugn meines feldes hat
            else{
                if(tmpPlacedModel.getOutputByPosition(tmpPosition).getOrientation().equals(counterOri)) return false;
                if(tmpPlacedModel.getInputByPosition(tmpPosition).getOrientation().equals(counterOri)) return false;

            }
        }
        return true;
    }



    private boolean checkForPlacement(PlacedModel thisModel){
        Factory factory= getFactoryByID(thisModel.getFactoryID());
        int height = factory.getHeight(), width = factory.getWidth(), depth = factory.getDepth();
        Field tmpField;
        PlacedModel tmpPlacedModel;
        Position tmpPosition;

        //check if fields are free
        for(Field f :thisModel.getPlacedFields()) {
            if (f.getPlacedModelID() != 0) return false;
        }

        for(Field f :thisModel.getPlacedFields()) {
            //check north
            if(!checkField(f,thisModel,"North",factory)) return false;
            //check north
            if(!checkField(f,thisModel,"South",factory)) return false;
            //check north
            if(!checkField(f,thisModel,"East",factory)) return false;
            //check north
            if(!checkField(f,thisModel,"West",factory)) return false;
        }
        //if everything is ok return true;
        return true;
    }

    public void rotateModel(PlacedModel thisModel, Position newPosition){
        List<Field> newPosList= new ArrayList<>();
        PlacedModel backupModel = thisModel;
        // check field if height or width still fits
        for(Field f: thisModel.getPlacedFields())
            newPosList.add(getFieldByPosition(adjustPosition(thisModel,newPosition,f.getPosition()),thisModel.getFactoryID()));

        for(Output o: thisModel.getOutputs()) {
            newPosList.add(getFieldByPosition(adjustPosition(thisModel, newPosition, o.getPosition()),thisModel.getFactoryID()));
            o.setOrientation(rotateOrientation(o.getOrientation()));
        }
        for(Input i: thisModel.getInputs()){
            newPosList.add(getFieldByPosition(adjustPosition(thisModel, newPosition, i.getPosition()),thisModel.getFactoryID()));
            i.setOrientation(rotateOrientation(i.getOrientation()));
        }
        thisModel.setOrientation(rotateOrientation(thisModel.getOrientation()));

        if(checkForPlacement(thisModel)){
            for(Field f: backupModel.getPlacedFields()){
                removeModelFromField(f);
            }
            for(Field f: thisModel.getPlacedFields()){
                placeModelIntoField(thisModel,f.getPosition());
            }
        }
    }

    private Position adjustPosition(PlacedModel thisModel, Position newPosition, Position tmpPos){
        int tmpValue = tmpPos.getX() - thisModel.getRootPos().getX();
        tmpPos.setX(tmpPos.getY() - thisModel.getRootPos().getY() +newPosition.getX());
        tmpPos.setY(tmpValue +newPosition.getY());
        tmpPos.setZ(tmpPos.getZ() - thisModel.getRootPos().getZ()+newPosition.getZ());
        return tmpPos;
    }

    private String rotateOrientation(String orientation){
        return switch (orientation) {
            case "North" -> "East";
            case "East" -> "South";
            case "South" -> "West";
            case "West" -> "North";
            default -> "";
        };
    }

    private void removeModelFromField(Field field){
        field.setPlacedModelID(null);
        //Todo: switch repository entry
    }

    public void removeModelFromFactory(PlacedModel placedModel){

        Factory factory = getFactoryByID(placedModel.getFactoryID());
        for(Field f: placedModel.getPlacedFields()){
            removeModelFromField(f);
        }
        //TODO: delete placedmodel from grid, model and factory repos
    }



    public boolean moveModel(long modelID){
        PlacedModel placedModel = getPlacedModelById(modelID);
        //Todo: remove from field and remove from repository
        //placeMachineToField(machine,newPos);
        //Todo: which informations are needed for this operatino?
        //Todo: switch fields and machine repos
        return true;
    }


    public void saveField(Field field) {

    }


}
