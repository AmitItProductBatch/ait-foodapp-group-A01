package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.RestaurantRequest;
import com.ait.app.dto.RestaurantResponse;

public interface RestaurantService {
	RestaurantResponse createRestaurant(RestaurantRequest request);
	
	 RestaurantResponse getRestaurant(Long id);  
	 
	 List<RestaurantResponse> getAllRestaurant();
	 
	 public String updateById(String field, String value, long id);
	 
	 void deleteById(Long id);
}