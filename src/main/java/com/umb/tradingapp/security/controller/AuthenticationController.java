package com.umb.tradingapp.security.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.umb.tradingapp.security.service.AuthenticationService;
import com.umb.tradingapp.security.service.UserRolesDto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
//@CrossOrigin("*")
public class AuthenticationController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/api/authentication")
    public void login(@RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentication,
                      HttpServletResponse response) {
        if (authentication.isEmpty()) { // uz nie sme prazdny
            response.setStatus(HttpStatus.FORBIDDEN.value());
            System.out.println("hehe \n");
            return;
        }
        System.out.println("haha \n");
        String[] credentials = credentialsDecode(authentication.get());
        String token = authenticationService.authenticate(credentials[0], credentials[1]);

        response.setStatus(HttpStatus.OK.value());
        response.addHeader(AUTHORIZATION_HEADER, "Bearer " + token);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody Map<String, String> registrationData) {
        // Tu môžete implementovať logiku na registráciu používateľa
        String username = registrationData.get("username");
        String password = registrationData.get("password");

        // Príklad overenia, že používateľské meno a heslo boli úspešne prijaté
        System.out.println("Received registration data:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // Tu by sa mal vytvoriť používateľ v databáze a vrátiť token


        // Namiesto toho tu bude len simulácia úspešnej registrácie
        String token = "your_generated_token_here";

        // Vytvorenie odpovede s hlavičkou Authorization obsahujúcou token a statusom 200 OK
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return new ResponseEntity<>("Registration successful", headers, HttpStatus.OK);
    }


    private static String[] credentialsDecode(String authorization) {
        System.out.println(authorization);
        //String base64Credentials = authorization.substring("Basic".length()).trim();
        String base64Credentials = authorization.substring("Bearer".length()).trim();

        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        return  credentials.split(":", 2);
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
