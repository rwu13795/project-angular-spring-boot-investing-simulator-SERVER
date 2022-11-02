package com.raywu.investingsimulator.stock.price.dto;

import lombok.Data;

import java.util.List;

@Data
public class PriceHistoryList {
    private List<PriceHistory> historical;

    public PriceHistoryList() {}

    public PriceHistoryList(List<PriceHistory> historical) {
        this.historical = historical;
    }
}

/*

The JSON returned from the "/historical-price-full" is
{
  historical: PriceHistory[];
}

*/