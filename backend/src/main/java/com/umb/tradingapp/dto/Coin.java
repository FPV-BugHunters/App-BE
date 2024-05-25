package com.umb.tradingapp.dto;

public class Coin {
    private String uuid;
    private String symbol;
    private String name;

    public Coin(String uuid, String symbol, String name) {
        this.uuid = uuid;
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
