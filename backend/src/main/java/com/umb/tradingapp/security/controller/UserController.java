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
    public UserBalanceDTO balance(@Parameter(description = "User's authorization token (Bearer token)") 
        @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification, HttpServletResponse response) {
        if (authentification.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error1", "getting balance not successful, token not sent");
            return null;
        }
        String token = authentification.get().substring("Bearer".length()).trim();

        Optional<TokenEntity> te;
        te = tokenRepository.findByToken(token);
        if (te.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "getting balance not successful, user does not exists");
            return null;
        }
        
        Long userId = te.get().getUser().getId();
        UserEntity entity = userRepository.getReferenceById(userId);
        return new UserBalanceDTO(entity.getBalance());
    }

    @PostMapping("/api/user/balance")
    public ResponseEntity<String> balance(@RequestBody UserBalanceDTO dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (authentification.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error1", "adding balance not successful, token not sent");
            return new ResponseEntity<>("Balance not added", null, HttpStatus.UNAUTHORIZED);
        }
        String token = authentification.get().substring("Bearer".length()).trim();

        Optional<TokenEntity> te;
        te = tokenRepository.findByToken(token);
        if (te.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "adding balance not successful, user does not exists");
            return new ResponseEntity<>("Balance not added", null, HttpStatus.UNAUTHORIZED);
        }

        System.out.println();

        if (dto.getBalance().equals(null)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("Error", "adding balance not successful, balance null");
            return new ResponseEntity<>("Balance not added", null, HttpStatus.NOT_FOUND);
        }
        
        Long userId = te.get().getUser().getId();
        UserEntity entity = userRepository.getReferenceById(userId);
        entity.setBalance(entity.getBalance() + dto.getBalance());
        userRepository.save(entity);
        return new ResponseEntity<>("Balance successfully added", null, HttpStatus.OK);
    }
}
