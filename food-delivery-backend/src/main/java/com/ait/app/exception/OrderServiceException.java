package com.ait.app.exception;

import org.springframework.http.HttpStatus;

public class OrderServiceException extends RuntimeException {

	String message;
	HttpStatus httpStatus;

	public OrderServiceException(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
