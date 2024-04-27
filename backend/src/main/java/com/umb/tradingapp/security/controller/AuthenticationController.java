package com.umb.tradingapp.security.controller;


import com.umb.tradingapp.security.repo.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.umb.tradingapp.security.dto.AccountCredentialsDTO;
import com.umb.tradingapp.security.dto.UserRolesDto;
import com.umb.tradingapp.security.service.AuthenticationService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@RestController
//@CrossOrigin("*")
public class AuthenticationController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login" ) // toto sa pouziva
    public void login( @RequestBody AccountCredentialsDTO  accountCredentials, HttpServletResponse response) {
        
        System.out.println("login " + accountCredentials.getUsername() + " " + accountCredentials.getPassword());
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

    @GetMapping("/api/user")
    public void getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, HttpServletResponse response) throws JSONException {
        String token = auth.substring("Bearer".length()).trim();

        UserRolesDto user = authenticationService.authenticate(token);
        String name = user.getUserName();
        String role = user.getRoles().toString();

        // Vytvorenie JSON objektu s menom a rolou používateľa
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("name", name);
        jsonResponse.put("role", role);

        // Nastavenie hlavičiek výstupnej odpovede na JSON typ
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Nastavenie telo odpovede s JSON objektom
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //authenticationService.
        // Nastavenie stavového kódu v odpovedi
        response.setStatus(HttpStatus.OK.value());


    }
}
