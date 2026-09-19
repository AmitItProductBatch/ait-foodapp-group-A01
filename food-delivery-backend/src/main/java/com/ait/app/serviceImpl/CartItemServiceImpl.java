package com.ait.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	MenuItemRepository itemRepository;

	@Autowired
	EntityManager entityManager;

	@Override
	public CartItemResponse addItemToCart(int cartId, CartItemRequest request) {

		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new CartItemServiceException("Cart Not Found", HttpStatus.NOT_FOUND));

		MenuItem menuItem = itemRepository.findById(request.getMenuItemId())
				.orElseThrow(() -> new CartItemServiceException("Menu Item Not Found", HttpStatus.NOT_FOUND));

		if (request.getQuantity() <= 0) {
			throw new CartItemServiceException("Quantity is Not Allowed", HttpStatus.NOT_ACCEPTABLE);
		}

		CartItem cartItem = new CartItem();

		cartItem.setCart(cart);
		cartItem.setMenuItem(menuItem);
		cartItem.setQuantity(request.getQuantity());

		CartItem savedCartItem = cartItemRepository.save(cartItem);

		CartItemResponse response = new CartItemResponse();

		response.setCartItemId(savedCartItem.getCartItemId());
		response.setCartId(savedCartItem.getCart().getCartId());
		response.setMenuItemId(savedCartItem.getMenuItem().getId());
		response.setQuantity(savedCartItem.getQuantity());

		return response;
	}

	@Override
	public List<CartItemResponse> getAllCartItemsByCartId(int cartId) {

		List<CartItem> cartItems = cartItemRepository.findByCartCartId(cartId);

		if (cartItems.isEmpty()) {
			throw new CartItemServiceException("No Cart Item is there ", HttpStatus.NOT_FOUND);
		}
		List<CartItemResponse> responses = new ArrayList<>();

		for (CartItem cartItem : cartItems) {

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

		if (quantity <= 0) {
			throw new CartItemServiceException("Quantity must be greater than 0", HttpStatus.BAD_REQUEST);
		}

		String query = "UPDATE cart_items SET quantity = :quantity " + "WHERE cart_item_id = :cartItemId "
				+ "AND cart_id = :cartId";

		int result = entityManager.createNativeQuery(query).setParameter("quantity", quantity)
				.setParameter("cartItemId", cartItemId).setParameter("cartId", cartId).executeUpdate();

		if (result == 0) {
			throw new CartItemServiceException("CartItem does not exist in the specified Cart", HttpStatus.NOT_FOUND);
			
		}
		return "Cart item updated successfully for CartItem Id : "
        + cartItemId;

	}


	@Override
public List<CartItemResponse> getCartItemsByMenuItemId(long menuItemId) {
    if (!itemRepository.existsById(menuItemId)) {
        throw new CartItemServiceException("menu item not found", HttpStatus.NOT_FOUND);
    }

    List<CartItem> cartItems = cartItemRepository.findByMenuItemId(menuItemId);

    List<CartItemResponse> responses = new ArrayList<>();


    for (CartItem cartItem : cartItems) 
		{
        CartItemResponse response = new CartItemResponse();
    response.setCartItemId(cartItem.getCartItemId());
        response.setCartId(cartItem.getCart().getCartId());
        response.setMenuItemId(cartItem.getMenuItem().getId());
    	response.setQuantity(cartItem.getQuantity());
        responses.add(response);
    }
return responses;
}

}
