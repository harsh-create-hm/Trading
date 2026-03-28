package com.Assignment.Trading.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String traderId;

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

	public Portfolio() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Portfolio(Long id, String traderId) {
		super();
		this.id = id;
		this.traderId = traderId;
	}

	@Override
	public String toString() {
		return "Portfolio [id=" + id + ", traderId=" + traderId + "]";
	}
    
    
}