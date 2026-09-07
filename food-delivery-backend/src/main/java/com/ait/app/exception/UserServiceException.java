package com.ait.app.exception;


import org.springframework.http.HttpStatusCode;

public class UserServiceException extends RuntimeException{
	
	HttpStatusCode httpStatusCode;
	String Msg;
	
	public UserServiceException(HttpStatusCode httpStatusCode, String msg) {
		super();
		this.httpStatusCode = httpStatusCode;
		Msg = msg;
	}
	public HttpStatusCode getHttpStatusCode() {
		return httpStatusCode;
	}
	public String getMsg() {
		return Msg;
	}
	 
	

}
