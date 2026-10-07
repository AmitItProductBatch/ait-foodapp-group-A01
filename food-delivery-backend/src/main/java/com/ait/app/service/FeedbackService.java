package com.ait.app.service;

import org.springframework.data.domain.Page;

import com.ait.app.dto.FeedbackDto;
import com.ait.app.dto.FeedbackResponseDto;
import com.ait.app.model.Feedback;

public interface FeedbackService {

	Feedback createFeedBack(FeedbackDto dto);

	Page<FeedbackResponseDto> getFeedbackByRestaurantId(long restaurantId, int page, int size);
	
	Feedback updateFeedBack(int feedbackId,FeedbackDto dto);
	
	void deleteFeedBack(int feedbackId);
}
