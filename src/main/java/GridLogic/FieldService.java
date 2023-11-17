package GridLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class FieldService {

    @Autowired
    private GridRepository gridRepository;

    /**
     * public void saveField(Field field) {
        gridRepository.save(field);
    }



     **/

    public Field getFieldByPosition(Position pos){
        for(Field f: gridRepository.findAll()){
            if( f.getX() == pos.getX() &&
                f.getY() == pos.getY() &&
                f.getZ() == pos.getZ())
                return f;
        }
        return null;
    }
    public Field getFieldById(Long id) {
        return gridRepository.findById(id).orElse(null);
    }

    public Long getMachineIdFromField(@PathVariable Long id) {
        Field field = getFieldById(id);
        return field.getMachineID();
    }


    // Weitere Methoden zum Aktualisieren, Löschen, Abfragen von Feldern, etc.
}
