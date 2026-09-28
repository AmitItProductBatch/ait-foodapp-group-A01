package com.ait.app.exception;

import org.springframework.http.HttpStatus;

public class PaymentServiceException extends RuntimeException {
	
	private String Message;

	private HttpStatus httpStatus;

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public PaymentServiceException(String message, HttpStatus httpStatus) {
		super();
		Message = message;
		this.httpStatus = httpStatus;
	}


}
