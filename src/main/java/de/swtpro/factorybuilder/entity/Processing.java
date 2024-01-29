package de.swtpro.factorybuilder.entity;

import de.swtpro.factorybuilder.utility.Position;

public interface Processing {
    String getOrientation();

    Position getPosition();

    void setPosition(Position position);

    void setOrientation(String orientation);

}
