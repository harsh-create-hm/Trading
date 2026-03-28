package com.Assignment.Trading.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Assignment.Trading.DTO.OverlapResponse;
import com.Assignment.Trading.DTO.PortfolioResponse;
import com.Assignment.Trading.ServiceImpl.PortfolioServiceImpl;


@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

	@Autowired
    private  PortfolioServiceImpl portfolioServiceImpl;

    @GetMapping("/{traderId}")
    public PortfolioResponse get(@PathVariable String traderId) {
        return portfolioServiceImpl.getPortfolio(traderId);
    }

    @GetMapping("/{traderId}/overlap")
    public OverlapResponse overlap(@PathVariable String traderId) {
        return portfolioServiceImpl.getOverlap(traderId);
    }

    @PostMapping("/{traderId}/add")
    public String add(@PathVariable String traderId,
                      @RequestParam String stock,
                      @RequestParam String sector,
                      @RequestParam int quantity) {

        portfolioServiceImpl.addStock(traderId, stock, sector, quantity);
        return "Stock added";
    }
}