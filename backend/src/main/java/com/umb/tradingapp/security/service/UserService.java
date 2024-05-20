package com.umb.tradingapp.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.security.dto.UserBalanceDTO;
import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.TokenRepository;
import com.umb.tradingapp.security.repo.UserRepository;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;
    
    private final String AUTHORIZATION_HEADER = "Authorization";

    public Integer balance(Optional<String> authentification, HttpServletResponse response) {
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
        return entity.getBalance();
        
    }

    public ResponseEntity<String> addBalance(Integer dto, HttpServletResponse response, Optional<String> authentification) {
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

        if (dto.equals(null)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("Error", "adding balance not successful, balance null");
            return new ResponseEntity<>("Balance not added", null, HttpStatus.NOT_FOUND);
        }
        
        Long userId = te.get().getUser().getId();
        UserEntity entity = userRepository.getReferenceById(userId);
        entity.setBalance(entity.getBalance() + dto);
        userRepository.save(entity);
        return new ResponseEntity<>("Balance successfully added", null, HttpStatus.OK);
    }

    public ResponseEntity<String> removeBalance(Integer dto, HttpServletResponse response, Optional<String> authentification) {
        if (authentification.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error1", "removing balance not successful, token not sent");
            return new ResponseEntity<>("Balance not added", null, HttpStatus.UNAUTHORIZED);
        }
        String token = authentification.get().substring("Bearer".length()).trim();

        Optional<TokenEntity> te;
        te = tokenRepository.findByToken(token);
        if (te.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "removing balance not successful, user does not exists");
            return new ResponseEntity<>("Balance not removed", null, HttpStatus.UNAUTHORIZED);
        }

        if (dto.equals(null)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("Error", "removing balance not successful, balance null");
            return new ResponseEntity<>("Balance not removed", null, HttpStatus.NOT_FOUND);
        }
        
        Long userId = te.get().getUser().getId();
        UserEntity entity = userRepository.getReferenceById(userId);
        entity.setBalance(entity.getBalance() - dto);
        userRepository.save(entity);
        return new ResponseEntity<>("Balance successfully removed", null, HttpStatus.OK);
    }

    public Boolean enoughBalance(Integer dto, HttpServletResponse response, Optional<String> authentification) {
        if (authentification.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error1", "comparing balance not successful, token not sent");
            return null;
        }
        String token = authentification.get().substring("Bearer".length()).trim();

        Optional<TokenEntity> te;
        te = tokenRepository.findByToken(token);
        if (te.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "comparing balance not successful, user does not exists");
            return null; 
        }

        if (dto.equals(null)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("Error", "comparing balance not successful, balance null");
            return null;
        }
        
        Long userId = te.get().getUser().getId();
        UserEntity entity = userRepository.getReferenceById(userId);
        if (entity.getBalance() - dto < 0)
            return false;
        return true;
    }
}
