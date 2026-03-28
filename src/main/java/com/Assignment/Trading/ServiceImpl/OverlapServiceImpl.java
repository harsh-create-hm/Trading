package com.Assignment.Trading.ServiceImpl;


import org.springframework.stereotype.Service;

import com.Assignment.Trading.DTO.BasketOverlap;
import com.Assignment.Trading.DTO.OverlapResponse;
import com.Assignment.Trading.Service.OverlapService;
 
import java.util.*;

@Service
public class OverlapServiceImpl implements OverlapService {

    private static final Map<String, Set<String>> BASKETS = Map.of(
            "TECH_HEAVY", Set.of("AAPL", "MSFT", "GOOGL", "TSLA", "NVDA"),
            "FINANCE_HEAVY", Set.of("JPM", "GS", "BAC", "MS", "WFC"),
            "BALANCED", Set.of("AAPL", "JPM", "XOM", "JNJ", "TSLA")
    );

    @Override
    public OverlapResponse calculateOverlap(Set<String> portfolio) {

        if (portfolio.isEmpty()) {
            return new OverlapResponse(Collections.emptyList(), "NONE", "LOW");
        }

        List<BasketOverlap> results = new ArrayList<>();
        double max = 0;
        String dominant = "";

        for (var entry : BASKETS.entrySet()) {

            Set<String> basket = entry.getValue();
            Set<String> common = new HashSet<>(portfolio);
            common.retainAll(basket);

            double overlap = (2.0 * common.size()) /
                    (portfolio.size() + basket.size()) * 100;

            results.add(new BasketOverlap(
                    entry.getKey(),
                    String.format("%.2f%%", overlap)
            ));

            if (overlap > max) {
                max = overlap;
                dominant = entry.getKey();
            }
        }

        String risk = max >= 60 ? "HIGH" : max >= 40 ? "MEDIUM" : "LOW";

        return new OverlapResponse(results, dominant, risk);
    }
}