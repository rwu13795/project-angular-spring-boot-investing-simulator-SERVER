package com.raywu.investingsimulator.stock.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResult {
    private String symbol;
    private String name;
    private String currency;
    private String stockExchange;
    private String exchangeShortName;

    public SearchResult() {}
}
