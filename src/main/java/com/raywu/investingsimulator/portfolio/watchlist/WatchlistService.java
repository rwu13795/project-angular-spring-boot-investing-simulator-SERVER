package com.raywu.investingsimulator.portfolio.watchlist;

import java.util.HashMap;
import java.util.List;

public interface WatchlistService {
    HashMap<String, String> getWatchlist(int userId);

    void addToList(int userId, String symbol, String exchange);

    void removeFromList(int userId, String symbol);

    void removeFromList_batch(int userId, String[] symbols);

    List<Watchlist> findByPageNum(int userId, int pageNum);

    String findByPageNum_withPrice(int userId, int pageNum);
}
