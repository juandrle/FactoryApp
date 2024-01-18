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

    @CrossOrigin
    @GetMapping("/getScriptContent/{modelId}") // + modelId oder requestbody siehe getRequests.ts
    public ResponseEntity<String> getScriptingContent(@PathVariable long modelId) {
        try {
            Resource resource = resourceLoader.getResource("classpath:scripting/test.txt");
            InputStream input = resource.getInputStream();

            LOGGER.info("BE Funktion getScriptContent wurde aufgerufen LULE");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                String fileContent = content.toString();
                LOGGER.info(fileContent);
                return ResponseEntity.ok(fileContent);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed reading content of script");
        }

        // if file in ordner(resources/scripting) : file oeffnen und inhalt als string
        // zurueckgeben,
        // else : leeren string zurueckgeben
        // (ohne datenbank zugriff)

    }

    @CrossOrigin
    @PostMapping("/postScript")
    public void postScriptingContent(@RequestBody saveScriptDTO saveScriptRequest) { // statt modelID in url mitgeben:
                                                                                     // body in postrequest mit modelID
                                                                                     // und scriptContent, siehe
                                                                                     // postRequests.ts

        LOGGER.info("postScriptingContent() (RestAPI) erreicht. Script, das gespeichert werden soll: ", saveScriptRequest.scriptContent().toString());                                                                        
        LOGGER.info(saveScriptRequest.toString()); // test

        // if file in ordner(resources/scripting) : file überschreiben,
        // else : file anlegen und string von frontend rein PS: file name einfach immer
        // modelId oder modelName + modelID, damit beim GET wieder finden koennen
        // (ohne datenbank zugriff)
    }

}
