package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.MenuItemDto;
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

//	@PatchMapping("/{restaurantId}/menu{itemId}")
//	public ResponseEntity<String> updateMenuItemById(@PathVariable long restaurantId, @PathVariable long itemId,
//			@PathVariable String field, @PathVariable String value) {
//
//		String response = menuService.updateMenuItemById(restaurantId, itemId, field, value);
//
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
}
