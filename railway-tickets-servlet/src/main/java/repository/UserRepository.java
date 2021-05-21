package repository;

import model.User;

public interface UserRepository {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    User save(User user);
}
