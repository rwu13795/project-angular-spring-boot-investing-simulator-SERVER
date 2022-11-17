package com.raywu.investingsimulator.portfolio.transaction.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "transaction")
public class Transaction extends TransactionTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "buy")
    private boolean buy;

    @Column(name = "price_per_share")
    private double pricePerShare;

    @Column(name = "shares")
    private int shares;

    @Column(name = "realized_gain_loss")
    private double realizedGainLoss;

    @Column(name = "asset_total_realized_gain_loss")
    private double assetTotalRealizedGainLoss;

    @Column(name = "timestamp")
    private long timestamp;

    public Transaction() {}
}
