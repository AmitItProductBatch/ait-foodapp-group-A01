package com.ait.app.serviceImpl;

import com.ait.app.dto.RestaurantRequest;
import org.springframework.transaction.annotation.Transactional;
import com.ait.app.dto.RestaurantResponse;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Restaurant;
import com.ait.app.model.Users;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.RestaurantService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public RestaurantResponse createRestaurant(RestaurantRequest request) {

		if (restaurantRepository.existsByEmail(request.getEmail())) {
			throw new RestaurantServiceException(HttpStatus.CONFLICT, "Email already exists");
		}

		if (restaurantRepository.existsByContactDetails(request.getContactDetails())) {
			throw new RestaurantServiceException(HttpStatus.CONFLICT, "Contact details already exists");
		}

		Users user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RestaurantServiceException(HttpStatus.NOT_FOUND, "User not found"));

		Restaurant restaurant = new Restaurant();

		restaurant.setName(request.getName());
		restaurant.setAddress(request.getAddress());
		restaurant.setCountry(request.getCountry());
		restaurant.setEmail(request.getEmail());
		restaurant.setContactDetails(request.getContactDetails());
		restaurant.setStatus("PENDING");
		restaurant.setUser(user);

		Restaurant savedRestaurant = restaurantRepository.save(restaurant);

		RestaurantResponse response = new RestaurantResponse();

		response.setId(savedRestaurant.getId());
		response.setName(savedRestaurant.getName());
		response.setAddress(savedRestaurant.getAddress());
		response.setCountry(savedRestaurant.getCountry());
		response.setEmail(savedRestaurant.getEmail());
		response.setContactDetails(savedRestaurant.getContactDetails());
		response.setStatus(savedRestaurant.getStatus());

		return response;
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
		restaurantResponse.setUserId(restaurant.getUser().getUserId());
		if (restaurant.getUser() != null) {
			restaurantResponse.setUserId(restaurant.getUser().getUserId());
		}

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
			restaurantResponse.setUserId(restaurant.getUser().getUserId());

			responses.add(restaurantResponse);

		}
		return responses;
	}

	@Transactional
	@Override
	public String updateById(String field, String value, long id) {
		String query = "UPDATE restaurants SET " + field + " = :value WHERE id = :id";

		int result = entityManager.createNativeQuery(query).setParameter("value", value).setParameter("id", id)
				.executeUpdate();
		if (result == 0) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Restaurant not exist");

		}

		return "Restaurant updated successfully";
	}

	@Override
	public void deleteById(Long id) {
		Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
				() -> new RestaurantServiceException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id));

		restaurantRepository.delete(restaurant);

	}

}