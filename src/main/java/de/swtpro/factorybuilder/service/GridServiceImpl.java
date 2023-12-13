package de.swtpro.factorybuilder.service;

// import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.swtpro.factorybuilder.entity.PlacedModel;
import org.springframework.stereotype.Service;

@Service
public class GridServiceImpl implements GridService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GridServiceImpl.class);

    @Override
    public String addCubeToGrid(String gridID, PlacedModel cube) {

        // Grid aus Datenbank laden – noch nicht implementiert
        // z.B. grid.setCube(…) …
        // im Anschluss Wert in der Datenbank speichern (automatisch ?)

        throw new UnsupportedOperationException("Unimplemented method 'addCubeToGrid'");
    }
    
    @Override
    public PlacedModel getCubeFromJSON(String cubeJSON) {
        PlacedModel testCube = parseJSON(cubeJSON);
        return testCube;
    }

    @Override
    public PlacedModel parseJSON(String cubeJSON) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            PlacedModel cube = mapper.readValue(cubeJSON, PlacedModel.class);
            LOGGER.info(cubeJSON);
            return cube;
        } catch (JsonMappingException e) {
            LOGGER.error(cubeJSON, e);
            return null;
        } catch (JsonProcessingException e) {
            LOGGER.error(cubeJSON, e);
            return null;
        }
    }

    @Override
    public String getJSONFromPojo(PlacedModel cube) {
        return generateJSON(cube);
    }

    @Override
    public String generateJSON(PlacedModel cube) {
        ObjectWriter JSONWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = JSONWriter.writeValueAsString(JSONWriter);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* public Optional<Cube> getCubeFromOb(String cubeId) {
        // Cube-Objekt aus Datenbank laden – noch nicht implementiert
        return cubeRepo.findById(cubeId); 
    } */

}
