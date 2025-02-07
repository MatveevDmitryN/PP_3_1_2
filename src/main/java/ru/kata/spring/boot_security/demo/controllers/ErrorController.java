package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @RequestMapping("/custom-error")
    public String handleError() {
        return "error";
    }
}
