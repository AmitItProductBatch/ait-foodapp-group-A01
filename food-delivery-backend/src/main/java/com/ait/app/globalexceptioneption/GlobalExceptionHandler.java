package com.ait.app.globalexceptioneption;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ait.app.exception.RestaurantServiceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RestaurantServiceException.class)
	public ResponseEntity<Map<String, String>> handleRestaurantServiceException(RestaurantServiceException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return new ResponseEntity<>(error, ex.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", "Something went wrong: " + ex.getMessage());
		return new ResponseEntity<>(error, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
	}
}