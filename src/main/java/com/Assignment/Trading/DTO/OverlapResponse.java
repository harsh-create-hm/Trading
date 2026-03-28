package com.Assignment.Trading.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public class OverlapResponse {

	private List<BasketOverlap> overlaps;
	private String dominantBasket;
	private String riskFlag;

	public List<BasketOverlap> getOverlaps() {
		return overlaps;
	}

	public void setOverlaps(List<BasketOverlap> overlaps) {
		this.overlaps = overlaps;
	}

	public String getDominantBasket() {
		return dominantBasket;
	}

	public void setDominantBasket(String dominantBasket) {
		this.dominantBasket = dominantBasket;
	}

	public String getRiskFlag() {
		return riskFlag;
	}

	public void setRiskFlag(String riskFlag) {
		this.riskFlag = riskFlag;
	}

	public OverlapResponse(List<BasketOverlap> overlaps, String dominantBasket, String riskFlag) {
		super();
		this.overlaps = overlaps;
		this.dominantBasket = dominantBasket;
		this.riskFlag = riskFlag;
	}

	@Override
	public String toString() {
		return "OverlapResponse [overlaps=" + overlaps + ", dominantBasket=" + dominantBasket + ", riskFlag=" + riskFlag
				+ "]";
	}

}