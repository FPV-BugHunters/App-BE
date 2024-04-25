package com.umb.tradingapp.security.controller;
import com.umb.tradingapp.security.service.AuthenticationService;
import com.umb.tradingapp.security.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegistrationController {

    @GetMapping("/registration")
    public String showRegistrationForm() {
        System.out.printf("!!!!");
        return "redirect:/register.html";
    }

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody Map<String, String> registrationData) {
        // Tu môžete implementovať logiku na registráciu používateľa
        String username = registrationData.get("username");
        String password = registrationData.get("password");

        // Príklad overenia, že používateľské meno a heslo boli úspešne prijaté
        System.out.println("Received registration data:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        String udaje [] = new String[2] ;
        udaje[0] = username;
        udaje[1] = password;
        // Tu by sa mal vytvoriť používateľ v databáze a vrátiť token

        registrationService.saveUser(udaje);
        // Namiesto toho tu bude len simulácia úspešnej registrácie
        String token = "your_generated_token_here";

        // Vytvorenie odpovede s hlavičkou Authorization obsahujúcou token a statusom 200 OK
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + token);
        return new ResponseEntity<>("Registration successful", null, HttpStatus.OK);
    }

}