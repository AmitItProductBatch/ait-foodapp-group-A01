package com.ait.app.globalexception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ait.app.exception.RestaurantServiceException;

@ControllerAdvice
public class RestaurantGlobalException {

    @ExceptionHandler(RestaurantServiceException.class)
    public ResponseEntity<String> restaurantExceptionHandler(
            RestaurantServiceException restaurantServiceException) {

        return new ResponseEntity<>(
                restaurantServiceException.getMsg(),
                restaurantServiceException.getHttpStatusCode()
        );
    }
}