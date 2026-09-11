package com.ait.app.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{
	 Optional<MenuItem> findByRestaurantIdAndName(
	            Long restaurantId,String name
	    );

}
