package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.RatingRequest;
import com.ait.app.dto.RatingResponse;
import com.ait.app.service.RatingService;

@RestController
@RequestMapping("/api/feedback")
public class RatingController {
	
	@Autowired
    private RatingService ratingService;
	
	@PostMapping("/ratings")
	public ResponseEntity<RatingResponse> addRating(@RequestBody RatingRequest request) {

		RatingResponse response = ratingService.addRating(request);

		return new ResponseEntity(response, HttpStatus.CREATED);
	}

}
