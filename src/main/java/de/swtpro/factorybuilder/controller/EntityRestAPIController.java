package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.repository.ModelRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity")
public class EntityRestAPIController{
    ModelRepository modelRepository;

    }
