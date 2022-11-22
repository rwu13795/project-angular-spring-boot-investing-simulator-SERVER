package com.raywu.investingsimulator.portfolio.watchlist;

import com.raywu.investingsimulator.stock.price.PriceService;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class WatchlistService_impl implements WatchlistService {
    final int ITEM_PER_PAGE = 10;
    @Autowired
    WatchlistRepository watchlistRepository;
    @Autowired
    PriceService priceService;

    @Override
    public HashMap<String, String> getWatchlist(int userId) {
        List<Watchlist> list = watchlistRepository.getWatchlist(userId);
        HashMap<String, String> watchlist = new HashMap<>();

        for(var entry : list) {
            watchlist.put(entry.getSymbol(), entry.getSymbol());
        }
        return watchlist;
    }

    @Override
    public void addToList(int userId, String symbol, String exchange){
        long timestamp = System.currentTimeMillis();
        this.watchlistRepository.addToList(userId, symbol, exchange, timestamp);
    }

    @Override
    public void removeFromList(int user_id, String symbol) {

        this.watchlistRepository.removeFromList(user_id, symbol);
    }

    @Override
    public void removeFromList_batch(int userId, String[] symbols) {
        for (var s : symbols) {
            this.watchlistRepository.removeFromList(userId, s);
        }
    }

    @Override
    public List<Watchlist> findByPageNum(int userId, int pageNum) {
        int offset = (pageNum - 1) * ITEM_PER_PAGE;

        return watchlistRepository.findByPageNum(userId, offset);
    }

    @Override
    public String findByPageNum_withPrice(int userId, int pageNum) {
        int offset = (pageNum - 1) * ITEM_PER_PAGE;

        List<Watchlist> list = watchlistRepository.findByPageNum(userId, offset);
        if(list.size() == 0) return "[]";

        List<String> symbols = list.stream().map(item -> item.getSymbol()).toList();
        String symbolString = StringUtils.join(symbols,',');

        return priceService.fetchRealTimeQuote(symbolString);
    }
}
