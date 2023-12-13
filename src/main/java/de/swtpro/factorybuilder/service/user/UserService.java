package de.swtpro.factorybuilder.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import de.swtpro.factorybuilder.entity.User;

@Service
public interface UserService {
    
    User createUser(User v);

    boolean checkUsername(String username);

    public Optional<User> getUserByName(String username);

}
