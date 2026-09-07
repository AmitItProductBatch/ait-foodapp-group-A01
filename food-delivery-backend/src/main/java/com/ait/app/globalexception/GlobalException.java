package com.ait.app.globalexception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
 

import com.ait.app.userexception.UserException;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<String> m1(UserException ue) {
		return new ResponseEntity(ue.getMsg(),ue.getHttpStatusCode());
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> m1(Exception e){
		return new ResponseEntity (e.getMessage(),HttpStatus.BAD_REQUEST);
		
		
		
	}
}
