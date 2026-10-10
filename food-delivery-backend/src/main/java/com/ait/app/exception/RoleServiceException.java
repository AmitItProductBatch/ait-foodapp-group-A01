package com.ait.app.exception;

import org.springframework.http.HttpStatus;

public class RoleServiceException extends RuntimeException{

	private HttpStatus status;
	private String msg;
	public RoleServiceException(HttpStatus status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
