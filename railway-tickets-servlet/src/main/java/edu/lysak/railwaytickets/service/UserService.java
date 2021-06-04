package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.model.Role;
import edu.lysak.railwaytickets.model.User;
import edu.lysak.railwaytickets.repository.UserRepository;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Used for processing operations with user
 *
 * @author Yuliia Lysak
 */
public class UserService {

    private final UserRepository userRepository;
    private final Supplier<MessageDigest> digestFactory;

    public UserService(UserRepository userRepository, Supplier<MessageDigest> digestFactory) {
        this.userRepository = userRepository;
        this.digestFactory = digestFactory;
    }

    /**
     * Add new user.
     *
     * @param user - user object for saving.
     * @return true if user was successfully added.
     *
     * @throws BusinessLogicException if user was not added
     */
    public boolean addUser(User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb != null) {
            throw new BusinessLogicException("User with this email already exists");
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    /**
     * Gets user
     *
     * @param email - email of user
     * @param password - password of user
     *
     * @return User object
     */
    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, encode(password));
    }

    /**
     * Provides password encoding.
     *
     * @param password - a password to encode.
     * @return encoded password.
     */
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
