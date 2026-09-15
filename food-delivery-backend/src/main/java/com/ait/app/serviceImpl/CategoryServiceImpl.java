package com.ait.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.CategoryDto;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Category;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.CategoryRepository;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.CategoryService;

@Service
public class CategoryServiceImpl  implements CategoryService{
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	RestaurantRepository restaurantRepository;

	@Override
	public void createCategory(int rid, CategoryDto dto) {
		Optional<Restaurant> optional = restaurantRepository.findById((long) rid);
		
		if(optional.isEmpty()) {
			throw new RestaurantServiceException( HttpStatus.NOT_FOUND,"Restaurent is not found for id :"+rid);
		}
		
		Restaurant restaurant =optional.get();
		
		Category category = new Category();
		
		category.setCategoryName(dto.getCategoryName());
		category.setType(dto.getType());
		
		category.setRestaurant(restaurant);
		
		categoryRepository.save(category);
		
	}


}
