package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ait.app.model.Category;

@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	 Optional<Category> findByIdAndRestaurant_Id(int id, int restaurantId);

}
