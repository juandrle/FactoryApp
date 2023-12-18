package de.swtpro.factorybuilder.controller;


import de.swtpro.factorybuilder.DTO.PlacedModelDTO;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.service.FactoryService;

import de.swtpro.factorybuilder.utility.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/entity")
public class EntityRestAPIController{

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRestAPIController.class);

    public EntityRestAPIController(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }
    // do we need the repos in here as well ?
    ModelRepository modelRepository;

    @Autowired
    FactoryService factoryService;

    private record PlaceRequestDTO(int x, int y, int z, String entityID, String orientation, long factoryID) {
    };
    private record ResponseDTO(long id, String modelGltf){};

    @CrossOrigin
    @PostMapping("/place")
    public boolean place (@RequestBody PlaceRequestDTO placeRequestDTO){
        Position pos = new Position(placeRequestDTO.x, placeRequestDTO.y, placeRequestDTO.z);
        boolean placed = factoryService.createPlacedModel(placeRequestDTO.entityID,pos,placeRequestDTO.factoryID);
        LOGGER.info("placed entity: " + String.valueOf(placed));
        return placed;
    }

    @CrossOrigin
    @PostMapping("/delete")
    public boolean delete (@RequestBody PlaceRequestDTO placeRequestDTO){
        // factoryID long or int ?
        // factoryService.getEntitysFromFactory(placeRequestDTO.factoryID);
        // create IEntityDelete in backendEntity
        // go into factory (if UUID isn't unique)
        // delete by entityID (from placedModelRepository)
        factoryService.removeModelFromFactory(null);
        return false;
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public List<Model> getAll(){
        LOGGER.info(modelRepository.findAll().toString());
        return modelRepository.findAll();
    }

    @CrossOrigin
    @GetMapping("getAll/{factoryId}")
    public List<PlacedModelDTO> getAllByFactoryId(@PathVariable int factoryId) {
        return factoryService.getEntitysFromFactory(factoryId);
    }
    
}
