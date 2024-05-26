package com.umb.tradingapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.umb.tradingapp.dto.BuyTransactionDTO;
import com.umb.tradingapp.dto.CryptoDTO;
import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.dto.PortfolioDTO;
import com.umb.tradingapp.dto.TransactionDTO;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.service.CryptoService;
import com.umb.tradingapp.service.UserPortfolioService;
import com.umb.tradingapp.service.UserService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
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
     * @Operation(summary = "DELETE user(token) - logout", description =
     * "Logs out the user by removing the authentication token")
     * 
     * @ApiResponses(value = {
     * 
     * @ApiResponse(responseCode = "200", description = "Successfully logged off",
     * headers = {
     * 
     * @Header(name = "authorization", description = "logout successful ", schema
     * = @Schema(type = "string"))
     * }
     * ),
     * 
     * @ApiResponse(responseCode = "401", description =
     * "Error: response status is 401",
     * headers = {
     * 
     * @Header(name = "error", description =
     * "logout not successful, user does not exists", schema = @Schema(type =
     * "string")),
     * 
     * @Header(name = "error1", description =
     * "logout not successful, token not sent", schema = @Schema(type = "string"))
     * 
     * }
     * )
     * 
     * 
     * })
     */
    @GetMapping("/api/user/balance")
    public Double balance(
            @Parameter(description = "User's authorization token (Bearer token)") @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification,
            HttpServletResponse response) {
        Long userId = userService.getUserId(authentification);
        return userService.balance(userId, response);
    }

    @PostMapping("/api/user/balance/compare")
    public Boolean enoughBalance(@RequestBody Double price, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkDtoExists(price, response))
            return null;

        Long userId = userService.getUserId(authentification);
        return userService.checkEnoughBalance(price, response, userId);
    }

    @PostMapping("/api/user/balance/add")
    public Boolean addBalance(@RequestBody Double price, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!userService.checkDtoExists(price, response))
            return null;

        Long userId = userService.getUserId(authentification);
        return userService.addBalance(price, response, userId);
    }

    @PostMapping("/api/user/balance/remove")
    public Boolean removeBalance(@RequestBody Double price, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification);

        if (!userService.checkDtoExists(price, response))
            return null;
        if (!userService.checkEnoughBalance(price, response, userId))
            return false;
        return userService.removeBalance(price, response, userId);
    }

    @PostMapping("/api/user/transaction/buy")
    public Boolean buyTransaction(@RequestBody BuyTransactionDTO dto, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
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

    @GetMapping("/api/user/user_transactions")
    public List<TransactionDTO> listAllTransactions(HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification);

        return userService.getUserTransactions(userId);
    }

    @GetMapping("/api/user/user_transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification);

        return userService.getUserTransactionById(userId, id);
    }

    @GetMapping("/api/user/portfolio/{id}")
    public PortfolioDTO getCryptoFromPortfolio(@PathVariable Integer id, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        // TODO implementovat
        return new PortfolioDTO(2l, 2, "woeur", "LBK", 234., 12., 32432., 20948., 2340., 13.54f, -12f, 13f, 4f);
    }

    @GetMapping("/api/user/watchlist")
    public List<CryptoPriceDTO> listWatchlist(HttpServletResponse response, HttpServletRequest request,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {

        UserEntity userEntity = (UserEntity) request.getAttribute("user");
        return userService.getUserWatchlist(userEntity.getId());
    }
    

    @GetMapping("/api/user/watchlist/not-in-watchlist")
    public List<CryptoDTO> getCryptoNotInWatchlist(HttpServletRequest request) {
        System.out.println("/api/user/watchlist/not-in-watchlist/");
        
        UserEntity user = (UserEntity) request.getAttribute("user");
        return userService.getCryptoNotInWatchlist(user.getId());
    }

        

    @GetMapping("/api/user/watchlist/{cryptoId}")
    public CryptoPriceDTO getCryptoFromWatchlist(@PathVariable Long cryptoId, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification);

        return userService.getUserWatchlistItem(cryptoId, userId);
    }

    @PostMapping("/api/user/watchlist")
    public Boolean addToWatchlist(@RequestBody Long cryptoId, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!cryptoService.checkCryptoExists(cryptoId, response))
            return null;

        Long userId = userService.getUserId(authentification);

        // TODO pridat response header alebo responseBody
        if (userService.inWatchlist(cryptoId, userId))
            return false;

        return userService.addToWatchlist(cryptoId, userId);
    }

    @DeleteMapping("/api/user/watchlist")
    public Boolean removeFromWatchlist(@RequestBody Long cryptoId, HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        if (!cryptoService.checkCryptoExists(cryptoId, response))
            return null;

        Long userId = userService.getUserId(authentification);

        // TODO pridat response header alebo responseBody
        if (!userService.inWatchlist(cryptoId, userId))
            return false;

        return userService.removeFromWatchlist(cryptoId, userId);
    }

}