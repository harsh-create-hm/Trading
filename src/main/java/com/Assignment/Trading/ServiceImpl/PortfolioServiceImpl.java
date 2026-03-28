package com.Assignment.Trading.ServiceImpl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Assignment.Trading.DTO.OverlapResponse;
import com.Assignment.Trading.DTO.PortfolioResponse;
import com.Assignment.Trading.Entity.Holding;
import com.Assignment.Trading.Entity.Portfolio;
import com.Assignment.Trading.Exception.BusinessException;
import com.Assignment.Trading.Repository.HoldingRepository;
import com.Assignment.Trading.Repository.PortfolioRepository;
import com.Assignment.Trading.Service.OverlapService;
import com.Assignment.Trading.Service.PortfolioService;


@Service
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
    private  PortfolioRepository portfolioRepository;
	@Autowired
    private  HoldingRepository holdingRepository;
	@Autowired
    private  OverlapService overlapService;

    @Override
    public PortfolioResponse getPortfolio(String traderId) {

        Portfolio portfolio = portfolioRepository.findByTraderId(traderId)
                .orElseThrow(() -> new BusinessException("Portfolio not found"));

        List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);

        Map<String, Integer> positions = new HashMap<>();
        Map<String, Integer> sectors = new HashMap<>();

        for (Holding h : holdings) {
            positions.merge(h.getStock(), h.getQuantity(), Integer::sum);
            sectors.merge(h.getSector(), h.getQuantity(), Integer::sum);
        }

        return new PortfolioResponse(traderId, positions, sectors);
    }

    @Transactional
    @Override
    public void addStock(String traderId, String stock, String sector, int quantity) {

        Portfolio portfolio = portfolioRepository.findByTraderId(traderId)
                .orElseGet(() -> portfolioRepository.save(new Portfolio(null, traderId)));

        Optional<Holding> existing =
                holdingRepository.findByPortfolioAndStockForUpdate(portfolio, stock);

        if (existing.isPresent()) {
            Holding h = existing.get();
            h.setQuantity(h.getQuantity() + quantity);
            holdingRepository.save(h);
        } else {
            Holding h = new Holding();
            h.setPortfolio(portfolio);
            h.setStock(stock);
            h.setSector(sector);
            h.setQuantity(quantity);
            holdingRepository.save(h);
        }
    }

    @Transactional
    @Override
    public void removeStock(String traderId, String stock, int quantity) {

        Portfolio portfolio = portfolioRepository.findByTraderId(traderId)
                .orElseThrow(() -> new BusinessException("Portfolio not found"));

        Holding holding = holdingRepository
                .findByPortfolioAndStockForUpdate(portfolio, stock)
                .orElseThrow(() -> new BusinessException("Stock not found"));

        if (holding.getQuantity() < quantity) {
            throw new BusinessException("Not enough shares");
        }

        holding.setQuantity(holding.getQuantity() - quantity);
        holdingRepository.save(holding);
    }

    @Override
    public boolean hasEnoughShares(String traderId, String stock, int quantity) {

        return portfolioRepository.findByTraderId(traderId)
                .flatMap(p -> holdingRepository.findByPortfolioAndStock(p, stock))
                .map(h -> h.getQuantity() >= quantity)
                .orElse(false);
    }

    @Override
    public OverlapResponse getOverlap(String traderId) {

        Portfolio portfolio = portfolioRepository.findByTraderId(traderId)
                .orElseThrow(() -> new BusinessException("Portfolio not found"));

        Set<String> stocks = holdingRepository.findByPortfolio(portfolio)
                .stream()
                .map(Holding::getStock)
                .collect(Collectors.toSet());

        return overlapService.calculateOverlap(stocks);
    }
}