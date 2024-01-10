package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Field;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.repository.FieldRepository;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FieldService {
    private final FieldRepository fieldRepository;

    FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public Optional<Field> getFieldByPosition(Position pos, long factoryId){
        return fieldRepository.findByPosAndFactoryID(pos, factoryId);
    }
    public Optional<Field> getFieldById(Long id) {
        return fieldRepository.findById(id);
    }
    public void initializeField(Factory factory) {
        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < factory.getWidth(); i++) {
            for (int j = 0; j < factory.getDepth(); j++) {
                for (int k = 0; k < factory.getHeight(); k++) {
                    Field field = new Field();
                    field.setPosition(new Position(i - factory.getWidth() / 2, j - factory.getDepth() / 2, k));
                    field.setFactoryID(factory.getFactoryID());
                    fields.add(field);

                }
            }
        }
        fieldRepository.saveAll(fields);
    }
    @Transactional
    public void setPlacedModelOnField(PlacedModel placedModel, Field field) {
        field.setPlacedModel(placedModel);
    }
    @Transactional
    public void deletePlacedModelOnField(Field field) {
        field.setPlacedModel(null);
    }
}
