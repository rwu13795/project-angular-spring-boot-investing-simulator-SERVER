package com.raywu.investingsimulator.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {
    private String symbol;
    private int shares;
    private double priceLimit;
    private String type;
}
