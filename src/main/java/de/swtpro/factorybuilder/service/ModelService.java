package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.utility.ModelType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ModelService {

    ModelRepository modelRepository;

    ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> getAllByTypes(ModelType... types) {
        List<Model> temp = new ArrayList<>();
        for (ModelType type : types) {
            temp.addAll(modelRepository.findAllByType(type));
        }
        return temp;
    }


    public List<Model> saveAllModels(List<Model> m) {
        return modelRepository.saveAll(m);
    }

    public Optional<Model> getByID(long id) {
        return modelRepository.findById(id);
    }

    public Optional<Model> getByName(String name) {
        return modelRepository.findByName(name);
    }
}
