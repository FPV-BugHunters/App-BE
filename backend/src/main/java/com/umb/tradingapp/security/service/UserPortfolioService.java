package com.umb.tradingapp.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.security.dto.UserPortfolioDTO;
import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.entity.UserPortfolioEntity;
import com.umb.tradingapp.security.repo.TokenRepository;
import com.umb.tradingapp.security.repo.UserPortfolioRepository;
import com.umb.tradingapp.security.repo.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserPortfolioService {

    @Autowired
    private UserPortfolioRepository userPortfolioRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TokenRepository tokenRepo;

    public Boolean createUserPortfolio(HttpServletResponse response, Optional<String> authentification, String name) {
        String token = authentification.get().substring("Bearer".length()).trim();
        Optional<TokenEntity> te = tokenRepo.findByToken(token);
        Long userId = te.get().getUser().getId();
        UserEntity user = userRepo.getReferenceById(userId);
        UserPortfolioEntity portfolio = new UserPortfolioEntity();

        portfolio.setUser(user);
        portfolio.setName(name);
        portfolio.setNumberOfItems(0);
        portfolio.setTotalBalance(0.);
        userPortfolioRepo.save(portfolio);        
        return true;
    }
    
    public List<UserPortfolioDTO> listUserPortfolio(HttpServletResponse response, Optional<String> authentification) {
        String token = authentification.get().substring("Bearer".length()).trim();
        Optional<TokenEntity> te = tokenRepo.findByToken(token);
        Long userId = te.get().getUser().getId();
        List<UserPortfolioEntity> list = userPortfolioRepo.findByUserId(userId);
        List<UserPortfolioDTO> dtoList = new ArrayList<>();

        for (UserPortfolioEntity e : list) {
            UserPortfolioDTO dto = new UserPortfolioDTO(
                e.getPortfolioId(),
                e.getNumberOfItems(),
                e.getTotalBalance(),
                e.getName()
            );
            dtoList.add(dto);
        }

        return dtoList;
    }
}
