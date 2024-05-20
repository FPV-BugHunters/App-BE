package com.umb.tradingapp.security.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.umb.tradingapp.security.dto.UserBalanceDTO;
import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.TokenRepository;
import com.umb.tradingapp.security.repo.UserRepository;
import com.umb.tradingapp.security.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.headers.Header;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /*
    @Operation(summary = "DELETE user(token) - logout", description = "Logs out the user by removing the authentication token")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Successfully logged off",
                        headers = {
                        @Header(name = "authorization", description = "logout successful ", schema = @Schema(type = "string"))
                }
                ),
                @ApiResponse(responseCode = "401", description = "Error: response status is 401",
                        headers = {
                                @Header(name = "error", description = "logout not successful, user does not exists", schema = @Schema(type = "string")),
                                @Header(name = "error1", description = "logout not successful, token not sent", schema = @Schema(type = "string"))

                        }
                )


    })*/
    @GetMapping("/api/user/balance")
    public Integer balance(@Parameter(description = "User's authorization token (Bearer token)") 
        @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification, HttpServletResponse response) {
            return userService.balance(authentification, response);
    }

    @PostMapping("/api/user/balance/compare")
    public Boolean enoughBalance(@RequestBody Integer dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        return userService.enoughBalance(dto, response, authentification);
    }

    @PostMapping("/api/user/balance/add")
    public ResponseEntity<String> addBalance(@RequestBody Integer dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        return userService.addBalance(dto, response, authentification);
    }

    @PostMapping("/api/user/balance/remove")
    public ResponseEntity<String> removeBalance(@RequestBody Integer dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        return userService.removeBalance(dto, response, authentification);
    }

}
