package com.ait.app.service;

import com.ait.app.dto.MenuItemDto;
import com.ait.app.model.MenuItem;

public interface MenuService {
	
	 MenuItem addMenuItem(MenuItemDto dto);

//	   public String updateMenuItemById(long restaurantId, long itemId, String field, 
//			String value);

}
