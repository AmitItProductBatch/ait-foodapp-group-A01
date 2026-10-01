package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long>{
	
	Optional<Rating> findByOrderOrderId(int orderId);

}
