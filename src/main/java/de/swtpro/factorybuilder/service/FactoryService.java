package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.utility.Position;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.Field;
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



    //TODO: factory einbeziehen und die größe des feldes anpassen

    public void initializeField(int fieldsize){
        for(int i= 0; i < fieldsize; i++){
            for(int j= 0; j < fieldsize; j++){
                Field field = new Field();
                field.setPosition(new Position(i,j,0));
                field.setMachineID(null);
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

    public Field getFieldById(Long id) {
        return gridRepository.findById(id).orElse(null);
    }

    public Model getModelById(Long id) {
        return modelRepository.findById(id).orElse(null);
    }
    public Long getModelIdFromField(@PathVariable Long id) {
        Field field = getFieldById(id);
        return field.getModelID();
    }

    public void placeMachineToField(Model model, Position position){
        Field f = getFieldByPosition(position);
        f.setMachineID(model.getId());
        //Todo: save in repository
    }

    private boolean checkForPlacement(Position rootPosition, Model thisModel){

        return true;
    }

    public void removeModelFromField(Position pos){
        Field field = getFieldByPosition(pos);
        //Todo: switch repository entry
    }



    public boolean moveMachine(long modelID,Position newPos){
        Field field = getFieldByPosition(newPos);
        Model model = getModelById(field.getModelID());
        //Todo: remove from field and remove from repository
        //placeMachineToField(machine,newPos);
        //Todo: which informations are needed for this operatino?
        //Todo: switch fields and machine repos
        return true;
    }


    public void saveField(Field field) {

    }


}
