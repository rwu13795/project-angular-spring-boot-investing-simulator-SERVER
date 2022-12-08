package com.raywu.investingsimulator.portfolio.asset;

import com.raywu.investingsimulator.exception.exceptions.BadRequestException;
import com.raywu.investingsimulator.portfolio.dto.TransactionGainLoss;
import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.dto.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService_impl implements AssetService {
    @Autowired
    AssetRepository assetRepository;

    @Override
    public List<Asset> findAllAssets(int userId) {

        return assetRepository.findAllAssets(userId);
    }

    @Override
    public Asset findAssetBySymbol(int userId, String symbol) {
        Optional<Asset> asset = assetRepository.findAssetBySymbol(userId, symbol);
        return asset.orElse(null);
    }

    @Override
    public void saveAsset(Asset asset) {
        assetRepository.save(asset);
    }

    @Override
    public TransactionGainLoss updateAsset(int userId, double currentPrice, TransactionRequest tr) {
        Asset asset = findAssetBySymbol(userId, tr.getSymbol());
        if(asset == null) asset = new Asset(userId, tr.getSymbol(), tr.getExchange());

        TransactionGainLoss transactionGainLoss = new TransactionGainLoss();

        // need to return the realizedGainLoss for the transaction if user sell or buy_to_cover
        // and record the current asset total realized gain/loss for the charts
        double realizedGainLoss = 0.0;

        switch (TransactionType.valueOf(tr.getType())) {
            case BUY: {
                double previousTotal = asset.getAvgCost() * asset.getShares();
                double orderTotal = currentPrice * tr.getShares();
                int newShares = asset.getShares() + tr.getShares();
                double newAvg = (previousTotal + orderTotal) / newShares;
                asset.setShares(newShares);
                asset.setAvgCost(newAvg);

                transactionGainLoss.setAssetTotalRealizedGainLoss(asset.getRealizedGainLoss());
                break;
            }
            case SELL: {
                if(asset.getShares() < tr.getShares()) {
                    throw new BadRequestException
                            ("You are trying to sell more shares than you own", "QUANTITY");
                }
                asset.setShares(asset.getShares() - tr.getShares());

                // record the realized gain/loss for this specific transaction
                realizedGainLoss = (currentPrice - asset.getAvgCost()) * tr.getShares();

                asset.setRealizedGainLoss(asset.getRealizedGainLoss() + realizedGainLoss);

                // record the current total realized gain/loss in the transaction
                transactionGainLoss.setAssetTotalRealizedGainLoss(asset.getRealizedGainLoss());
                break;
            }
            case SELL_SHORT: {
                double previousTotal = asset.getAvgBorrowed() * asset.getSharesBorrowed();
                double orderTotal = currentPrice * tr.getShares();
                int newShares = asset.getSharesBorrowed() + tr.getShares();
                double newAvg = (previousTotal + orderTotal) / newShares;
                asset.setSharesBorrowed(newShares);
                asset.setAvgBorrowed(newAvg);

                transactionGainLoss.setAssetTotalRealizedGainLoss(asset.getRealizedGainLossShortSelling());
                break;
            }
            case BUY_TO_COVER: {
                if(asset.getSharesBorrowed() < tr.getShares()) {
                    throw new BadRequestException
                            ("You are trying to cover more shares than you borrowed", "QUANTITY");
                }
                asset.setSharesBorrowed(asset.getSharesBorrowed() - tr.getShares());

                // record the realized gain/loss for this specific transaction
                realizedGainLoss = -(currentPrice - asset.getAvgBorrowed()) * tr.getShares();

                asset.setRealizedGainLossShortSelling(asset.getRealizedGainLossShortSelling() + realizedGainLoss);

                // record the current total realized short sale gain/loss in the transaction
                transactionGainLoss.setAssetTotalRealizedGainLoss(asset.getRealizedGainLossShortSelling());
                break;
            }
            default: throw new BadRequestException("Invalid transaction type", "transaction");
        }

        saveAsset(asset);

        transactionGainLoss.setRealizedGainLoss(realizedGainLoss);

        return transactionGainLoss;
    }

}

