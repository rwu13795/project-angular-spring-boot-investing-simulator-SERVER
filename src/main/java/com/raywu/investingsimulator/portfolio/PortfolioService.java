package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;

public interface PortfolioService {
    Portfolio getPortfolio(int user_id);

    Transaction buyAndSell(int userId, TransactionRequest tr);

    TransactionShortSell shortSellAndBuyToCover(int userId, TransactionRequest tr);
}
