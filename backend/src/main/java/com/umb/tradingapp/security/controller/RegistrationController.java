package com.umb.tradingapp.security.controller;
import com.umb.tradingapp.security.repo.UserRepository;
import com.umb.tradingapp.security.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody Map<String, String> registrationData) {
        // Tu môžete implementovať logiku na registráciu používateľa
        String username = registrationData.get("username");
        String password = registrationData.get("password");

        if (userRepository.findByUsername(username).isEmpty() == false){
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        // Príklad overenia, že používateľské meno a heslo boli úspešne prijaté
        System.out.println("Received registration data:");

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        
        String udaje [] = new String[2] ;

        udaje[0] = username;
        udaje[1] = password;

        registrationService.saveUser(udaje);


        return new ResponseEntity<>("Registration successful", null, HttpStatus.OK);
    }
}