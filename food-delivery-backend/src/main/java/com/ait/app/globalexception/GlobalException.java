package com.ait.app.globalexception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ait.app.exception.AddressServiceException;
import com.ait.app.exception.UserServiceException;

import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(value = UserServiceException.class)
	public ResponseEntity UserExceptionHandler(UserServiceException ue) {
		return new ResponseEntity(ue.getMsg(), ue.getHttpStatusCode());

	}

	@ExceptionHandler(value = AddressServiceException.class)
	public ResponseEntity UserExceptionHandler(AddressServiceException ae) {
		return new ResponseEntity(ae.getMessage(), ae.getHttpStatus());

	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity UserExceptionHandler(Exception e) {
		return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);

	}

}
