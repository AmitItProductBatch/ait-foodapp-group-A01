package com.ait.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.CartItemRequest;
import com.ait.app.dto.CartItemResponse;
import com.ait.app.exception.CartItemServiceException;
import com.ait.app.model.Cart;
import com.ait.app.model.CartItem;
import com.ait.app.model.MenuItem;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.service.CartItemService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class CartItemServiceImpl implements CartItemService {

	private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	MenuItemRepository itemRepository;

	@Autowired
	EntityManager entityManager;

	@Transactional
	@Override
	public CartItemResponse addItemToCart(int cartId, CartItemRequest request) {

		logger.info("Adding CartItem. CartId: {}, MenuItemId: {}, Quantity: {}", cartId, request.getMenuItemId(),
				request.getQuantity());

		Cart cart = cartRepository.findById(cartId).orElseThrow(() -> {
			logger.error("Cart not found while adding CartItem. CartId: {}", cartId);
			return new CartItemServiceException("Cart Not Found", HttpStatus.NOT_FOUND);
		});

		MenuItem menuItem = itemRepository.findById(request.getMenuItemId()).orElseThrow(() -> {
			logger.error("MenuItem not found while adding CartItem. MenuItemId: {}", request.getMenuItemId());
			return new CartItemServiceException("Menu Item Not Found", HttpStatus.NOT_FOUND);
		});

		if (request.getQuantity() <= 0) {
			logger.error("Invalid quantity while adding CartItem. CartId: {}, MenuItemId: {}, Quantity: {}", cartId,
					request.getMenuItemId(), request.getQuantity());
			throw new CartItemServiceException("Quantity is Not Allowed", HttpStatus.NOT_ACCEPTABLE);
		}

		CartItem cartItem = new CartItem();

		cartItem.setCart(cart);
		cartItem.setMenuItem(menuItem);
		cartItem.setQuantity(request.getQuantity());

		CartItem savedCartItem = cartItemRepository.save(cartItem);
		logger.info("CartItem added successfully. CartId: {}, CartItemId: {}, MenuItemId: {}", cartId,
				savedCartItem.getCartItemId(), savedCartItem.getMenuItem().getId());

		recalculateCartTotal(cartId);

		CartItemResponse response = new CartItemResponse();

		response.setCartItemId(savedCartItem.getCartItemId());
		response.setCartId(savedCartItem.getCart().getCartId());
		response.setMenuItemId(savedCartItem.getMenuItem().getId());
		response.setQuantity(savedCartItem.getQuantity());

		return response;
	}

	@Override
	public List<CartItemResponse> getAllCartItemsByCartId(int cartId) {

		logger.info("Retrieving CartItems. CartId: {}", cartId);

		List<CartItem> cartItems = cartItemRepository.findByCartCartId(cartId);

		if (cartItems.isEmpty()) {
			logger.warn("No CartItems found. CartId: {}", cartId);
			throw new CartItemServiceException("No Cart Item is there ", HttpStatus.NOT_FOUND);
		}
		List<CartItemResponse> responses = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			logger.debug("CartItem retrieved. CartId: {}, CartItemId: {}, MenuItemId: {}", cartId,
					cartItem.getCartItemId(), cartItem.getMenuItem().getId());

			CartItemResponse response = new CartItemResponse();

			response.setCartItemId(cartItem.getCartItemId());
			response.setCartId(cartItem.getCart().getCartId());
			response.setQuantity(cartItem.getQuantity());
			response.setMenuItemId(cartItem.getMenuItem().getId());

			responses.add(response);
		}
		return responses;
	}

	@Transactional
	@Override
	public String updateCartItemQuantity(int cartId, int cartItemId, int quantity) {
		logger.info("Updating CartItem quantity. CartId: {}, CartItemId: {}, NewQuantity: {}", cartId, cartItemId,
				quantity);

		if (quantity <= 0) {
			logger.error("Invalid quantity for CartItem update. CartId: {}, CartItemId: {}, Quantity: {}", cartId,
					cartItemId, quantity);
			throw new CartItemServiceException("Quantity must be greater than 0", HttpStatus.BAD_REQUEST);
		}

		String query = "UPDATE cart_items SET quantity = :quantity " + "WHERE cart_item_id = :cartItemId "
				+ "AND cart_id = :cartId";

		int result = entityManager.createNativeQuery(query).setParameter("quantity", quantity)
				.setParameter("cartItemId", cartItemId).setParameter("cartId", cartId).executeUpdate();

		if (result == 0) {
			logger.error("CartItem not found for update. CartId: {}, CartItemId: {}", cartId, cartItemId);
			throw new CartItemServiceException("CartItem does not exist in the specified Cart", HttpStatus.NOT_FOUND);

		}
		logger.info("CartItem quantity updated successfully. CartId: {}, CartItemId: {}, NewQuantity: {}", cartId,
				cartItemId, quantity);
		recalculateCartTotal(cartId);
		return "Cart item updated successfully for CartItem Id : " + cartItemId;
	}

	@Override
	public List<CartItemResponse> getCartItemsByMenuItemId(long menuItemId) {
		logger.info("Retrieving CartItems by MenuItemId: {}", menuItemId);
		if (!itemRepository.existsById(menuItemId)) {
			logger.error("MenuItem not found while retrieving CartItems. MenuItemId: {}", menuItemId);
			throw new CartItemServiceException("menu item not found", HttpStatus.NOT_FOUND);
		}

		List<CartItem> cartItems = cartItemRepository.findByMenuItemId(menuItemId);
		logger.info("Retrieved {} CartItems for MenuItemId: {}", cartItems.size(), menuItemId);

		List<CartItemResponse> responses = new ArrayList<>();

		for (CartItem cartItem : cartItems) {

			logger.debug("CartItem retrieved by MenuItem. CartId: {}, CartItemId: {}, MenuItemId: {}",
					cartItem.getCart().getCartId(), cartItem.getCartItemId(), menuItemId);
			CartItemResponse response = new CartItemResponse();
			response.setCartItemId(cartItem.getCartItemId());
			response.setCartId(cartItem.getCart().getCartId());
			response.setMenuItemId(cartItem.getMenuItem().getId());
			response.setQuantity(cartItem.getQuantity());
			responses.add(response);
		}
		return responses;
	}

	private void recalculateCartTotal(int cartId) {
		logger.info("Starting cart total recalculation. CartId: {}", cartId);

		Cart cart = cartRepository.findById(cartId).orElseThrow(() -> {
			logger.error("Cart not found during total recalculation. CartId: {}", cartId);
			return new CartItemServiceException("Cart Not Found for Cart Id : " + cartId, HttpStatus.NOT_FOUND);
		});
		List<CartItem> cartItems = cartItemRepository.findByCartCartId(cartId);

		double totalAmount = 0.0;

		for (CartItem cartItem : cartItems) {

			double price = cartItem.getMenuItem().getFullPrice();

			totalAmount = totalAmount + (price * cartItem.getQuantity());
		}

		cart.setTotalAmount(totalAmount);

		cartRepository.save(cart);
		logger.info("Cart total recalculated successfully. CartId: {}, TotalAmount: {}", cartId, totalAmount);
	}

	@Override
	public String deleteCartItemFromCart(int cartId, int cartItemId) {
		logger.info("Deleting CartItem. CartId: {}, CartItemId: {}", cartId, cartItemId);

		if (!cartRepository.existsById(cartId)) {
			logger.error("Cart not found while deleting CartItem. CartId: {}", cartId);
			throw new CartItemServiceException("Cart Not Found", HttpStatus.NOT_FOUND);
		}

		CartItem cartItem = cartItemRepository.findByCartCartIdAndCartItemId(cartId, cartItemId).orElseThrow(() -> {
			logger.error("CartItem not found for deletion. CartId: {}, CartItemId: {}", cartId, cartItemId);
			return new CartItemServiceException("CartItem does not exist in the specified Cart", HttpStatus.NOT_FOUND);
		});
		cartItemRepository.delete(cartItem);
		logger.info("CartItem deleted successfully. CartId: {}, CartItemId: {}, MenuItemId: {}", cartId, cartItemId,
				cartItem.getMenuItem().getId());

		return "Cart item deleted successfully for CartItem Id: " + cartItemId;
	}

}
