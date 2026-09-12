package com.ait.app.service;

import com.ait.app.dto.MenuItemDto;

public interface MenuService {
	
	   public Long addMenuItem(Long restaurantId, MenuItemDto dto);

}
