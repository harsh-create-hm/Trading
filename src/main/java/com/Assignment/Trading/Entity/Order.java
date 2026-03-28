package com.Assignment.Trading.Entity;

import java.time.LocalDateTime;

import com.Assignment.Trading.Enums.OrderSide;
import com.Assignment.Trading.Enums.OrderStatus;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String traderId;
	private String stock;
	private String sector;
	private int quantity;
	@Enumerated(EnumType.STRING)
	private OrderSide side;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	private LocalDateTime createdAt;
	
}
