package de.swtpro.factorybuilder.entity;

import java.util.List;

// import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "app_user")
public class User implements Serializable {
    @Id
    private String username;

    @NotBlank
    private String password;

    // @NotBlank(message = "Password can not be empty!")
    // private String passwordCheck;

    private String role = "USER";

    // private Position position;

    // private Position pointOfView;

    // @Column
    // private Long lastFactoryId;

    @OneToMany
    private List<Factory> createdFactories;


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

    // public Position getPosition() {
    //     return position;
    // }

    // public void setPosition(Position position) {
    //     this.position = position;
    // }

    // public Position getPointOfView() {
    //     return pointOfView;
    // }

    // public void setPointOfView(Position pointOfView) {
    //     this.pointOfView = pointOfView;
    // }

    // public Long getLastFactoryId() {
    //     return lastFactoryId;
    // }

    // public void setLastFactoryId(Long lastFactoryId) {
    //     this.lastFactoryId = lastFactoryId;
    // }

    public List<Factory> getCreatedFactories() {
        return createdFactories;
    }

    public void setCreatedFactories(List<Factory> createdFactories) {
        this.createdFactories = createdFactories;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // public String getPasswordCheck() {
    //     return passwordCheck;
    // }

    // public void setPasswordCheck(String passwordCheck) {
    //     this.passwordCheck = passwordCheck;
    // }



    // getters and setters
}
