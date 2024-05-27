package com.umb.tradingapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.umb.tradingapp.dto.BalanceHistoryDTO;
import com.umb.tradingapp.dto.BuyTransactionDTO;
import com.umb.tradingapp.dto.CryptoDTO;
import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.dto.PortfolioDTO;
import com.umb.tradingapp.dto.PortfolioValueHistoryDTO;
import com.umb.tradingapp.dto.TransactionDTO;
import com.umb.tradingapp.entity.BalanceHistoryEntity;
import com.umb.tradingapp.repo.CryptoQuoteRepository;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.service.BalanceHistoryService;
import com.umb.tradingapp.service.CryptoService;
import com.umb.tradingapp.service.PortfolioValueHistoryService;
import com.umb.tradingapp.service.UserPortfolioService;
import com.umb.tradingapp.service.UserService;

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

    @Autowired
    private CryptoQuoteRepository cryptoQuoteRepo;

    @Autowired
    private BalanceHistoryService balanceHistory;

    @Autowired
    private PortfolioValueHistoryService portfolioValueHistory;

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
    public Double balance(HttpServletRequest request, HttpServletResponse response) {

        UserEntity userEntity = (UserEntity) request.getAttribute("user");
        return userService.balance(userEntity.getId(), response);
    }

    @PostMapping("/api/user/balance/compare")
    public Boolean enoughBalance(@RequestBody Double price, HttpServletRequest request , HttpServletResponse response) {

        UserEntity userEntity = (UserEntity) request.getAttribute("user");
        return userService.checkEnoughBalance(price, userEntity.getId(), response);
    }

    @PostMapping("/api/user/balance/add")
    public Boolean addBalance(@RequestBody Double price, HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = (UserEntity) request.getAttribute("user");
        return userService.addBalance(price, userEntity.getId(), response);
    }

    @PostMapping("/api/user/balance/remove")
    public Boolean removeBalance(@RequestBody Double price, HttpServletResponse response, HttpServletRequest request) {
        UserEntity userEntity = (UserEntity) request.getAttribute("user");

        if (!userService.checkEnoughBalance(price, userEntity.getId(), response))
            return false;
        return userService.removeBalance(price, response, userEntity.getId());
    }

    @PostMapping("/api/user/check-crypto-exists")
    public Boolean checkCryptoExists(@RequestBody Long cryptoId, HttpServletResponse response) {
        return cryptoService.checkCryptoExists(cryptoId, response);
    }


    @PostMapping("/api/user/transaction/buy")
    public Boolean buyTransaction(@RequestBody BuyTransactionDTO dto, HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = (UserEntity) request.getAttribute("user");
        //
        Double totalPrice = cryptoQuoteRepo.findByCryptoIdOrderByLastUpdatedDesc(dto.getCryptoId()).get(0).getPrice() * dto.getAmount();

        if (!userService.checkEnoughBalance(totalPrice, userEntity.getId(), response))
            return false;

        if (!cryptoService.checkCryptoExists(dto.getCryptoId(), response))
            return false;

        if (!userPortfolioService.checkPortfolioExists(response, userEntity.getId(), dto.getUserPortfolioId()))
            return false;

        userPortfolioService.buyCrypto(response, userEntity.getId(), dto);
        userService.removeBalance(totalPrice, response, userEntity.getId());
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
        userService.addBalance(totalPrice, userId, response);

        return true;
    }

    @GetMapping("/api/user/user-transactions")
    public List<TransactionDTO> listAllTransactions(HttpServletResponse response,
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) Optional<String> authentification) {
        Long userId = userService.getUserId(authentification);

        return userService.getUserTransactions(userId);
    }
    
    @GetMapping("/api/user/user-transactions/portfolio/{id}")
    public List<TransactionDTO> getUserTransactionsByPortfolioId(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
        UserEntity user = (UserEntity) request.getAttribute("user");
        return userService.getUserTransactionsByPortfolioId(user.getId(), id);
    }

    @GetMapping("/api/user/user-transactions/{id}")
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
    public List<CryptoPriceDTO> listWatchlist(HttpServletRequest request) {

        UserEntity userEntity = (UserEntity) request.getAttribute("user");
        return userService.getUserWatchlist(userEntity.getId());
    }
    

    @GetMapping("/api/user/watchlist/not-in-watchlist")
    public List<CryptoDTO> getCryptoNotInWatchlist(HttpServletRequest request) {
        
        UserEntity user = (UserEntity) request.getAttribute("user");
        return userService.getCryptoNotInWatchlist(user.getId());
    }

    @GetMapping("/api/user/balance-history")
    public List<BalanceHistoryDTO> getBalanceHistory(HttpServletRequest request) {
        UserEntity user = (UserEntity) request.getAttribute("user");
        return balanceHistory.getBalanceHistory(user.getId());
    }
    
    @GetMapping("/api/user/portfolio-value-history")
    public List<PortfolioValueHistoryDTO> getPortfolioValueHistory(HttpServletRequest request, HttpServletResponse response, Long portfolioId) {
        UserEntity user = (UserEntity) request.getAttribute("user");

        return portfolioValueHistory.getPortfolioValueHistory(user.getId(), portfolioId);
    }

        

    @GetMapping("/api/user/watchlist/{cryptoId}")
    public CryptoPriceDTO getCryptoFromWatchlist(@PathVariable Long cryptoId, HttpServletRequest request) {

        UserEntity user = (UserEntity) request.getAttribute("user");
        return userService.getUserWatchlistItem(cryptoId, user.getId());
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