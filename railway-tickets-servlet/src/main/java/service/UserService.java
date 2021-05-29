package service;

import model.Role;
import model.User;
import repository.UserRepository;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class UserService {
    //    TODO - change logger to log4j
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final Supplier<MessageDigest> digestFactory;

    public UserService(UserRepository userRepository, Supplier<MessageDigest> digestFactory) {
        this.userRepository = userRepository;
        this.digestFactory = digestFactory;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb != null) {
            return false;
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, encode(password));
    }

    private String encode(String password) {
        var result = new StringBuilder();
        MessageDigest digest = digestFactory.get();
        digest.update(password.getBytes());

        byte[] hash = digest.digest();
        for (byte b : hash) {
            result.append(String.format("%02X", b));
        }

        return result.toString();
    }
}
