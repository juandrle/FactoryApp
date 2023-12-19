package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.DTO.PlacedModelDTO;
import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.FieldRepository;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.utility.Position;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FactoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryService.class);

    private final FactoryRepository factoryRepository;

    FactoryService(FactoryRepository factoryRepository) {
        this.factoryRepository = factoryRepository;
    }

    // @Bean
    // private PasswordEncoder passwordEncoderService(){
    // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    // }
    public Factory saveFactory(Factory factory) {
        // PasswordEncoder passwordEncoder = passwordEncoderService();
        // factory.setPassword(passwordEncoder.encode(factory.getPassword()));
        return factoryRepository.save(factory);
    }

    public Optional<Factory> getFactoryById(long id) {
        return factoryRepository.findById(id);
    }

    public void deleteFactoryById(long id) {
        factoryRepository.deleteById(id);
    }


    /**
     * public Model getModelById(Long id) {
     * return placedModelRepository.findById(id).orElse(null);
     * }
     **/


}
