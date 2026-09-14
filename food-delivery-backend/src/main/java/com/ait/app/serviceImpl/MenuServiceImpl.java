package com.ait.app.serviceImpl;


import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.ait.app.dto.MenuItemDto;
import com.ait.app.exception.MenuItemServiceException;
import com.ait.app.exception.MenuServiceException;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.MenuService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
@Service
public class MenuServiceImpl implements MenuService {
	
    @Autowired
    private MenuItemRepository  menuItemRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @PersistenceContext 
    EntityManager entityManager;
    

	@Override
	public MenuItem addMenuItem(MenuItemDto dto) {
		 if (dto == null) {
	            throw new MenuServiceException(HttpStatus.BAD_REQUEST,"Menu item details cannot be null");
	        }

	        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
	        		throw new MenuServiceException( HttpStatus.BAD_REQUEST,"Food name cannot be empty");
	        }

	        if (dto.getType() == null || dto.getType().trim().isEmpty()) {
	            throw new MenuServiceException( HttpStatus.BAD_REQUEST,"Food type cannot be empty");
	        }

	        if (dto.getDescription() == null|| dto.getDescription().trim().isEmpty()) {
	        		throw new MenuServiceException( HttpStatus.BAD_REQUEST,"Description cannot be empty");
	        }

	        if (dto.getRestaurantId() <= 0) {
	            throw new MenuServiceException(HttpStatus.BAD_REQUEST,"Restaurant id must be greater than 0");
	        }

	        if (dto.getHalfPrice() <= 0) {
	            throw new MenuServiceException(HttpStatus.BAD_REQUEST,"Half price must be greater than 0");
	        }

	        if (dto.getFullPrice() <= 0) {
	            throw new MenuServiceException(HttpStatus.BAD_REQUEST,"Full price must be greater than 0");
	        }

	        Restaurant restaurant = restaurantRepository.findById((long) dto.getRestaurantId()).orElse(null);

	        if (restaurant == null) {
	            throw new MenuServiceException(HttpStatus.NOT_FOUND,"Restaurant not found with id: "+ dto.getRestaurantId());
	        }

	        Optional<MenuItem> existingItem =
	                menuItemRepository.findByRestaurantIdAndName(dto.getRestaurantId(),dto.getName().trim());

	        if (existingItem.isPresent()) {
	            throw new MenuServiceException(HttpStatus.CONFLICT,
	                    "Food item already exists in this restaurant"
	            );
	        }

	        MenuItem item = new MenuItem();

	        item.setName(dto.getName().trim());
	        item.setDescription(dto.getDescription().trim());
	        item.setType(dto.getType().trim());
	        item.setHalfPrice(dto.getHalfPrice());
	        item.setFullPrice(dto.getFullPrice());
	        item.setAvailable(dto.isAvailable());
	        item.setRestaurant(restaurant);

	      return   menuItemRepository.save(item);
	         
	    }

//	@Transactional
//	@Override
//	public String updateMenuItemById(long restaurantId, long itemId, String field, String value) {
//		String query = "UPDATE menu_items SET " + field + " = :value WHERE id = :itemId "
//				+ "AND restaurant_id = :restaurantId";
//
//		int result = entityManager.createNativeQuery(query).setParameter("value", value).setParameter("itemId", itemId)
//				.setParameter("restaurantId", restaurantId).executeUpdate();
//
//		if (result == 0) {
//			throw new MenuItemServiceException(HttpStatus.NOT_FOUND, "Menu item not exist");
//		}
//
//		return "Menu item updated successfully";
//	}

}
