package com.Assignment.Trading.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

public class BasketOverlap {
 
	private String basket;
    private String overlap;
    
    public String getBasket() {
 		return basket;
 	}
 	public void setBasket(String basket) {
 		this.basket = basket;
 	}
 	public String getOverlap() {
 		return overlap;
 	}
 	public void setOverlap(String overlap) {
 		this.overlap = overlap;
 	}
 	
 	
	public BasketOverlap(String basket, String overlap) {
		super();
		this.basket = basket;
		this.overlap = overlap;
	}
	@Override
	public String toString() {
		return "BasketOverlap [basket=" + basket + ", overlap=" + overlap + "]";
	}
 	
 	
}