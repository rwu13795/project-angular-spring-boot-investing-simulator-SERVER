package com.raywu.investingsimulator.portfolio.watchlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class WatchlistService_impl implements WatchlistService {

    @Autowired
    WatchlistRepository watchlistRepository;

    @Override
    public HashMap<String, String> getWatchlist(int userId) {
        List<Watchlist> list = watchlistRepository.getWatchlist(userId);
        HashMap<String, String> watchlist = new HashMap<>();

        for(var entry : list) {
            watchlist.put(entry.getSymbol(), entry.getSymbol());
        }

        return watchlist;
    }
}
