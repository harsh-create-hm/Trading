package com.Assignment.Trading.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Tables;

import com.Assignment.Trading.Enums.OrderSide;
import com.Assignment.Trading.Enums.OrderStatus;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name="orders")
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", traderId=" + traderId + ", stock=" + stock + ", sector=" + sector + ", quantity="
				+ quantity + ", side=" + side + ", status=" + status + ", createdAt=" + createdAt + "]";
	}	
	
	
}
