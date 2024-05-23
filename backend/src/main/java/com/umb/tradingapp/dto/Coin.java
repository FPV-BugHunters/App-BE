package com.umb.tradingapp.dto;

public class Coin {
    private String uuid;
    private String symbol;
    private String nazov;

    public Coin(String uuid, String symbol, String nazov) {
        this.uuid = uuid;
        this.symbol = symbol;
        this.nazov = nazov;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNazov() {
        return nazov;
    }
}
