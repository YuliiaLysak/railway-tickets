package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.User;

public interface UserRepository {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    User save(User user);
}
