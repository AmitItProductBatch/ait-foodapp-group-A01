package com.ait.app.controller;

import java.net.CacheResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.CartDto;
import com.ait.app.dto.CartResponseDto;
import com.ait.app.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	@Autowired
	CartService cartService;

	@PostMapping("/userid/{uId}")
	public ResponseEntity createCart(@PathVariable int uId, @RequestBody CartDto dto) {

		cartService.createCart(uId, dto);
		return new ResponseEntity("Cart created successfully for user id :" + uId, HttpStatus.CREATED);
	}

	@GetMapping("getcart/{cId}")
	public ResponseEntity<CacheResponse> getCartByCartId(@PathVariable int cId) {

		CartResponseDto dto = cartService.getCartByCartId(cId);
		return new ResponseEntity(dto, HttpStatus.OK);

	}

	@GetMapping("getcartbyuser/{uId}")
	public ResponseEntity<CacheResponse> getCartByUserId(@PathVariable int uId) {

		CartResponseDto dto = cartService.getCartByUserId(uId);
		return new ResponseEntity(dto, HttpStatus.OK);

	}

	@PatchMapping("/userId/{uId}/cartId/{cId}")
	public ResponseEntity updateCart(@PathVariable int uId, @PathVariable int cId) {

		cartService.updateCart(uId, cId);

		return new ResponseEntity("Cart Updated Successfully for user id :" + uId + " and " + cId, HttpStatus.OK);

	}

	@DeleteMapping("/delete/{cId}")
	public ResponseEntity deleteFromCart(@PathVariable int cId) {

		cartService.deleteFromCart(cId);

		return new ResponseEntity("Cart deleted successfully for cart id :" + cId, HttpStatus.OK);

	}

}
