package com.Assignment.Trading.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Assignment.Trading.DTO.OverlapResponse;
import com.Assignment.Trading.DTO.PortfolioResponse;
import com.Assignment.Trading.Service.PortfolioService;


@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

	@Autowired
    private  PortfolioService portfolioService;

    @GetMapping("/{traderId}")
    public PortfolioResponse get(@PathVariable String traderId) {
    	logger.info("Portfolio Fetched Successfully for "+traderId);
        return portfolioService.getPortfolio(traderId);
    }

    @GetMapping("/{traderId}/overlap")
    public OverlapResponse overlap(@PathVariable String traderId) {
    	logger.info("Portfolio Fetched Successfully with overlap for "+traderId);
    	return portfolioService.getOverlap(traderId);
    }

    @PostMapping("/{traderId}/add")
    public String add(@PathVariable String traderId,
                      @RequestParam String stock,
                      @RequestParam String sector,
                      @RequestParam int quantity) {
    	logger.info("Stock added into portfolio "+traderId);
        portfolioService.addStock(traderId, stock, sector, quantity);
        return "Stock added";
    }
}