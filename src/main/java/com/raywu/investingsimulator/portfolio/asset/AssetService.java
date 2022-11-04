package com.raywu.investingsimulator.portfolio.asset;

import java.util.List;

public interface AssetService {
    List<Asset> findAllAssets(int user_id);
}
