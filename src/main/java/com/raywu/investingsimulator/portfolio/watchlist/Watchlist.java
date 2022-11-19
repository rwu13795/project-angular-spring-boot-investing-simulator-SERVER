package com.raywu.investingsimulator.portfolio.watchlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "asset")
@IdClass(WatchlistPK.class)
@AllArgsConstructor
public class Watchlist implements Serializable {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "symbol")
    private String symbol;

    @Column(name = "exchange")
    private String exchange;

    // no-arg constructor is required by Hibernate
    public Watchlist() {}
}
