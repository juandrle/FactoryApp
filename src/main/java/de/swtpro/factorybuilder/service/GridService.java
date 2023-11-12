package de.swtpro.factorybuilder.service;

import de.swtpro.factorybuilder.entity.Cube;

public interface GridService {

    String addCubeToGrid(String gridID, Cube cube);

    // JSON-Parser parseCubeJSON implementieren
}
