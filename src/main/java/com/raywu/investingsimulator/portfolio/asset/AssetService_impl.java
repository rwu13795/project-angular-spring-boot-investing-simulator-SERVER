package com.raywu.investingsimulator.portfolio.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService_impl implements AssetService {
    @Autowired
    AssetRepository assetRepository;

    @Override
    public List<Asset> findAllAssets(int user_id) {
        List<Asset> assets = assetRepository.findAllAssets(user_id);

        return assets;
    }
}

