package com.Assignment.Trading.DTO;

import com.Assignment.Trading.Enums.OrderSide;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


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


	public String getTraderId() {
		return traderId;
	}

	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}
	
	
	public OrderRequest(@NotBlank String traderId, @NotBlank String stock, @NotBlank String sector,
			@Min(1) int quantity, @NotNull OrderSide side) {
		super();
		this.traderId = traderId;
		this.stock = stock;
		this.sector = sector;
		this.quantity = quantity;
		this.side = side;
	}

	@Override
	public String toString() {
		return "OrderRequest [traderId=" + traderId + ", stock=" + stock + ", sector=" + sector + ", quantity="
				+ quantity + ", side=" + side + "]";
	}

}
