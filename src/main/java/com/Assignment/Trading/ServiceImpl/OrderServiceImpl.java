package com.Assignment.Trading.ServiceImpl;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Assignment.Trading.DTO.OrderRequest;
import com.Assignment.Trading.Entity.Order;
import com.Assignment.Trading.Enums.OrderSide;
import com.Assignment.Trading.Enums.OrderStatus;
import com.Assignment.Trading.Exception.BusinessException;
import com.Assignment.Trading.Repository.OrderRepository;
import com.Assignment.Trading.Service.OrderService;
import com.Assignment.Trading.Service.PortfolioService;


@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
    private  OrderRepository orderRepository;
	@Autowired
    private  PortfolioService portfolioService;

    @Override
    public void placeOrder(OrderRequest request) {

        long count = orderRepository.countByTraderIdAndStatus(
               request.getTraderId(), OrderStatus.PENDING);

        if (count >= 3) {
            throw new BusinessException("Max 3 pending orders allowed");
        }

        if (request.getSide() == OrderSide.SELL &&
                !portfolioService.hasEnoughShares(
                        request.getTraderId(),
                        request.getStock(),
                        request.getQuantity())) {

            throw new BusinessException("Insufficient shares");
        }

        Order order = new Order();
        order.setTraderId(request.getTraderId());
        order.setStock(request.getStock());
        order.setSector(request.getSector());
        order.setQuantity(request.getQuantity());
        order.setSide(request.getSide());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void fillOrder(Long orderId) {

        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Only pending orders can be filled");
        }

        if (order.getSide() == OrderSide.BUY) {
            portfolioService.addStock(
                    order.getTraderId(),
                    order.getStock(),
                    order.getSector(),
                    order.getQuantity());
        } else {
            portfolioService.removeStock(
                    order.getTraderId(),
                    order.getStock(),
                    order.getQuantity());
        }

        order.setStatus(OrderStatus.FILLED);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Only pending orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}