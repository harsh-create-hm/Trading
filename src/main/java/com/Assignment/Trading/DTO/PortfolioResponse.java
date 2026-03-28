package com.Assignment.Trading.DTO;

import java.util.Map;

public class PortfolioResponse {
    private String traderId;
    private Map<String, Integer> positions;
    private Map<String, Integer> sectorBreakdown;
	public String getTraderId() {
		return traderId;
	}
	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}
	public Map<String, Integer> getPositions() {
		return positions;
	}
	public void setPositions(Map<String, Integer> positions) {
		this.positions = positions;
	}
	public Map<String, Integer> getSectorBreakdown() {
		return sectorBreakdown;
	}
	public void setSectorBreakdown(Map<String, Integer> sectorBreakdown) {
		this.sectorBreakdown = sectorBreakdown;
	}
	public PortfolioResponse(String traderId, Map<String, Integer> positions, Map<String, Integer> sectorBreakdown) {
		super();
		this.traderId = traderId;
		this.positions = positions;
		this.sectorBreakdown = sectorBreakdown;
	}
	@Override
	public String toString() {
		return "PortfolioResponse [traderId=" + traderId + ", positions=" + positions + ", sectorBreakdown="
				+ sectorBreakdown + "]";
	}
	
    
}