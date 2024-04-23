package com.umb.tradingapp.service;

import com.umb.tradingapp.DTO.CryptoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {

    @Autowired
    private ApiService JE;

    public List<CryptoDTO> listAllCryptos(){
        ArrayList<CryptoDTO> list = new ArrayList<>();

        return JE.main();
    }
}
