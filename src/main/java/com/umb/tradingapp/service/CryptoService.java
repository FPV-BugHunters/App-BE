package com.umb.tradingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.dto.CryptoPriceDTO;

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
