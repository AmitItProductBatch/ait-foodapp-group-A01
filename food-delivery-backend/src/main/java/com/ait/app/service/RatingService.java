package com.ait.app.service;

import com.ait.app.dto.RatingRequest;
import com.ait.app.dto.RatingResponse;

public interface RatingService {
	
	public RatingResponse addRating(RatingRequest request);
	

}
