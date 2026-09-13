package com.ait.app.service;

import com.ait.app.dto.MenuItemDto;

public interface MenuService {
	
	   public Long addMenuItem(Long restaurantId, MenuItemDto dto);
	   
	   public String updateMenuItemById(long restaurantId, long itemId, String field, 
			String value);

}
