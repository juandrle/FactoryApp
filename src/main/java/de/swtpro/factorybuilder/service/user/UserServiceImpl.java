package de.swtpro.factorybuilder.service.user;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

    @Autowired UserRepository userRepo;
    @Autowired PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User v){
        User vs = new User();
        String encodedPassword= passwordEncoder.encode(v.getPassword());
        v.setPassword(encodedPassword);
        vs = userRepo.save(v);
        return vs;
    }
    
    @Override
    public boolean checkUsername(String username){
        return userRepo.existsById(username);
    }

    @Override
    public Optional<User> getUserByName(String username){
        Optional<User> v = userRepo.findById(username);
        return v;
    }
}
