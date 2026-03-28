package com.Assignment.Trading.ServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.Assignment.Trading.DTO.OrderRequest;
import com.Assignment.Trading.Entity.Order;
import com.Assignment.Trading.Enums.OrderSide;
import com.Assignment.Trading.Enums.OrderStatus;
import com.Assignment.Trading.Exception.BusinessException;
import com.Assignment.Trading.Repository.OrderRepository;
import com.Assignment.Trading.Service.PortfolioService;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PortfolioService portfolioService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest orderRequest;
    private Order order;

    @Before
    public void setUp() {
        orderRequest = new OrderRequest("TRADER1", "AAPL", "TECH", 100, OrderSide.BUY);
        order = new Order();
        order.setId(1L);
        order.setTraderId("TRADER1");
        order.setStock("AAPL");
        order.setSector("TECH");
        order.setQuantity(100);
        order.setSide(OrderSide.BUY);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
    }


    @Test
    public void testPlaceOrderSuccess_Buy() {
        when(orderRepository.countByTraderIdAndStatus("TRADER1", OrderStatus.PENDING)).thenReturn(0L);

        orderService.placeOrder(orderRequest);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testPlaceOrderSuccess_Sell_WithEnoughShares() {
        OrderRequest sellRequest = new OrderRequest("TRADER1", "AAPL", "TECH", 50, OrderSide.SELL);
        when(orderRepository.countByTraderIdAndStatus("TRADER1", OrderStatus.PENDING)).thenReturn(0L);
        when(portfolioService.hasEnoughShares("TRADER1", "AAPL", 50)).thenReturn(true);

        orderService.placeOrder(sellRequest);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test(expected = BusinessException.class)
    public void testPlaceOrderFailure_MaxPendingOrdersExceeded() {
        when(orderRepository.countByTraderIdAndStatus("TRADER1", OrderStatus.PENDING)).thenReturn(3L);

        orderService.placeOrder(orderRequest);
    }

    @Test(expected = BusinessException.class)
    public void testPlaceOrderFailure_Sell_InsufficientShares() {
        OrderRequest sellRequest = new OrderRequest("TRADER1", "AAPL", "TECH", 100, OrderSide.SELL);
        when(orderRepository.countByTraderIdAndStatus("TRADER1", OrderStatus.PENDING)).thenReturn(0L);
        when(portfolioService.hasEnoughShares("TRADER1", "AAPL", 100)).thenReturn(false);

        orderService.placeOrder(sellRequest);
    }

    @Test
    public void testPlaceOrderCreatesOrderWithCorrectStatus() {
        when(orderRepository.countByTraderIdAndStatus("TRADER1", OrderStatus.PENDING)).thenReturn(0L);

        orderService.placeOrder(orderRequest);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        
        assertEquals(OrderStatus.PENDING, capturedOrder.getStatus());
        assertEquals("TRADER1", capturedOrder.getTraderId());
        assertEquals("AAPL", capturedOrder.getStock());
        assertEquals(100, capturedOrder.getQuantity());
    }

    @Test
    public void testPlaceOrderWithBoundaryPendingOrders() {
        when(orderRepository.countByTraderIdAndStatus("TRADER1", OrderStatus.PENDING)).thenReturn(2L);

        orderService.placeOrder(orderRequest);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test(expected = BusinessException.class)
    public void testPlaceOrderFailure_ExactlyThreePendingOrders() {
        when(orderRepository.countByTraderIdAndStatus("TRADER1", OrderStatus.PENDING)).thenReturn(3L);

        orderService.placeOrder(orderRequest);
    }


    @Test
    public void testFillOrderSuccess_BuyOrder() {
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.fillOrder(1L);

        verify(portfolioService, times(1)).addStock("TRADER1", "AAPL", "TECH", 100);
        verify(orderRepository, times(1)).save(any(Order.class));
        
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(OrderStatus.FILLED, orderCaptor.getValue().getStatus());
    }

    @Test
    public void testFillOrderSuccess_SellOrder() {
        Order sellOrder = new Order();
        sellOrder.setId(1L);
        sellOrder.setTraderId("TRADER1");
        sellOrder.setStock("AAPL");
        sellOrder.setSector("TECH");
        sellOrder.setQuantity(50);
        sellOrder.setSide(OrderSide.SELL);
        sellOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sellOrder));

        orderService.fillOrder(1L);

        verify(portfolioService, times(1)).removeStock("TRADER1", "AAPL", 50);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test(expected = BusinessException.class)
    public void testFillOrderFailure_OrderNotFound() {
        when(orderRepository.findByIdForUpdate(999L)).thenReturn(Optional.empty());

        orderService.fillOrder(999L);
    }

    @Test(expected = BusinessException.class)
    public void testFillOrderFailure_OrderNotPending_Filled() {
        order.setStatus(OrderStatus.FILLED);
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.fillOrder(1L);
    }

    @Test(expected = BusinessException.class)
    public void testFillOrderFailure_OrderNotPending_Cancelled() {
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.fillOrder(1L);
    }

    @Test
    public void testFillOrderUpdatesOrderStatusToFilled() {
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.fillOrder(1L);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(OrderStatus.FILLED, orderCaptor.getValue().getStatus());
    }


    @Test
    public void testCancelOrderSuccess() {
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        verify(orderRepository, times(1)).save(any(Order.class));
        
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(OrderStatus.CANCELLED, orderCaptor.getValue().getStatus());
    }

    @Test(expected = BusinessException.class)
    public void testCancelOrderFailure_OrderNotFound() {
        when(orderRepository.findByIdForUpdate(999L)).thenReturn(Optional.empty());

        orderService.cancelOrder(999L);
    }

    @Test(expected = BusinessException.class)
    public void testCancelOrderFailure_OrderAlreadyFilled() {
        order.setStatus(OrderStatus.FILLED);
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);
    }

    @Test(expected = BusinessException.class)
    public void testCancelOrderFailure_OrderAlreadyCancelled() {
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);
    }

    @Test
    public void testCancelOrderUpdatesStatusToCancelled() {
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(OrderStatus.CANCELLED, orderCaptor.getValue().getStatus());
    }

    @Test
    public void testCancelOrderDoesNotAffectPortfolio() {
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        verify(portfolioService, never()).addStock(anyString(), anyString(), anyString(), anyInt());
        verify(portfolioService, never()).removeStock(anyString(), anyString(), anyInt());
    }
}