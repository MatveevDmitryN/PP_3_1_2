package ru.kata.spring.boot_security.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.security.CustomUserDetails;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            logger.error("Authenticated user is null");
            return "redirect:/login";
        }

        logger.info("Authenticated user: {}", userDetails.getUsername());

        model.addAttribute("user", userDetails);
        model.addAttribute("roles", userDetails.getAuthorities());

        return "user-dashboard";
    }
}
