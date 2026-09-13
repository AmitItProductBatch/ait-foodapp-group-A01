package com.ait.app.dto;

public class RestaurantResponse {

	private Long id;
	private String name;
	private String address;
	private String country;
	private String contactDetails;
	private String email;
	private String status;
	private int userId;

	public RestaurantResponse() {
	}

	public RestaurantResponse(Long id, String name, String address, String country, String contactDetails,
			String status,int userId) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.country = country;
		this.contactDetails = contactDetails;
		this.status = status;
		this.userId = userId;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public int getUserId() {
	    return userId;
	}

	public void setUserId(int userId) {
	    this.userId = userId;
	}
}