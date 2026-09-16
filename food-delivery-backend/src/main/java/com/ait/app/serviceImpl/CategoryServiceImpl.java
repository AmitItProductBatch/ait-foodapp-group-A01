package com.ait.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.CategoryDto;
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
		Optional<Restaurant> optional = restaurantRepository.findById((long) rid);

		if (optional.isEmpty()) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND, "Restaurent is not found for id :" + rid);
		}

		Restaurant restaurant = optional.get();

		Category category = new Category();

		category.setFoodName(dto.getFoodName());
		category.setCategory(dto.getCategory());

		category.setRestaurant(restaurant);

		categoryRepository.save(category);

	}

	@Override
	public CategoryDto getCategoryById(int categoryId) {
		Optional<Category> optional = categoryRepository.findById(categoryId);

		if (optional.isEmpty()) {
			throw new CategoryServiceException("Category is not found for id : " + categoryId, HttpStatus.NOT_FOUND);
		}

		Category category = optional.get();

		CategoryDto dto = new CategoryDto();

		dto.setCategoryId(category.getId());
		dto.setFoodName(category.getFoodName());
		dto.setCategory(category.getCategory());

		return dto;
	}

	@Override
	@Transactional
	public Category updateCategory(int cId, int rId, CategoryDto dto) {

		Optional<Category> optional = categoryRepository.findByIdAndRestaurant_Id(cId, rId);

		if (optional.isEmpty()) {
			throw new RestaurantServiceException(HttpStatus.NOT_FOUND,
					"Category not found for id " + cId + " for restaurant id " + rId);
		}

		StringBuilder sql = new StringBuilder("UPDATE category SET ");

		boolean hasUpdate = false;

		if (dto.getCategory() != null) {
			sql.append("category = :category, ");
			hasUpdate = true;
		}

		if (dto.getFoodName() != null && !dto.getFoodName().isBlank()) {

			sql.append("food_name = :foodName, ");
			hasUpdate = true;
		}

		if (!hasUpdate) {
			return optional.get();
		}

		sql.setLength(sql.length() - 2);

		sql.append(" WHERE id = :categoryId " + "AND restaurant_id = :restaurantId");

		Query query = entityManager.createNativeQuery(sql.toString());

		query.setParameter("categoryId", cId);
		query.setParameter("restaurantId", rId);

		if (dto.getCategory() != null) {
			query.setParameter("category", dto.getCategory().name());
		}

		if (dto.getFoodName() != null && !dto.getFoodName().isBlank()) {

			query.setParameter("foodName", dto.getFoodName());
		}

		query.executeUpdate();

		entityManager.clear();

		return entityManager.find(Category.class, cId);
	}

	@Override
	public void deleteCategory(int categoryId) {

		Optional<Category> optional = categoryRepository.findById(categoryId);

		if (optional.isEmpty()) {
			throw new CategoryServiceException("Category is not found for id : " + categoryId, HttpStatus.NOT_FOUND);
		}

		if (itemRepository.existsByCategoryId(categoryId)) {
			throw new CategoryServiceException("Category is being used by MenuItem", HttpStatus.CONFLICT);
		}

		categoryRepository.deleteById(categoryId);

	}

}
