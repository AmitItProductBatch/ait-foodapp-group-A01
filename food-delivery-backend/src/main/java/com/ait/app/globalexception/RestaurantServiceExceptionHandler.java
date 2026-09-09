package com.ait.app.globalexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ait.app.exception.RestaurantServiceException;

@RestControllerAdvice
public class RestaurantServiceExceptionHandler {

	@ExceptionHandler(RestaurantServiceException.class)
	public ResponseEntity<String> handleRestaurantServiceException(RestaurantServiceException ex) {
		return new ResponseEntity<>(ex.getMsg(), ex.getHttpStatusCode());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		return new ResponseEntity<>("Something went wrong: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}