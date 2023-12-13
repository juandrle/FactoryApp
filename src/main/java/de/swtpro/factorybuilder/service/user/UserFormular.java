package de.swtpro.factorybuilder.service.user;

import java.util.List;

import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class UserFormular {
    
    @NotBlank(message = "Username can not be empty")
    @Size(min=2,max=80, message = "Username has to have between 2 and 80 characters")
    private String username;

    @NotBlank(message = "Password can not be empty")
    @Size(min=2,max=80, message = "Password has to have between 2 and 80 characters")
    private String password;


    @NotBlank(message = "Password can not be empty!")
    private String passwordCheck;

   
    private Position position;

    private Position pointOfView;

    
    public UserFormular(){
        this.username="";
        this.password="";
    }

    public void toUser(User b){
        b.setUsername(this.username);
        b.setPassword(this.password);
        b.setRole("USER");
        
    }

    public void fromUser(User b){
        this.username = b.getUsername();
        this.password = b.getPassword();
       
    }

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

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
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



}
