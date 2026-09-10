package com.ait.app.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.app.dto.RestaurantDto;
import com.ait.app.dto.RestaurantResponseDto;
import com.ait.app.dto.UpdateRestaurantDto;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.RestaurantService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RestaurantResponseDto createRestaurant(RestaurantDto dto) {

		if (dto.getName() == null || dto.getName().isBlank()) {
			throw new RestaurantServiceException(HttpStatus.BAD_REQUEST, "Restaurant name is required");
		}

		if (dto.getAddress() == null || dto.getAddress().isBlank()) {
			throw new RestaurantServiceException(HttpStatus.BAD_REQUEST, "Address is required");
		}

		if (dto.getCuisineType() == null || dto.getCuisineType().isBlank()) {
			throw new RestaurantServiceException(HttpStatus.BAD_REQUEST, "Cuisine type is required");
		}

		boolean alreadyExists = restaurantRepository.existsByNameAndAddress(dto.getName(), dto.getAddress());

		if (alreadyExists) {
			throw new RestaurantServiceException(HttpStatus.CONFLICT,
					"Restaurant already exists at this name and address");
		}

		Restaurant restaurant = new Restaurant();

		restaurant.setName(dto.getName());
		restaurant.setAddress(dto.getAddress());
		restaurant.setCuisineType(dto.getCuisineType());
		restaurant.setPhone(dto.getPhone());
		restaurant.setEmail(dto.getEmail());
		restaurant.setStatus("PENDING"); // default status until admin approves

		Restaurant savedRestaurant = restaurantRepository.save(restaurant);

		RestaurantResponseDto response = new RestaurantResponseDto();
		response.setRestaurantId(savedRestaurant.getRestaurantId());
		response.setName(savedRestaurant.getName());
		response.setAddress(savedRestaurant.getAddress());
		response.setCuisineType(savedRestaurant.getCuisineType());
		response.setPhone(savedRestaurant.getPhone());
		response.setEmail(savedRestaurant.getEmail());
		response.setStatus(savedRestaurant.getStatus());

		return response;
	}

	@Override
	public RestaurantResponseDto getRestaurantDetails(int id) {

		Optional<Restaurant> o = restaurantRepository.findById(id);

		if (o.isEmpty()) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Please Enter Valid Restaurant ID!");
		}

		Restaurant restaurant = o.get();

		RestaurantResponseDto responseDto = new RestaurantResponseDto();

		responseDto.setRestaurantId(restaurant.getRestaurantId());
		responseDto.setName(restaurant.getName());
		responseDto.setAddress(restaurant.getAddress());
		responseDto.setCuisineType(restaurant.getCuisineType());
		responseDto.setPhone(restaurant.getPhone());
		responseDto.setEmail(restaurant.getEmail());
		responseDto.setStatus(restaurant.getStatus());

		return responseDto;
	}

	@Override
	public List<RestaurantResponseDto> getAllRestaurants() {

		return restaurantRepository.findAll().stream().map(restaurant -> {
			RestaurantResponseDto dto = new RestaurantResponseDto();
			dto.setRestaurantId(restaurant.getRestaurantId());
			dto.setName(restaurant.getName());
			dto.setAddress(restaurant.getAddress());
			dto.setCuisineType(restaurant.getCuisineType());
			dto.setPhone(restaurant.getPhone());
			dto.setEmail(restaurant.getEmail());
			dto.setStatus(restaurant.getStatus());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Restaurant updateRestaurantById(int id, UpdateRestaurantDto dto) {

		Optional<Restaurant> optional = restaurantRepository.findById(id);

		if (optional.isEmpty()) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Restaurant is not found for id " + id);
		}

		StringBuilder sql = new StringBuilder("UPDATE food_delivery_restaurants SET ");
		boolean hasUpdate = false;

		if (dto.getName() != null && !dto.getName().isBlank()) {
			sql.append("name = :name, ");
			hasUpdate = true;
		}

		if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
			sql.append("address = :address, ");
			hasUpdate = true;
		}

		if (dto.getCuisineType() != null && !dto.getCuisineType().isBlank()) {
			sql.append("cuisine_type = :cuisineType, ");
			hasUpdate = true;
		}

		if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
			sql.append("phone = :phone, ");
			hasUpdate = true;
		}

		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			sql.append("email = :email, ");
			hasUpdate = true;
		}

		if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
			sql.append("status = :status, ");
			hasUpdate = true;
		}

		if (!hasUpdate) {
			return optional.get();
		}

		sql.setLength(sql.length() - 2);

		sql.append(" WHERE restaurant_id = :id");

		Query query = entityManager.createNativeQuery(sql.toString());

		query.setParameter("id", id);

		if (dto.getName() != null && !dto.getName().isBlank()) {
			query.setParameter("name", dto.getName());
		}

		if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
			query.setParameter("address", dto.getAddress());
		}

		if (dto.getCuisineType() != null && !dto.getCuisineType().isBlank()) {
			query.setParameter("cuisineType", dto.getCuisineType());
		}

		if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
			query.setParameter("phone", dto.getPhone());
		}

		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			query.setParameter("email", dto.getEmail());
		}

		if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
			query.setParameter("status", dto.getStatus());
		}

		query.executeUpdate();

		entityManager.clear();

		return entityManager.find(Restaurant.class, id);
	}

	@Override
	public void deleteRestaurantById(int id) {
		Optional<Restaurant> optional = restaurantRepository.findById(id);

		if (optional.isEmpty()) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Restaurant is not found for id: " + id);
		}

		restaurantRepository.deleteById(id);
	}
}