package com.umb.tradingapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String showRegistrationForm() {
        System.out.printf("!!!!");
        return "redirect:/register.html";
    }

}