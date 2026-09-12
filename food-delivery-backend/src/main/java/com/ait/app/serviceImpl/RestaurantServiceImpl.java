package com.ait.app.serviceImpl;

import com.ait.app.dto.CreateRestaurantRequest;
import com.ait.app.dto.RestaurantResponse;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.RestaurantService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Override
	public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
		try {
			
			if (restaurantRepository.existsByEmail(request.getEmail())) {
			    throw new RestaurantServiceException(
			            HttpStatus.CONFLICT,
			            "Email already exists"
			    );
			}

			if (restaurantRepository.existsByContactDetails(
			        request.getContactDetails())) {

			    throw new RestaurantServiceException(
			            HttpStatus.CONFLICT,
			            "Contact details already exists"
			    );
			}
			Restaurant restaurant = new Restaurant();
			restaurant.setName(request.getName());
			restaurant.setAddress(request.getAddress());
			restaurant.setCountry(request.getCountry());
			restaurant.setContactDetails(request.getContactDetails());
			restaurant.setStatus("PENDING");

			Restaurant savedRestaurant = restaurantRepository.save(restaurant);

			RestaurantResponse response = new RestaurantResponse();
			response.setId(savedRestaurant.getId());
			response.setName(savedRestaurant.getName());
			response.setAddress(savedRestaurant.getAddress());
			response.setCountry(savedRestaurant.getCountry());
			response.setContactDetails(savedRestaurant.getContactDetails());
			response.setStatus(savedRestaurant.getStatus());

			return response;
		} catch (Exception ex) {
			// Throw RestaurantServiceException instead of RestaurantServiceExceptionHandler
			throw new RestaurantServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Failed to create Restaurant: " + ex.getMessage());
		}
	}

	@Override
	public RestaurantResponse getRestaurant(Long id) {
		Optional<Restaurant> o = restaurantRepository.findById(id);
		if (o.isEmpty()) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Please Enter Valid Id");
		}
		Restaurant restaurant = o.get();
		RestaurantResponse restaurantResponse = new RestaurantResponse();
		restaurantResponse.setId(restaurant.getId());
		restaurantResponse.setName(restaurant.getName());
		restaurantResponse.setContactDetails(restaurant.getContactDetails());
		restaurantResponse.setEmail(restaurant.getEmail());
		restaurantResponse.setAddress(restaurant.getAddress());
		restaurantResponse.setStatus(restaurant.getStatus());
		restaurantResponse.setCountry(restaurant.getCountry());
		return restaurantResponse;
	}

	@Override
	public List<RestaurantResponse> getAllRestaurant() {
		List<Restaurant> r = restaurantRepository.findAll();
		if (r.isEmpty()) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Please Give Proper ID");
		}
		List<RestaurantResponse> responses = new ArrayList();

		for (Restaurant restaurant : r) {
			RestaurantResponse restaurantResponse = new RestaurantResponse();
			restaurantResponse.setId(restaurant.getId());
			restaurantResponse.setName(restaurant.getName());
			restaurantResponse.setContactDetails(restaurant.getContactDetails());
			restaurantResponse.setEmail(restaurant.getEmail());
			restaurantResponse.setAddress(restaurant.getAddress());
			restaurantResponse.setStatus(restaurant.getStatus());
			restaurantResponse.setCountry(restaurant.getCountry());

			responses.add(restaurantResponse);

		}
		return responses;
	}
}