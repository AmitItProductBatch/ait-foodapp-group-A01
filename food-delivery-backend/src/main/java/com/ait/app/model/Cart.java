package com.ait.app.model;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cart")
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;
	private double totalAmount;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updetedAt;
	
	
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
	
	
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public LocalDateTime getUpdetedAt() {
		return updetedAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public void setUpdetedAt(LocalDateTime updetedAt) {
		this.updetedAt = updetedAt;
	}
	public int getCartId() {
		return cartId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public Users getUser() {
		return user;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	
	public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
	
			
}
