package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Field;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.repository.FieldRepository;
import de.swtpro.factorybuilder.service.model.AbstractModelService;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FieldService {
    private final FieldRepository fieldRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelService.class);

    FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public Optional<Field> getFieldByPosition(Position pos, long factoryId){
        LOGGER.info("POS: " + pos + ", FactoryID: " + factoryId);
        assert fieldRepository.countByPosAndFactoryID(pos, factoryId) == 1;
        return fieldRepository.findByPosAndFactoryID(pos, factoryId);
    }
    public Optional<Field> getFieldById(Long id) {
        return fieldRepository.findById(id);
    }
    @Transactional
    public void deleteAllByFactoryID(Long id) {
        fieldRepository.deleteAllByFactoryID(id);
    }
    public void initializeField(Factory factory) {
        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < factory.getWidth(); i++) {
            for (int j = 0; j < factory.getDepth(); j++) {
                for (int k = 0; k < factory.getHeight(); k++) {
                    Field field = new Field(new Position(i - factory.getWidth() / 2, j - factory.getDepth() / 2, k));
                    // Field field = new Field(new Position(i - factory.getDepth() / 2, j - factory.getWidth() / 2, k));
                    //field.setPosition(new Position(i - factory.getWidth() / 2, j - factory.getDepth() / 2, k));
                    field.setFactoryID(factory.getFactoryID());
                    fields.add(field);

                }
            }
        }
        fieldRepository.saveAll(fields);
    }
    @Transactional
    public void setPlacedModelOnField(AbstractModel abstractModel, Field field) {
        field.setPlacedModel(abstractModel);
    }
    @Transactional
    public void deletePlacedModelOnField(Field field) {
        field.setPlacedModel(null);
    }
}
