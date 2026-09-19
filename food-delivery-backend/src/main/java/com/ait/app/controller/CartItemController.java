package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.CartItemRequest;
import com.ait.app.dto.CartItemResponse;
import com.ait.app.service.CartItemService;

@RestController
@RequestMapping("/api/carts")
public class CartItemController {

	@Autowired
	CartItemService cartItemService;

	@PostMapping("/{cartId}/items")
	public ResponseEntity<CartItemResponse> addItemToCart(@PathVariable int cartId,
			@RequestBody CartItemRequest request) {

		cartItemService.addItemToCart(cartId, request);

		return new ResponseEntity("Cart Item Created ", HttpStatus.CREATED);
	}

	@GetMapping("/{cartId}/items")
	public ResponseEntity<List<CartItemResponse>> getAllCartItemsByCartId(@PathVariable int cartId) {

		List<CartItemResponse> response = cartItemService.getAllCartItemsByCartId(cartId);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{cartId}/{cartItemId}")
	public ResponseEntity<String> updateCartItemQuantity(@PathVariable int cartId, @PathVariable int cartItemId,
			@RequestBody CartItemRequest request) {

		String response = cartItemService.updateCartItemQuantity(cartId, cartItemId, request.getQuantity());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/menuItem/{menuItemId}")
	public ResponseEntity<List<CartItemResponse>> getCartItemsByMenuItemId(@PathVariable long menuItemId) {
		List<CartItemResponse> response = cartItemService.getCartItemsByMenuItemId(menuItemId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
