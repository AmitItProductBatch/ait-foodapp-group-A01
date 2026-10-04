package com.ait.app.dto;

import java.time.LocalDateTime;

public class FeedbackResponseDto {

    private int feedbackId;
    private int orderId;
    private long restaurantId;
    private String content;
    private int rating;
    private LocalDateTime createdAt;

    public FeedbackResponseDto() {
    }

    public FeedbackResponseDto(int feedbackId, int orderId, long restaurantId, String content, int rating,
            LocalDateTime createdAt) {
        this.feedbackId = feedbackId;
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
