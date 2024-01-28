package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.repository.FactoryRepository;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

@Service
public class FactoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryService.class);

    private final FactoryRepository factoryRepository;
    private final PasswordEncoder passwordEncoder;

    FactoryService(FactoryRepository factoryRepository, PasswordEncoder passwordEncoder) {
        this.factoryRepository = factoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // @Bean
    // private PasswordEncoder passwordEncoderService(){
    // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    // }

    private Map<Long, List<User>> factoryUsers = new HashMap<>();

    public void addUserToFactory(Long factoryId, User user){
        factoryUsers.computeIfAbsent(factoryId, k -> new ArrayList<>()).add(user);
    }

    public void removeUserFromFactory(Long factoryId, User user){
        factoryUsers.computeIfPresent(factoryId, (k, users) -> {
            users.remove(user);
            return users.isEmpty() ? null : users;
        });
    }

    public List<User> getCurrentUsersInFactory(Long factoryId) {
        return factoryUsers.getOrDefault(factoryId, new ArrayList<>());
    }

    public Factory saveFactory(Factory factory) {
        if (!factory.getPassword().isEmpty())factory.setPassword(passwordEncoder.encode(factory.getPassword()));
        return factoryRepository.save(factory);
    }

    public Optional<Factory> getFactoryById(long id) {
        return factoryRepository.findById(id);
    }

    public void deleteFactoryById(long id) {
        factoryRepository.deleteById(id);
    }

    public List<Factory> getAllFactories(){
        return factoryRepository.findAll();
    }
    /**
     * public Model getModelById(Long id) {
     * return placedModelRepository.findById(id).orElse(null);
     * }
     **/
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


}
