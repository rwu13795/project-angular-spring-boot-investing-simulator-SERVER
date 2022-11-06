package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.portfolio.asset.Asset;
import com.raywu.investingsimulator.portfolio.dto.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Portfolio {
    private AccountResponse account;
    private List<String> symbols;
    private HashMap<String, Asset> assets;
}
