package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.utility.Position;
import de.swtpro.factorybuilder.entity.Model;
import org.hibernate.result.Outputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class FactoryService {
    @Autowired
    private GridRepository gridRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private FactoryRepository factoryRepository;

    public Factory getFactoryByID(long id){ return factoryRepository.findById(id).orElse(null);}

    public PlacedModel getPlacedModelById(long id){ return modelRepository.findById(id).orElse(null);}

    public void initializeField(long factoryID){
        Factory factory = getFactoryByID(factoryID);

        for(int i= 0; i < factory.getHeight(); i++){
            for(int j= 0; j < factory.getWidth(); j++){
                for(int k = 0; k < factory.getDepth(); k++){
                    Field field = new Field();
                    field.setPosition(new Position(i,j,k));
                    field.setPlacedModelID(null);
                }
            }
        }
        //Todo: put fields in repository
    }


    public Field getFieldByPosition(Position pos){
        for(Field f: gridRepository.findAll()){
            Position fieldPos = f.getPosition();
            if( fieldPos.getX() == pos.getX() &&
                    fieldPos.getY() == pos.getY() &&
                    fieldPos.getZ() == pos.getZ())
                return f;
        }
        return null;
    }

    public Field getFieldByPosition(Position pos, long factoryID){
        //todo getField by position;
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

    private void placeModelInoField(PlacedModel placedModel, Position position){
        Field f = getFieldByPosition(position);
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

    private boolean checkForPlacement(PlacedModel thisModel){
        Factory factory= getFactoryByID(thisModel.getFactoryID());
        int height = factory.getHeight(), width = factory.getWidth(), depth = factory.getDepth();
        Field tmpField;
        PlacedModel tmpPlacedModel;
        Position tmpPosition;

        //pruefe felder ob auf ihnen etwas platziert ist
        for(Field f :thisModel.getPlacedFields()) {
            if (f.getPlacedModelID() != 0) return false;
        }

        for(Field f :thisModel.getPlacedFields()) {
            Input input = thisModel.getInputByPosition(f.getPosition());
            Output output = thisModel.getOutputByPosition(f.getPosition());

            //Check North ------------------------------
            if(f.getPosition().getY()> 0){
                tmpPosition = new Position(f.getPosition().getX(),f.getPosition().getY()-1,f.getPosition().getZ());
                tmpField = getFieldByPosition(tmpPosition,thisModel.getFactoryID());
                tmpPlacedModel  = getPlacedModelById(tmpField.getPlacedModelID());

                //zeigt mein input auf ein feld das kein output ist
                if (input != null && input.getOrientation().equals("North")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getOutputByPosition(tmpPosition).equals("South")) return false;
                    }
                }
                //zeigt mein output auf ein feld das kein input ist
                else if(output != null && output.getOrientation().equals("North")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getInputByPosition(tmpPosition).equals("South")) return false;
                    }
                }
                //zeigt mein feld auf ein feld das einen in/output in richtugn meines feldes hat
                else{
                    if(tmpPlacedModel != null){
                        if(tmpPlacedModel.getOutputByPosition(tmpPosition).equals("South")) return false;
                        if(tmpPlacedModel.getInputByPosition(tmpPosition).equals("South")) return false;
                    }
                }
            }
            //zeigt mein in/output auserhalb des Spielfeldrandes
            else if(f.getPosition().getY() == 0){
                if(thisModel.getOutputByPosition(f.getPosition()).getOrientation().equals("North"))return false;
                if(thisModel.getInputByPosition(f.getPosition()).getOrientation().equals("North"))return false;
            }
            //end of North check -----------------------
            //-----------------------------------------------------------------------------------------------------
            //-----------------------------------------------------------------------------------------------------
            //-----------------------------------------------------------------------------------------------------
            //Check South ------------------------------
            if(f.getPosition().getY()< height-1){
                tmpPosition = new Position(f.getPosition().getX(),f.getPosition().getY()+1,f.getPosition().getZ());
                tmpField = getFieldByPosition(tmpPosition,thisModel.getFactoryID());
                tmpPlacedModel  = getPlacedModelById(tmpField.getPlacedModelID());

                //zeigt mein input auf ein feld das kein output ist
                if (input != null && input.getOrientation().equals("South")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getOutputByPosition(tmpPosition).equals("North")) return false;
                    }
                }
                //zeigt mein output auf ein feld das kein input ist
                else if(output != null && output.getOrientation().equals("South")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getInputByPosition(tmpPosition).equals("North")) return false;
                    }
                }
                //zeigt mein feld auf ein feld das einen in/output in richtugn meines feldes hat
                else{
                    if(tmpPlacedModel != null){
                        if(tmpPlacedModel.getOutputByPosition(tmpPosition).equals("North")) return false;
                        if(tmpPlacedModel.getInputByPosition(tmpPosition).equals("North")) return false;
                    }
                }
            }
            //zeigt mein in/output auserhalb des Spielfeldrandes
            else if(f.getPosition().getY() == height-1){
                if(thisModel.getOutputByPosition(f.getPosition()).getOrientation().equals("South"))return false;
                if(thisModel.getInputByPosition(f.getPosition()).getOrientation().equals("South"))return false;
            }
            //end of South check -----------------------
            //-----------------------------------------------------------------------------------------------------
            //-----------------------------------------------------------------------------------------------------
            //-----------------------------------------------------------------------------------------------------
            //Check West -------------------------------
            if(f.getPosition().getX()> 0){
                tmpPosition = new Position(f.getPosition().getX()+1,f.getPosition().getY(),f.getPosition().getZ());
                tmpField = getFieldByPosition(tmpPosition,thisModel.getFactoryID());
                tmpPlacedModel  = getPlacedModelById(tmpField.getPlacedModelID());

                //zeigt mein input auf ein feld das kein output ist
                if (input != null && input.getOrientation().equals("West")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getOutputByPosition(tmpPosition).equals("East")) return false;
                    }
                }
                //zeigt mein output auf ein feld das kein input ist
                else if(output != null && output.getOrientation().equals("West")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getInputByPosition(tmpPosition).equals("East")) return false;
                    }
                }
                //zeigt mein feld auf ein feld das einen in/output in richtugn meines feldes hat
                else{
                    if(tmpPlacedModel != null){
                        if(tmpPlacedModel.getOutputByPosition(tmpPosition).equals("East")) return false;
                        if(tmpPlacedModel.getInputByPosition(tmpPosition).equals("East")) return false;
                    }
                }
            }
            //zeigt mein in/output auserhalb des Spielfeldrandes
            else if(f.getPosition().getY() == height-1){
                if(thisModel.getOutputByPosition(f.getPosition()).getOrientation().equals("West"))return false;
                if(thisModel.getInputByPosition(f.getPosition()).getOrientation().equals("West"))return false;
            }
            //end of West check -----------------------------------------
            //-----------------------------------------------------------------------------------------------------
            //-----------------------------------------------------------------------------------------------------
            //-----------------------------------------------------------------------------------------------------
            //Check East -------------------------------
            if(f.getPosition().getX()< width-1){
                tmpPosition = new Position(f.getPosition().getX()+1,f.getPosition().getY(),f.getPosition().getZ());
                tmpField = getFieldByPosition(tmpPosition,thisModel.getFactoryID());
                tmpPlacedModel  = getPlacedModelById(tmpField.getPlacedModelID());

                //zeigt mein input auf ein feld das kein output ist
                if (input != null && input.getOrientation().equals("East")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getOutputByPosition(tmpPosition).equals("West")) return false;
                    }
                }
                //zeigt mein output auf ein feld das kein input ist
                else if(output != null && output.getOrientation().equals("East")){
                    if(tmpPlacedModel != null){
                        if(!tmpPlacedModel.getInputByPosition(tmpPosition).equals("West")) return false;
                    }
                }
                //zeigt mein feld auf ein feld das einen in/output in richtugn meines feldes hat
                else{
                    if(tmpPlacedModel != null){
                        if(tmpPlacedModel.getOutputByPosition(tmpPosition).equals("West")) return false;
                        if(tmpPlacedModel.getInputByPosition(tmpPosition).equals("West")) return false;
                    }
                }
            }
            //zeigt mein in/output auserhalb des Spielfeldrandes
            else if(f.getPosition().getY() == height-1){
                if(thisModel.getOutputByPosition(f.getPosition()).getOrientation().equals("East"))return false;
                if(thisModel.getInputByPosition(f.getPosition()).getOrientation().equals("East"))return false;
            }
            //end of East check -----------------------------------------

        }
        //if everything is ok return true;
        return true;
    }

    public void removeModelFromField(Position pos){
        Field field = getFieldByPosition(pos);

        PlacedModel placedModel = getPlacedModelById(field.getPlacedModelID());

        //Todo: switch repository entry
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
