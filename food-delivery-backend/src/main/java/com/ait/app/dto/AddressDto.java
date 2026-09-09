package com.ait.app.dto;
import jakarta.validation.constraints.NotBlank;

public class AddressDto {
    @NotBlank(message = "Label is mandatory")
    private String label;
    @NotBlank(message = "Street is mandatory")
    private String street;
    private String apartment;
    private String landmark;
    @NotBlank(message = "City is mandatory")
    private String city;
    @NotBlank(message = "Postal Code is mandatory")
    private String postalCode;
    private String deliveryInstructions;


        // Getters and Setters

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

}