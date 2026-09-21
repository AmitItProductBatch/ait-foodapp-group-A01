package com.ait.app.service;

import java.util.List;

import com.ait.app.dto.CartItemRequest;
import com.ait.app.dto.CartItemResponse;

public interface CartItemService {

    CartItemResponse addItemToCart(int cartId, CartItemRequest request);

    List<CartItemResponse> getAllCartItemsByCartId(int cartId);

    List<CartItemResponse> getCartItemsByMenuItemId(long menuItemId);

    String updateCartItemQuantity(int cartId, int cartItemId, int quantity);

    String deleteCartItemFromCart(int cartId, int cartItemId);
}