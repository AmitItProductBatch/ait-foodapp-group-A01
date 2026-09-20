package com.ait.app.serviceImpl;

import com.ait.app.model.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.CategoryDto;
import com.ait.app.dto.RestaurantResponse;
import com.ait.app.exception.CategoryServiceException;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.model.Category;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.CategoryRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.CategoryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

	private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	CategoryRepository categoryRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	MenuItemRepository itemRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	@Override
	public void createCategory(int rid, CategoryDto dto) {

		log.info("Creating category for restaurantId: {}", rid);

		Optional<Restaurant> optional = restaurantRepository.findById((long) rid);

		if (optional.isEmpty()) {

			log.error("Restaurant not found with restaurantId: {}", rid);

			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Restaurent is not found for id :" + rid);
		}

		log.info("Restaurant found successfully with restaurantId: {}", rid);

		Restaurant restaurant = optional.get();

		Category category = new Category();

		category.setFoodName(dto.getFoodName());
		category.setCategory(dto.getCategory());
		category.setRestaurant(restaurant);

		log.info("Saving category for restaurantId: {}", rid);

		Category savedCategory = categoryRepository.save(category);

		if (savedCategory != null) {

			log.info("Category created successfully with categoryId: {} for restaurantId: {}", savedCategory.getId(),
					rid);

		} else {

			log.error("Failed to create category for restaurantId: {}", rid);
		}
	}

	@Override
	public CategoryDto getCategoryById(int categoryId) {

		log.info("Fetching category with categoryId: {}", categoryId);

		Optional<Category> optional = categoryRepository.findById(categoryId);

		if (optional.isEmpty()) {

			log.error("Category not found with categoryId: {}", categoryId);

			throw new CategoryServiceException("Category is not found for id : " + categoryId, HttpStatus.NOT_FOUND);
		}

		Category category = optional.get();

		log.info("Category found successfully with categoryId: {}", categoryId);

		CategoryDto dto = new CategoryDto();

		dto.setCategoryId(category.getId());
		dto.setFoodName(category.getFoodName());
		dto.setCategory(category.getCategory());

		log.debug("Category response created for categoryId: {}", categoryId);

		return dto;
	}

	@Override
	@Transactional
	public Category updateCategory(int cId, int rId, CategoryDto dto) {

		log.info("Updating category with categoryId: {} and restaurantId: {}", cId, rId);

		Optional<Category> optional = categoryRepository.findByIdAndRestaurant_Id(cId, rId);

		if (optional.isEmpty()) {

			log.error("Category not found with categoryId: {} and restaurantId: {}", cId, rId);

			throw new RestaurantServiceException(HttpStatus.NOT_FOUND,
					"Category not found for id " + cId + " for restaurant id " + rId);
		}

		log.info("Category found successfully with categoryId: {} and restaurantId: {}", cId, rId);

		StringBuilder sql = new StringBuilder("UPDATE category SET ");

		boolean hasUpdate = false;

		if (dto.getCategory() != null) {

			log.debug("Category type will be updated for categoryId: {}", cId);

			sql.append("category = :category, ");
			hasUpdate = true;
		}

		if (dto.getFoodName() != null && !dto.getFoodName().isBlank()) {

			log.debug("Food name will be updated for categoryId: {}", cId);

			sql.append("food_name = :foodName, ");
			hasUpdate = true;
		}

		if (!hasUpdate) {

			log.warn("No valid fields provided for category update. categoryId: {}, restaurantId: {}", cId, rId);

			return optional.get();
		}

		sql.setLength(sql.length() - 2);

		sql.append(" WHERE id = :categoryId AND restaurant_id = :restaurantId");

		log.debug("Executing category update query for categoryId: {} and restaurantId: {}", cId, rId);

		Query query = entityManager.createNativeQuery(sql.toString());

		query.setParameter("categoryId", cId);
		query.setParameter("restaurantId", rId);

		if (dto.getCategory() != null) {

			query.setParameter("category", dto.getCategory().name());
		}

		if (dto.getFoodName() != null && !dto.getFoodName().isBlank()) {

			query.setParameter("foodName", dto.getFoodName());
		}

		int rowsUpdated = query.executeUpdate();

		if (rowsUpdated > 0) {

			log.info("Category updated successfully. categoryId: {}, restaurantId: {}, rowsUpdated: {}", cId, rId,
					rowsUpdated);

		} else {

			log.warn("No category record updated for categoryId: {} and restaurantId: {}", cId, rId);
		}

		entityManager.clear();

		Category updatedCategory = entityManager.find(Category.class, cId);

		if (updatedCategory != null) {

			log.info("Updated category retrieved successfully with categoryId: {}", cId);

		} else {

			log.error("Failed to retrieve updated category with categoryId: {}", cId);
		}

		return updatedCategory;
	}

	@Override
	public void deleteCategory(int categoryId) {

		log.info("Deleting category with categoryId: {}", categoryId);

		Optional<Category> optional = categoryRepository.findById(categoryId);

		if (optional.isEmpty()) {

			log.error("Category not found with categoryId: {}", categoryId);

			throw new CategoryServiceException("Category is not found for id : " + categoryId, HttpStatus.NOT_FOUND);
		}

		log.info("Category found successfully with categoryId: {}", categoryId);

		boolean categoryInUse = itemRepository.existsByCategoryId(categoryId);

		if (categoryInUse) {

			log.error("Category cannot be deleted because it is being used by MenuItem. categoryId: {}", categoryId);

			throw new CategoryServiceException("Category is being used by MenuItem", HttpStatus.CONFLICT);
		}

		log.info("Category is not being used by any MenuItem. categoryId: {}", categoryId);

		categoryRepository.deleteById(categoryId);

		log.info("Category deleted successfully with categoryId: {}", categoryId);
	}

	@Override
	public List<CategoryDto> getAllCategory() {

		log.info("Fetching all categories");

		List<Category> c = categoryRepository.findAll();

		if (c.isEmpty()) {

			log.warn("No categories found");

			throw new CategoryServiceException("Please Provide appropriate Message", HttpStatus.NOT_FOUND);
		}

		log.info("Found {} categories", c.size());

		List<CategoryDto> dto = new ArrayList<>();

		for (Category category : c) {

			log.debug("Mapping categoryId: {}", category.getId());

			CategoryDto categoryDto = new CategoryDto();

			categoryDto.setCategoryId(category.getId());

			categoryDto.setFoodName(category.getFoodName());

			categoryDto.setCategory(category.getCategory());

			categoryDto.setRestaurantId(category.getId());

			dto.add(categoryDto);
		}

		log.info("Successfully fetched {} categories", dto.size());

		return dto;
	}

	@Override
	public List<RestaurantResponse> viewRestaurentsByCategory(int categoryId) {

		log.info("Fetching restaurants for categoryId: {}", categoryId);

		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isEmpty()) {

			log.error("Category not found with categoryId: {}", categoryId);

			throw new CategoryServiceException("Please Provide a Valid Id", HttpStatus.NOT_FOUND);
		}

		Category category = optionalCategory.get();

		log.info("Category found successfully with categoryId: {}", categoryId);

		List<RestaurantResponse> list = new ArrayList<>();

		List<MenuItem> menuItems = category.getMenuItem();

		if (menuItems == null || menuItems.isEmpty()) {

			log.warn("No menu items found for categoryId: {}", categoryId);

			return list;
		}

		log.info("Found {} menu items for categoryId: {}", menuItems.size(), categoryId);

		for (MenuItem menuItem : menuItems) {

			Restaurant restaurant = menuItem.getRestaurant();

			if (restaurant == null) {

				log.warn("Restaurant not found for menuItem associated with categoryId: {}", categoryId);

				continue;
			}

			log.debug("Adding restaurantId: {} for categoryId: {}", restaurant.getId(), categoryId);

			RestaurantResponse response = new RestaurantResponse();

			response.setId(restaurant.getId());

			response.setName(restaurant.getName());

			response.setAddress(restaurant.getAddress());

			response.setCountry(restaurant.getCountry());

			response.setContactDetails(restaurant.getContactDetails());

			response.setEmail(restaurant.getEmail());

			response.setStatus(restaurant.getStatus());

			list.add(response);
		}

		log.info("Successfully fetched {} restaurants for categoryId: {}", list.size(), categoryId);

		return list;
	}
}