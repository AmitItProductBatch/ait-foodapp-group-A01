package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserUserId(int userId);
    
    Optional<Cart> findByCartIdAndUser_UserId(int cartId, int userId);

}
