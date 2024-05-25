package com.umb.tradingapp.security.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.umb.tradingapp.security.dto.*;
import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import org.springframework.web.bind.annotation.*;

import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.security.service.UserPortfolioService;
import com.umb.tradingapp.security.service.UserService;
import com.umb.tradingapp.service.CryptoService;

import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private UserService userService;

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private UserPortfolioService userPortfolioService;

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
    public Double balance(@Parameter(description = "User's authorization token (Bearer token)") 
        @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification, HttpServletResponse response) {
            if (!userService.checkTokenGiven(authentification, response)) 
                return null;
            if (!userService.checkTokenExists(authentification, response))
                return null;
        
            Long userId = userService.getUserId(authentification);
            return userService.balance(userId, response);
    }
    @PutMapping("/api/user/{newUserName}")
    public Boolean changeUserName(@Parameter(description = "User's authorization token (Bearer token)")
                          @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification,@PathVariable String newUserName, HttpServletResponse response) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        Long userId = userService.getUserId(authentification);

        return userService.renameUser(userId,newUserName,response);
    }

    @PutMapping("/api/user/changePassword")
    public Boolean changeUserPassword(@Parameter(description = "User's authorization token (Bearer token)")
                                  @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification,
                                      @RequestBody String newPassword,
                                      HttpServletResponse response) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        Long userId = userService.getUserId(authentification);
        System.out.println(newPassword);

        return userService.changePassword(userId,newPassword,response);
    }

    @PutMapping("/api/user/changeTelephoneNumber")
    public Boolean changeTelephoneNumber(@Parameter(description = "User's authorization token (Bearer token)")
                                      @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification,
                                      @RequestBody String newNumber,
                                      HttpServletResponse response) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        Long userId = userService.getUserId(authentification);
        System.out.println(newNumber);

        return userService.changeTelephoneNumber(userId,newNumber,response);
    }

    @GetMapping("/api/user/briefInfoUser")
    public UserBriefInfoDTO listBriefInfoUser(HttpServletResponse response,
                                                     @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;
        Long userId = userService.getUserId(authentification);

        //List<UserBriefInfoDTO> listDto = new ArrayList<>();
        //listDto.add(new UserBriefInfoDTO());

        return userService.giveBriefInfoUser(userId,response);
    }


    @PostMapping("/api/user/balance/compare")
    public Boolean enoughBalance(@RequestBody Double dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;
        if (!userService.checkDtoExists(dto, response))
            return null;

        Long userId = userService.getUserId(authentification);
        return userService.checkEnoughBalance(dto, response, userId);
    }

    @PostMapping("/api/user/balance/add")
    public Boolean addBalance(@RequestBody Double dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;
        if (!userService.checkDtoExists(dto, response))
            return null;
        
        Long userId = userService.getUserId(authentification);
        return userService.addBalance(dto, response, userId);
    }

    @PostMapping("/api/user/balance/remove")
    public Boolean removeBalance(@RequestBody Double dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        Long userId = userService.getUserId(authentification);

        if (!userService.checkDtoExists(dto, response))
            return null;
        if (!userService.checkEnoughBalance(dto, response, userId))
            return false;
        return userService.removeBalance(dto, response, userId);
    }

    @PostMapping("/api/user/transaction/buy")
    public Boolean buyTransaction(@RequestBody BuyTransactionDTO dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;
        if (!userService.checkDtoExists(dto, response))
            return null;

        Long userId = userService.getUserId(authentification);
        Double totalPrice = cryptoService.getCryptoPrice(dto.getCryptoId()) * dto.getAmount();

        if (!userService.checkEnoughBalance(totalPrice, response, userId))
            return false;

        if (!cryptoService.checkCryptoExists(dto.getCryptoId(), response))
            return false;

        if (!userPortfolioService.checkPortfolioExists(response, userId, dto.getUserPortfolioId()))
            return false;

        userPortfolioService.buyCrypto(response, userId, dto);
        userService.removeBalance(totalPrice, response, userId);
        return true; 
    }

    @PostMapping("/api/user/transaction/sell")
    public Boolean sellTransaction(BuyTransactionDTO dto, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;
        if (!userService.checkDtoExists(dto, response))
            return null;
        Long userId = userService.getUserId(authentification);
        Double totalPrice;

        if (!cryptoService.checkCryptoExists(dto.getCryptoId(), response))
            return false;

        if (!userPortfolioService.checkPortfolioExists(response, userId, dto.getUserPortfolioId()))
            return false;

        if (!userPortfolioService.checkCryptoOwned(dto, response, userId))
            return false;

        totalPrice = userPortfolioService.sellCrypto(dto, response, userId);
        userService.addBalance(totalPrice, response, userId);

        return true;
    }

    @GetMapping("/api/user/transactions")
    public Iterable<TransactionDTO> listAllTransactions(HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        List<TransactionDTO> listDto = new ArrayList<>();
        listDto.add(new TransactionDTO(1l, 13.4f, 10., 134., 3145l, 1l, "Bitcoin", "BTC", "Sell"));
        listDto.add(new TransactionDTO(2l, 33.4f, 1.68, 424., 3145l, 1l, "Bitcoin", "BTC", "Sell"));
        listDto.add(new TransactionDTO(3l, 130.4f, 100., 134., 3145l, 1l, "Bitcoin", "BTC", "Buy"));
        listDto.add(new TransactionDTO(4l, 67.4f, 300., 134., 3145l, 1l, "Bitcoin", "BTC", "Sell"));
        listDto.add(new TransactionDTO(5l, 1.4f, 10.64, 134., 3145l, 1l, "Bitcoin", "BTC", "Buy"));

        //TODO implementovat
        return listDto;
    }

    @GetMapping("/api/user/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        //TODO implementovat
        return new TransactionDTO(2l, 33.4f, 1.68, 424., 3145l, 1l, "Bitcoin", "BTC", "Sell");
    }

    @GetMapping("/api/user/portfolio")
    public Iterable<PortfolioDTO> listPortfolio(HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        List<PortfolioDTO> listDto = new ArrayList<>();
        listDto.add(new PortfolioDTO(1l, 1, "woeur", "LBK", 234., 12., 32432., 20948., 2340., 13.54f, -12f, 13f, 4f));
        listDto.add(new PortfolioDTO(2l, 2, "woeur", "LBK", 234., 12., 32432., 20948., 2340., 13.54f, -12f, 13f, 4f));
        listDto.add(new PortfolioDTO(3l, 3, "woeur", "LBK", 234., 12., 32432., 20948., 2340., 13.54f, -12f, 13f, 4f));
        listDto.add(new PortfolioDTO(4l, 4, "woeur", "LBK", 234., 12., 32432., 20948., 2340., 13.54f, -12f, 13f, 4f));
        listDto.add(new PortfolioDTO(5l, 5, "woeur", "LBK", 234., 12., 32432., 20948., 2340., 13.54f, -12f, 13f, 4f));
        //TODO implementovat

        return listDto;
    }

    @GetMapping("/api/user/portfolio/{id}")
    public PortfolioDTO getCryptoFromPortfolio(@PathVariable Integer id, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        //TODO implementovat
        return new PortfolioDTO(2l, 2, "woeur", "LBK", 234., 12., 32432., 20948., 2340., 13.54f, -12f, 13f, 4f);
    }

    @GetMapping("/api/user/watchlist")
    public Iterable<CryptoPriceDTO> listWatchlist(HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        List<CryptoPriceDTO> listDto = new ArrayList<>();
        listDto.add(new CryptoPriceDTO(1l, "LBK", "woeur", 2, 12., 32432., 20948., 2340., 13.54f, -12f, 13f));
        listDto.add(new CryptoPriceDTO(2l, "LBK", "woeur", 3, 12., 32432., 20948., 2340., 13.54f, -12f, 13f));
        listDto.add(new CryptoPriceDTO(3l, "LBK", "woeur", 4, 12., 32432., 20948., 2340., 13.54f, -12f, 13f));
        listDto.add(new CryptoPriceDTO(4l, "LBK", "woeur", 5, 12., 32432., 20948., 2340., 13.54f, -12f, 13f));
        listDto.add(new CryptoPriceDTO(5l, "LBK", "woeur", 6, 12., 32432., 20948., 2340., 13.54f, -12f, 13f));
        //TODO implementovat

        return listDto;
    }

    @GetMapping("/api/user/watchlist/{id}")
    public CryptoPriceDTO getCryptoFromWatchlist(@PathVariable Integer id, HttpServletResponse response,
    @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkTokenGiven(authentification, response))
            return null;
        if (!userService.checkTokenExists(authentification, response))
            return null;

        return new CryptoPriceDTO(5l, "LBK", "woeur", 6, 12., 32432., 20948., 2340., 13.54f, -12f, 13f);
        //TODO implementovat
    }



    
}

