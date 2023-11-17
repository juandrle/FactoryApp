package GridLogic;

import FieldLogic.FieldOLD;
import FieldLogic.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldService {

    @Autowired
    private GridRepository gridRepository;

    /**
     * public void saveField(Field field) {
        gridRepository.save(field);
    }



     **/

    public Field getFieldById(Long id) {
        return gridRepository.findById(id).orElse(null);
    }

    public long getID


    // Weitere Methoden zum Aktualisieren, Löschen, Abfragen von Feldern, etc.
}
