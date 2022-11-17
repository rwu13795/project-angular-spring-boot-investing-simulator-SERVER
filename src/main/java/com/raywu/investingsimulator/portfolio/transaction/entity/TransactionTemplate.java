package com.raywu.investingsimulator.portfolio.transaction.entity;

public abstract class TransactionTemplate {
     int id;

     int userId;

     String symbol;

     double pricePerShare;

     int shares;

     double realizedGainLoss;

     double assetTotalRealizedGainLoss;

     long timestamp;
}
