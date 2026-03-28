package com.Assignment.Trading.Service;

import java.util.Set;

import com.Assignment.Trading.DTO.OverlapResponse;


public interface OverlapService {
	public OverlapResponse calculateOverlap(Set<String> portfolio);

}
