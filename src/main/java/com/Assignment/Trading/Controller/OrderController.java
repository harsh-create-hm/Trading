package com.Assignment.Trading.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Assignment.Trading.DTO.OrderRequest;
import com.Assignment.Trading.ServiceImpl.OrderServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private  OrderServiceImpl orderServiceImpl;
	
	@PostMapping
	public String placeOrder(@Valid @RequestBody OrderRequest orderRequest)
	{
		orderServiceImpl.placeOrder(orderRequest);
		return("Order placed successfully");
	}
	
	@PostMapping("/{orderId}/fill")
	public String fillOrder(@PathVariable Long orderId) {
		orderServiceImpl.fillOrder(orderId);
		return "Order Filled Succesfully";
	}
	
	@PostMapping("/{orderId}/cancel")
	public String cancelOrder(@PathVariable Long orderId)
	{
		orderServiceImpl.cancelOrder(orderId);
		return "Order Cancelled Successfully";
		
	}
	
	
	
	
	
	

}
