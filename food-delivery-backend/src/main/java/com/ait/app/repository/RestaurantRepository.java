package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Restaurant;

public interface RestaurantRepository
        extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByName(String name);
}