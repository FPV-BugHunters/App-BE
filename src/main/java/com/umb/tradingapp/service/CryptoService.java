package com.umb.tradingapp.service;

import com.umb.tradingapp.DTO.CryptoPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {

    @Autowired
    private ApiService JE;

    public List<CryptoPriceDTO> listAllCryptos(){
        ArrayList<CryptoPriceDTO> list = new ArrayList<>();

        return JE.main();
    }
}
