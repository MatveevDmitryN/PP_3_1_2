package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void saveUser(User user);
    void deleteUser(Long id);
    void updateUser(Long id, User user);
    void deleteAllUsers();
    Optional<User> findByUsername(String username);

}
