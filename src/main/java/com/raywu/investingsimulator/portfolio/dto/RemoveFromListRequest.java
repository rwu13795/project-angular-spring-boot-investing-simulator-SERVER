package com.raywu.investingsimulator.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoveFromListRequest {
    private String[] symbols;

    // no arg constructor is needed if the request body contains an array
    public RemoveFromListRequest() {}
}
