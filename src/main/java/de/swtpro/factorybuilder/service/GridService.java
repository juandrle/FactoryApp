package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.PlacedModel;

public interface GridService {

    String addCubeToGrid(String gridID, PlacedModel cube);

    PlacedModel getCubeFromJSON(String cubeJSON);

    PlacedModel parseJSON(String cubeJSON);

    String getJSONFromPojo(PlacedModel cube);

    String generateJSON(PlacedModel cube);

}
