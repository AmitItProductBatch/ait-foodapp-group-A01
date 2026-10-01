package com.ait.app.dto;

public class CartDto {
	
	private int userId;
	private double totalAmount;
	public int getUserId() {
		return userId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	

}
