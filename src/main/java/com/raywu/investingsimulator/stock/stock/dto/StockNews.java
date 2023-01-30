package com.raywu.investingsimulator.stock.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockNews {
    String symbol;
    String publishedDate;
    String title;
    String image;
    String site;
    String text;
    String url;

    public StockNews() {}
}
