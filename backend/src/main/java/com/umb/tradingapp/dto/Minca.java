package com.umb.tradingapp.dto;

public class Minca {
    private String uuid;
    private String symbol;
    private String nazov;

    public Minca(String uuid, String symbol, String nazov) {
        this.uuid = uuid;
        this.symbol = symbol;
        this.nazov = nazov;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNazov() {
        return nazov;
    }
}