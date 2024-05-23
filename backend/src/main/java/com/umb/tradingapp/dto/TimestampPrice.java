package com.umb.tradingapp.dto;

public class TimestampPrice {
    private long timestamp;
    private double price;

    public TimestampPrice(long timestamp, double price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Timestamp: " + timestamp + ", Price: " + price;
    }
}