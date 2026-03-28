package com.Assignment.Trading.ServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.Assignment.Trading.DTO.OverlapResponse;

public class OverlapServiceImplTest {

    private OverlapServiceImpl overlapService;

    @Before
    public void setUp() {
        overlapService = new OverlapServiceImpl();
    }


    @Test
    public void testCalculateOverlapEmptyPortfolio() {
        Set<String> portfolio = new HashSet<>();

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertEquals("NONE", response.getDominantBasket());
        assertEquals("LOW", response.getRiskFlag());
        assertTrue(response.getOverlaps().isEmpty());
    }

    @Test
    public void testCalculateOverlapTechHeavy_HighRisk() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT", "GOOGL", "TSLA"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertEquals("TECH_HEAVY", response.getDominantBasket());
        assertEquals("HIGH", response.getRiskFlag());
        assertEquals(3, response.getOverlaps().size());
    }

    @Test
    public void testCalculateOverlapFinanceHeavy_HighRisk() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("JPM", "GS", "BAC", "MS"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertEquals("FINANCE_HEAVY", response.getDominantBasket());
        assertEquals("HIGH", response.getRiskFlag());
    }

    @Test
    public void testCalculateOverlapBalanced_LowRisk() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("XOM"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertEquals("LOW", response.getRiskFlag());
    }

    @Test
    public void testCalculateOverlapMediumRisk() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT", "JPM"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertTrue(response.getRiskFlag().equals("MEDIUM") || response.getRiskFlag().equals("HIGH"));
    }

    @Test
    public void testCalculateOverlapSingleStock() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertFalse(response.getDominantBasket().equals("NONE"));
        assertEquals(3, response.getOverlaps().size());
    }

    @Test
    public void testCalculateOverlapMultipleBaskets() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "JPM", "XOM"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertEquals(3, response.getOverlaps().size());
        assertFalse(response.getOverlaps().isEmpty());
    }

    @Test
    public void testCalculateOverlapAllTechStocks() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT", "GOOGL", "TSLA", "NVDA"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertEquals("TECH_HEAVY", response.getDominantBasket());
        assertEquals("HIGH", response.getRiskFlag());
    }

    @Test
    public void testCalculateOverlapAllFinanceStocks() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("JPM", "GS", "BAC", "MS", "WFC"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertEquals("FINANCE_HEAVY", response.getDominantBasket());
        assertEquals("HIGH", response.getRiskFlag());
    }

    @Test
    public void testCalculateOverlapMixedStocks() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "BAC", "JNJ"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertEquals(3, response.getOverlaps().size());
    }

    @Test
    public void testCalculateOverlapPercentageFormatting() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        response.getOverlaps().forEach(basket -> {
            String overlap = basket.getOverlap();
            assertTrue(overlap.contains("%"));
            assertTrue(overlap.matches("\\d+\\.\\d{2}%"));
        });
    }

    @Test
    public void testCalculateOverlapDominantBasketSelection() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT", "GOOGL"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertEquals("TECH_HEAVY", response.getDominantBasket());
    }

    @Test
    public void testCalculateOverlapNonExistingStocks() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("UNKNOWN1", "UNKNOWN2"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertEquals("", response.getDominantBasket());
        assertEquals("LOW", response.getRiskFlag());
        assertEquals(3, response.getOverlaps().size());
    }

    @Test
    public void testCalculateOverlapPartialBasketMatch() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertEquals(3, response.getOverlaps().size());
        assertNotEquals("NONE", response.getDominantBasket());
    }

    @Test
    public void testCalculateOverlapValidBasketNames() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT", "GOOGL", "TSLA"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        for (var basketOverlap : response.getOverlaps()) {
            assertTrue(basketOverlap.getBasket().equals("TECH_HEAVY") ||
                      basketOverlap.getBasket().equals("FINANCE_HEAVY") ||
                      basketOverlap.getBasket().equals("BALANCED"));
        }
    }

    @Test
    public void testCalculateOverlapHighRiskThreshold() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT", "GOOGL", "TSLA", "NVDA"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertEquals("HIGH", response.getRiskFlag());
    }

    @Test
    public void testCalculateOverlapLowRiskThreshold() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("XOM"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertEquals("LOW", response.getRiskFlag());
    }

    @Test
    public void testCalculateOverlapResponseNotNull() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertNotNull(response);
        assertNotNull(response.getOverlaps());
        assertNotNull(response.getDominantBasket());
        assertNotNull(response.getRiskFlag());
    }

    @Test
    public void testCalculateOverlapOverlapCountAlwaysThree() {
        Set<String> portfolio = new HashSet<>(Arrays.asList("AAPL", "MSFT", "BAC", "XOM"));

        OverlapResponse response = overlapService.calculateOverlap(portfolio);

        assertEquals(3, response.getOverlaps().size());
    }
}