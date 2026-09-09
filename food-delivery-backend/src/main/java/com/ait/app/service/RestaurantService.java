package com.ait.app.service;

import com.ait.app.dto.CreateRestaurantRequest;
import com.ait.app.dto.RestaurantResponse;

public interface RestaurantService {
    RestaurantResponse createRestaurant(CreateRestaurantRequest request);
}