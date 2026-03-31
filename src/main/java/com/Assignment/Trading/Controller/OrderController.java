package com.Assignment.Trading.Controller;

import java.rmi.server.LogStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Assignment.Trading.DTO.OrderRequest;
import com.Assignment.Trading.Service.OrderService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private  OrderService orderService;
   
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@PostMapping
	public String placeOrder(@Valid @RequestBody OrderRequest orderRequest)
	{
		orderService.placeOrder(orderRequest);
		logger.info("Order palced for "+ orderRequest.getTraderId());
		return("Order placed successfully");
	}
	
	@PostMapping("/{orderId}/fill")
	public String fillOrder(@PathVariable Long orderId) {
		orderService.fillOrder(orderId);
		logger.info("Order got Filled");
		return "Order Filled Succesfully";
	}
	
	@PostMapping("/{orderId}/cancel")
	public String cancelOrder(@PathVariable Long orderId)
	{
		orderService.cancelOrder(orderId);
		logger.info("Order got Cancelled Successfully ");
		return "Order Cancelled Successfully";
		
	}
	
	
	
	
	
	

}
