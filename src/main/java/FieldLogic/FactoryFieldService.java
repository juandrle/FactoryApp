package FieldLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactoryFieldService {

    @Autowired
    private FactoryFieldRepository factoryFieldRepository;

    public void saveFactoryFields(FactoryFields factoryFields) {
        factoryFieldRepository.save(factoryFields);
    }

    public FactoryFields getFactoryFieldById(Long id) {
        return factoryFieldRepository.findById(id).orElse(null);
    }

    public void addFieldToFactoryFields(Long factoryFieldId, Field field) {
        FactoryFields factoryFields = factoryFieldRepository.findById(factoryFieldId).orElse(null);
        if (factoryFields != null) {
            factoryFields.addField(field);
            factoryFieldRepository.save(factoryFields);
        }
    }

    public void deleteFieldFromFactoryFields(Long factoryFieldsId, Field field) {
        FactoryFields factoryFields = factoryFieldRepository.findById(factoryFieldsId).orElse(null);
        if (factoryFields != null) {
            factoryFields.deleteField(field);
            factoryFieldRepository.save(factoryFields);
        }
    }

    // Weitere Methoden zum Aktualisieren, Löschen, Abfragen von FactoryFields, etc.
}

