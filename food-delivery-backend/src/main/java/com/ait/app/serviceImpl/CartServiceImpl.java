package com.ait.app.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.CartDto;
import com.ait.app.dto.CartResponseDto;
import com.ait.app.exception.CartServiceException;
import com.ait.app.exception.UserServiceException;
import com.ait.app.model.Cart;
import com.ait.app.model.Users;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.service.CartService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void createCart(int uId, CartDto dto) {

		Optional<Users> opt = userRepository.findById(uId);
		if (opt.isEmpty()) {
			throw new UserServiceException(HttpStatus.NOT_FOUND, "User Not Found for User Id :" + uId);
		}

		Users user = opt.get();
		if (user.getCart() != null) {
			throw new UserServiceException(HttpStatus.CONFLICT, "Cart already exists for User Id : " + uId);
		}

		Cart c = new Cart();
		c.setTotalAmount(0.0);
		c.setUser(user);
		cartRepository.save(c);

	}

	@Override
	public CartResponseDto getCartByCartId(int cId) {
		Optional<Cart> opt = cartRepository.findById(cId);

		if (opt.isEmpty()) {
			throw new CartServiceException("Cart Not Found for Cart Id :" + cId, HttpStatus.NOT_FOUND);
		}

		Cart c = opt.get();
		CartResponseDto dto = new CartResponseDto();

		dto.setCartId(c.getCartId());
		dto.setTotalAmount(c.getTotalAmount());
		dto.setCreatedAt(c.getCreatedAt());
		dto.setUpdetedAt(c.getUpdetedAt());
		dto.setUserId(c.getUser().getUserId());
		return dto;
	}

	@Override
	public CartResponseDto getCartByUserId(int uId) {
		Optional<Cart> opt = cartRepository.findByUserUserId(uId);
		if (opt.isEmpty()) {
			throw new UserServiceException(HttpStatus.NOT_FOUND, "User Not Found for User Id :" + uId);
		}

		Cart c = opt.get();

		CartResponseDto dto = new CartResponseDto();

		dto.setCartId(c.getCartId());
		dto.setTotalAmount(c.getTotalAmount());
		dto.setCreatedAt(c.getCreatedAt());
		dto.setUpdetedAt(c.getUpdetedAt());
		dto.setUserId(c.getUser().getUserId());

		return dto;
	}

	@Override
	@Transactional
	public Cart updateCart(int cartId, int userId) {

		Optional<Cart> optional = cartRepository.findByCartIdAndUser_UserId(cartId, userId);

		if (optional.isEmpty()) {
			throw new CartServiceException("Cart not found for id " + cartId + " for user id " + userId,
					HttpStatus.NOT_FOUND);
		}

		Cart cart = optional.get();

//	    i have to add  native Query in cartIteamRepositry once complete it
//	    double totalAmount =
//	            cartItemRepository.calculateTotalAmount(cartId);

		cart.setTotalAmount(0.0);

		return cartRepository.save(cart);
	}

	@Override
	public void deleteFromCart(int cId) {

		Optional<Cart> optional = cartRepository.findById(cId);

		if (optional.isEmpty()) {
			throw new CartServiceException("Cart not found for id " + cId, HttpStatus.NOT_FOUND);
		}

		Cart cart = optional.get();

		cartRepository.deleteById(cart.getCartId());

	}

}
