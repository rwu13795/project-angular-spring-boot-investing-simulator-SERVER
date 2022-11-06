package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.exception.exceptions.BadRequestException;
import com.raywu.investingsimulator.exception.exceptions.NotEnoughFundException;
import com.raywu.investingsimulator.exception.exceptions.PriceLimitException;
import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.account.AccountService;
import com.raywu.investingsimulator.portfolio.asset.Asset;
import com.raywu.investingsimulator.portfolio.asset.AssetService;
import com.raywu.investingsimulator.portfolio.dto.AccountResponse;
import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.dto.TransactionType;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.TransactionService;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;
import com.raywu.investingsimulator.stock.price.PriceService;
import com.raywu.investingsimulator.stock.price.dto.ShortQuote;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PortfolioService_impl implements PortfolioService {
    @Autowired
    private PriceService priceService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private TransactionService transactionService;

    @Override
    public Portfolio getPortfolio(int user_id) {
        Account account = accountService.findById(user_id);
        List<Asset> assets = assetService.findAllAssets(user_id);

        double totalRealizedGainLoss = 0.0;
        double totalUnrealizedGainLoss = 0.0;
        double shortSellingDeposit = 0.0;
        List<String> symbols = new ArrayList<>();
        HashMap<String, Asset> assetMap = new HashMap<>();

        if(assets.size() > 0) {
            symbols = assets.stream().map(asset -> asset.getSymbol()).toList();
            String symbolString = StringUtils.join(symbols,',');

            // map the assets using the symbol as key to create a normalized object
            assetMap = new HashMap<>();
            for(var asset : assets) {
                assetMap.put(asset.getSymbol(), asset);
                totalRealizedGainLoss += asset.getRealizedGainLoss();
            }

            // calculate unrealized the gain/loss using the current quote for each asset
            ShortQuote[] quotes = priceService.fetchShortQuoteList(symbolString);
            for(var q : quotes) {
                Asset asset = assetMap.get(q.getSymbol());
                asset.setCurrentPrice(q.getPrice());
                double unrealized = (q.getPrice() - asset.getAvgCost()) * asset.getShares();
                double unrealizedBorrowed = -(q.getPrice() - asset.getAvgBorrowed()) * asset.getSharesBorrowed();
                shortSellingDeposit += q.getPrice() * asset.getSharesBorrowed();
                asset.setUnrealizedGainLoss(unrealized);
                asset.setUnrealizedGainLossBorrowed(unrealizedBorrowed);
                totalUnrealizedGainLoss += (unrealized + unrealizedBorrowed);
            }
        }

        AccountResponse accountRes = new AccountResponse(account);
        accountRes.setTotalRealizedGainLoss(totalRealizedGainLoss);
        accountRes.setTotalUnrealizedGainLoss(totalUnrealizedGainLoss);
        // the deposit has to be 150% of all the short sale value
        accountRes.setShortSellingDeposit(shortSellingDeposit * 1.5);
        return new Portfolio(accountRes, symbols, assetMap);
    }

    @Override
    public Transaction buyAndSell(int userId, TransactionRequest tr) {
        ShortQuote[] list = priceService.fetchShortQuoteList(tr.getSymbol());
        double currentPrice = list[0].getPrice();
        Portfolio portfolio = getPortfolio(userId);

        if(tr.getType().equals(TransactionType.BUY.name())) {
            if(currentPrice > tr.getPriceLimit()) throw new PriceLimitException(tr.getType());
            if(!hasEnoughFund(portfolio, tr.getShares() * currentPrice, false)) {
                throw new NotEnoughFundException(tr.getType());
            }
        } else {
            if(currentPrice < tr.getPriceLimit()) throw new PriceLimitException(tr.getType());
        }

        double realizedGainLoss = assetService.updateAsset(userId, currentPrice, tr);

        Transaction transaction = transactionService
                .addNewTransaction(userId, currentPrice, tr, realizedGainLoss);

        accountService.updateAccountFund(userId, currentPrice, tr, realizedGainLoss);

        return transaction;
    }

    @Override
    public TransactionShortSell shortSellAndBuyToCover(int userId, TransactionRequest tr) {
        ShortQuote[] list = priceService.fetchShortQuoteList(tr.getSymbol());
        double currentPrice = list[0].getPrice();
        Portfolio portfolio = getPortfolio(userId);

        if(tr.getType().equals(TransactionType.BUY_TO_COVER.name())) {
            if(currentPrice > tr.getPriceLimit()) throw new PriceLimitException(tr.getType());
        } else {
            if(currentPrice < tr.getPriceLimit()) throw new PriceLimitException(tr.getType());
            if(!hasEnoughFund(portfolio, tr.getShares() * currentPrice, true)) {
                throw new NotEnoughFundException(tr.getType());
            }
        }

        double realizedGainLoss = assetService.updateAsset(userId, currentPrice, tr);

        TransactionShortSell transactionSS = transactionService
                .addNewTransactionShortSell(userId, currentPrice, tr, realizedGainLoss);

        accountService.updateAccountFund(userId, currentPrice, tr, realizedGainLoss);

        return transactionSS;
    }

    private boolean hasEnoughFund(Portfolio portfolio, double orderAmount, boolean shortSell) {
        AccountResponse account = portfolio.getAccount();
        double deposit = account.getShortSellingDeposit();
        double fund = account.getFund();
        // all short sale accounts must have 150% of the value of the short sale at
        // the time the sale is initiated. I need to check if there is enough fund when user
        // place "buy" or "sell short" order
        double amount = shortSell ? orderAmount * 1.5 : orderAmount;

        if(fund - deposit - amount < 0) return false;

        return true;
    }
}


/*
*{
    "account": {
        "id": 4,
        "email": "test002@test.com",
        "password": "",
        "fund": 30000.0,
        "shortSaleDeposit": 27676.0,
        "totalRealizedGainLoss": 0.0,
        "totalUnrealizedGainLoss": -200.99999999999773
    },
    "symbols": [
        "AAPL",
        "GOOG",
        "META"
    ],
    "assets": {
        "GOOG": {
            "userId": 4,
            "symbol": "GOOG",
            "avgCost": 84.2,
            "shares": 500,
            "avgBorrowed": 0.0,
            "sharesBorrowed": 0,
            "realizedGainLoss": 0.0,
            "unrealizedGainLoss": 1250.0,
            "unrealizedGainLossBorrowed": -0.0,
            "currentPrice": 86.7
        },
        "META": {
            "userId": 4,
            "symbol": "META",
            "avgCost": 92.34,
            "shares": 500,
            "avgBorrowed": 0.0,
            "sharesBorrowed": 0,
            "realizedGainLoss": 0.0,
            "unrealizedGainLoss": -774.9999999999986,
            "unrealizedGainLossBorrowed": -0.0,
            "currentPrice": 90.79
        },
        "AAPL": {
            "userId": 4,
            "symbol": "AAPL",
            "avgCost": 141.38,
            "shares": 1000,
            "avgBorrowed": 150.0,
            "sharesBorrowed": 200,
            "realizedGainLoss": 0.0,
            "unrealizedGainLoss": -3000.0,
            "unrealizedGainLossBorrowed": 2324.000000000001,
            "currentPrice": 138.38
        }
    }
}
*
* */