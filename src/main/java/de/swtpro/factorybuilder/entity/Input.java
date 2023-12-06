package de.swtpro.factorybuilder.entity;

import jakarta.persistence.Embeddable;

import javax.swing.text.Position;
@Embeddable
public class Input implements Processing{
    private String orientation;
    private Position position;

    public Input(Position position, String orientation){
        this.position = position;
        this.orientation = orientation;
    }

    public Input() {

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
