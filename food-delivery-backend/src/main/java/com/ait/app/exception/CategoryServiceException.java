package com.ait.app.exception;

import org.springframework.http.HttpStatus;

public class CategoryServiceException extends RuntimeException {

	String message;
	HttpStatus httpStatus;

	public CategoryServiceException(String message, HttpStatus httpStatus) {
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

	public void setMessage(String message) {
		this.message = message;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	
	

}
