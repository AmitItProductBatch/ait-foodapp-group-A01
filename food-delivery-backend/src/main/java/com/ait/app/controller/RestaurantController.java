package com.ait.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.RestaurantRequest;
import com.ait.app.dto.RestaurantResponse;
import com.ait.app.service.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;

	public RestaurantController(RestaurantService restaurantService) {

		this.restaurantService = restaurantService;
	}

	@PostMapping
	public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest request) {

		RestaurantResponse response = restaurantService.createRestaurant(request);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}