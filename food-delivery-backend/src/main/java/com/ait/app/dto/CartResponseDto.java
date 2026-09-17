package com.ait.app.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class CartResponseDto {
	
	private int cartId;
	private double totalAmount;
	
	
	private LocalDateTime createdAt;
	
	 
	private LocalDateTime updetedAt;
	
	private int userId;

	public int getCartId() {
		return cartId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdetedAt() {
		return updetedAt;
	}

	public int getUserId() {
		return userId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdetedAt(LocalDateTime updetedAt) {
		this.updetedAt = updetedAt;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	

}
