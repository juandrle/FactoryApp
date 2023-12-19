package de.swtpro.factorybuilder.controller;


import de.swtpro.factorybuilder.DTO.PlacedModelDTO;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.service.FactoryService;

import de.swtpro.factorybuilder.service.ModelService;
import de.swtpro.factorybuilder.service.PlacedModelService;
import de.swtpro.factorybuilder.utility.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/entity")
public class EntityRestAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRestAPIController.class);

    // do we need the repos in here as well ?
    @Autowired
    ModelService modelService;

    @Autowired
    FactoryService factoryService;

    @Autowired
    PlacedModelService placedModelService;

    private record PlaceRequestDTO(int x, int y, int z, String entityID, String orientation, long factoryID) {
    }

    ;

    private record ResponseDTO(long id, String modelGltf) {
    }

    ;

    @CrossOrigin
    @PostMapping("/place")
    public boolean place(@RequestBody PlaceRequestDTO placeRequestDTO) {
        Position pos = new Position(placeRequestDTO.x, placeRequestDTO.y, placeRequestDTO.z);
        // Long Parse for workaround to not error
        boolean placed = placedModelService.createPlacedModel(modelService.getByID(Long.parseLong(placeRequestDTO.entityID)).orElseThrow(), pos, placeRequestDTO.factoryID);
        LOGGER.info("placed entity: " + String.valueOf(placed));
        return placed;
    }

    @CrossOrigin
    @PostMapping("/delete")
    public boolean delete(@RequestBody long idToDelete) {
        // factoryID long or int ?
        // factoryService.getEntitysFromFactory(placeRequestDTO.factoryID);
        // delete by entityID (from placedModelRepository)

        boolean deleted = placedModelService.removeModelFromFactory(idToDelete);
        LOGGER.info("deleted entity: " + String.valueOf(idToDelete) + String.valueOf(deleted));
        return deleted;
    }

    @CrossOrigin
    @PostMapping("/rotate")
    public boolean rotate(@RequestBody long idToRotate, PlaceRequestDTO placeRequestDTO) {
        Position pos = new Position(placeRequestDTO.x, placeRequestDTO.y, placeRequestDTO.z);

        boolean rotated = placedModelService.rotateModel(idToRotate, pos, factoryService.getFactoryById(placeRequestDTO.factoryID).orElseThrow());

        LOGGER.info("rotate entity: " + String.valueOf(idToRotate) + String.valueOf(rotated));
        return rotated;
    }

    @CrossOrigin
    @PostMapping("/move")
    public boolean move(@RequestBody long idToMove, PlaceRequestDTO placeRequestDTO) {
        Position pos = new Position(placeRequestDTO.x, placeRequestDTO.y, placeRequestDTO.z);
        boolean moved = placedModelService.moveModel(idToMove, pos);
        LOGGER.info("move entity: " + String.valueOf(idToMove) + String.valueOf(moved));
        return moved;
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public List<Model> getAll() {
        return modelService.getAll();
    }

}
