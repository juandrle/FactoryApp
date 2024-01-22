package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.DTO.factory.FactoryCreateDTO;
import de.swtpro.factorybuilder.DTO.factory.FactoryEnterDTO;
import de.swtpro.factorybuilder.DTO.factory.FactoryPasswordCheckDTO;
import de.swtpro.factorybuilder.DTO.factory.FactoryUserDTO;
import de.swtpro.factorybuilder.DTO.factory.UpdateImageFactoryDTO;
import de.swtpro.factorybuilder.DTO.entity.PlacedModelDTO;
import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.service.*;
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
    PlacedModelService placedModelService;
    ModelService modelService;
    FieldService fieldService;
    ImgConverterService imgConverterService;
    UserService userService;

    FactoryRestAPIController(FactoryService factoryService, PlacedModelService placedModelService,
                             ModelService modelService, FieldService fieldService, ImgConverterService imgConverterService,
                             UserService userService) {
        this.factoryService = factoryService;
        this.placedModelService = placedModelService;
        this.modelService = modelService;
        this.fieldService = fieldService;
        this.imgConverterService = imgConverterService;
        this.userService = userService;
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
        List<PlacedModel> allPlacedModelsOfFactory = placedModelService.findAllByFactoryId(f);
        new Thread(() -> {
            fieldService.deleteAllByFactoryID(idToDelete);
        }).start();
        for (PlacedModel m: allPlacedModelsOfFactory) {
            placedModelService.removeModelFromFactory(m.getId());
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

    @CrossOrigin
    @PostMapping("/enter")
    public ResponseEntity<Boolean> enter(@RequestBody FactoryUserDTO factoryUserDTO){
        LOGGER.info("LULE ENTER HAT GEKLAPPT");
        LOGGER.info("Received FactoryUserDTO: " + factoryUserDTO);
        try {
            long factoryId = factoryUserDTO.factoryId();
            String username = factoryUserDTO.username();

            LOGGER.info("Factory ID: " + factoryId + ", Username: " + username);

            // Factory factory = factoryService.getFactoryById(factoryId).orElseThrow();
            User user = userService.getUserByName(username).orElseThrow();

            List<User> currentUsers = factoryService.getCurrentUsersInFactory(factoryId);
            LOGGER.info("DAS IST UNSERE LISTE" + currentUsers);
            if(currentUsers.contains(user)){
                return ResponseEntity.ok(false);
            }else {
                factoryService.addUserToFactory(factoryId, user);
                return ResponseEntity.ok(true);
            }
           
            
        }catch (Exception e){
            LOGGER.error("Exception in enter method", e);
            return ResponseEntity.ok(false);
        }
        
    }

    @CrossOrigin
    @PostMapping("/leave")
    public ResponseEntity<Boolean> leave(@RequestBody FactoryUserDTO factoryUserDTO){
        try {
            long factoryId = factoryUserDTO.factoryId();
            String username = factoryUserDTO.username();

            Factory factory = factoryService.getFactoryById(factoryId).orElseThrow();
            User user = userService.getUserByName(username).orElseThrow();

            List<User> currentUsers = factoryService.getCurrentUsersInFactory(factoryId);

            if(currentUsers.contains(user)){
                factoryService.removeUserFromFactory(factoryId, user);
                return ResponseEntity.ok(true);
            }else {
                
                return ResponseEntity.ok(false);
            }
            
        }catch (Exception e){
            return ResponseEntity.ok(false);
        }
        
    }

    public List<PlacedModelDTO> getEntitiesFromFactory(Factory factory) {
        // Nimm alle aus modelRepository mit factoryId == factoryId
        List<PlacedModel> placedModels = placedModelService.findAllByFactoryId(factory);
        List<PlacedModelDTO> dtos = new ArrayList<>();

        for (PlacedModel placedModel : placedModels) {
            // TODO: NULL CHECK
            Model m = modelService.getByID(placedModel.getModelId()).orElse(null);
            assert m != null;
            PlacedModelDTO dto = new PlacedModelDTO(
                    placedModel.getFactoryID(),
                    placedModel.getId(),
                    placedModel.getOrientation(),
                    placedModel.getRootPos().getX(),
                    placedModel.getRootPos().getY(),
                    placedModel.getRootPos().getZ(),
                    m.getModelFile(), // Füge den Pfad hinzu, wie erforderlich
                    m.getName()
            );
            dtos.add(dto);
        }

        return dtos;
    }
}
