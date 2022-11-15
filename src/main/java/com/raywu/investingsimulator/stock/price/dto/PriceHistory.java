package com.raywu.investingsimulator.stock.price.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceHistory {
    private String date;
    private double open;
    private double low;
    private double high;
    private double close;
    private long volume;

    public PriceHistory() {}
}
