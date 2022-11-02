package com.raywu.investingsimulator.stock.preview;

public interface PreviewService {
    String fetchPreviewList(String option);
    String fetchPeersList(String symbol);

}
