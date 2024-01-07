package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.DTO.FactoryDTO;
import de.swtpro.factorybuilder.DTO.FactoryEnterDTO;
import de.swtpro.factorybuilder.DTO.FactoryPasswordCheckDTO;
import de.swtpro.factorybuilder.DTO.PlacedModelDTO;
import de.swtpro.factorybuilder.DTO.UpdateImageFactoryDTO;
import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.service.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.html.HTMLImageElement;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/factory")
public class FactoryRestAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryRestAPIController.class);
    FactoryService factoryService;
    PlacedModelService placedModelService;
    ModelService modelService;
    FieldService fieldService;
    ImgConverterService imgConverterService;

    FactoryRestAPIController(FactoryService factoryService, PlacedModelService placedModelService,
            ModelService modelService, FieldService fieldService, ImgConverterService imgConverterService) {
        this.factoryService = factoryService;
        this.placedModelService = placedModelService;
        this.modelService = modelService;
        this.fieldService = fieldService;
        this.imgConverterService = imgConverterService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody FactoryDTO factoryDTO) {
        Factory f = new Factory();
        f.setName(factoryDTO.name());
        f.setWidth(factoryDTO.width());
        f.setDepth(factoryDTO.depth());
        f.setHeight(factoryDTO.height());
        f.setPassword(factoryDTO.password());
        f = factoryService.saveFactory(f);
        fieldService.initializeField(f);
        return ResponseEntity.ok(f.getFactoryID());
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody long idToDelete) {
        factoryService.deleteFactoryById(idToDelete);
        return ResponseEntity.ok(true);
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public List<FactoryEnterDTO> getAll() {
        List<FactoryEnterDTO> factories = new ArrayList<>();
        for (Factory f : factoryService.getAllFactories()) {
            factories
                    .add(new FactoryEnterDTO(f.getFactoryID(), f.getName(), f.getWidth(), f.getDepth(), f.getHeight(), f.getPassword().equals("") ? false : true));
                    LOGGER.info(f.getPassword());
        }
        ;
        return factories;
    }

    @PostMapping("/updateImage")
    @Transactional
    public ResponseEntity<Boolean> updateImage(@RequestBody UpdateImageFactoryDTO updateImageFactoryDTO) {
        try {
            Factory factory = factoryService.getFactoryById(updateImageFactoryDTO.factoryID()).orElseThrow();
            factory.setScreenshot(imgConverterService.dataUrlToByteArray(updateImageFactoryDTO.screenshot()));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @CrossOrigin
    @GetMapping("/getImage/{factoryID}")
    public ResponseEntity<String> getImage(@PathVariable long factoryID) {
        try {
            Factory factory = factoryService.getFactoryById(factoryID).orElseThrow();
            String dataUrl = imgConverterService.byteArrayToDataUrl(factory.getScreenshot(), "image/png");
            return ResponseEntity.ok(dataUrl);

        } catch (Exception e) {
            return ResponseEntity.ok("failed");
        }
    }

    @CrossOrigin
    @PostMapping("/checkPassword")
    public ResponseEntity<Boolean> checkPassword(@RequestBody FactoryPasswordCheckDTO check) {
        try {
            // get password from DB 
            Factory factory = factoryService.getFactoryById(check.id()).orElseThrow();
            String passwordFromDB = factory.getPassword();
    
            boolean passwordsMatch = check.password().equals(passwordFromDB);
            LOGGER.info(passwordFromDB);
    
            return ResponseEntity.ok(passwordsMatch);

        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    public ResponseEntity<List<PlacedModelDTO>> load(@RequestBody long idToLoad) {
        return ResponseEntity.ok(getEntitysFromFactory(factoryService.getFactoryById(idToLoad).orElseThrow()));
    }

    public List<PlacedModelDTO> getEntitysFromFactory(Factory factory) {
        // Nimm alle aus modelRepository mit factoryId == factoryId
        List<PlacedModel> placedModels = placedModelService.findAllByFactoryId(factory);
        List<PlacedModelDTO> dtos = new ArrayList<>();

        for (PlacedModel placedModel : placedModels) {
            // TODO: NULL CHECK
            Model m = modelService.getByID(placedModel.getModelId()).orElse(null);
            PlacedModelDTO dto = new PlacedModelDTO(
                    placedModel.getFactoryID(),
                    placedModel.getId(),
                    placedModel.getOrientation(),
                    placedModel.getRootPos().getX(),
                    placedModel.getRootPos().getY(),
                    placedModel.getRootPos().getZ(),
                    m.getModelFile() // Füge den Pfad hinzu, wie erforderlich
            );
            dtos.add(dto);
        }

        return dtos;
    }
}
