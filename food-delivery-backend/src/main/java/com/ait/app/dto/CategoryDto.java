package com.ait.app.dto;

import com.ait.app.enums.FoodType;

public class CategoryDto {

	private FoodType type;

	private String categoryName;

	public FoodType getType() {
		return type;
	}

	public void setType(FoodType type) {
		this.type = type;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
