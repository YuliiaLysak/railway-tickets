package repository;

import model.User;

public interface UserRepository {

    User findByEmail(String email);

    User save(User user);
}
