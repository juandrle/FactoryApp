package de.swtpro.factorybuilder.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.service.user.UserFormular;
import de.swtpro.factorybuilder.service.user.UserServiceImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping()
public class UserRestController {
    Logger logger = LoggerFactory.getLogger(UserRestController.class); 
    @Autowired
    private UserServiceImpl userServiceImpl;

    
    @PostMapping("/api/users/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@Valid @RequestBody UserDTO userDTO, BindingResult userFormularError) {
    if (userFormularError.hasErrors()) {
        // Return error response
        return ResponseEntity.badRequest().body("Invalid user data");
    } else {
        User currentUser = new User();
        currentUser.setUsername(userDTO.userName());
        currentUser.setPassword(userDTO.password());

        boolean nameTaken = userServiceImpl.checkUsername(currentUser.getUsername());

        // check if the username is already taken
        if (nameTaken) {
            return ResponseEntity.badRequest().body("Username is taken.");
        }

        // check if the passwords match
        if (!userDTO.password().equals(userDTO.passwordCheck())) {
            return ResponseEntity.badRequest().body("The passwords are not the same.");
        }

        // create user
        userServiceImpl.signUp(userDTO);

        // Return success response or any relevant data
        logger.info("User created successfully. Username: " + currentUser.getUsername());

        return ResponseEntity.ok("User created successfully");
    }
}

}





