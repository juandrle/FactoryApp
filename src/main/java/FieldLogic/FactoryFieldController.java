package FieldLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("FactoryFields")
public class FactoryFieldController {

    @Autowired
    private FactoryFieldService factoryFieldService;

    @PostMapping
    public void createFactoryFields(@RequestBody FactoryFields factoryFields) {
        factoryFieldService.saveFactoryFields(factoryFields);
    }

    @GetMapping("/{id}")
    public FieldOLD getFactoryFieldsById(@PathVariable Long id) {
        return factoryFieldService.getFactoryFieldById(id);
    }

    @PostMapping("/{id}/add-field")
    public void addFieldToFactoryFields(@PathVariable Long id, @RequestBody FieldOLD field) {
        factoryFieldService.addFieldToFactoryFields(id, field);
    }

    @DeleteMapping("/{id}/delete-field")
    public void deleteFieldFromFactoryFields(@PathVariable Long id, @RequestBody FieldOLD field) {
        factoryFieldService.deleteFieldFromFactoryFields(id, field);
    }
}

