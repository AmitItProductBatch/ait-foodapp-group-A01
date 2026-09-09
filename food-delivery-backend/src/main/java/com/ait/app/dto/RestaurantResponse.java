package com.ait.app.dto;

import com.ait.app.model.RestaurantStatus;

public class RestaurantResponse {

    private Long id;
    private String name;
    private String address;
    private String cuisine;
    private String contactNumber;
    private String email;
    private RestaurantStatus status;

    public RestaurantResponse(
            Long id,
            String name,
            String address,
            String cuisine,
            String contactNumber,
            String email,
            RestaurantStatus status) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisine = cuisine;
        this.contactNumber = contactNumber;
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public RestaurantStatus getStatus() {
        return status;
    }
}