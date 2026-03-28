package com.Assignment.Trading.Controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.Assignment.Trading.DTO.OrderRequest;
import com.Assignment.Trading.Enums.OrderSide;
import com.Assignment.Trading.Exception.BusinessException;
import com.Assignment.Trading.ServiceImpl.OrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @Mock
    private OrderServiceImpl orderServiceImpl;

    @InjectMocks
    private OrderController orderController;

    private OrderRequest orderRequest;

    @Before
    public void setUp() {
        orderRequest = new OrderRequest("TRADER1", "AAPL", "TECH", 100, OrderSide.BUY);
    }

    @Test
    public void testPlaceOrderSuccess() {
        String result = orderController.placeOrder(orderRequest);

        assertEquals("Order placed successfully", result);
        verify(orderServiceImpl, times(1)).placeOrder(orderRequest);
    }

    @Test(expected = BusinessException.class)
    public void testPlaceOrderFailure() {
        doThrow(new BusinessException("Max 3 pending orders allowed")).when(orderServiceImpl).placeOrder(orderRequest);

        orderController.placeOrder(orderRequest);
    }

    @Test
    public void testFillOrderSuccess() {
        String result = orderController.fillOrder(1L);

        assertEquals("Order Filled Succesfully", result);
        verify(orderServiceImpl, times(1)).fillOrder(1L);
    }

    @Test
    public void testCancelOrderSuccess() {
        String result = orderController.cancelOrder(1L);

        assertEquals("Order Cancelled Successfully", result);
        verify(orderServiceImpl, times(1)).cancelOrder(1L);
    }

    @Test(expected = BusinessException.class)
    public void testCancelOrderFailure() {
        doThrow(new BusinessException("Order not found")).when(orderServiceImpl).cancelOrder(999L);

        orderController.cancelOrder(999L);
    }
}