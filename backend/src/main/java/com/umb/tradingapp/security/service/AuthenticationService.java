package com.umb.tradingapp.security.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umb.tradingapp.security.dto.UserRolesDto;
import com.umb.tradingapp.security.entity.RoleEntity;
import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.TokenRepository;
import com.umb.tradingapp.security.repo.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
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
        
        TokenEntity token = new TokenEntity();
        String randomString = UUID.randomUUID().toString();
        token.setToken(randomString);
        token.setUser(optionalUser.get());
        token.setCreatedAt(new Date());
        tokenRepository.save(token);
        System.out.println("token id: " + token.getToken());

        return token.getToken();
    }

    @Transactional
    public UserRolesDto authenticate(String token) {
        Optional<TokenEntity> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
        }


        Set<RoleEntity> roles = optionalToken.get().getUser().getRoles();
        Set<String> roleNames = roles.stream()
                                     .map( entry -> entry.getRoleName())
                                     .collect(Collectors.toSet());

        return new UserRolesDto(optionalToken.get().getUser().getUsername(), roleNames);
    }

    @Transactional
    public UserEntity getUser(String token) {
        Optional<TokenEntity> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
        }

        UserEntity user = optionalToken.get().getUser();
        return user;
    }



    @Transactional
    public void tokenRemove(String token) {
        System.out.println("removujem token");
        tokenRepository.deleteByToken(token);
    }

}
