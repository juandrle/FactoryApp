package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Field;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.utility.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldService {
    private final GridRepository gridRepository;

    FieldService(GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }


    public Optional<Field> getFieldByPosition(Position pos, long factoryId){
        return gridRepository.findByPosAndFactoryID(pos, factoryId);
    }
    public Field saveField(Field f) {
        return gridRepository.save(f);
    }


       /*
    initialize field(angestossen von frontend
     */



    //Todo: id zurueckgeben, dann ist es nicht mehr notwendig bei position zu suchen
}
