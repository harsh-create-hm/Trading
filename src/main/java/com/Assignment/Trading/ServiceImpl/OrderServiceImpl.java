package com.Assignment.Trading.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.Assignment.Trading.DTO.OrderRequest;
import com.Assignment.Trading.Service.OrderService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class OrderServiceImpl implements OrderService {

	
	@Override
	@Transactional
	public void placeOrder( OrderRequest request) {
	
	}

	@Override
	public void fillOrder(Long orderId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelOrder(Long orderId) {
		// TODO Auto-generated method stub
		
	}

}
