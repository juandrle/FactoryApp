package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.DTO.entity.MoveRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.PlaceRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.RotateRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.saveScriptDTO;
import de.swtpro.factorybuilder.DTO.factory.DeleteRequestDTO;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.service.FactoryService;

import de.swtpro.factorybuilder.service.ModelService;
import de.swtpro.factorybuilder.service.PlacedModelService;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/entity")
public class EntityRestAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRestAPIController.class);
    ModelService modelService;
    FactoryService factoryService;
    PlacedModelService placedModelService;

    @Autowired
    ResourceLoader resourceLoader;

    EntityRestAPIController(ModelService modelService, FactoryService factoryService,
            PlacedModelService placedModelService) {
        this.modelService = modelService;
        this.factoryService = factoryService;
        this.placedModelService = placedModelService;
    }

    @CrossOrigin
    @PostMapping("/place")
    public ResponseEntity<Long> place(@RequestBody PlaceRequestDTO placeRequestDTO) {

        Position pos = new Position(placeRequestDTO.x(), placeRequestDTO.y(), placeRequestDTO.z());
        Model model = modelService.getByName(placeRequestDTO.modelId()).orElseThrow();
        PlacedModel placedModel = placedModelService.createPlacedModel(model, pos, placeRequestDTO.factoryID());

        LOGGER.info(placedModel.toString());

        // Entity wir in Datenbank erzeugt, und id wird gesendet
        return ResponseEntity.ok(placedModel.getId());
    }

    @CrossOrigin
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody DeleteRequestDTO deleteRequestDTO) {
        boolean deleted = placedModelService.removeModelFromFactory(deleteRequestDTO.id());
        return ResponseEntity.ok(deleted);
    }

    @CrossOrigin
    @PostMapping("/rotate")
    public ResponseEntity<Boolean> rotate(@RequestBody RotateRequestDTO rotateRequestDTO) {
        // Position pos = new Position(placeRequestDTO.x, placeRequestDTO.y,
        // placeRequestDTO.z);

        // boolean rotated = placedModelService.rotateModel(idToRotate, pos,
        // factoryService.getFactoryById(placeRequestDTO.factoryID).orElseThrow());

        // LOGGER.info("rotate entity: " + String.valueOf(idToRotate) +
        // String.valueOf(rotated));

        LOGGER.info(rotateRequestDTO.toString());
        return ResponseEntity.ok(true);
    }

    @CrossOrigin
    @PostMapping("/move")
    public ResponseEntity<Boolean> move(@RequestBody MoveRequestDTO moveRequestDTO) {
        Position pos = new Position(moveRequestDTO.x(), moveRequestDTO.y(), moveRequestDTO.z());
        boolean moved = placedModelService.moveModel(moveRequestDTO.id(), pos);
        LOGGER.info(moveRequestDTO.toString());
        LOGGER.info("move entity: " + moveRequestDTO.id() + moved);
        return ResponseEntity.ok(true);
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public List<Model> getAll() {
        return modelService.getAllByTypes(ModelType.MACHINE, ModelType.TRANSPORT, ModelType.OTHER,
                ModelType.ITEM_PROCESSED, ModelType.ITEM_RESOURCE, ModelType.ITEM_PRODUCT);
    }



    // Scripting:

    @CrossOrigin
    @GetMapping("/getScriptContent/{modelId}") 
    public ResponseEntity<String> getScriptingContent(@PathVariable long modelId) {

        LOGGER.info("ModelId: ", modelId); 
        LOGGER.info("BE Funktion getScriptContent wurde aufgerufen LULE");

        PlacedModel placedModel = null;

        try {
            placedModel = placedModelService.getPlacedModelById(modelId).orElseThrow();
            String scriptContent = placedModel.getScript(); 
            LOGGER.info("Script wurde in DB gefunden.");
            return ResponseEntity.ok(scriptContent);
            //return scriptContent;

        } catch(NoSuchElementException e) {
            LOGGER.info("Script konnte nicht gespeichert werden, da es modelId in DB nicht gefunden wurde.", e.getCause());
        }

        return ResponseEntity.ok("default Script, weil script nicht aus DB gezogen werden konnte.");
    }
    
    
    @CrossOrigin
    @PostMapping("/postScript/{modelId}")
    public void postScriptingContent(@RequestBody saveScriptDTO saveScriptRequest) { 

        LOGGER.info("postScriptingContent() (RestAPI) erreicht. Script, das gespeichert werden soll: ", saveScriptRequest.scriptContent().toString());                                                                        
        LOGGER.info(saveScriptRequest.toString());

        PlacedModel placedModel = null;

        try {
            placedModel = placedModelService.getPlacedModelById(saveScriptRequest.modelID()).orElseThrow();
            placedModel.setScript(saveScriptRequest.scriptContent());
            LOGGER.info("Script wurde erfolgreich in DB gespeichert.");
        } catch (NoSuchElementException e) {
            LOGGER.info("ModelId wurde in DB nicht gefunden -> Script kann nicht gespeichert werden.", e.getCause());
        }

        // PS: nur script wird in DB gespeichert, user- & systemProperties werden bei jedem get-Aufruf neu aus Skript interpretiert
    }
    
    
    @CrossOrigin
    @GetMapping("/systemProperties/getAll/{modelId}") 
    public ResponseEntity<String> getSystemProperties(@PathVariable long modelId) {
        
        LOGGER.info("BE Funktion getSystemProperties wurde aufgerufen LULE");
        return ResponseEntity.ok("getSystemProperties default");

        // hier muss das Script aus der DB geholt werden, interpretiert werden und ausgelesene Variable systemProperties zurueckgeben
    }

    @CrossOrigin
    @GetMapping("/userProperties/getAll/{modelId}") 
    public ResponseEntity<String> getUserProperties(@PathVariable long modelId) {
        
        LOGGER.info("BE Funktion getUserProperties wurde aufgerufen LULE");
        return ResponseEntity.ok("getUserProperties default");

        // hier muss das Script aus der DB geholt werden, interpretiert werden und ausgelesene Variable userProperties zurueckgeben
    }
    
}
