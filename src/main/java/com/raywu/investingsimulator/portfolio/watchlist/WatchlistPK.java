package com.raywu.investingsimulator.portfolio.watchlist;

import java.io.Serializable;
import java.util.Objects;

public class WatchlistPK implements Serializable {
    private int userId;
    private String symbol;

    public WatchlistPK() {}

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WatchlistPK watchlistPK = (WatchlistPK) o;
//        return userId == watchlistPK.userId &&
//                symbol.equals(watchlistPK.symbol);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, symbol);
    }
}