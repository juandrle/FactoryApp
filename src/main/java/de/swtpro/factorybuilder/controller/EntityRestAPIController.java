package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.service.FactoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity")
public class EntityRestAPIController{

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRestAPIController.class);

    // do we need the repos in here as well ?
    ModelRepository modelRepository;
    PlacedModelRepository placedModelRepository;

    @Autowired
    FactoryService factoryService;

    private record PlaceRequestDTO(int x, int y, int z, String entityID, String orientation) {
    };

    @CrossOrigin
    @PostMapping("/place")
    public boolean place (@RequestBody PlaceRequestDTO placeRequestDTO){
        LOGGER.info(placeRequestDTO.toString());






        return true;
    }
}
