package com.ait.app.dto;

public class ItemPriceResponseDto {

    private Long itemId;
    private String name;
    private double fullPrice;
    private double halfPrice;
    private boolean available;

    public ItemPriceResponseDto() {
    }

    public ItemPriceResponseDto(Long itemId, String name, double fullPrice, double halfPrice, boolean available) {
        this.itemId = itemId;
        this.name = name;
        this.fullPrice = fullPrice;
        this.halfPrice = halfPrice;
        this.available = available;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(double fullPrice) {
        this.fullPrice = fullPrice;
    }

    public double getHalfPrice() {
        return halfPrice;
    }

    public void setHalfPrice(double halfPrice) {
        this.halfPrice = halfPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
