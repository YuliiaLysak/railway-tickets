package service;

import model.Role;
import model.User;
import repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.logging.Logger;

public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    private static String encode(String password) {
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());

            byte[] hash = digest.digest();
            for (byte b : hash) {
                result.append(String.format("%02X", b));
            }
        } catch (NoSuchAlgorithmException exception) {
            LOGGER.warning("Could not encode password");
            LOGGER.warning(exception.getMessage());
        }

        return result.toString();
    }
}
