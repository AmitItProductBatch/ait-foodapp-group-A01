package com.ait.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{
	
	 List<CartItem> findByCartCartId(int cartId);

}
