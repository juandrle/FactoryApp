package de.swtpro.factorybuilder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.swtpro.factorybuilder.service.GridService;

@RestController
@RequestMapping("/api")
public class CubeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CubeController.class);
    
    @Autowired
    private GridService gridService;

    @PostMapping(value = "/cube", produces = "application/json")
    public ResponseEntity<String> setCubeJSON(@RequestBody String cubeJSON) {
        LOGGER.info("JSON received");
        gridService.addCubeToGrid(cubeJSON, null);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
