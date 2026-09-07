package com.ait.app.userexception;

import org.springframework.http.HttpStatusCode;

 

public class UserException extends RuntimeException{
	
	
	String Msg;
	HttpStatusCode httpStatusCode;
	public UserException(HttpStatusCode httpStatusCode, String msg) {
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
