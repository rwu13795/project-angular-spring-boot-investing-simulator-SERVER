package com.raywu.investingsimulator.portfolio.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, AssetPK> {

    @Query(value = "SELECT * FROM asset a WHERE a.user_id = ?1",
            nativeQuery = true)
    List<Asset> findAllAssets(int userId);

    @Query(value = "SELECT * FROM asset a WHERE a.user_id = ?1 and a.symbol = ?2",
            nativeQuery = true)
    Optional<Asset> findAssetBySymbol(int userId, String symbol);
}