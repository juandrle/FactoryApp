package de.swtpro.factorybuilder.controller;
import de.swtpro.factorybuilder.DTO.entity.MoveRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.PlaceRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.RotateRequestDTO;
import de.swtpro.factorybuilder.DTO.factory.DeleteRequestDTO;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.service.FactoryService;

import de.swtpro.factorybuilder.service.ModelService;
import de.swtpro.factorybuilder.service.model.AbstractModelService;
import de.swtpro.factorybuilder.service.model.ManipulateAbstractModelService;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/entity")
public class EntityRestAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRestAPIController.class);
    ModelService modelService;
    AbstractModelService abstractModelService;
    ManipulateAbstractModelService manipulateAbstractModelService;

    EntityRestAPIController(ModelService modelService, AbstractModelService abstractModelService,
                            ManipulateAbstractModelService manipulateAbstractModelService) {
        this.modelService = modelService;
        this.abstractModelService = abstractModelService;
        this.manipulateAbstractModelService = manipulateAbstractModelService;
    }

    @CrossOrigin
    @PostMapping("/place")
    public ResponseEntity<Long> place(@RequestBody PlaceRequestDTO placeRequestDTO) {

        Position pos = new Position(placeRequestDTO.x(), placeRequestDTO.y(), placeRequestDTO.z());
        Model model = modelService.getByName(placeRequestDTO.modelId()).orElseThrow();
       AbstractModel abstractModel = abstractModelService.createPlacedModel(model,pos,placeRequestDTO.factoryID());


       if (abstractModel == null) {

           // return conflict status (HTTP 409) when placedModel is null
           return ResponseEntity.status(HttpStatus.CONFLICT).build();
       }

       LOGGER.info("placed Model with placedModelID: " + abstractModel.getId() + " and modelID: " + model.getId() + " ('" + abstractModel.getName() + "')");

       // Entity wir in Datenbank erzeugt, und id wird gesendet
       return ResponseEntity.ok(abstractModel.getId());
    }

    @CrossOrigin
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody DeleteRequestDTO deleteRequestDTO) {
        boolean deleted = manipulateAbstractModelService.removeModelFromFactory(deleteRequestDTO.id());
        return ResponseEntity.ok(deleted);
    }

    @CrossOrigin
    @PostMapping("/rotate")
    public ResponseEntity<Boolean> rotate(@RequestBody RotateRequestDTO rotateRequestDTO) {
        boolean rotated = manipulateAbstractModelService.rotateModel(rotateRequestDTO.id(), rotateRequestDTO.orientation());

        LOGGER.info("rotate entity: " + String.valueOf(rotateRequestDTO.id()) + " is " + String.valueOf(rotated));

        return ResponseEntity.ok(rotated);
    }

    @CrossOrigin
    @PostMapping("/move")
    public ResponseEntity<Boolean> move(@RequestBody MoveRequestDTO moveRequestDTO) {
        Position pos = new Position(moveRequestDTO.x(), moveRequestDTO.y(), moveRequestDTO.z());
        boolean moved = manipulateAbstractModelService.moveModel(moveRequestDTO.id(), pos);

        //LOGGER.info(moveRequestDTO.toString());
        //LOGGER.info("move entity: " + String.valueOf(moveRequestDTO.id) + String.valueOf(moved));
        LOGGER.info("Moved: " + moved);
        return ResponseEntity.ok(moved);
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public List<Model> getAll() {
        return modelService.getAllByTypes(ModelType.MACHINE, ModelType.TRANSPORT, ModelType.OTHER,
                ModelType.ITEM_PROCESSED, ModelType.ITEM_RESOURCE, ModelType.ITEM_PRODUCT);
    }

}
