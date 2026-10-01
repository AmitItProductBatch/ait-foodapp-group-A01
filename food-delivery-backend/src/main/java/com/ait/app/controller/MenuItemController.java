package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.MenuItemDto;
import com.ait.app.dto.MenuItemResponseDto;
import com.ait.app.service.MenuService;

@RestController
@RequestMapping("/api/restaurants")
public class MenuItemController {

	@Autowired
	private MenuService menuService;

	@PostMapping("/addmenu")
	public ResponseEntity<String> addMenuItem(@RequestBody MenuItemDto dto) {

		menuService.addMenuItem(dto);

		return new ResponseEntity<>("Menu item added successfully for restaurant: " + dto.getRestaurantId(),
				HttpStatus.CREATED);
	}

	@GetMapping("/menu/{menuId}")
	public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable long menuId) {

		MenuItemResponseDto menuItem = menuService.viewMenuById(menuId);
		return new ResponseEntity(menuItem, HttpStatus.OK);
	}

	@GetMapping("/{resId}/menu")
	public ResponseEntity<List<MenuItemResponseDto>> getMenuByRestaurenId(@PathVariable long resId) {

		List<MenuItemResponseDto> list = menuService.viewAllMenuByRestaurent(resId);
		return new ResponseEntity(list, HttpStatus.OK);

	}

	@DeleteMapping("/menu/{resId}/{id}")
	public ResponseEntity deleteMenuItem(@PathVariable long resId, @PathVariable long id) {

		menuService.deleteMenuItemById(resId, id);
		return new ResponseEntity("Menu Item Deleted!", HttpStatus.OK);

	}

	@PatchMapping("/{restaurantId}/menu/{itemId}/{field}/{value}")
	public ResponseEntity<String> updateMenuItemById(@PathVariable long restaurantId, @PathVariable long itemId,
			@PathVariable String field, @PathVariable String value) {

		String response = menuService.updateMenuItemById(restaurantId, itemId, field, value);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
