package com.ait.app.exception;

import org.springframework.http.HttpStatusCode;

public class MenuItemServiceException extends RuntimeException{
	
	private HttpStatusCode httpStatusCode;

	private String msg;

	public MenuItemServiceException(HttpStatusCode httpStatusCode, String msg) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.msg = msg;
	}

	public HttpStatusCode getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getMsg() {
		return msg;
	}

}
