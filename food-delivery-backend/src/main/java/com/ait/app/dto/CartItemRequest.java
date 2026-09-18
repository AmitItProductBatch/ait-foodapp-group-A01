package com.ait.app.dto;

public class CartItemRequest {
	
	private long menuItemId;
	
    private int quantity;

    
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
