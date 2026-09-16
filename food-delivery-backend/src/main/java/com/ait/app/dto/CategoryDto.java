package com.ait.app.dto;

import com.ait.app.enums.FoodCategory;

public class CategoryDto {

	private FoodCategory category;
	
	private String foodName;
	
	private int restaurantId;
	
	

	public int getRestaurantId() {
	    return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
	    this.restaurantId = restaurantId;
	}

	

	public FoodCategory getCategory() {
		return category;
	}

	public void setCategory(FoodCategory category) {
		this.category = category;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	
}
