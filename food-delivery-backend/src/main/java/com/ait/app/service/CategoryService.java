package com.ait.app.service;

import com.ait.app.dto.CategoryDto;

public interface CategoryService {
	
	void createCategory(int rid, CategoryDto dto);
	
	CategoryDto getCategoryById(int categoryId);
	
	void deleteCategory(int categoryId);

}
