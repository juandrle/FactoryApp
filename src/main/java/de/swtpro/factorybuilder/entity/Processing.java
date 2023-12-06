package de.swtpro.factorybuilder.entity;

import javax.swing.text.Position;

public interface Processing {
     public String getOrientation();

     public Position getPosition();

     public void setPosition(Position position);
     public void setOrientation(String orientation);

}
