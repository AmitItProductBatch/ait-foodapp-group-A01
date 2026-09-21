package com.ait.app.globalexception;

import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ait.app.exception.AddressServiceException;
import com.ait.app.exception.CartItemServiceException;
import com.ait.app.exception.CartServiceException;
import com.ait.app.exception.CategoryServiceException;
import com.ait.app.exception.MenuItemServiceException;
import com.ait.app.exception.RestaurantServiceException;
import com.ait.app.exception.OrderServiceException;
import com.ait.app.exception.UserServiceException;

import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(value = UserServiceException.class)
	public ResponseEntity UserExceptionHandler(UserServiceException ue) {
		return new ResponseEntity(ue.getMsg(), ue.getHttpStatusCode());

	}
	@ExceptionHandler(RestaurantServiceException.class)
	public ResponseEntity handleRestaurantServiceException(RestaurantServiceException ex) {
		return new ResponseEntity(ex.getMsg(), ex.getHttpStatusCode());
	}
	
	@ExceptionHandler(value = MenuItemServiceException.class)
	public ResponseEntity MenuItemExceptionHandler(MenuItemServiceException me) {
		return new ResponseEntity(me.getMsg(),me.getHttpStatusCode());
	}
	

	@ExceptionHandler(value = AddressServiceException.class)
	public ResponseEntity UserExceptionHandler(AddressServiceException ae) {
		return new ResponseEntity(ae.getMessage(), ae.getHttpStatus());

	}
	
	@ExceptionHandler(value = CategoryServiceException.class)
	public ResponseEntity UserExceptionHandler(CategoryServiceException ce) {
		return new ResponseEntity(ce.getMessage(), ce.getHttpStatus());

	}
	
	@ExceptionHandler(value = CartServiceException.class)
	public ResponseEntity UserExceptionHandler(CartServiceException ce) {
		return new ResponseEntity(ce.getMessage(), ce.getHttpStatus());

	}
	
	@ExceptionHandler(CartItemServiceException.class )
	public ResponseEntity CartItemServiceExceptionHandler(CartItemServiceException ct) {
		return new ResponseEntity(ct.getMessage(), ct.getHttpStatus());
		
	}

	@ExceptionHandler(OrderServiceException.class)
	public ResponseEntity OrderServiceExceptionHandler(OrderServiceException oe) {
		return new ResponseEntity(oe.getMessage(), oe.getHttpStatus());
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity UserExceptionHandler(Exception e) {
		return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);

	}

}
