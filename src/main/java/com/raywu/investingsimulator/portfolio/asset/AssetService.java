package com.raywu.investingsimulator.portfolio.asset;

import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;

import java.util.List;

public interface AssetService {
    List<Asset> findAllAssets(int userId);

    Asset findAssetBySymbol(int userId, String symbol);

    void saveAsset(Asset asset);

    double updateAsset(int userId, double currentPrice, TransactionRequest tr);
}
