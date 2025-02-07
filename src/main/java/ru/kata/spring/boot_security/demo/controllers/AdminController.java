package ru.kata.spring.boot_security.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.CustomUserDetails;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (customUserDetails == null) {
            logger.error("Authenticated admin is null");
            return "redirect:/login";
        }

        User admin = customUserDetails.getUser();
        model.addAttribute("admin", admin);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin-dashboard";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, @RequestParam List<Long> roles, Model model) {
        try {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("errorMessage", "Username уже используется!");
                model.addAttribute("roles", roleService.getAllRoles());
                model.addAttribute("user", user);
                return "add-new-user";
            }

            user.setRoles(getUserRolesByIds(roles));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
            logger.info("User added successfully: {}", user.getUsername());
        } catch (Exception e) {
            logger.error("Error adding user: {}", e.getMessage(), e);
        }
        return "redirect:/admin";
    }


    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "/add-new-user";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user, @RequestParam List<Long> roles) {
        try {
            user.setRoles(getUserRolesByIds(roles));
            userService.updateUser(user.getId(), user);
            logger.info("User updated successfully: {}", user.getUsername());
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage(), e);
        }
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            logger.info("User deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: {}", id, e.getMessage(), e);
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit-user/{id}")
    public String showEditUserPage(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", roleService.getAllRoles());
            return "edit-user";
        } else {
            logger.error("User not found with ID {}", id);
            return "redirect:/admin";
        }
    }

    @PostMapping("/edit-user")
    public String editUser(@ModelAttribute User user, @RequestParam List<Long> roles, Model model) {
        try {
            Optional<User> existingUser = userService.findByUsername(user.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                model.addAttribute("errorMessage", "Username уже используется!");
                model.addAttribute("roles", roleService.getAllRoles());
                model.addAttribute("user", user);
                return "edit-user";
            }

            user.setRoles(getUserRolesByIds(roles));
            userService.updateUser(user.getId(), user);
            return "redirect:/admin";
        } catch (Exception e) {
            logger.error("Error editing user: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("user", user);
            return "edit-user";
        }
    }

    private Set<Role> getUserRolesByIds(List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        for (Long id : roleIds) {
            Optional<Role> role = roleService.getRoleById(id);
            role.ifPresent(roles::add);
        }
        return roles;
    }
}