package com.ait.app.service;

import com.ait.app.dto.CategoryDto;
import com.ait.app.model.Category;

public interface CategoryService {
	
	void createCategory(int rid, CategoryDto dto);

	CategoryDto getCategoryById(int categoryId);

	public Category updateCategory(int cId, int rId, CategoryDto dto); 
	
	void deleteCategory(int categoryId);


}
