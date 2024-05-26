package com.umb.tradingapp.security.controller;


import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.TokenRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.umb.tradingapp.security.dto.LoginUserDTO;
import com.umb.tradingapp.security.dto.UserInfoDto;
import com.umb.tradingapp.security.dto.UserRolesDto;
import com.umb.tradingapp.security.service.AuthenticationService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

@RestController
//@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private TokenRepository tokenRepository;

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "POST user's credentials", description = "Posts user credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    headers = {
                            @Header(name = "authorization", description = "Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", schema = @Schema(type = "string"))
                    }
            ),
            @ApiResponse(responseCode = "401", description = "Error: response status is 401",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Response body: Username and/or password do not match!")
                    )
            )
    })
    @PostMapping("/login" ) // toto sa pouziva
    public void login( @RequestBody LoginUserDTO  accountCredentials, HttpServletResponse response) {
        
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


    })
    

    
    @DeleteMapping("/api/logout") // funguje
    public void logout(@Parameter(description = "User's authorization token (Bearer token), (swagger-ui, hore zamok na endpointe treba pouzit)")
                           @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentication, HttpServletResponse response) {

        if (authentication.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error1", "logout not successful, token not sent");
            return;
        }
        String token = authentication.get().substring("Bearer".length()).trim();

        System.out.println("Idem sa pokusit removnut pouzivatela: ");

        Optional<TokenEntity> te;
        te =tokenRepository.findByToken(token);
        if (te.isEmpty()){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "logout not successful, user does not exists" );

        }
        else{
            authenticationService.tokenRemove(token);
            response.setStatus(HttpStatus.OK.value());
            response.addHeader(AUTHORIZATION_HEADER, "logout successful" );
        }

    }

    @Operation(summary = "GET user's atributes by unique token", description = "Returns atributes: role, name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRolesDto.class),
                            examples = @ExampleObject(value = "{\"role\": \"[USER]\", \"name\": \"admin\"}"))),
            @ApiResponse(responseCode = "401", description = "Not found - wrong token")
    })
    
    
    @GetMapping("/api/user") //funkčne
    public UserInfoDto getUser( HttpServletRequest request) throws JSONException {
        UserEntity userEntity = (UserEntity) request.getAttribute("user");
        
        UserInfoDto user = new UserInfoDto();
        user.setUsername(userEntity.getUsername());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setEmail(userEntity.getEmail());
        user.setPhoneNumber(userEntity.getPhoneNumber());
        
        Set<String> roles = new HashSet<>();
        userEntity.getRoles().forEach(role -> roles.add(role.getRoleName()));
        user.setRoles(roles);

        return user;

    }
}
