package GridLogic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    /** @PostMapping public void createField(@RequestBody Field field) {
        fieldService.saveField(field);
    } **/

    @GetMapping("/{id}")
    public Field getFieldById(@PathVariable Long id) {
        return fieldService.getFieldById(id);
    }






    //Todoliste:
    /*
    initialize field(angestossen von frontend
    aktualisieren??
    isPlaceable
    place machine to Field
    remove machine from field
    move (add und remove benutzen)


     */
}


/**
 * muss das in die propperties?
 * spring.datasource.url=jdbc:mysql://localhost:3306/deineDatenbank??????
 * spring.datasource.username=deinBenutzername
 * spring.datasource.password=deinPasswort
 * spring.jpa.hibernate.ddl-auto=update
 */