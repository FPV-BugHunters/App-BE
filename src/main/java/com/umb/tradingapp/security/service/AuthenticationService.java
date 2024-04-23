package com.umb.tradingapp.security.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.umb.tradingapp.security.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private static final int TOKEN_VALIDITY_IN_MINUTES = 15;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    // https://bcrypt-generator.com/, round 1


    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public String authenticate(String username, String password) {
        System.out.printf("username: %s, password: %s\n", username, password);
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        System.out.println(optionalUser);

        if (optionalUser.isEmpty()) {
            System.out.printf("empty");
            throw new AuthenticationCredentialsNotFoundException("Username and/or password do not match!");
        }

        if (!passwordEncoder.matches(password, optionalUser.get().getPasswordHash())) {
            System.out.println("wrong password: " + optionalUser.get().getPasswordHash() +"password: "+password);
            throw new AuthenticationCredentialsNotFoundException("Username and/or password do not match!");
        }
        System.out.println("vytvaram token");
        TokenEntity token = new TokenEntity();
        String randomString = UUID.randomUUID().toString();
        token.setToken(randomString);
        token.setUser(optionalUser.get());
        token.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(token);
        System.out.println("token id: " + token.getToken());

        return token.getToken();
    }

    @Transactional
    public UserRolesDto authenticate(String token) {
        System.out.println("toto je token "+ token);
        // Aladdin
        //hash
        Optional<TokenEntity> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            System.out.printf("je to empty");
            throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
        }

        validateTokenExpiration(optionalToken.get());

        Set<RoleEntity> roles = optionalToken.get().getUser().getRoles();
        Set<String> roleNames = roles.stream()
                                     .map( entry -> entry.getRoleName())
                                     .collect(Collectors.toSet());

        return new UserRolesDto(optionalToken.get().getUser().getUsername(), roleNames);
    }

    private void validateTokenExpiration(TokenEntity token) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiration = token.getCreatedAt().plus(TOKEN_VALIDITY_IN_MINUTES, ChronoUnit.MINUTES);

        if (now.isAfter(tokenExpiration)) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
        }
    }

    @Transactional
    public void tokenRemove(String token) {
        tokenRepository.deleteByToken(token);
    }

}
