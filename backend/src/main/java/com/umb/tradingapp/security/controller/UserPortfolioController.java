package com.umb.tradingapp.security.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.umb.tradingapp.security.dto.PortfolioDTO;
import com.umb.tradingapp.security.dto.UserPortfolioDTO;
import com.umb.tradingapp.security.service.UserPortfolioService;
import com.umb.tradingapp.security.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserPortfolioController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private UserPortfolioService userPortfolioService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/user/user_portfolio")
    public Boolean createUserPortfolio(@RequestBody String name, HttpServletResponse response, 
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification);
        
        return userPortfolioService.createUserPortfolio(response, userId, name);
    }

    @GetMapping("/api/user/user_portfolio")
    public List<UserPortfolioDTO> listUserPortfolio(HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification); 

        return userPortfolioService.listUserPortfolio(response, userId);
    }

    @GetMapping("/api/user/user_portfolio/{userPortfolioId}")
    public Iterable<PortfolioDTO> listPortfolio(@PathVariable Long userPortfolioId, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification);

        if (!userPortfolioService.checkPortfolioExists(response, userId, userPortfolioId))
            return null;

        return userPortfolioService.listPortfolio(userPortfolioId);
    }

    //TODO pridat moznost pre zmenu nazvu portfolia
}