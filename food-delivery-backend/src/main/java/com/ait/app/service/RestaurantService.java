package com.ait.app.service;

import com.ait.app.dto.RestaurantRequest;
import com.ait.app.dto.RestaurantResponse;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request);
}