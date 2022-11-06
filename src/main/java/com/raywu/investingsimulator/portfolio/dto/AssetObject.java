package com.raywu.investingsimulator.portfolio.dto;

import com.raywu.investingsimulator.portfolio.asset.Asset;
import lombok.Data;

import java.util.HashMap;

@Data
public class AssetObject {
    private HashMap<String, Asset> map;

    public AssetObject(Asset asset) {
        this.map.put(asset.getSymbol(), asset);
    }
}
