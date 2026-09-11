package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@PostMapping("/{restaurantId}")
	public ResponseEntity AddMenuItem( @PathVariable long restaurantId, @RequestBody MenuItemDto dto) {
		long menuItemId = menuService.addMenuItem(restaurantId, dto);
		return new ResponseEntity<>("Item added for restaurant successfully",HttpStatus.CREATED);
	}
	
}
