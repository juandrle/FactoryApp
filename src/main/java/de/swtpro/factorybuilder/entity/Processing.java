package de.swtpro.factorybuilder.entity;


import de.swtpro.factorybuilder.utility.Position;

public interface Processing {
     public String getOrientation();

     public Position getPosition();

     public void setPosition(Position position);
     public void setOrientation(String orientation);

}
