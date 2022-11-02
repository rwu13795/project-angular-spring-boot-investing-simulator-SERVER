package com.raywu.investingsimulator.stock.price.dto;

import lombok.Data;

@Data
public class PriceHistory {
    private String date;
    private double open;
    private double low;
    private double high;
    private double close;
    private int volume;

    public PriceHistory() {}

    public PriceHistory(String date, double open, double low,
                        double high, double close, int volume) {
        this.date = date;
        this.open = open;
        this.low = low;
        this.high = high;
        this.close = close;
        this.volume = volume;
    }
}
