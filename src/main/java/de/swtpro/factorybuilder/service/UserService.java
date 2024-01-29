package de.swtpro.factorybuilder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.swtpro.factorybuilder.DTO.user.UserDTO;
import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.repository.UserRepository;


@Service
public class UserService {

    UserRepository userRepo;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    // @Override
    // public User createUser(User v){
    //     User vs = new User();
    //     String encodedPassword= passwordEncoder.encode(v.getPassword());
    //     v.setPassword(encodedPassword);
    //     vs = userRepo.save(v);
    //     return vs;
    // }


    public boolean checkUsername(String username) {
        return userRepo.existsById(username);
    }


    public Optional<User> getUserByName(String username) {
        return userRepo.findById(username);
    }

    public void signUp(UserDTO userDTO) {
        User vs = new User();
        vs.setUsername(userDTO.username());
        String encodedPassword = passwordEncoder.encode(userDTO.password());
        vs.setPassword(encodedPassword);
        userRepo.save(vs);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    public List<User> getAll() {
        return userRepo.findAll();
    }

}
