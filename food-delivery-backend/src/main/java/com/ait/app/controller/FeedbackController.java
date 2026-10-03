package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.FeedbackDto;
import com.ait.app.service.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	@Autowired
	FeedbackService feedbackService;
	
	@PostMapping
	public ResponseEntity createFeedBack(@RequestBody FeedbackDto dto)
	{
		return new ResponseEntity(feedbackService.createFeedBack(dto),HttpStatus.CREATED);
	}
}
