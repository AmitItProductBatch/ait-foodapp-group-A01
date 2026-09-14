package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.MenuItemDto;
import com.ait.app.dto.MenuItemResponseDto;
import com.ait.app.model.MenuItem;

public interface MenuService {

	MenuItem addMenuItem(MenuItemDto dto);

	public MenuItemResponseDto viewMenuById(long menuId);

	public List<MenuItemResponseDto> viewAllMenuByRestaurent(long resId);

	public void deleteMenuItemById(long res, long id);

	public String updateMenuItemById(long restaurantId, long itemId, String field, String value);

}
