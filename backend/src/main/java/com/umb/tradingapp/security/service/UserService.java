package com.umb.tradingapp.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.TokenRepository;
import com.umb.tradingapp.security.repo.UserPortfolioRepository;
import com.umb.tradingapp.security.repo.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserPortfolioRepository userPortfolioRepo;

    private final String BALANCE = "Balance";
    
    public Double balance(Long userId, HttpServletResponse response) {
        UserEntity entity = userRepository.getReferenceById(userId);
        return entity.getBalance();
    }

    public Boolean addBalance(Double dto, HttpServletResponse response, Long userId) {
        UserEntity entity = userRepository.getReferenceById(userId);
        entity.setBalance(entity.getBalance() + dto);
        userRepository.save(entity);
        return true;
    }

    public Boolean removeBalance(Double dto, HttpServletResponse response, Long userId) {
        UserEntity entity = userRepository.getReferenceById(userId);
        entity.setBalance(entity.getBalance() - dto);
        userRepository.save(entity);
        return true;
    }

    public Boolean checkEnoughBalance(Double dto, HttpServletResponse response, Long userId) {
        UserEntity entity = userRepository.getReferenceById(userId);
        if (entity.getBalance() - dto < 0) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            response.addHeader(BALANCE, "Not enough balance");
            return false;
        }
        response.addHeader(BALANCE, "Enough balance");
        return true;
    }

    public Boolean checkTokenGiven(Optional<String> authentification, HttpServletResponse response) {
        if (authentification.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "token not sent"); 
            return false;
        }
        return true;
    }

    public Boolean checkTokenExists(Optional<String> authentification, HttpServletResponse response) {
        String token = authentification.get().substring("Bearer".length()).trim();
        Optional<TokenEntity> te;
        te = tokenRepository.findByToken(token);
        if (te.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "user does not exists");
            return false; 
        }
        return true;
    }

    public <T> Boolean checkDtoExists(T dto, HttpServletResponse response) {
        if (dto.equals(null)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("Error", "dto not found");
            return false;
        }
        return true;
    }

    public Long getUserId(Optional<String> authentification) {
        String token = authentification.get().substring("Bearer".length()).trim();
        Optional<TokenEntity> te = tokenRepository.findByToken(token);
        return te.get().getUser().getId();
    }
}
