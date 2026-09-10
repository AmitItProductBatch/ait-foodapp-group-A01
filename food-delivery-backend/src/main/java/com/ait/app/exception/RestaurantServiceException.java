package com.ait.app.exception;

import org.springframework.http.HttpStatus;

public class RestaurantServiceException extends RuntimeException {

	private final HttpStatus status;

	public RestaurantServiceException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}