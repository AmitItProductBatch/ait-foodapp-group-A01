package com.ait.app.dto;

import jakarta.validation.constraints.NotBlank;

public class RestaurantRequest {

	@NotBlank(message = "Restaurant name is required")
	private String name;

	@NotBlank(message = "Address is required")
	private String address;

	@NotBlank(message = "Country is required")
	private String country;

	private String contactDetails;
	
	private String  email;
	
	private int userId;
	
	

	public RestaurantRequest() {
	}

	public RestaurantRequest(String name, String address, String country, String contactDetails) {
		this.name = name;
		this.address = address;
		this.country = country;
		this.contactDetails = contactDetails;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}
	public int getUserId() {
	    return userId;
	}

	public void setUserId(int userId) {
	    this.userId = userId;
	}
}