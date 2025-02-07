package ru.kata.spring.boot_security.demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner dataLoader() {
        return args -> {
            userService.deleteAllUsers();
            roleService.deleteAllRoles();

            // Создаем роли
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleUser = new Role("ROLE_USER");

            if (roleService.getAllRoles().isEmpty()) {
                roleService.saveRole(roleAdmin);
                roleService.saveRole(roleUser);
                logger.info("Roles have been created successfully.");
            } else {
                logger.info("Roles already exist in the database.");
            }

            String encodedPasswordAdmin = passwordEncoder.encode("123");
            String encodedPasswordUser = passwordEncoder.encode("123");

            logger.debug("Encoded Admin Password: {}", encodedPasswordAdmin);
            logger.debug("Encoded User Password: {}", encodedPasswordUser);

            User admin = new User("admin", encodedPasswordAdmin, "John", "Doe", 35, "admin@example.com", Set.of(roleAdmin));
            User user = new User("user", encodedPasswordUser, "Jane", "Smith", 28, "user@example.com", Set.of(roleUser));

            if (userService.getAllUsers().isEmpty()) {
                userService.saveUser(admin);
                userService.saveUser(user);
                logger.info("Admin and User have been created successfully!");
            } else {
                logger.info("Users already exist in the database.");
            }
        };
    }
}
