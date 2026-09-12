package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.CreateRestaurantRequest;
import com.ait.app.dto.RestaurantResponse;

public interface RestaurantService {
	RestaurantResponse createRestaurant(CreateRestaurantRequest request);
	
	 RestaurantResponse getRestaurant(Long id);  
	 
	 List<RestaurantResponse> getAllRestaurant();
}