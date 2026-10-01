package com.ait.app.service;

import com.ait.app.dto.CartDto;
import com.ait.app.dto.CartResponseDto;
import com.ait.app.model.Cart;

public interface CartService {

	public void createCart(int uId, CartDto dto);

	public CartResponseDto getCartByCartId(int cId);

	public CartResponseDto getCartByUserId(int uId);

	Cart updateCart(int uId, int cId);

	void deleteFromCart(int cId);

}
