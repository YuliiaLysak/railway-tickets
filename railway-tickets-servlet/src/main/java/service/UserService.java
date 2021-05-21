package service;

import model.Role;
import model.User;
import repository.UserRepository;

import java.util.Collections;

public class UserService {
    private final UserRepository userRepository;
//    TODO - implement password encoder
//    private final PasswordEncoder passwordEncoder;

//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb != null) {
            return false;
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(user.getPassword());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
