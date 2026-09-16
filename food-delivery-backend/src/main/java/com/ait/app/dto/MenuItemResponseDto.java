package com.ait.app.dto;

import com.ait.app.enums.FoodType;

public class MenuItemResponseDto {
	private String name;
	private String description;
	private FoodType type;
	private double halfPrice;
	private double fullPrice;
	private boolean available;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FoodType getType() {
		return type;
	}

	public void setType(FoodType type) {
		this.type = type;
	}

	public double getHalfPrice() {
		return halfPrice;
	}

	public void setHalfPrice(double halfPrice) {
		this.halfPrice = halfPrice;
	}

	public double getFullPrice() {
		return fullPrice;
	}

	public void setFullPrice(double fullPrice) {
		this.fullPrice = fullPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

}
