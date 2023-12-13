package de.swtpro.factorybuilder.entity;

import java.util.List;

import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import java.io.Serializable;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;

    @NotBlank
    private String password;

    @NotBlank(message = "Password can not be empty!")
    private String passwordCheck;

    private String role = "USER";

    private Position position;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private Position pointOfView;

    @Column
    private Long lastFactoryId;

    @ElementCollection
    @Column(name = "factory_id")
    private List<Long> usedFactories;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPointOfView() {
        return pointOfView;
    }

    public void setPointOfView(Position pointOfView) {
        this.pointOfView = pointOfView;
    }

    public Long getLastFactoryId() {
        return lastFactoryId;
    }

    public void setLastFactoryId(Long lastFactoryId) {
        this.lastFactoryId = lastFactoryId;
    }

    public List<Long> getUsedFactories() {
        return usedFactories;
    }

    public void setUsedFactories(List<Long> usedFactories) {
        this.usedFactories = usedFactories;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }



    // getters and setters
}
