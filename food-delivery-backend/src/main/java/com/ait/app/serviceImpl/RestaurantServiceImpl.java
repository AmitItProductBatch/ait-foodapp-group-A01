package com.ait.app.serviceImpl;

import com.ait.app.dto.CreateRestaurantRequest;
import com.ait.app.dto.RestaurantResponse;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        try {
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
                    "Failed to create restaurant: " + ex.getMessage());
        }
    }
}