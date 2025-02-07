package ru.kata.spring.boot_security.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordChecker {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";
        String encodedPasswordFromDB = "$2a$10$RpjMApwkrA7CrO6nmPlJ5OvK4GWy4/Y7BH6kP3qeCWpFzw7CTPvum";

        boolean matches = passwordEncoder.matches(rawPassword, encodedPasswordFromDB);
        System.out.println("Password matches: " + matches);
    }
}





