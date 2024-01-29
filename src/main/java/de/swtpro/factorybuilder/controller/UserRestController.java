package de.swtpro.factorybuilder.controller;

import de.swtpro.factorybuilder.DTO.user.UserNameDTO;
import de.swtpro.factorybuilder.service.UserService;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import de.swtpro.factorybuilder.DTO.user.UserDTO;
import de.swtpro.factorybuilder.entity.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserRestController {


    Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDTO userDTO, BindingResult userFormularError) {
        if (userFormularError.hasErrors()) {
            // Return error response
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid user data\"}");

        } else {

            boolean nameTaken = userService.checkUsername(userDTO.username());

            // check if the username is already taken
            if (nameTaken) {
                return ResponseEntity.ok("username taken");
            }

            // check if the passwords match
            if (!userDTO.password().equals(userDTO.passwordCheck())) {
                return ResponseEntity.ok("passwords don't match");
            }

            // create user
            userService.signUp(userDTO);

            // Return success response or any relevant data
            logger.info("User created successfully. Username: " + userDTO.username());

            return ResponseEntity.ok("successful");
        }
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserDTO userDTO, BindingResult userFormularError) {
        if (userFormularError.hasErrors()) {
            // Return error response
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid user data\"}");

        } else {
            logger.info("wir sind aufm richtigen weg");
            Optional<User> userExists = userService.getUserByName(userDTO.username());

            if (userExists.isPresent()) {
                User currentUser = userExists.get();

                if (userService.checkPassword(userDTO.password(), currentUser.getPassword())) {
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(currentUser.getUsername(), null));
                    return ResponseEntity.ok("login successful");
                } else {
                    return ResponseEntity.ok("wrong password");
                }

            } else {
                return ResponseEntity.ok("user not found");
            }
        }
    }

    @CrossOrigin
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.ok("logout successful");
    }

    
    @CrossOrigin
    @GetMapping("/getAll")
    public ResponseEntity<List<UserNameDTO>> getAll() {
        List<User> users = userService.getAll();
        List<UserNameDTO> userNameDTOs = new ArrayList<>();
        for (User u: users) if (!u.getCreatedFactories().isEmpty())userNameDTOs.add(new UserNameDTO(u.getUsername()));
        return ResponseEntity.ok(userNameDTOs);
    }

}