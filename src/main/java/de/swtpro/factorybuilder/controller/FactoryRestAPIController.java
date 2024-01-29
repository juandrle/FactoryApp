package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.DTO.factory.FactoryCreateDTO;
import de.swtpro.factorybuilder.DTO.factory.FactoryEnterDTO;
import de.swtpro.factorybuilder.DTO.factory.FactoryPasswordCheckDTO;
import de.swtpro.factorybuilder.DTO.factory.UpdateImageFactoryDTO;
import de.swtpro.factorybuilder.DTO.entity.PlacedModelDTO;
import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.service.*;
import de.swtpro.factorybuilder.service.model.AbstractModelService;
import de.swtpro.factorybuilder.service.model.ManipulateAbstractModelService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/factory")
public class FactoryRestAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryRestAPIController.class);
    FactoryService factoryService;
    AbstractModelService abstractModelService;
    ModelService modelService;
    FieldService fieldService;
    ImgConverterService imgConverterService;
    UserService userService;
    ManipulateAbstractModelService manipulateAbstractModelService;

    FactoryRestAPIController(FactoryService factoryService, AbstractModelService abstractModelService,
                             ModelService modelService, FieldService fieldService, ImgConverterService imgConverterService,
                             UserService userService, ManipulateAbstractModelService manipulateAbstractModelService) {
        this.factoryService = factoryService;
        this.abstractModelService = abstractModelService;
        this.modelService = modelService;
        this.fieldService = fieldService;
        this.imgConverterService = imgConverterService;
        this.userService = userService;
        this.manipulateAbstractModelService = manipulateAbstractModelService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody FactoryCreateDTO factoryDTO) {
        try {
            Factory f = new Factory();
            f.setName(factoryDTO.name());
            f.setWidth(factoryDTO.width());
            f.setDepth(factoryDTO.depth());
            f.setHeight(factoryDTO.height());
            f.setPassword(factoryDTO.password());
            User u = userService.getUserByName(factoryDTO.author()).orElseThrow();
            f.setAuthor(u);
            f = factoryService.saveFactory(f);
            u.addFactoryToCreatedFactories(f);
            fieldService.initializeField(f);
            return ResponseEntity.ok(f.getFactoryID());
        } catch (Exception e) {
            return ResponseEntity.ok(0L);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody long idToDelete) {
        Factory f = factoryService.getFactoryById(idToDelete).orElseThrow();
        List<AbstractModel> allAbstractModelsOfFactory = abstractModelService.findAllByFactoryId(f);
        new Thread(() -> {
            fieldService.deleteAllByFactoryID(idToDelete);
        }).start();
        for (AbstractModel m: allAbstractModelsOfFactory) {
            manipulateAbstractModelService.removeModelFromFactory(m.getId());
        }
        f.getAuthor().removeFactoryFromCreatedFactories(f);
        factoryService.deleteFactoryById(idToDelete);
        return ResponseEntity.ok(true);
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public List<FactoryEnterDTO> getAll() {
        List<FactoryEnterDTO> factories = new ArrayList<>();
        for (Factory f : factoryService.getAllFactories()) {
            factories
                    .add(new FactoryEnterDTO(f.getFactoryID(), f.getName(),
                            f.getWidth(), f.getDepth(), f.getHeight(), !f.getPassword().isEmpty(), f.getAuthor().getUsername()));
        }

        return factories;
    }

    @GetMapping("/getAll/{idToLoad}")
    public ResponseEntity<List<PlacedModelDTO>> load(@PathVariable long idToLoad) {
        LOGGER.info(idToLoad + " test");
        return ResponseEntity.ok(getEntitiesFromFactory(factoryService.getFactoryById(idToLoad).orElseThrow()));
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
            if (factory.getScreenshot() == null) throw new Exception("failed");
            String dataUrl = imgConverterService.byteArrayToDataUrl(factory.getScreenshot(), "image/png");
            return ResponseEntity.ok(dataUrl);

        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/checkPassword")
    public ResponseEntity<Boolean> checkPassword(@RequestBody FactoryPasswordCheckDTO check) {
        try {
            Factory factory = factoryService.getFactoryById(check.id()).orElseThrow();
            String passwordFromDB = factory.getPassword();
            boolean passwordsMatch = factoryService.checkPassword(check.password(), passwordFromDB);
            return ResponseEntity.ok(passwordsMatch);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    public List<PlacedModelDTO> getEntitiesFromFactory(Factory factory) {
        // Nimm alle aus modelRepository mit factoryId == factoryId
        List<AbstractModel> abstractEntities = abstractModelService.findAllByFactoryId(factory);
        List<PlacedModelDTO> dtos = new ArrayList<>();

       for (AbstractModel abstractModel : abstractEntities) {
           // TODO: NULL CHECK
//           Model m = modelService.getByID(abstractModel.getModelId()).orElse(null);
//           assert m != null;
           PlacedModelDTO dto = new PlacedModelDTO(
                   abstractModel.getFactory().getFactoryID(),
                   abstractModel.getId(),
                   abstractModel.getOrientation(),
                   abstractModel.getRootPos().getX(),
                   abstractModel.getRootPos().getY(),
                   abstractModel.getRootPos().getZ(),
                   abstractModel.getModelGltf(), // Füge den Pfad hinzu, wie erforderlich
                   abstractModel.getName()
           );
           dtos.add(dto);
       }

        return dtos;
    }
}
