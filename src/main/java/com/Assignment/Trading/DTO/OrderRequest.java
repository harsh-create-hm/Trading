package com.Assignment.Trading.DTO;

import com.Assignment.Trading.Enums.OrderSide;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
	
	@NotBlank
	private String traderId;
	
	@NotBlank
	private String stock;
	
	@NotBlank
	private String sector;
	
	@Min(1)
	private int quantity;
	
	@NotNull
	private OrderSide side;

}
