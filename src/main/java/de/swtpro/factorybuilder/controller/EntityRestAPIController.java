package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.DTO.entity.MoveRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.PlaceRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.PropertyDTO;
import de.swtpro.factorybuilder.DTO.entity.RotateRequestDTO;
import de.swtpro.factorybuilder.DTO.entity.saveScriptDTO;
import de.swtpro.factorybuilder.DTO.factory.DeleteRequestDTO;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.model.AbstractModel;

import de.swtpro.factorybuilder.service.ModelService;
import de.swtpro.factorybuilder.service.model.AbstractModelService;
import de.swtpro.factorybuilder.service.model.ManipulateAbstractModelService;
import de.swtpro.factorybuilder.utility.ModelType;
import de.swtpro.factorybuilder.utility.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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



    // Scripting:

    @CrossOrigin
    @GetMapping("/getScriptContent/{modelId}")
    public ResponseEntity<String> getScriptingContent(@PathVariable long modelId) {

        LOGGER.info("ModelId: ", modelId);
        LOGGER.info("BE Funktion getScriptContent wurde aufgerufen LULE");

        AbstractModel abstractModel;

        try {
            abstractModel = abstractModelService.getPlacedModelById(modelId).orElseThrow();
            if (abstractModel.getScript() != null) {
                String scriptContent = abstractModel.getScript();
                LOGGER.info("Script wurde in DB gefunden: ", scriptContent);
                return ResponseEntity.ok(scriptContent);
            } else {
                LOGGER.info("Script ist null (in DB).");
            }
            //return scriptContent;

        } catch(NoSuchElementException e) {
            LOGGER.info("Script konnte nicht gespeichert werden, da es modelId in DB nicht gefunden wurde.", e.getCause());
        }

        return ResponseEntity.ok("default Script, weil script nicht aus DB gezogen werden konnte.");
    }


    @CrossOrigin
    @PostMapping("/postScript/{modelId}")
    public void postScriptingContent(@RequestBody saveScriptDTO saveScriptRequest, @PathVariable long modelId) {

        LOGGER.info("postScriptingContent() (RestAPI) erreicht. Script, das gespeichert werden soll: ", saveScriptRequest.scriptContent().toString());
        LOGGER.info(saveScriptRequest.toString());

        AbstractModel abstractModel;

        try {
            abstractModel = abstractModelService.getPlacedModelById(modelId).orElseThrow();
            abstractModelService.savePlacedModelWithNewScript(abstractModel,saveScriptRequest.scriptContent());

            LOGGER.info("Script wurde erfolgreich in DB gespeichert.");
        } catch (NoSuchElementException e) {
            LOGGER.info("ModelId wurde in DB nicht gefunden -> Script kann nicht gespeichert werden.", e.getCause());
        }

        // PS: nur script wird in DB gespeichert, user- & systemProperties werden bei jedem get-Aufruf neu aus Skript interpretiert
    }


    @CrossOrigin
    @GetMapping("/systemProperties/getAll/{modelId}")
    public ResponseEntity<PropertyDTO[]> getSystemProperties(@PathVariable long modelId) {

        // hier muss das Script aus der DB geholt werden, interpretiert werden und ausgelesene Variable systemProperties zurueckgeben

        // hier geben wir default die Standartwerte zurueck, siehe WIKI: scripting
        // diese muessen überschrieben werden, wenn sie im Script veraendert wurden
        // folgende Zeilen koennen auch in die Klasse, wo das Skript interpretiert wird, sodass man die Variablen durch einen Methodenaufruf bekommt
        PropertyDTO[] systemproperties = {new PropertyDTO("Verarbeitungsgeschwindigkeit", "5 sek"),
        new PropertyDTO("Erfolgswahrscheinlichkeit", "100%"),
        new PropertyDTO("ProduktionsQualitaet", "100%")};

        LOGGER.info("BE Funktion getSystemProperties wurde aufgerufen LULE");
        return ResponseEntity.ok(systemproperties);
    }

    @CrossOrigin
    @GetMapping("/userProperties/getAll/{modelId}")
    public ResponseEntity<PropertyDTO[]> getUserProperties(@PathVariable long modelId) {

        // hier muss das Script aus der DB geholt werden, interpretiert werden und ausgelesene Variable userProperties zurueckgeben

        LOGGER.info("BE Funktion getUserProperties wurde aufgerufen LULE");
        return ResponseEntity.ok(null);
    }

}
