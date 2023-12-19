package de.swtpro.factorybuilder.entity;

import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.Embeddable;
@Embeddable
public class Output implements Processing{
    private String orientation;
    private Position position;

    public Output(Position position, String orientation){
        this.position = position;
        this.orientation = orientation;
    }

    public Output() {

    }

    @Override
    public String getOrientation() {
        return this.orientation;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}

