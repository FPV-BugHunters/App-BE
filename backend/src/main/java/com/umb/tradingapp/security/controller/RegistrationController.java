package com.umb.tradingapp.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import com.umb.tradingapp.security.dto.RegisterUserDTO;
import com.umb.tradingapp.security.repo.UserRepository;
import com.umb.tradingapp.security.service.RegistrationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "POST user's credentials", description = "Posts user credentials")
    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody RegisterUserDTO registerUserDTO) {

        if (userRepository.findByUsername(registerUserDTO.getUsername()).isEmpty() == false) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        registrationService.saveUser(registerUserDTO);

        return new ResponseEntity<>("Registration successful", null, HttpStatus.OK);
    }
}