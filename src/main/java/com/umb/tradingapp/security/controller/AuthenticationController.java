package com.umb.tradingapp.security.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.umb.tradingapp.security.dto.UserRolesDto;
import com.umb.tradingapp.security.service.AuthenticationService;

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
