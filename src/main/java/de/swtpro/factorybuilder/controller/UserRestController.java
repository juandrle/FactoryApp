package de.swtpro.factorybuilder.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.swtpro.factorybuilder.DTO.UserDTO;
import de.swtpro.factorybuilder.component.SessionManager;
import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.service.user.UserFormular;
import de.swtpro.factorybuilder.service.user.UserServiceImpl;
import jakarta.validation.Valid;

@RestController

public class UserRestController {
    Logger logger = LoggerFactory.getLogger(UserRestController.class); 
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private SessionManager sessionManager;
    

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentUser() {
        String username = sessionManager.getCurrentUsername();
        return ResponseEntity.ok(username);
    }
    
    
    @PostMapping("/api/users/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@Valid @RequestBody UserDTO userDTO, BindingResult userFormularError) {
    if (userFormularError.hasErrors()) {
        // Return error response
        return ResponseEntity.badRequest().body("{\"error\": \"Invalid user data\"}");

    } else {

        boolean nameTaken = userServiceImpl.checkUsername(userDTO.username());

        // check if the username is already taken
        if (nameTaken) {
            return ResponseEntity.ok("username taken");
        }

        // check if the passwords match
        if (!userDTO.password().equals(userDTO.passwordCheck())) {
            return ResponseEntity.ok("passwords don't match");
        }

        // create user
        userServiceImpl.signUp(userDTO);

        // Return success response or any relevant data
        logger.info("User created successfully. Username: " + userDTO.username());

        return ResponseEntity.ok("successful");
    }
}

    @PostMapping("/api/users/login")
    @ResponseBody
    public ResponseEntity<String> login(@Valid @RequestBody UserDTO userDTO, BindingResult userFormularError) {
        if (userFormularError.hasErrors()) {
        // Return error response
        return ResponseEntity.badRequest().body("{\"error\": \"Invalid user data\"}");

    } else {
        logger.info("wir sind aufm richtigen weg");
        Optional<User> userExists = userServiceImpl.getUserByName(userDTO.username());

        if(userExists.isPresent()){
            User currentUser = userExists.get();

            if(userServiceImpl.checkPassword(userDTO.password(), currentUser.getPassword())){
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(currentUser.getUsername(), null));
                return ResponseEntity.ok("login successful");
            }else{
                return ResponseEntity.ok("wrong password");
            }

        }else{
            return ResponseEntity.ok("user not found");
        }
    }

        
    }

    @PostMapping("/api/users/logout")
    @ResponseBody
    public ResponseEntity<String> logout(){

        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.ok("logout successful");
    }

}