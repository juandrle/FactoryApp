package FieldLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactoryFieldService {

    @Autowired
    private FactoryFieldRepository factoryFieldRepository;

    public void saveFactoryFields(List factoryFields) {
        factoryFieldRepository.save(factoryFields);
    }

    public FieldOLD getFactoryFieldById(Long id) {
        return factoryFieldRepository.findById(id).orElse(null);
    }

    public void addFieldToFactoryFields(Long factoryFieldId, FieldOLD field, FactoryFields factoryFields) {
        FieldOLD field = factoryFieldRepository.findById(factoryFieldId).orElse(null);
        if (factoryFields != null) {
            factoryFields.addField(field);
            factoryFieldRepository.save(factoryFields);
        }
    }

    public void deleteFieldFromFactoryFields(Long factoryFieldsId, FieldOLD field) {
        FieldOLD factoryFields = factoryFieldRepository.findById(factoryFieldsId).orElse(null);
        if (factoryFields != null) {
            factoryFields.deleteField(field);
            factoryFieldRepository.save(factoryFields);
        }
    }

    // Weitere Methoden zum Aktualisieren, Löschen, Abfragen von FactoryFields, etc.
}

