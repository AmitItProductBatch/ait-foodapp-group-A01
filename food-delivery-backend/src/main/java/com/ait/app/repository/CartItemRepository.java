package com.ait.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
	List<CartItem> findByCartCartId(int cartId);

	Optional<CartItem> findByCartCartIdAndCartItemId(int cartId, int cartItemId);

}
