package de.swtpro.factorybuilder.controller;


import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.service.FactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factory")
public class FactoryRestAPIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryRestAPIController.class);
    @Autowired
    FactoryService factoryService;
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
        factoryService.initializeField(f.getFactoryID());
        return ResponseEntity.ok(f.getFactoryID());
    }
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete (@RequestBody long idToDelete) {
        factoryService.deleteFactoryById(idToDelete);
        return ResponseEntity.ok(true);
    }
}
