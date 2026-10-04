package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import com.ait.app.dto.FeedbackDto;
import com.ait.app.dto.FeedbackResponseDto;
import com.ait.app.service.FeedbackService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	@Autowired
	FeedbackService feedbackService;

	@PostMapping
	public ResponseEntity createFeedBack(@RequestBody FeedbackDto dto) {
		return new ResponseEntity(feedbackService.createFeedBack(dto), HttpStatus.CREATED);
	}

	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<Page<FeedbackResponseDto>> getRestaurantFeedback(
			@PathVariable long restaurantId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<FeedbackResponseDto> feedbackPage = feedbackService.getFeedbackByRestaurantId(restaurantId, page, size);
		return new ResponseEntity<>(feedbackPage, HttpStatus.OK);
	}
}
