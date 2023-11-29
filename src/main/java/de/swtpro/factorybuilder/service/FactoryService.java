package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.repository.MachineRepository;
import de.swtpro.factorybuilder.utility.Position;
import de.swtpro.factorybuilder.entity.Machine;
import de.swtpro.factorybuilder.entity.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class FactoryService {
    @Autowired
    private GridRepository gridRepository;
    @Autowired
    private MachineRepository machineRepository;
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

    public Machine getMachineById(Long id) {
        return machineRepository.findById(id).orElse(null);
    }
    public Long getMachineIdFromField(@PathVariable Long id) {
        Field field = getFieldById(id);
        return field.getMachineID();
    }



    public Machine getMachineByPosition(Position pos){
        return getMachineById(getFieldByPosition(pos).getMachineID());
    }

    public void placeMachineToField(Machine machine, Position position){
        Field f = getFieldByPosition(position);
        f.setMachineID(machine.getMachineID());
        //Todo: save in repository
    }

    private boolean checkForPlacement(Position rootPosition, Machine thisMachine){
        Machine otherMachine;
        int fieldsize = 5// getFieldByPosition(rootPosition).getCompleteFieldSize(); ändern
        Field checkFields;
        Position checkPosition = null;

        //Abfrage sind die Felder belegt auf denen die Maschine platziert werden soll
        for(int i = 0; i <thisMachine.getHeight();i++){
            for(int j = 0; j <thisMachine.getWidth();j++){
                int x = rootPosition.getX(),y = rootPosition.getY(),z = rootPosition.getZ();

                switch(thisMachine.getOrientation()) {
                    case "North":
                        checkPosition = new Position(x+j,y+i,z);
                        break;
                    case "South":
                        checkPosition = new Position(x+j,y-i,z);
                        break;
                    case "East":
                        checkPosition = new Position(x+i,y+j,z);
                        break;
                    case "West":
                        checkPosition = new Position(x-i,y+j,z);
                        break;
                }
                checkFields = getFieldByPosition(checkPosition);
                if(checkFields.getMachineID() != 0)return false;
            }
        }

        //pruefe umliegende Felder
        //Todo: umliegende felder korrigieren fuer N_O_S_W sowie input output
        for(int i = thisMachine.getHeight(); i <thisMachine.getHeight()+1;i++) {
            for (int j = thisMachine.getWidth(); j < thisMachine.getWidth()+1; j++) {
                int x = rootPosition.getX(),y = rootPosition.getY(),z = rootPosition.getZ();
                boolean skip = false;
                switch (thisMachine.getOrientation()) {
                    case "North":
                        if (x + j < fieldsize && y + i < fieldsize) {
                            checkPosition = new Position(x + j, y + i, z);
                            break;
                        } else skip = true;
                        break;

                    case "South":
                        if (x + j < fieldsize && y - i >= 0) {
                            checkPosition = new Position(x + j, y - i, z);
                            break;
                        } else skip = true;
                        break;
                    case "East":
                        if (x + i < fieldsize && y + j < fieldsize) {
                            checkPosition = new Position(x + i, y + j, z);
                            break;
                        } else skip = true;
                        break;
                    case "West":
                        if (x - i >= 0 && y + j < fieldsize) {
                            checkPosition = new Position(x - i, y + j, z);
                            break;
                        } else skip = true;
                        break;
                }
                if (!skip) {// skip is for the edges of the field
                    checkFields = getFieldByPosition(checkPosition);
                    if (checkFields.getMachineID() != 0){
                        otherMachine = getMachineById(checkFields.getMachineID());
                    }
                }
                else skip = false;
            }
        }

        return true;
    }

    public void removeMachineFromField(Position pos){
        Field field = getFieldByPosition(pos);
        //Todo: switch repository entry
    }



    public boolean moveMachine(Position pos,Position newPos){
        Machine machine = getMachineByPosition(pos);
        //Todo: remove from field and remove from repository
        placeMachineToField(machine,newPos);
        //Todo: which informations are needed for this operatino?
        //Todo: switch fields and machine repos
        return true;
    }

    public void saveField(Field field) {

    }


}
