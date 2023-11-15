package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Cube;

public interface GridService {

    String addCubeToGrid(String gridID, Cube cube);

    Cube getCubeFromJSON(String cubeJSON);

    Cube parseJSON(String cubeJSON);

    String getJSONFromPojo(Cube cube);

    String generateJSON(Cube cube);

}
