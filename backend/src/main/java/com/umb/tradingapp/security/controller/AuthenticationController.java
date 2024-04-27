package com.umb.tradingapp.security.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.umb.tradingapp.security.dto.AccountCredentialsDTO;
import com.umb.tradingapp.security.dto.UserRolesDto;
import com.umb.tradingapp.security.service.AuthenticationService;

import java.util.Optional;

@RestController
//@CrossOrigin("*")
public class AuthenticationController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login" )
    public void login( @RequestBody AccountCredentialsDTO  accountCredentials, HttpServletResponse response) {
        
        System.out.println("login" + accountCredentials.getUsername() + " " + accountCredentials.getPassword()); 
        String token = authenticationService.authenticate(accountCredentials.getUsername(), accountCredentials.getPassword());

        response.setStatus(HttpStatus.OK.value());
        response.addHeader(AUTHORIZATION_HEADER, "Bearer " + token);
    }


    @GetMapping("/api/authentication")
    public UserRolesDto getRoles(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String token = auth.substring("Bearer".length()).trim();
        return authenticationService.authenticate(token);
    }

    @DeleteMapping("/api/authentication")
    public void logout(@RequestHeader(value = AUTHORIZATION_HEADER, required = true) Optional<String> authentication) {
        String token = authentication.get().substring("Bearer".length()).trim();
        System.out.println("removujem token");
        authenticationService.tokenRemove(token);
    }

}
