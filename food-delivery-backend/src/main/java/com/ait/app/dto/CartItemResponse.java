package com.ait.app.dto;

public class CartItemResponse {

	private int cartItemId;
	private int cartId;
    private long menuItemId;
    private int quantity;
    
	public int getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public long getMenuItemId() {
		return menuItemId;
	}
	public void setMenuItemId(long menuItemId) {
		this.menuItemId = menuItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    

}
