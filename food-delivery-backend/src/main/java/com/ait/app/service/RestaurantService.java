package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.RestaurantDto;
import com.ait.app.dto.RestaurantResponseDto;
import com.ait.app.dto.UpdateRestaurantDto;
import com.ait.app.model.Restaurant;

public interface RestaurantService {

	// Story: create restaurant, default status PENDING //
	public RestaurantResponseDto createRestaurant(RestaurantDto dto);

	public RestaurantResponseDto getRestaurantDetails(int id);

	public List<RestaurantResponseDto> getAllRestaurants();

	public Restaurant updateRestaurantById(int id, UpdateRestaurantDto dto);

	public void deleteRestaurantById(int id);

}