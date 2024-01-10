package de.swtpro.factorybuilder.controller;


import de.swtpro.factorybuilder.DTO.PlacedModelDTO;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.repository.ModelRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import de.swtpro.factorybuilder.service.FactoryService;

import de.swtpro.factorybuilder.service.ModelService;
import de.swtpro.factorybuilder.service.PlacedModelService;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private record PlaceRequestDTO(int x, int y, int z, String modelId, long factoryID) {
    };

    private record MoveRequestDTO(int x, int y, int z, long id, long factoryId) {
    };

    private record RotateRequestDTO(long id, long factoryID, String orientation) {
    };

    private record ManipulationRequestDTO(int x, int y, int z, long id, String orientation, long factoryID) {
    };


    private record DeleteRequestDTO(long factoryId, long id){
    };

    private record ResponseDTO(long id, String modelGltf) {
    };

    @CrossOrigin
    @PostMapping("/place")
    public ResponseEntity<Long> place(@RequestBody PlaceRequestDTO placeRequestDTO) {

        Position pos = new Position(placeRequestDTO.x, placeRequestDTO.y, placeRequestDTO.z);
        Model model = modelService.getByName(placeRequestDTO.modelId).orElseThrow();
        PlacedModel placedModel = placedModelService.createPlacedModel(model,pos,placeRequestDTO.factoryID);

        LOGGER.info(placedModel.toString());

        // Entity wir in Datenbank erzeugt, und id wird gesendet
        return ResponseEntity.ok(placedModel.getId());
    }

    @CrossOrigin
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody DeleteRequestDTO deleteRequestDTO) {
        boolean deleted = placedModelService.removeModelFromFactory(deleteRequestDTO.id);
        return ResponseEntity.ok(deleted);
    }

    @CrossOrigin
    @PostMapping("/rotate")
    public ResponseEntity<Boolean> rotate(@RequestBody RotateRequestDTO rotateRequestDTO) {
        //Position pos = new Position(placeRequestDTO.x, placeRequestDTO.y, placeRequestDTO.z);

        //boolean rotated = placedModelService.rotateModel(idToRotate, pos, factoryService.getFactoryById(placeRequestDTO.factoryID).orElseThrow());

        //LOGGER.info("rotate entity: " + String.valueOf(idToRotate) + String.valueOf(rotated));

        LOGGER.info(rotateRequestDTO.toString());
        return ResponseEntity.ok(true);
    }

    @CrossOrigin
    @PostMapping("/move")
    public ResponseEntity<Boolean> move(@RequestBody MoveRequestDTO moveRequestDTO) {
        Position pos = new Position(moveRequestDTO.x, moveRequestDTO.y, moveRequestDTO.z);
        boolean moved = placedModelService.moveModel(moveRequestDTO.id, pos);
        LOGGER.info(moveRequestDTO.toString());
        LOGGER.info("move entity: " + String.valueOf(moveRequestDTO.id) + String.valueOf(moved));
        LOGGER.info("Moved: " + moved);
        return ResponseEntity.ok(moved);
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public List<Model> getAll() {
        return modelService.getAllByTypes(ModelType.MACHINE, ModelType.OTHER,
                ModelType.ITEM_PROCESSED, ModelType.ITEM_RESOURCE, ModelType.ITEM_PRODUCT);
    }

}
