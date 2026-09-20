package com.ait.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.app.dto.MenuItemDto;
import com.ait.app.dto.MenuItemResponseDto;
import com.ait.app.exception.CategoryServiceException;
import com.ait.app.exception.MenuItemServiceException;
import com.ait.app.exception.MenuServiceException;
import com.ait.app.model.Category;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.CategoryRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepository;
import com.ait.app.service.MenuService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class MenuServiceImpl implements MenuService {

	private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public MenuItem addMenuItem(MenuItemDto dto) {
		log.info("Starting MenuItem creation");
		if (dto == null) {
			log.error("Menu Creation failed: MenuItem details are null");
			throw new MenuServiceException(HttpStatus.BAD_REQUEST, "Menu item details cannot be null");
		}

		if (dto.getName() == null || dto.getName().trim().isEmpty()) {
			log.error("MenuItem creation failed: Food name is empty. Restaurant ID: {}");
			throw new MenuServiceException(HttpStatus.BAD_REQUEST, "Food name cannot be empty");
		}

		if (dto.getType() == null) {
			log.error("MenuItem creation failed: Food type is null. Restaurant ID: {}");

			throw new MenuServiceException(HttpStatus.BAD_REQUEST, "Food type cannot be null or empty");
		}

		if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
			log.error("MenuItem creation failed: Description is empty. Restaurant ID: {}");
			throw new MenuServiceException(HttpStatus.BAD_REQUEST, "Description cannot be empty");
		}

		if (dto.getRestaurantId() <= 0) {
			log.error("MenuItem creation failed: Invalid Restaurant ID: {}");
			throw new MenuServiceException(HttpStatus.BAD_REQUEST, "Restaurant id must be greater than 0");
		}

		if (dto.getHalfPrice() <= 0) {
			log.error("MenuItem creation failed: Invalid half price. Restaurant ID: {}");
			throw new MenuServiceException(HttpStatus.BAD_REQUEST, "Half price must be greater than 0");
		}

		if (dto.getFullPrice() <= 0) {
			log.error("MenuItem creation failed: Invalid full price. Restaurant ID: {}");
			throw new MenuServiceException(HttpStatus.BAD_REQUEST, "Full price must be greater than 0");
		}

		Restaurant restaurant = restaurantRepository.findById((long) dto.getRestaurantId()).orElse(null);

		if (restaurant == null) {
			log.error("MenuItem creation failed: Restaurant not found. Restaurant ID: {}");
			throw new MenuServiceException(HttpStatus.NOT_FOUND,
					"Restaurant not found with id: " + dto.getRestaurantId());
		}

		Optional<MenuItem> existingItem = menuItemRepository.findByRestaurantIdAndName(dto.getRestaurantId(),
				dto.getName().trim());

		if (existingItem.isPresent()) {
			log.error("MenuItem creation failed: MenuItem already exists. Restaurant ID: {}, Category ID: {}",
					dto.getRestaurantId(), dto.getCategoryId());
			throw new MenuServiceException(HttpStatus.CONFLICT, "Food item already exists in this restaurant");
		}

//		----------

//		Category category = categoryRepository.findById((long) dto.getRestaurantId()).orElse(null);
		Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);

		Optional<Category> existingCategory = categoryRepository.findByRestaurant_IdAndFoodName(dto.getCategoryId(),
				dto.getName().trim());

		if (existingCategory.isPresent()) {
			log.error(
					"MenuItem creation failed: Food item already exists in category. Category ID: {}, Restaurant ID: {}",
					dto.getCategoryId(), dto.getRestaurantId());

			throw new CategoryServiceException("Food item already exists in this restaurant", HttpStatus.CONFLICT);
		}

		MenuItem item = new MenuItem();
		log.info("MenuItem created successfully. MenuItem ID: {}, Restaurant ID: {}, Category ID: {}", item.getId(),
				dto.getRestaurantId(), dto.getCategoryId());

		item.setName(dto.getName().trim());
		item.setDescription(dto.getDescription().trim());
		item.setType(dto.getType());
		item.setHalfPrice(dto.getHalfPrice());
		item.setFullPrice(dto.getFullPrice());
		item.setAvailable(dto.isAvailable());
		item.setRestaurant(restaurant);
		item.setCategory(category);

		return menuItemRepository.save(item);

	}

	@Override
	public MenuItemResponseDto viewMenuById(long menuId) {
		Optional<MenuItem> o = menuItemRepository.findById(menuId);
		log.info("Fetching MenuItem. MenuItem ID: {}", menuId);
		if (o.isEmpty()) {

			log.error("MenuItem retrieval failed: MenuItem not found. MenuItem ID: {}", menuId);
			throw new MenuServiceException(HttpStatus.NOT_FOUND, "Please Provide Valid ID!");
		}

		MenuItem menuItem = o.get();
		log.info("MenuItem retrieved successfully. MenuItem ID: {}, Restaurant ID: {}, Category ID: {}",
				menuItem.getId(), menuItem.getRestaurant() != null ? menuItem.getRestaurant().getId() : null,
				menuItem.getCategory() != null ? menuItem.getCategory().getId() : null);

		MenuItemResponseDto menuItemResponseDto = new MenuItemResponseDto();
		menuItemResponseDto.setName(menuItem.getName());
		menuItemResponseDto.setDescription(menuItem.getDescription());
		menuItemResponseDto.setFullPrice(menuItem.getFullPrice());
		menuItemResponseDto.setHalfPrice(menuItem.getHalfPrice());
		menuItemResponseDto.setType(menuItem.getType());
		menuItemResponseDto.setAvailable(menuItem.isAvailable());

		return menuItemResponseDto;

	}

	@Override
	public List<MenuItemResponseDto> viewAllMenuByRestaurent(long resId) {
		log.info("Fetching all MenuItems. Restaurant ID: {}", resId);
		Optional<Restaurant> restuarent = restaurantRepository.findById(resId);
		if (restuarent.isEmpty()) {
			log.error("MenuItem retrieval failed: Restaurant not found. Restaurant ID: {}", resId);
			throw new MenuServiceException(HttpStatus.NOT_FOUND, "Restaurant Not Found!");
		}

		Restaurant res = restuarent.get();
		List<MenuItemResponseDto> list = new ArrayList();
		for (MenuItem menuItem : res.getMenuItems()) {

			if (menuItem.isAvailable()) {

				MenuItemResponseDto dto = new MenuItemResponseDto();
				dto.setName(menuItem.getName());
				dto.setDescription(menuItem.getDescription());
				dto.setFullPrice(menuItem.getFullPrice());
				dto.setHalfPrice(menuItem.getHalfPrice());
				dto.setAvailable(menuItem.isAvailable());
				dto.setType(menuItem.getType());
				list.add(dto);

			}

		}
		log.info("MenuItems retrieved successfully. Restaurant ID: {}, Available MenuItem count: {}", resId,
				list.size());

		return list;
	}

	@Override
	public void deleteMenuItemById(long resId, long id) {
		log.info("Starting MenuItem deletion. MenuItem ID: {}, Restaurant ID: {}", id, resId);

		Optional<Restaurant> restaurant = restaurantRepository.findById(resId);

		if (restaurant.isEmpty()) {
			log.error("MenuItem deletion failed: Restaurant not found. Restaurant ID: {}, MenuItem ID: {}", resId, id);

			throw new MenuServiceException(HttpStatus.NOT_FOUND, "Restaurant Not Found!");
		}

		Restaurant res = restaurant.get();

		for (MenuItem menuItem : res.getMenuItems()) {

			if (menuItem.getId() == id) {

				res.getMenuItems().remove(menuItem);

				menuItemRepository.delete(menuItem);
				log.info("MenuItem deleted successfully. MenuItem ID: {}, Restaurant ID: {}", id, resId);

				return;
			}
		}
		log.error("MenuItem deletion failed: MenuItem not found. MenuItem ID: {}, Restaurant ID: {}", id, resId);
		throw new MenuServiceException(HttpStatus.NOT_FOUND, "Menu Item Not Found!");
	}

	@Transactional
	@Override
	public String updateMenuItemById(long restaurantId, long itemId, String field, String value) {
		log.info("Starting MenuItem update. MenuItem ID: {}, Restaurant ID: {}, Field: {}", itemId, restaurantId,
				field);
		String query = "UPDATE menu_items SET " + field + " = :value WHERE id = :itemId "
				+ "AND restaurant_id = :restaurantId";

		int result = entityManager.createNativeQuery(query).setParameter("value", value).setParameter("itemId", itemId)
				.setParameter("restaurantId", restaurantId).executeUpdate();

		if (result == 0) {

			log.error("MenuItem update failed: MenuItem not found. MenuItem ID: {}, Restaurant ID: {}", itemId,
					restaurantId);

			throw new MenuItemServiceException(HttpStatus.NOT_FOUND, "Menu item not exist");
		}
		log.info("MenuItem updated successfully. MenuItem ID: {}, Restaurant ID: {}, Field: {}", itemId, restaurantId,
				field);
		return "Menu item updated successfully";
	}
}
