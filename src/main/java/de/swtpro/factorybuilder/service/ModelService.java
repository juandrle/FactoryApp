package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService {

    ModelRepository modelRepository;

    ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> getAll() {
        return modelRepository.findAll();
    }

    public List<Model> saveAllModels(List<Model> m) {
        return modelRepository.saveAll(m);
    }

    public Optional<Model> getByID(long id) {
        return modelRepository.findById(id);
    }
}
