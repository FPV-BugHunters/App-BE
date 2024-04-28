package com.umb.tradingapp.security.controller;


import com.umb.tradingapp.security.repo.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @GetMapping("/api/authentication")// funkčne ale nepouživa sa
    public UserRolesDto getRoles(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {

        String token = auth.substring("Bearer".length()).trim();
        return authenticationService.authenticate(token);
    }

    @DeleteMapping("/api/authentication")// funguje, zatial nepouzivane
    public void logout(@RequestHeader(value = AUTHORIZATION_HEADER, required = true) Optional<String> authentication) {
        String token = authentication.get().substring("Bearer".length()).trim();
        authenticationService.tokenRemove(token);
    }

    @Operation(summary = "Get user's atributes by unique token", description = "Returns atributes: role, name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRolesDto.class),
                            examples = @ExampleObject(value = "{\"role\": \"[USER]\", \"name\": \"admin\"}"))),
            @ApiResponse(responseCode = "401", description = "Not found - wrong token")
    })
    @GetMapping("/api/user") //funkčne
    public void getUser(@Parameter(description = "User's authorization token (Bearer token), (swagger-ui, hore zamok na endpointe treba pouzit)",
            example = "3de1b5ce-c647-4043-8d89-8f8d31c0fe4f")
                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String auth,
                        HttpServletResponse response) throws JSONException {
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
