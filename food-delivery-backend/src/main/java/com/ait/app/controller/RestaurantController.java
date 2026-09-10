package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.RestaurantDto;
import com.ait.app.dto.RestaurantResponseDto;
import com.ait.app.dto.UpdateRestaurantDto;
import com.ait.app.model.Restaurant;
import com.ait.app.service.RestaurantService;

@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {

	@Autowired
	RestaurantService restaurantService;

	@PostMapping
	public ResponseEntity createRestaurant(@RequestBody RestaurantDto dto) {

		RestaurantResponseDto created = restaurantService.createRestaurant(dto);
		return new ResponseEntity(created, HttpStatus.CREATED);

	}

	@GetMapping("/{id}")
	public ResponseEntity getRestaurantDetails(@PathVariable int id) {

		RestaurantResponseDto responseDto = restaurantService.getRestaurantDetails(id);
		return new ResponseEntity(responseDto, HttpStatus.OK);

	}

	@GetMapping
	public ResponseEntity getAllRestaurants() {

		List<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants();
		return new ResponseEntity(restaurants, HttpStatus.OK);

	}

	@PatchMapping("/{id}")
	public ResponseEntity updateRestaurantById(@PathVariable int id, @RequestBody UpdateRestaurantDto dto) {
		Restaurant updatedRestaurant = restaurantService.updateRestaurantById(id, dto);
		return new ResponseEntity(updatedRestaurant, HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteRestaurant(@PathVariable int id) {
		restaurantService.deleteRestaurantById(id);
		return new ResponseEntity("Restaurant Deleted successfully", HttpStatus.OK);
	}

}