package com.ait.app.serviceImpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.RestaurantRequest;
import com.ait.app.dto.RestaurantResponse;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Restaurant;
import com.ait.app.model.RestaurantStatus;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger log =
            LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository) {

        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantResponse createRestaurant(
            RestaurantRequest request) {

        log.info(
                "Starting restaurant creation. Restaurant name: {}",
                request.getName()
        );

        // Check duplicate restaurant
        Optional<Restaurant> existingRestaurant =
                restaurantRepository.findByName(request.getName());

        if (existingRestaurant.isPresent()) {

            log.warn(
                    "Restaurant already exists. Restaurant name: {}",
                    request.getName()
            );

            throw new RestaurantServiceException(
                    HttpStatus.CONFLICT,
                    "Restaurant already exists with name: "
                            + request.getName()
            );
        }

        // Create Restaurant entity
        Restaurant restaurant = new Restaurant();

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setContactNumber(request.getContactNumber());
        restaurant.setEmail(request.getEmail());

        // Default status
        restaurant.setStatus(RestaurantStatus.PENDING);

        // Save restaurant
        Restaurant savedRestaurant =
                restaurantRepository.save(restaurant);

        log.info(
                "Restaurant created successfully. Restaurant ID: {}",
                savedRestaurant.getId()
        );

        // Convert Entity -> Response DTO
        return new RestaurantResponse(
                savedRestaurant.getId(),
                savedRestaurant.getName(),
                savedRestaurant.getAddress(),
                savedRestaurant.getCuisine(),
                savedRestaurant.getContactNumber(),
                savedRestaurant.getEmail(),
                savedRestaurant.getStatus()
        );
    }
}