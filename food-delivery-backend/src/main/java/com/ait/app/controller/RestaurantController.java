package com.ait.app.controller;

import com.ait.app.dto.RestaurantRequest;
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
	@Autowired
	private RestaurantService restaurantService;

	@PostMapping
	public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
		restaurantService.createRestaurant(request);
		return new ResponseEntity("Restuarant Details Save Successfully!", HttpStatus.CREATED);
	}

	@GetMapping("getRestaurant/{id}")
	public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long id) {
		RestaurantResponse response = restaurantService.getRestaurant(id);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("getAllRestaurant")
	public ResponseEntity<List<RestaurantResponse>> getAllRestaurant() {
		List<RestaurantResponse> response = restaurantService.getAllRestaurant();
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@PatchMapping("/update/{field}/{value}/{id}")
	public ResponseEntity<String> updateRestaurant(@PathVariable String field, @PathVariable String value,
			@PathVariable long id) {
       String response = restaurantService.updateById(field, value, id);

		return new ResponseEntity(response, HttpStatus.OK);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
	    restaurantService.deleteById(id);
	    return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);
	}
}