package com.Assignment.Trading.Controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.Assignment.Trading.DTO.OverlapResponse;
import com.Assignment.Trading.DTO.PortfolioResponse;
import com.Assignment.Trading.Exception.BusinessException;
import com.Assignment.Trading.ServiceImpl.PortfolioServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioControllerTest {

    @Mock
    private PortfolioServiceImpl portfolioServiceImpl;

    @InjectMocks
    private PortfolioController portfolioController;

    private PortfolioResponse portfolioResponse;
    private OverlapResponse overlapResponse;

    @Before
    public void setUp() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("AAPL", 100);
        
        Map<String, Integer> sectors = new HashMap<>();
        sectors.put("TECH", 100);
        
        portfolioResponse = new PortfolioResponse("TRADER1", positions, sectors);
        overlapResponse = new OverlapResponse(new ArrayList<>(), "TECH_HEAVY", "HIGH");
    }

    @Test
    public void testGetPortfolioSuccess() {
        when(portfolioServiceImpl.getPortfolio("TRADER1")).thenReturn(portfolioResponse);

        PortfolioResponse result = portfolioController.get("TRADER1");

        assertNotNull(result);
        assertEquals("TRADER1", result.getTraderId());
        verify(portfolioServiceImpl, times(1)).getPortfolio("TRADER1");
    }

    @Test(expected = BusinessException.class)
    public void testGetPortfolioFailure() {
        when(portfolioServiceImpl.getPortfolio("UNKNOWN")).thenThrow(new BusinessException("Portfolio not found"));

        portfolioController.get("UNKNOWN");
    }

    @Test
    public void testOverlapSuccess() {
        when(portfolioServiceImpl.getOverlap("TRADER1")).thenReturn(overlapResponse);

        OverlapResponse result = portfolioController.overlap("TRADER1");

        assertNotNull(result);
        assertEquals("TECH_HEAVY", result.getDominantBasket());
        verify(portfolioServiceImpl, times(1)).getOverlap("TRADER1");
    }

    @Test
    public void testAddStockSuccess() {
        String result = portfolioController.add("TRADER1", "AAPL", "TECH", 100);

        assertEquals("Stock added", result);
        verify(portfolioServiceImpl, times(1)).addStock("TRADER1", "AAPL", "TECH", 100);
    }

    @Test
    public void testAddStockWithDifferentQuantity() {
        String result = portfolioController.add("TRADER1", "MSFT", "TECH", 50);

        assertEquals("Stock added", result);
        verify(portfolioServiceImpl, times(1)).addStock("TRADER1", "MSFT", "TECH", 50);
    }
}