package com.raywu.investingsimulator.stock.price.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortQuote {
    String symbol;
    double price;
    long volume;

    public ShortQuote() {}
}
