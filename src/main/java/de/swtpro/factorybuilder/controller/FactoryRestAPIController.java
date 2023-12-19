package de.swtpro.factorybuilder.controller;


import de.swtpro.factorybuilder.DTO.PlacedModelDTO;
import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import de.swtpro.factorybuilder.service.FactoryService;
import de.swtpro.factorybuilder.service.FieldService;
import de.swtpro.factorybuilder.service.ModelService;
import de.swtpro.factorybuilder.service.PlacedModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/factory")
public class FactoryRestAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryRestAPIController.class);
    @Autowired
    FactoryService factoryService;
    @Autowired
    PlacedModelService placedModelService;
    @Autowired
    ModelService modelService;
    @Autowired
    FieldService fieldService;
    private record FactoryDTO(String name, String password, int width, int depth, int height){
    };
    @PostMapping("/create")
    public ResponseEntity<Long> create (@RequestBody FactoryDTO factoryDTO){
        Factory f = new Factory();
        f.setName(factoryDTO.name);
        f.setWidth(factoryDTO.width);
        f.setDepth(factoryDTO.depth);
        f.setHeight(factoryDTO.height);
        f.setPassword(factoryDTO.password);
        f = factoryService.saveFactory(f);
        fieldService.initializeField(f);
        return ResponseEntity.ok(f.getFactoryID());
    }
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete (@RequestBody long idToDelete) {
        factoryService.deleteFactoryById(idToDelete);
        return ResponseEntity.ok(true);
    }
    @PostMapping("/getAll")
    public ResponseEntity<List<Factory>> getAll() {
        return ResponseEntity.ok(factoryService.getAllFactories());
    }

    public ResponseEntity<List<PlacedModelDTO>> load(@RequestBody long idToLoad){
        return ResponseEntity.ok(getEntitysFromFactory(idToLoad));
    }


    public List<PlacedModelDTO> getEntitysFromFactory(long factoryId) {
        // Nimm alle aus modelRepository mit factoryId == factoryId
        List<PlacedModel> placedModels = placedModelService.findAllByFactoryId(factoryId);
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
