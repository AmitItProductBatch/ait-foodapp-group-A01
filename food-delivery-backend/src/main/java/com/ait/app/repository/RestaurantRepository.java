package com.ait.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ait.app.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	
	  boolean existsByEmail(String email);

	    boolean existsByContactDetails(String contactDetails);
}