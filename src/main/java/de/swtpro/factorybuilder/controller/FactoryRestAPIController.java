package de.swtpro.factorybuilder.controller;


import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.service.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/factory")
public class FactoryRestAPIController {
    @Autowired
    FactoryService factoryService;
    private record FactoryDTO(String name, String password, int width, int depth, int height){
    };

    @PostMapping("/create")
    public ResponseEntity<Long> save (@RequestBody FactoryDTO factoryDTO){
        Factory f = new Factory();
        f.setName(factoryDTO.name);
        f.setWidth(factoryDTO.width);
        f.setDepth(factoryDTO.depth);
        f.setHeight(factoryDTO.height);
        f.setPassword(factoryDTO.password);
        factoryService.saveFactory(f);
        return ResponseEntity.ok(f.getFactoryID());
    }
}
