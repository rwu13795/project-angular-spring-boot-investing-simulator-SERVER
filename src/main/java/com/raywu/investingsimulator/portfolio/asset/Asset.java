package com.raywu.investingsimulator.portfolio.asset;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "asset")
@IdClass(AssetPK.class)
public class Asset implements Serializable {
    @Id
    @Column(name = "user_id")
    private int user_id;

    @Id
    @Column(name = "symbol")
    private String symbol;

    @Column(name = "avg_cost")
    private double avg_cost;

    @Column(name = "shares")
    private int shares;

    @Transient
    private double gainOrLoss;

    @Transient
    private double currentPrice;

    // no-arg constructor is required by Hibernate
    public Asset() {}
}
