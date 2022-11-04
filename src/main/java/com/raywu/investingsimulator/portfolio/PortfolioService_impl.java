package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.account.AccountService;
import com.raywu.investingsimulator.portfolio.asset.Asset;
import com.raywu.investingsimulator.portfolio.asset.AssetService;
import com.raywu.investingsimulator.stock.StockService;
import com.raywu.investingsimulator.stock.price.PriceService;
import com.raywu.investingsimulator.stock.price.dto.ShortQuote;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService_impl implements PortfolioService {
    @Autowired
    private PriceService priceService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AssetService assetService;

    @Override
    public ResponseEntity<Portfolio> getPortfolio(int user_id) {

        Account account = accountService.findById(user_id);
        account.setPassword("xxxxx");
        List<Asset> assets = assetService.findAllAssets(user_id);

        List<String> symbols = assets.stream().map(asset -> asset.getSymbol()).toList();
        String symbolString = StringUtils.join(symbols,',');

        // map the assets using the symbol as key
        HashMap<String, Asset> assetMap = new HashMap<>();
        assets.stream().forEach(asset -> assetMap.put(asset.getSymbol(), asset));

        // calculate the gain/loss using the current quote for each asset
        ShortQuote[] quotes = priceService.fetchShortQuoteList(symbolString);
        for(var q : quotes) {
            Asset asset = assetMap.get(q.getSymbol());
            asset.setCurrentPrice(q.getPrice());
            asset.setGainOrLoss(q.getPrice() * asset.getShares() - asset.getAvg_cost());
        }

        return ResponseEntity.ok().body(new Portfolio(account, symbols, assetMap));
    }
}
