package de.swtpro.factorybuilder.service.user;

import de.swtpro.factorybuilder.entity.User;
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

    private String role = "USER";

    // @PositiveOrZero(message = "Punkte müssen größer oder gleich Null sein")
    // private int points;
    
    public UserFormular(){
        this.username="";
        this.password="";
        this.role = "USER";
        // this.punkte = 0;
    }

    public void toBenutzer(User b){
        b.setUsername(this.username);
        b.setPassword(this.password);
        b.setPasswordCheck(passwordCheck);
        b.setRole(this.role);
        // b.setPunkte(this.punkte);
    }

    public void fromBenutzer(User b){
        this.username = b.getUsername();
        this.password = b.getPassword();
        this.role = b.getRole();
        // this.punkte = b.getPunkte();
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
