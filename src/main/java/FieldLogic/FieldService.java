package FieldLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    public void saveField(Field field) {
        fieldRepository.save(field);
    }

    public Field getFieldById(Long id) {
        return fieldRepository.findById(id).orElse(null);
    }

    // Weitere Methoden zum Aktualisieren, Löschen, Abfragen von Feldern, etc.
}
