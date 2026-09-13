package com.ait.app.dto;

public class MenuItemDto {

    private String name;
    private String description;
    private String type;
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private double halfPrice;
	private double fullPrice;
	private boolean available;
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

    

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}