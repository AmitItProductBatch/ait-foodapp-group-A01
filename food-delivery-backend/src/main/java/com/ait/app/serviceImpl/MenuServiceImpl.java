package com.ait.app.serviceImpl;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.MenuItemDto;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.MenuService;
@Service
public class MenuServiceImpl implements MenuService {
	
    @Autowired
    private MenuItemRepository  menuItemRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    

	@Override
	public Long addMenuItem(Long restaurantId, MenuItemDto dto) {
		 Optional<Restaurant> optionalRestaurant =
	                restaurantRepository.findById(restaurantId);

	        if (optionalRestaurant.isEmpty()) {
	            throw new UserServiceException(HttpStatus.NOT_FOUND,"Restaurant not found for id: " + restaurantId);
	        }
	        if (dto.getName() == null || dto.getName().isBlank()) {
	            throw new UserServiceException( HttpStatus.BAD_REQUEST,"Menu item name is required");
	        }
	        
	        if (dto.getHalfPrice() <= 0 || dto.getFullPrice() <= 0) {
	            throw new UserServiceException( HttpStatus.BAD_REQUEST,"Half price and full price must be greater than 0");
	        }
	        
	        Optional<MenuItem> existingItem = menuItemRepository.findByRestaurantIdAndName(restaurantId,dto.getName());

	        if (existingItem.isPresent()) {
	            throw new UserServiceException(HttpStatus.CONFLICT,"Menu item already exists in this restaurant");
	        }
	        
	        Restaurant restaurant = optionalRestaurant.get();

	        MenuItem menuItem = new MenuItem();

	        menuItem.setName(dto.getName());
	        menuItem.setDescription(dto.getDescription());
	        menuItem.setFullPrice(dto.getFullPrice());
	        menuItem.setHalfPrice(dto.getHalfPrice());
	        menuItem.setAvailable(dto.isAvailable());
	        menuItem.setType(dto.getType());
	        menuItem.setRestaurant(restaurant);
	        
	        MenuItem savedItem = menuItemRepository.save(menuItem);

	        return savedItem.getId();

	}

}
