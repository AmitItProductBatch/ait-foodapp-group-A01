package com.ait.app.exception;

import org.springframework.http.HttpStatus;

public class CartItemServiceException extends RuntimeException{
	
	String message;
	HttpStatus httpStatus;
	public CartItemServiceException( String message,HttpStatus httpStatus) {
		super();
		this.message = message;
		
		this.httpStatus = httpStatus;
	}
	public CartItemServiceException(String message) {
		super();
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	

}
