package com.ait.app.controller;

import com.ait.app.dto.CreateRestaurantRequest;
import com.ait.app.dto.RestaurantResponse;
import com.ait.app.service.RestaurantService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;

	@Autowired
	public RestaurantController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@PostMapping
	public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
		restaurantService.createRestaurant(request);
		return new ResponseEntity("Restuarant Details Save Successfully!", HttpStatus.CREATED);
	}

	@GetMapping("getRestaurant/{id}")
	public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable
			Long id) {
		RestaurantResponse response =
                restaurantService.getRestaurant(id);

		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@GetMapping("getAllRestaurant")
	public ResponseEntity<List<RestaurantResponse>> getAllRestaurant() {
		List<RestaurantResponse> response=	restaurantService.getAllRestaurant();
		return new ResponseEntity(response, HttpStatus.OK);
	}
}