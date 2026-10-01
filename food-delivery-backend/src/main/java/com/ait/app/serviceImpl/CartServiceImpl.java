package com.ait.app.serviceImpl;

import java.util.List;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

	@Autowired
	CartRepository cartRepository;

	@Autowired
	UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void createCart(int uId, CartDto dto) {

		logger.info("Creating cart for User ID: {}", uId);

		Optional<Users> opt = userRepository.findById(uId);
		if (opt.isEmpty()) {

			logger.error("Cart creation failed. User not found for User ID: {}", uId);
			throw new UserServiceException(HttpStatus.NOT_FOUND, "User Not Found for User Id :" + uId);
		}

		Users user = opt.get();
		if (user.getCart() != null) {
			logger.error("Cart creation failed. Cart already exists for User ID: {}", uId);

			throw new UserServiceException(HttpStatus.CONFLICT, "Cart already exists for User Id : " + uId);
		}

		Cart c = new Cart();
		c.setTotalAmount(0.0);
		c.setUser(user);
		cartRepository.save(c);

		logger.info("Cart created successfully for User ID: {}", uId);

	}

	@Override
	public CartResponseDto getCartByCartId(int cId) {

		logger.info("Fetching cart details for Cart ID: {}", cId);
		Optional<Cart> opt = cartRepository.findById(cId);

		if (opt.isEmpty()) {
			logger.error("Cart retrieval failed. Cart not found for Cart ID: {}", cId);

			throw new CartServiceException("Cart Not Found for Cart Id :" + cId, HttpStatus.NOT_FOUND);
		}

		Cart c = opt.get();
		CartResponseDto dto = new CartResponseDto();

		dto.setCartId(c.getCartId());
		dto.setTotalAmount(c.getTotalAmount());
		dto.setCreatedAt(c.getCreatedAt());
		dto.setUpdetedAt(c.getUpdetedAt());
		dto.setUserId(c.getUser().getUserId());

		logger.info("Cart retrieved successfully for Cart ID: {}", cId);

		return dto;
	}

	@Override
	public CartResponseDto getCartByUserId(int uId) {

		logger.info("Fetching cart details for User ID: {}", uId);
		Optional<Cart> opt = cartRepository.findByUserUserId(uId);
		if (opt.isEmpty()) {
			logger.error("Cart retrieval failed. Cart not found for User ID: {}", uId);
			throw new UserServiceException(HttpStatus.NOT_FOUND, "User Not Found for User Id :" + uId);
		}

		Cart c = opt.get();

		CartResponseDto dto = new CartResponseDto();

		dto.setCartId(c.getCartId());
		dto.setTotalAmount(c.getTotalAmount());
		dto.setCreatedAt(c.getCreatedAt());
		dto.setUpdetedAt(c.getUpdetedAt());
		dto.setUserId(c.getUser().getUserId());
		logger.info("Cart retrieved successfully for User ID: {} and Cart ID: {}", uId, c.getCartId());

		return dto;
	}

	@Override
	@Transactional
	public Cart updateCart(int cartId, int userId) {

		logger.info("Updating cart. Cart ID: {}, User ID: {}", cartId, userId);

		Optional<Cart> optional = cartRepository.findByCartIdAndUser_UserId(cartId, userId);

		if (optional.isEmpty()) {

			logger.error("Cart update failed. Cart not found for Cart ID: {} and User ID: {}", cartId, userId);
			throw new CartServiceException("Cart not found for id " + cartId + " for user id " + userId,
					HttpStatus.NOT_FOUND);
		}

		Cart cart = optional.get();

//	    i have to add  native Query in cartIteamRepositry once complete it
//	    double totalAmount =
//	            cartItemRepository.calculateTotalAmount(cartId);

		cart.setTotalAmount(0.0);

		logger.info("Cart updated successfully. Cart ID: {}, User ID: {}", cartId, userId);

		return cartRepository.save(cart);
	}

	@Override
	public void deleteFromCart(int cId) {
		logger.info("Deleting cart. Cart ID: {}", cId);

		Optional<Cart> optional = cartRepository.findById(cId);

		if (optional.isEmpty()) {
			logger.error("Cart deletion failed. Cart not found for Cart ID: {}", cId);

			throw new CartServiceException("Cart not found for id " + cId, HttpStatus.NOT_FOUND);
		}

		Cart cart = optional.get();

		cartRepository.deleteById(cart.getCartId());
		logger.info("Cart deleted successfully. Cart ID: {}", cId);

	}

}
