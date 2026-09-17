package com.ait.app.service;

import com.ait.app.dto.CartDto;
import com.ait.app.dto.CartResponseDto;

public interface CartService {
	
	public void createCart(int uId,CartDto dto);
	
	public CartResponseDto getCartByCartId(int cId);
	public CartResponseDto getCartByUserId(int uId);

}
