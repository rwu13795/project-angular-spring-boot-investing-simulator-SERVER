package com.raywu.investingsimulator.stock.preview.models;

import lombok.Data;

import java.util.List;

@Data
public class PeersList {
    private String symbol;
    private List<String> peersList;

    public PeersList() {}

    public PeersList(String symbol, List<String> peersList) {
        this.symbol = symbol;
        this.peersList = peersList;
    }
}

