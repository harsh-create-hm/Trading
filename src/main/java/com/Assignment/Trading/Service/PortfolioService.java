package com.Assignment.Trading.Service;

import com.Assignment.Trading.DTO.OverlapResponse;
import com.Assignment.Trading.DTO.PortfolioResponse;

public interface PortfolioService {
    PortfolioResponse getPortfolio(String traderId);
    void addStock(String traderId, String stock, String sector, int quantity);
    void removeStock(String traderId, String stock, int quantity);
    boolean hasEnoughShares(String traderId, String stock, int quantity);
    OverlapResponse getOverlap(String traderId);
}