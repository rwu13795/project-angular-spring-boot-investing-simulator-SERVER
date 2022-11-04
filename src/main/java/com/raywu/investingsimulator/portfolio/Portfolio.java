package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.asset.Asset;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Portfolio {
    private Account account;
    private List<String> symbols;
    private HashMap<String, Asset> assets;
}
