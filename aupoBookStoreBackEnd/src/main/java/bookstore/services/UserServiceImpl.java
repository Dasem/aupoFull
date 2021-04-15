package bookstore.services;

import bookstore.model.entities.*;
import bookstore.model.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        if (userRepository.findUserByLogin(user.getLogin()) != null) {
            throw new IllegalArgumentException("User already exist!");
        }
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }
}
