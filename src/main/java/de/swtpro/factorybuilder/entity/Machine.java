package de.swtpro.factorybuilder.entity;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.swing.text.Position;

public interface Machine {

    public long getMachineID();

    public Position getOutputPosition();
    public Position getInputPosition();

    public int getWidth();
    public int getHeight();
    public String getInputOrientation();
    public String getOutputOrientation();
    public String getOrientation();


}
