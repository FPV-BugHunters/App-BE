package com.umb.tradingapp.service;

import com.umb.tradingapp.DTO.CryptoDTO;
import com.umb.tradingapp.api.JavaExample;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {

    public List<CryptoDTO> listAllCryptos(){
        ArrayList<CryptoDTO> list = new ArrayList<>();
        CryptoDTO d = new CryptoDTO();

        d.setName("hehe");
        list.add(d);


        //return JavaExample.main();
        return list;
    }
}
