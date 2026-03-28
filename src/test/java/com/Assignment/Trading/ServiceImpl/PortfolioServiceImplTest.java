package com.Assignment.Trading.ServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.Assignment.Trading.DTO.OverlapResponse;
import com.Assignment.Trading.DTO.PortfolioResponse;
import com.Assignment.Trading.Entity.Holding;
import com.Assignment.Trading.Entity.Portfolio;
import com.Assignment.Trading.Exception.BusinessException;
import com.Assignment.Trading.Repository.HoldingRepository;
import com.Assignment.Trading.Repository.PortfolioRepository;
import com.Assignment.Trading.Service.OverlapService;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioServiceImplTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private HoldingRepository holdingRepository;

    @Mock
    private OverlapService overlapService;

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    private Portfolio portfolio;
    private Holding holding;

    @Before
    public void setUp() {
        portfolio = new Portfolio(1L, "TRADER1");
        
        holding = new Holding();
        holding.setId(1L);
        holding.setPortfolio(portfolio);
        holding.setStock("AAPL");
        holding.setSector("TECH");
        holding.setQuantity(100);
    }


    @Test
    public void testGetPortfolioSuccess() {
        List<Holding> holdings = new ArrayList<>();
        holdings.add(holding);
        
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);

        PortfolioResponse response = portfolioService.getPortfolio("TRADER1");

        assertNotNull(response);
        assertEquals("TRADER1", response.getTraderId());
        assertTrue(response.getPositions().containsKey("AAPL"));
        assertEquals(100, response.getPositions().get("AAPL").intValue());
    }

    @Test(expected = BusinessException.class)
    public void testGetPortfolioFailure_PortfolioNotFound() {
        when(portfolioRepository.findByTraderId("UNKNOWN")).thenReturn(Optional.empty());

        portfolioService.getPortfolio("UNKNOWN");
    }

    @Test
    public void testGetPortfolioWithMultipleHoldings() {
        List<Holding> holdings = new ArrayList<>();
        holdings.add(holding);
        
        Holding holding2 = new Holding();
        holding2.setId(2L);
        holding2.setPortfolio(portfolio);
        holding2.setStock("MSFT");
        holding2.setSector("TECH");
        holding2.setQuantity(50);
        holdings.add(holding2);
        
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);

        PortfolioResponse response = portfolioService.getPortfolio("TRADER1");

        assertEquals(2, response.getPositions().size());
        assertEquals(100, response.getPositions().get("AAPL").intValue());
        assertEquals(50, response.getPositions().get("MSFT").intValue());
    }

    @Test
    public void testGetPortfolioWithDuplicateStocks() {
        List<Holding> holdings = new ArrayList<>();
        holdings.add(holding);
        
        Holding holding2 = new Holding();
        holding2.setId(2L);
        holding2.setPortfolio(portfolio);
        holding2.setStock("AAPL");
        holding2.setSector("TECH");
        holding2.setQuantity(50);
        holdings.add(holding2);
        
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);

        PortfolioResponse response = portfolioService.getPortfolio("TRADER1");

        assertEquals(150, response.getPositions().get("AAPL").intValue());
    }


    @Test
    public void testAddStockNewHolding() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStockForUpdate(portfolio, "AAPL")).thenReturn(Optional.empty());

        portfolioService.addStock("TRADER1", "AAPL", "TECH", 100);

        ArgumentCaptor<Holding> holdingCaptor = ArgumentCaptor.forClass(Holding.class);
        verify(holdingRepository).save(holdingCaptor.capture());
        
        Holding capturedHolding = holdingCaptor.getValue();
        assertEquals("AAPL", capturedHolding.getStock());
        assertEquals("TECH", capturedHolding.getSector());
        assertEquals(100, capturedHolding.getQuantity());
    }

    @Test
    public void testAddStockExistingHolding() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStockForUpdate(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        portfolioService.addStock("TRADER1", "AAPL", "TECH", 50);

        ArgumentCaptor<Holding> holdingCaptor = ArgumentCaptor.forClass(Holding.class);
        verify(holdingRepository).save(holdingCaptor.capture());
        
        Holding capturedHolding = holdingCaptor.getValue();
        assertEquals(150, capturedHolding.getQuantity());
    }

    @Test
    public void testAddStockCreatesPortfolioIfNotExists() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.empty());
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);
        when(holdingRepository.findByPortfolioAndStockForUpdate(portfolio, "AAPL")).thenReturn(Optional.empty());

        portfolioService.addStock("TRADER1", "AAPL", "TECH", 100);

        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
        verify(holdingRepository, times(1)).save(any(Holding.class));
    }


    @Test
    public void testRemoveStockSuccess() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStockForUpdate(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        portfolioService.removeStock("TRADER1", "AAPL", 50);

        ArgumentCaptor<Holding> holdingCaptor = ArgumentCaptor.forClass(Holding.class);
        verify(holdingRepository).save(holdingCaptor.capture());
        
        Holding capturedHolding = holdingCaptor.getValue();
        assertEquals(50, capturedHolding.getQuantity());
    }

    @Test(expected = BusinessException.class)
    public void testRemoveStockFailure_PortfolioNotFound() {
        when(portfolioRepository.findByTraderId("UNKNOWN")).thenReturn(Optional.empty());

        portfolioService.removeStock("UNKNOWN", "AAPL", 50);
    }

    @Test(expected = BusinessException.class)
    public void testRemoveStockFailure_StockNotFound() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStockForUpdate(portfolio, "UNKNOWN")).thenReturn(Optional.empty());

        portfolioService.removeStock("TRADER1", "UNKNOWN", 50);
    }

    @Test(expected = BusinessException.class)
    public void testRemoveStockFailure_InsufficientShares() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStockForUpdate(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        portfolioService.removeStock("TRADER1", "AAPL", 150);
    }

    @Test
    public void testRemoveStockWithExactQuantity() {
        Holding smallHolding = new Holding();
        smallHolding.setId(1L);
        smallHolding.setPortfolio(portfolio);
        smallHolding.setStock("AAPL");
        smallHolding.setQuantity(50);
        
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStockForUpdate(portfolio, "AAPL")).thenReturn(Optional.of(smallHolding));

        portfolioService.removeStock("TRADER1", "AAPL", 50);

        ArgumentCaptor<Holding> holdingCaptor = ArgumentCaptor.forClass(Holding.class);
        verify(holdingRepository).save(holdingCaptor.capture());
        
        Holding capturedHolding = holdingCaptor.getValue();
        assertEquals(0, capturedHolding.getQuantity());
    }

    @Test
    public void testHasEnoughSharesSuccess() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStock(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        boolean result = portfolioService.hasEnoughShares("TRADER1", "AAPL", 50);

        assertTrue(result);
    }

    @Test
    public void testHasEnoughSharesExactQuantity() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStock(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        boolean result = portfolioService.hasEnoughShares("TRADER1", "AAPL", 100);

        assertTrue(result);
    }

    @Test
    public void testHasEnoughSharesFailure_InsufficientShares() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStock(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        boolean result = portfolioService.hasEnoughShares("TRADER1", "AAPL", 150);

        assertFalse(result);
    }

    @Test
    public void testHasEnoughSharesFailure_PortfolioNotFound() {
        when(portfolioRepository.findByTraderId("UNKNOWN")).thenReturn(Optional.empty());

        boolean result = portfolioService.hasEnoughShares("UNKNOWN", "AAPL", 50);

        assertFalse(result);
    }

    @Test
    public void testHasEnoughSharesFailure_StockNotFound() {
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStock(portfolio, "UNKNOWN")).thenReturn(Optional.empty());

        boolean result = portfolioService.hasEnoughShares("TRADER1", "UNKNOWN", 50);

        assertFalse(result);
    }


    @Test
    public void testGetOverlapSuccess() {
        List<Holding> holdings = new ArrayList<>();
        holdings.add(holding);
        
        OverlapResponse mockResponse = new OverlapResponse(new ArrayList<>(), "TECH_HEAVY", "HIGH");
        
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);
        when(overlapService.calculateOverlap(any())).thenReturn(mockResponse);

        OverlapResponse response = portfolioService.getOverlap("TRADER1");

        assertNotNull(response);
        assertEquals("TECH_HEAVY", response.getDominantBasket());
        verify(overlapService, times(1)).calculateOverlap(any());
    }

    @Test(expected = BusinessException.class)
    public void testGetOverlapFailure_PortfolioNotFound() {
        when(portfolioRepository.findByTraderId("UNKNOWN")).thenReturn(Optional.empty());

        portfolioService.getOverlap("UNKNOWN");
    }

    @Test
    public void testGetOverlapWithEmptyPortfolio() {
        List<Holding> emptyHoldings = new ArrayList<>();
        
        OverlapResponse mockResponse = new OverlapResponse(new ArrayList<>(), "NONE", "LOW");
        
        when(portfolioRepository.findByTraderId("TRADER1")).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(emptyHoldings);
        when(overlapService.calculateOverlap(any())).thenReturn(mockResponse);

        OverlapResponse response = portfolioService.getOverlap("TRADER1");

        assertNotNull(response);
        verify(overlapService, times(1)).calculateOverlap(any());
    }
}