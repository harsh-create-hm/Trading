package com.Assignment.Trading.Service;

import com.Assignment.Trading.DTO.OrderRequest;

public interface OrderService {
	
	 void placeOrder(OrderRequest request);
	 
	 void fillOrder(Long orderId);
	 
	 void cancelOrder(Long orderId);
	

}
